package org.memehazard.wheel.scene.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.scene.model.Scene;
import org.memehazard.wheel.scene.model.SceneFragment;
import org.memehazard.wheel.scene.model.SceneFragmentMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/scene/{id}/zip")
public class SceneZipController
{
    private static final int BUFFER_SIZE    = 4096;

    @Autowired
    private SceneFacade      facade;

    @Autowired
    private AssetFacade      facade_asset;

    private Logger           log            = LoggerFactory.getLogger(this.getClass());

    private String[]         files          = new String[] {
                                            "resources/images/cga/help_button.png",
                                            "resources/scripts/3rdparty/jquery-1.10.2.js",
                                            "resources/scripts/3rdparty/jquery.mousewheel.min.js",
                                            "resources/scripts/3rdparty/obj_loader.js",
                                            "resources/scripts/3rdparty/three.min.56.js",
                                            "resources/scripts/ava/AVA_ContentsPanel.js",
                                            "resources/scripts/ava/AVA_Controller.js",
                                            "resources/scripts/ava/AVA_Main.js",
                                            "resources/scripts/ava/AVA_PickingPanel.js",
                                            "resources/scripts/cga/CGA_BaseController.js",
                                            "resources/scripts/cga/CGA_ExamineCamera.js",
                                            "resources/scripts/cga/CGA_GeometryObject.js",
                                            "resources/scripts/cga/CGA_GraphicsEngine.js",
                                            "resources/scripts/cga/CGA_GroupObject.js",
                                            "resources/scripts/cga/CGA_InteractionManager.js",
                                            "resources/scripts/cga/CGA_Main.js",
                                            "resources/scripts/cga/CGA_MeshLoader.js",
                                            "resources/scripts/cga/CGA_MeshManager.js",
                                            "resources/scripts/cga/CGA_ObjectDescriptor.js",
                                            "resources/scripts/cga/CGA_Scene.js",
                                            "resources/scripts/cga/CGA_UI_HelpPanel.js",
                                            "resources/scripts/cga/CGA_UI_LoadProgressPanel.js",
                                            "resources/scripts/utils/DOMUtils.js",
                                            "resources/scripts/utils/Lang.js",
                                            "resources/scripts/utils/UI_Panel.js",
                                            "resources/styles/ava.css",
                                            "resources/styles/cga.css",
                                            "resources/styles/3rdparty/jquery-ui.css",
                                            "resources/styles/3rdparty/images/ui-bg_flat_75_ffffff_40x100.png" };

    private String           fn_html_top    = "org/memehazard/wheel/scene/controller/zip_top.html";
    private String           fn_html_mid    = "org/memehazard/wheel/scene/controller/zip_mid.html";
    private String           fn_html_bottom = "org/memehazard/wheel/scene/controller/zip_bottom.html";


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public void performGet(
            @PathVariable int id,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ParserException, IOException

    {
        log.trace("Handling REST request - " + request.getServletPath() + " - " + request.getMethod());

        // Retrieve file
        Scene scene = facade.getFullScene(id);

        log.trace("Retrieving zip file for scene " + scene.getName() + " (" + id + ")");

        // Set necessary headers
        response.setHeader("Content-Disposition", "attachment; filename=\"scene.zip\"");
        response.setHeader("Content-Type", "application/zip, application/octet-stream");

        // Create zip file output
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());

        // Copy model files
        for (SceneFragment sf : scene.getFragments())
        {
            for (SceneFragmentMember sfm : sf.getMembers())
            {
                String filename = FilenameUtils.getName(sfm.getAsset().getObjFilename());
                log.info("Adding " + filename);
                File f = facade_asset.retrieveFile(filename);
                this.copyToZip(f, out, "models/" + filename);
            }
        }

        // Get root directories
        File f_war_root = new File(getClass().getClassLoader().getResource("../../").getPath());
        File f_class_root = new File(getClass().getClassLoader().getResource("").getPath());

        // Copy script files
        for (String s : files)
            this.copyToZip(new File(f_war_root, s), out, s.substring(10));

        // Assemble HTML
        out.putNextEntry(new ZipEntry("index.html"));
        Writer w = new OutputStreamWriter(out);
        IOUtils.copy(new FileInputStream(new File(f_class_root, fn_html_top)), out);

        // HTML - object descriptors
        for (SceneFragment sf : scene.getFragments())
        {
            for (SceneFragmentMember sfm : sf.getMembers())
            {
                // new CGA_ObjectDescriptor (
                // 1,
                // "Left parietal bone",
                // "./assets/110.obj",
                // { ambient: "#ffb040", diffuse: "#ffb040", emissive: "#000000", specular: "#ffffff", shininess: "50",
                // alpha: 1}, true, {}),
                w.append(String
                        .format("new CGA_ObjectDescriptor( %d, \"%s\", \"%s\", "
                                + "{ ambient: \"%s\", diffuse: \"%s\", emissive: \"%s\", specular: \"%s\", shininess: %d, alpha: %f}, %b, {}),\n",
                                sfm.getId(), sfm.getEntity().getName(), 
                                "models/" + FilenameUtils.getName(sfm.getAsset().getObjFilename()),
                                sfm.getStyle().getAmbient(), sfm.getStyle().getDiffuse(), sfm.getStyle().getEmissive(), sfm.getStyle().getSpecular(),
                                sfm.getStyle().getShininess(), sfm.getStyle().getAlpha(), sfm.isVisible()));
            }
        }
        w.flush();

        IOUtils.copy(new FileInputStream(new File(f_class_root, fn_html_mid)), out);

        // HTML - camera parameters
        w.append(String.format("position: { x: %f, y: %f, z: %f },",
                scene.getViewpoint().getPosition().x,
                scene.getViewpoint().getPosition().y,
                scene.getViewpoint().getPosition().z));
        w.append(String.format("rotation: { x: %f, y: %f, z: %f },",
                scene.getViewpoint().getRotation().x,
                scene.getViewpoint().getRotation().y,
                scene.getViewpoint().getRotation().z));
        w.append(String.format("target: { x: %f, y: %f, z: %f },",
                scene.getViewpoint().getTarget().x,
                scene.getViewpoint().getTarget().y,
                scene.getViewpoint().getTarget().z));
        w.append(String.format("up: { x: %f, y: %f, z: %f },",
                scene.getViewpoint().getUp().x,
                scene.getViewpoint().getUp().y,
                scene.getViewpoint().getUp().z));
        w.flush();

        IOUtils.copy(new FileInputStream(new File(f_class_root, fn_html_bottom)), out);

        // Clean up
        out.close();
    }


    private void copyToZip(File f_in, ZipOutputStream out, String name)
            throws IOException
    {
        FileInputStream in = new FileInputStream(f_in);

        // Transfer data from file into zip
        out.putNextEntry(new ZipEntry(name));
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, bytesRead);
        }

        // Clean up
        out.flush();
        in.close();
    }
}
