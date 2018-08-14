package org.memehazard.wheel.asset.facade;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.asset.codec.AssetZipDescriptor;
import org.memehazard.wheel.asset.codec.AssetZipDescriptorXMLParser;
import org.memehazard.wheel.asset.codec.CodecException;
import org.memehazard.wheel.asset.codec.ObjCodec;
import org.memehazard.wheel.asset.codec.X3DCodec;
import org.memehazard.wheel.asset.dao.AssetDAO;
import org.memehazard.wheel.asset.dao.AssetSetDAO;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.asset.model.AssetSet;
import org.memehazard.wheel.asset.model.Mesh;
import org.memehazard.wheel.asset.view.AssetDescriptor;
import org.memehazard.wheel.core.WheelException;
import org.memehazard.wheel.core.fs.FileRepository;
import org.memehazard.wheel.query.dao.QueryDAO;
import org.memehazard.wheel.query.facade.QueryDispatchFacadeImpl;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.viewer.view.SceneContentDescriptor;
import org.memehazard.wheel.viewer.view.SceneObjectDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AssetFacade
{
    @Autowired
    public FileRepository              fileRepo;

    public AssetZipDescriptorXMLParser zipDescriptorParser = new AssetZipDescriptorXMLParser();
    private ObjCodec                   codec_obj           = new ObjCodec();
    private X3DCodec                   codec_x3d           = new X3DCodec();

    @Autowired
    private AssetDAO                   dao_asset;
    @Autowired
    private AssetSetDAO                dao_assetSet;
    @Autowired
    private QueryDAO                   dao_query;

    private Logger                     log                 = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QueryDispatchFacadeImpl    svc_queryDispatch;


    public void addAsset(Asset asset, MultipartFile file, String fileRetrievalPath) throws
            IOException,
            CodecException
    {
        log.trace("addAsset " + asset + " with file " + file.getOriginalFilename());

        // This exception should never thrown - files with incorrect extensions should be caught
        // during binding & validation
        if (!fileRepo.isValidAssetFile(file.getOriginalFilename()))
            throw new RuntimeException("File of invalid type uploaded. OBJ & X3D supported");

        // Persist asset, thereby receiving DB ID for use in naming files
        dao_asset.add(asset);

        // Sort out file names for saving data
        File f_obj = fileRepo.toUniqueFile("" + asset.getId() + ".obj");
        File f_x3d = fileRepo.toUniqueFile("" + asset.getId() + ".x3d");

        // Convert file to alternate format
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        Mesh mesh = null;
        if ("x3d".equalsIgnoreCase(ext))
        {
            // Store model data
            file.transferTo(f_x3d);

            // Convert X3D file to OBJ file
            mesh = codec_x3d.decode(f_x3d);
            codec_obj.encode(f_obj, mesh);
        }
        else if ("obj".equalsIgnoreCase(ext))
        {
            // Store model data
            file.transferTo(f_obj);

            // Convert OBJ file to X3D file
            mesh = codec_obj.decode(f_obj);
            codec_x3d.encode(f_x3d, mesh);
        }

        // Save file names
        asset.setX3dFilename(fileRetrievalPath + f_x3d.getName());
        asset.setObjFilename(fileRetrievalPath + f_obj.getName());

        // Derive model statistics
        asset.setStats(mesh.calculateStatistics());

        // Save the updated asset in the DB
        dao_asset.update(asset);
    }


    public void addAssetSet(AssetSet obj)
    {
        dao_assetSet.add(obj);
    }


    @Async
    public void addAssetZip(AssetSet assetSet, MultipartFile file, String fileRetrievalPath) throws
            IOException, CodecException, XMLException
    {
        log.trace("Unpacking zip file " + file.getOriginalFilename());

        // Save uploaded zip file as temp file for working.
        File f_zipFile = fileRepo.toUniqueFile("temp.zip");
        log.trace("step 1");
        file.transferTo(f_zipFile);
        log.trace("step 2");
        ZipFile zipFile = new ZipFile(f_zipFile);

        log.trace("Copied incoming file to " + f_zipFile.getAbsolutePath());

        // Locate descriptor file (if one exists)
        Map<String, AssetZipDescriptor> descriptors = new HashMap<String, AssetZipDescriptor>();
        for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();)
        {
            ZipEntry zipEntry = e.nextElement();

            log.trace("Examining zip entry " + zipEntry.getName());

            if ("descriptors.xml".equals(zipEntry.getName()))
            {
                log.trace("Located descriptors XML file. Parsing.");

                // Parse descriptors file
                descriptors.putAll(zipDescriptorParser.parse(new InputStreamReader(zipFile.getInputStream(zipEntry))));

                for (String s : descriptors.keySet())
                {
                    log.trace("Found descriptor: " + s + " = " + descriptors.get(s));
                }
            }
        }

        // Attempt to process each zip entry
        for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();)
        {
            // Grab next file
            ZipEntry zipEntry = e.nextElement();
            String ext = FilenameUtils.getExtension(zipEntry.getName());

            // Verify - does file name indicate this is of a valid type?
            if (!fileRepo.isValidAssetFile(zipEntry.getName()))
                log.trace("Ignoring " + zipEntry.getName());

            else
            {
                // Grab this file's corresponding descriptor
                AssetZipDescriptor descriptor = descriptors.get(zipEntry.getName());

                // Verify a descriptor exists
                if (descriptor == null)
                    log.trace("Ignoring file - no descriptor found - " + zipEntry.getName());

                else
                {
                    log.trace("Processing asset file " + zipEntry.getName());

                    // Persist asset, thereby receiving DB ID for use in naming files
                    Asset asset = new Asset();
                    asset.setName(descriptor.name);
                    asset.setEntityId(descriptor.getEntityId());
                    asset.setAssetSet(assetSet);
                    dao_asset.add(asset);

                    // Create file handles for asset
                    File f_obj = fileRepo.toUniqueFile("" + asset.getId() + ".obj");
                    File f_x3d = fileRepo.toUniqueFile("" + asset.getId() + ".x3d");

                    // Extract, convert, and save files.
                    Mesh mesh = null;
                    if ("x3d".equalsIgnoreCase(ext))
                    {
                        // Store model data
                        FileUtils.copyInputStreamToFile(zipFile.getInputStream(zipEntry), f_x3d);

                        // Convert X3D file to OBJ file
                        mesh = codec_x3d.decode(f_x3d);
                        codec_obj.encode(f_obj, mesh);
                    }
                    else if ("obj".equalsIgnoreCase(ext))
                    {
                        // Store model data
                        FileUtils.copyInputStreamToFile(zipFile.getInputStream(zipEntry), f_obj);

                        // Convert OBJ file to X3D file
                        mesh = codec_obj.decode(f_obj);
                        codec_x3d.encode(f_x3d, mesh);
                    }

                    // Save file names
                    asset.setX3dFilename(fileRetrievalPath + f_x3d.getName());
                    asset.setObjFilename(fileRetrievalPath + f_obj.getName());

                    // Derive model statistics
                    asset.setStats(mesh.calculateStatistics());

                    dao_asset.update(asset);

                    log.trace("Successfully created asset from " + zipEntry.getName());
                }
            }
        }

        // Clean up
        zipFile.close();
        f_zipFile.delete();

        log.trace("Completed asset creation from " + file.getOriginalFilename());
    }


    public void deleteAsset(Integer id)
    {
        Asset obj = dao_asset.get(id);

        if (null != obj)
        {
            // Delete file
            if (null != obj.getObjFilename())
                fileRepo.removeFile(FilenameUtils.getName(obj.getObjFilename()));
            if (null != obj.getX3dFilename())
                fileRepo.removeFile(FilenameUtils.getName(obj.getX3dFilename()));

            // Delete Asset
            dao_asset.delete(obj.getId());
        }
    }


    public void deleteAssetSet(Integer id)
    {
        // Delete associated assets and their files
        List<Asset> assets = dao_asset.listByAssetSet(id);

        // Delete dependent assets
        for (Asset a : assets)
        {
            // Delete file
            if (null != a.getObjFilename())
                fileRepo.removeFile(FilenameUtils.getName(a.getObjFilename()));
            if (null != a.getX3dFilename())
                fileRepo.removeFile(FilenameUtils.getName(a.getX3dFilename()));

            // Assets themselves do not need deletion - they are cascade deleted within the database
        }

        // Delete Asset Set
        dao_assetSet.delete(id);
    }


    public AssetSet findAssetSetByName(String name)
    {
        return dao_assetSet.getByName(name);
    }


    /**
     * Generate a <code>SceneContentDescriptor</code> from an <code>Ssset</code>. If a stylesheet Id is provided,
     * apply.
     * 
     * @param assetId ID of asset to generate scene contents from
     * @param stylesheetId ID of style sheet to apply. If null, apply no styles
     * @return Generated <code>SceneContentDescriptor</code>
     */
    public SceneContentDescriptor generateSceneContentFromAsset(Integer assetId)
    {
        // Retrieve asset
        Asset a = dao_asset.get(assetId);

        // Create scene
        SceneContentDescriptor scd = new SceneContentDescriptor("Generated from Asset \"" + a.getName() + "\"");
        scd.addSceneObjectDescriptor(new SceneObjectDescriptor(new AssetDescriptor(a)));

        return scd;
    }


    /**
     * Generate a <code>SceneContentDescriptor</code> from a list of assets. If a stylesheet Id is provided, apply.
     * 
     * @param assetIds ID of assets to generate scene contents from
     * @param stylesheetId ID of style sheet to apply. If null, apply no styles
     * @return Generated <code>SceneContentDescriptor</code>
     */
    public SceneContentDescriptor generateSceneContentFromCustomAssetSet(int[] assetIds)
    {
        // Retrieve assets
        List<Asset> assets = new ArrayList<Asset>();
        for (int assetId : assetIds)
            assets.add(dao_asset.get(assetId));

        // Create scene contents
        SceneContentDescriptor scd = new SceneContentDescriptor("Generated from Custom List of Assets");
        for (Asset asset : assets)
            scd.addSceneObjectDescriptor(new SceneObjectDescriptor(new AssetDescriptor(asset)));

        return scd;
    }


    public Asset getAsset(Integer id)
    {
        return dao_asset.get(id);
    }


    public AssetSet getAssetSet(Integer id)
    {
        return dao_assetSet.get(id);
    }


    public List<Asset> listAssets()
    {
        return dao_asset.listAll();
    }


    public List<Asset> listAssetsBySet(Integer id)
    {
        return dao_asset.listByAssetSet(id);
    }


    public List<AssetSet> listAssetSets()
    {
        return dao_assetSet.listAllWithCounts();
    }


    public List<AssetSet> listAssetSetsWithAssets()
    {
        return dao_assetSet.listAllWithAssets();
    }


    public List<Integer> listIdsByAssetSet(Integer id)
    {
        return dao_asset.listIdsByAssetSet(id);
    }


    public File retrieveFile(String filename)
    {
        return fileRepo.retrieveFile(filename);
    }


    public void updateAsset(Asset asset, MultipartFile file) throws
            IOException,
            CodecException
    {
        log.trace("updateAsset " + asset + " with file " + file.getOriginalFilename());

        // Handle new file upload
        if (!file.isEmpty())
        {
            // Delete old file
            fileRepo.removeFile(asset.getX3dFilename());
            fileRepo.removeFile(asset.getObjFilename());

            // Sort out file names for saving data
            File f_obj = fileRepo.toUniqueFile("" + asset.getId() + ".obj");
            File f_x3d = fileRepo.toUniqueFile("" + asset.getId() + ".x3d");

            // Convert file to alternate format
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            Mesh mesh = null;
            if ("x3d".equalsIgnoreCase(ext))
            {
                // Store model data
                file.transferTo(f_x3d);

                // Convert X3D file to OBJ file
                mesh = codec_x3d.decode(f_x3d);
                codec_obj.encode(f_obj, mesh);
            }
            else if ("obj".equalsIgnoreCase(ext))
            {
                // Store model data
                file.transferTo(f_obj);

                // Convert OBJ file to X3D file
                mesh = codec_obj.decode(f_obj);
                codec_x3d.encode(f_x3d, mesh);
            }
            else
            {
                // This exception should never thrown - files with incorrect extensions should be caught
                // during binding & validation
                throw new RuntimeException("File of invalid type uploaded. OBJ & X3D supported");
            }

            // Save file names
            asset.setX3dFilename(f_x3d.getName());
            asset.setObjFilename(f_obj.getName());

            // Derive model statistics
            asset.setStats(mesh.calculateStatistics());
        }

        // Save the updated asset in the DB
        dao_asset.update(asset);
    }


    public void updateAssetSet(AssetSet obj)
    {
        dao_assetSet.update(obj);
    }


    /**
     * Update the style tag list information for all assets in given set
     * 
     * @param setId
     */
    @Async
    public void updateStyleTags(int setId)
            throws WheelException
    {
        log.trace("Updating style tag lists for asset set " + setId);

        // Retrieve assets to update
        List<Asset> assets = dao_asset.listByAssetSet(setId);

        for (Asset a : assets)
        {
            try
            {
                // Retrieve ancestor entities
                List<Entity> ancestorEntities = svc_queryDispatch.retrieveAncestors(a.getEntityId());

                // Extract tags from entities
                List<String> entityNames = new ArrayList<String>();
                for (Entity entity : ancestorEntities)
                    entityNames.add(entity.getName());

                // Update asset
                if (entityNames.size() > 0)
                {
                    a.setStyleTags(entityNames);
                    dao_asset.updateStyleTags(a);
                }
                else
                    dao_asset.deleteStyleTags(a.getId());

                log.trace("Updated style tag list for asset " + a.getName() + " with entity ID " + a.getEntityId());
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
                log.trace("Unable to retrieve style tag list for asset " + a.getName() + " with entity ID " + a.getEntityId());
            }
        }
    }
}
