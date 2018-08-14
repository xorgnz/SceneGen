/**
 *
 */
package org.memehazard.wheel.scene.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.asset.dao.AssetDAO;
import org.memehazard.wheel.asset.dao.AssetSetDAO;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.query.dao.QueryDAO;
import org.memehazard.wheel.query.facade.QueryDispatchFacade;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.scene.dao.SceneDAO;
import org.memehazard.wheel.scene.dao.SceneFragmentDAO;
import org.memehazard.wheel.scene.dao.SceneFragmentMemberDAO;
import org.memehazard.wheel.scene.model.Scene;
import org.memehazard.wheel.scene.model.SceneFragment;
import org.memehazard.wheel.scene.model.SceneFragmentMember;
import org.memehazard.wheel.scene.model.SimpleSceneFragmentMember;
import org.memehazard.wheel.scene.model.Transform;
import org.memehazard.wheel.scene.view.SceneDescriptor;
import org.memehazard.wheel.scene.view.SceneFragmentMemberDescriptor;
import org.memehazard.wheel.style.dao.StylesheetDAO;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.model.Stylesheet;
import org.memehazard.wheel.viewer.model.Viewpoint;
import org.memehazard.wheel.viewer.view.SceneContentDescriptor;
import org.memehazard.wheel.viewer.view.ViewpointDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SceneFacade
{
    @Autowired
    private AssetDAO               dao_asset;
    @Autowired
    private AssetSetDAO            dao_assetSet;
    @Autowired
    private QueryDAO               dao_query;
    @Autowired
    private SceneDAO               dao_scn;
    @Autowired
    private SceneFragmentDAO       dao_scnf;
    @Autowired
    private SceneFragmentMemberDAO dao_scnfm;
    @Autowired
    private StylesheetDAO          dao_stylesheet;
    @Autowired
    private QueryDispatchFacade    facade_queryDispatch;

    @SuppressWarnings("unused")
    private Logger                 log = LoggerFactory.getLogger(this.getClass());


    public Scene addScene(String name)
    {
        Scene obj = new Scene(name);

        dao_scn.add(obj);

        return obj;
    }


    public SceneFragment addSceneFragment(int sceneId, String name, Integer stylesheetId, Integer assetSetId)
    {
        Scene s = getScene(sceneId);

        SceneFragment fragment = new SceneFragment();
        fragment.setScene(s);
        fragment.setName(name);
        if (assetSetId != null)
            fragment.setAssetSet(dao_assetSet.get(assetSetId));
        if (stylesheetId != null)
            fragment.setStylesheet(dao_stylesheet.get(stylesheetId));
        dao_scnf.addMixedListType(fragment);

        return fragment;
    }


    public SceneFragment addSceneFragmentFromAssetList(int sceneId, String name, int stylesheetId, int assetSetId, int[] assetIds)
    {
        // Load scene
        Scene s = getFullScene(sceneId);

        // Add fragment to scene
        SceneFragment fragment = new SceneFragment();
        fragment.setScene(s);
        fragment.setName(name);
        fragment.setStylesheet(dao_stylesheet.get(stylesheetId));
        fragment.setAssetSet(dao_assetSet.get(assetSetId));
        dao_scnf.addListType(fragment);

        // Create SFMs from assets
        List<SceneFragmentMember> members = generateSceneFragmentMembersFromAssetList(fragment, assetSetId, stylesheetId, assetIds);

        // Hide new SFMs that duplicate existing SFMs
        hideDuplicateSceneFragmentMembers(s, members, null);

        // Add SFMs to fragment
        for (SceneFragmentMember sfm : members)
            dao_scnfm.add(sfm);

        // Retrieve fragment (accounting for any validation).
        fragment = dao_scnf.get(fragment.getId());

        return fragment;
    }


    public SceneFragment addSceneFragmentFromEntityList(int sceneId, String name, int stylesheetId, int assetSetId, int[] entityIds)
    {
        // Load scene
        Scene s = getFullScene(sceneId);

        // Add fragment to scene
        SceneFragment fragment = new SceneFragment();
        fragment.setScene(s);
        fragment.setName(name);
        fragment.setStylesheet(dao_stylesheet.get(stylesheetId));
        fragment.setAssetSet(dao_assetSet.get(assetSetId));
        dao_scnf.addListType(fragment);

        // Create SFMs from assets
        List<SceneFragmentMember> members = generateSceneFragmentMembersFromEntityList(fragment, assetSetId, stylesheetId, entityIds);

        // Hide new SFMs that duplicate existing SFMs
        hideDuplicateSceneFragmentMembers(s, members, null);

        // Add SFMs to fragment
        for (SceneFragmentMember sfm : members)
            dao_scnfm.add(sfm);

        // Retrieve fragment (accounting for any validation).
        fragment = dao_scnf.get(fragment.getId());

        return fragment;
    }


    public SceneFragment addSceneFragmentFromQueryParams(
            int sceneId,
            String name,
            int stylesheetId,
            int assetSetId,
            int queryId,
            Map<String, String> queryParamMap)
            throws IOException, ParserException
    {
        // Load scene
        Scene s = getFullScene(sceneId);

        // Add fragment to scene
        SceneFragment fragment = new SceneFragment();
        fragment.setScene(s);
        fragment.setName(name);
        fragment.setStylesheet(dao_stylesheet.get(stylesheetId));
        fragment.setAssetSet(dao_assetSet.get(assetSetId));
        fragment.setQuery(dao_query.get(queryId));
        fragment.setQueryParamValues(queryParamMap);
        dao_scnf.addQueryType(fragment);

        // Generate SFMs
        List<SceneFragmentMember> members = this.generateSceneFragmentMembersFromQuery(fragment, fragment.getQuery(),
                fragment.parseParameterValues(), assetSetId,
                stylesheetId);

        // Hide new SFMs that duplicate existing SFMs
        hideDuplicateSceneFragmentMembers(s, members, null);

        // Add SFMs to fragment
        for (SceneFragmentMember sfm : members)
            dao_scnfm.add(sfm);

        // Retrieve fragment (accounting for any validation).
        fragment = dao_scnf.get(fragment.getId());

        return fragment;
    }


    public void addSceneFragmentMemberFromSimpleMember(SceneFragment scnf, List<SimpleSceneFragmentMember> simpleMembers)
    {
        for (SimpleSceneFragmentMember ssfm : simpleMembers)
        {
            // Create AssetDescriptor
            Asset asset = dao_asset.get(ssfm.getAssetId());
            Transform transform = new Transform();

            dao_scnfm.add(new SceneFragmentMember(scnf, true, ssfm.getEntity(), asset, transform, ssfm.getStyle()));
        }
    }


    public void deleteScene(int id)
    {
        // Delete Scene
        dao_scn.delete(id);
    }


    public void deleteSceneFragment(int id)
    {
        dao_scnf.delete(id);
    }


    public void deleteSceneFragmentsByScene(int sceneId)
    {
        dao_scnf.deleteByScene(sceneId);
    }


    /**
     * Generate a <code>SceneContentDescriptor</code> from a given <code>Scene</code>.
     * 
     * @param sceneId ID of scene to retrieve
     * @return Generated <code>SceneContentDescriptor</code>
     */
    public SceneContentDescriptor generateSceneContentDescriptor(int sceneId)
    {
        // Retrieve Scene and create descriptor
        Scene scn = this.getFullScene(sceneId);
        SceneContentDescriptor scd = new SceneContentDescriptor(scn.getName());
        if (scn.getViewpoint() != null)
            scd.setViewpoint(new ViewpointDescriptor(scn.getViewpoint()));

        // Extract SceneObjects from SceneFragments
        for (SceneFragment scnf : scn.getFragments())
            for (SceneFragmentMember scnfm : scnf.getMembers())
                scd.addSceneObjectDescriptor(new SceneFragmentMemberDescriptor(scnfm));

        return scd;
    }


    /**
     * Generate a <code>SceneDescriptor</code> for a given <code>Scene</code>
     * 
     * @param sceneId ID of <code>Scene</code> to create <code>SceneDescriptor</code> for
     * @return Generated <code>SceneDescriptor</code>
     */
    public SceneDescriptor generateSceneDescriptor(int sceneId)
    {
        // TODO - Full scene needs to be built using a facade
        // Retrieve Scene and create descriptor
        Scene scn = this.getFullScene(sceneId);
        SceneDescriptor scnd = new SceneDescriptor(scn);

        return scnd;
    }


    public Scene getFullScene(int sceneId)
    {
        Scene scene = dao_scn.get(sceneId);
        scene.setFragments(dao_scnf.listBySceneWithAssociations(sceneId));

        for (SceneFragment scnf : scene.getFragments())
            scnf.setMembers(dao_scnfm.listByFragment(scnf.getId()));

        return scene;
    }


    public Scene getScene(int id)
    {
        return dao_scn.get(id);
    }


    public List<Scene> listScenes()
    {
        return dao_scn.listAll();
    }


    public void updateScene(Scene obj)
    {
        dao_scn.update(obj);
    }


    public void updateSceneFragmentFromAssetList(
            int id,
            String name,
            int stylesheetId,
            int assetSetId,
            int[] assetIds)
    {
        // Load scene and target fragment
        SceneFragment fragment = dao_scnf.get(id);
        Scene s = getFullScene(fragment.getScene().getId());

        // Create preservation maps for existing SFMs in this fragment
        Map<Integer, Style> preserveStyleMap = new HashMap<Integer, Style>();
        Map<Integer, Boolean> preserveVisibilityMap = new HashMap<Integer, Boolean>();
        for (SceneFragmentMember sfm : dao_scnfm.listByFragment(id))
        {
            preserveStyleMap.put(sfm.getEntity().getId(), sfm.getStyle());
            preserveVisibilityMap.put(sfm.getEntity().getId(), sfm.isVisible());
        }

        // Update scene fragment
        if (fragment != null)
        {
            fragment.setName(name);
            fragment.setStylesheet(dao_stylesheet.get(stylesheetId));
            fragment.setAssetSet(dao_assetSet.get(assetSetId));
        }
        dao_scnf.updateListType(fragment);

        // Delete old SFMs
        dao_scnfm.deleteByFragment(fragment.getId());

        // Generate new SFMs
        List<SceneFragmentMember> members = this.generateSceneFragmentMembersFromAssetList(
                fragment,
                assetSetId,
                stylesheetId,
                assetIds);

        // Re-apply preserved styles
        for (SceneFragmentMember sfm : members)
        {
            if (preserveStyleMap.containsKey(sfm.getEntity().getId()))
                sfm.setStyle(preserveStyleMap.get(sfm.getEntity().getId()));
        }

        // Hide SFMs that duplicate existing SFMs, preserving current visibility
        hideDuplicateSceneFragmentMembers(s, members, preserveVisibilityMap);

        // Add SFMs to fragment
        for (SceneFragmentMember sfm : members)
            dao_scnfm.add(sfm);
    }


    public void updateSceneFragmentFromEntityList(
            int id,
            String name,
            int stylesheetId,
            int assetSetId,
            int[] entityIds)
    {
        // Load scene and target fragment
        SceneFragment fragment = dao_scnf.get(id);
        Scene s = getFullScene(fragment.getScene().getId());

        // Create preservation maps for existing SFMs in this fragment
        Map<Integer, Style> preserveStyleMap = new HashMap<Integer, Style>();
        Map<Integer, Boolean> preserveVisibilityMap = new HashMap<Integer, Boolean>();
        for (SceneFragmentMember sfm : dao_scnfm.listByFragment(id))
        {
            preserveStyleMap.put(sfm.getEntity().getId(), sfm.getStyle());
            preserveVisibilityMap.put(sfm.getEntity().getId(), sfm.isVisible());
        }

        // Update scene fragment
        if (fragment != null)
        {
            fragment.setName(name);
            fragment.setStylesheet(dao_stylesheet.get(stylesheetId));
            fragment.setAssetSet(dao_assetSet.get(assetSetId));
        }
        dao_scnf.updateListType(fragment);

        // Delete old SFMs
        dao_scnfm.deleteByFragment(fragment.getId());

        // Generate new SFMs
        List<SceneFragmentMember> members = this.generateSceneFragmentMembersFromEntityList(
                fragment,
                assetSetId,
                stylesheetId,
                entityIds);

        // Re-apply preserved styles
        for (SceneFragmentMember sfm : members)
        {
            if (preserveStyleMap.containsKey(sfm.getEntity().getId()))
                sfm.setStyle(preserveStyleMap.get(sfm.getEntity().getId()));
        }

        // Hide SFMs that duplicate existing SFMs, preserving current visibility
        hideDuplicateSceneFragmentMembers(s, members, preserveVisibilityMap);

        // Add SFMs to fragment
        for (SceneFragmentMember sfm : members)
            dao_scnfm.add(sfm);
    }


    public void updateSceneFragmentFromQueryParams(
            int id,
            String name,
            int stylesheetId,
            int assetSetId,
            int queryId,
            Map<String, String> queryParams)
            throws IOException, ParserException
    {
        // Load scene and target fragment
        SceneFragment fragment = dao_scnf.get(id);
        Scene s = getFullScene(fragment.getScene().getId());

        // Create preservation maps for existing SFMs in this fragment
        Map<Integer, Style> preserveStyleMap = new HashMap<Integer, Style>();
        Map<Integer, Boolean> preserveVisibilityMap = new HashMap<Integer, Boolean>();
        for (SceneFragmentMember sfm : dao_scnfm.listByFragment(id))
        {
            preserveStyleMap.put(sfm.getEntity().getId(), sfm.getStyle());
            preserveVisibilityMap.put(sfm.getEntity().getId(), sfm.isVisible());
        }

        // Update scene fragment
        if (fragment != null)
        {
            fragment.setName(name);
            fragment.setStylesheet(dao_stylesheet.get(stylesheetId));
            fragment.setAssetSet(dao_assetSet.get(assetSetId));
            fragment.setQuery(dao_query.get(queryId));
            fragment.setQueryParamValues(queryParams);
        }
        dao_scnf.updateQueryType(fragment);

        // Delete old SFMs
        dao_scnfm.deleteByFragment(fragment.getId());

        // Generate new SFMs
        List<SceneFragmentMember> members = this.generateSceneFragmentMembersFromQuery(
                fragment,
                fragment.getQuery(),
                fragment.parseParameterValues(),
                assetSetId,
                stylesheetId);

        // Re-apply preserved styles
        for (SceneFragmentMember sfm : members)
        {
            if (preserveStyleMap.containsKey(sfm.getEntity().getId()))
                sfm.setStyle(preserveStyleMap.get(sfm.getEntity().getId()));
        }

        // Hide SFMs that duplicate existing SFMs, preserving current visibility
        hideDuplicateSceneFragmentMembers(s, members, preserveVisibilityMap);

        // Add SFMs to fragment
        for (SceneFragmentMember sfm : members)
            dao_scnfm.add(sfm);
    }


    public void updateSceneFragmentMemberStyle(int id, String ambient, String diffuse, String emissive, String specular, int shininess, double alpha)
    {
        SceneFragmentMember member = dao_scnfm.get(id);
        member.getStyle().setAmbient(ambient);
        member.getStyle().setDiffuse(diffuse);
        member.getStyle().setEmissive(emissive);
        member.getStyle().setSpecular(specular);
        member.getStyle().setShininess(shininess);
        member.getStyle().setAlpha(alpha);
        dao_scnfm.update(member);
    }


    public void updateSceneFragmentMemberVisibility(int id, boolean visible)
    {
        // Update given member
        SceneFragmentMember member = dao_scnfm.get(id);
        member.setVisible(visible);
        dao_scnfm.update(member);

        // Hide members representing the same entity
        if (visible)
        {
            SceneFragment fragment = dao_scnf.get(member.getFragmentId());
            for (SceneFragmentMember sfm : getFullScene(fragment.getScene().getId()).getAllMembers())
            {
                if (sfm.getEntity().getId().equals(member.getEntity().getId()) && sfm.getId() != id && sfm.isVisible())
                {
                    sfm.setVisible(false);
                    dao_scnfm.update(sfm);
                }
            }
        }
    }


    public void updateSceneWithName(int id, String name)
    {
        Scene scn = dao_scn.get(id);

        if (scn != null)
            scn.setName(name);

        dao_scn.update(scn);
    }


    public void updateSceneWithViewpoint(int id, Viewpoint vp)
    {
        Scene scn = dao_scn.get(id);

        if (scn != null)
            scn.setViewpoint(vp);

        dao_scn.update(scn);
    }


    private List<SceneFragmentMember> generateSceneFragmentMembersFromAssetList(SceneFragment fragment, int assetSetId, int stylesheetId,
            int[] assetIds)
    {
        // Storage
        List<SceneFragmentMember> members = new ArrayList<SceneFragmentMember>();

        // Create set of asset IDs for quick comparison
        HashSet<Integer> assetIdSet = new HashSet<Integer>();
        for (int i : assetIds)
            assetIdSet.add(i);

        // Work through assets in asset set, creating SFMs for those selected
        for (Asset a : dao_asset.listByAssetSet(assetSetId))
        {
            if (assetIdSet.contains(a.getId()))
            {
                Transform transform = new Transform();
                Entity e = new Entity("", a.getEntityId(), a.getName());

                members.add(new SceneFragmentMember(fragment, true, e, a, transform, null));
            }
        }

        // Style SFMs
        Stylesheet stylesheet = dao_stylesheet.get(stylesheetId);
        if (stylesheet != null)
            stylesheet.styleObjects(members);

        return members;
    }


    private List<SceneFragmentMember> generateSceneFragmentMembersFromEntityList(SceneFragment fragment, int assetSetId, int stylesheetId,
            int[] entityIds)
    {
        // Storage
        List<SceneFragmentMember> members = new ArrayList<SceneFragmentMember>();

        // Create set of asset IDs for quick comparison
        HashSet<Integer> entityIdSet = new HashSet<Integer>();
        for (int i : entityIds)
            entityIdSet.add(i);

        // Work through assets in asset set, creating SFMs for those selected
        for (Asset a : dao_asset.listByAssetSet(assetSetId))
        {
            if (entityIdSet.contains(a.getEntityId()))
            {
                Transform transform = new Transform();
                Entity e = new Entity("", a.getEntityId(), a.getName());

                members.add(new SceneFragmentMember(fragment, true, e, a, transform, null));
            }
        }

        // Style SFMs
        Stylesheet stylesheet = dao_stylesheet.get(stylesheetId);
        if (stylesheet != null)
            stylesheet.styleObjects(members);

        return members;
    }


    /**
     * Generate <code>SceneFragmentMember</code>s from a given query and parameter values, using the given asset set and
     * style.
     * 
     * @param query
     * @param parseParameterValues
     * @param assetSetId
     * @param stylesheetId
     * @return
     */
    private List<SceneFragmentMember> generateSceneFragmentMembersFromQuery(
            SceneFragment fragment,
            Query query,
            Map<String, String> paramValues,
            int assetSetId,
            int stylesheetId)
            throws IOException, ParserException
    {
        // Retrieve assets
        Map<Integer, Asset> entityAssetMap = new HashMap<Integer, Asset>();
        for (Asset a : dao_asset.listByAssetSet(assetSetId))
            entityAssetMap.put(a.getEntityId(), a);

        // Execute query and create SceneFragmentMembers from results
        List<Entity> entities = facade_queryDispatch.retrieveEntitiesByQuery(query, paramValues);
        List<SceneFragmentMember> members = new ArrayList<SceneFragmentMember>();
        for (Entity e : entities)
        {
            // Create AssetDescriptor
            Asset asset = entityAssetMap.get(e.getId());
            Transform transform = new Transform();

            members.add(new SceneFragmentMember(fragment, true, e, asset, transform, null));
        }

        // Retrieve stylesheet and style fragments
        Stylesheet stylesheet = dao_stylesheet.get(stylesheetId);
        if (stylesheet != null)
            stylesheet.styleObjects(members);

        return members;
    }


    private void hideDuplicateSceneFragmentMembers(Scene s, List<SceneFragmentMember> members, Map<Integer, Boolean> preserveVisibilityMap)
    {
        // Validate
        if (preserveVisibilityMap == null)
            preserveVisibilityMap = new HashMap<Integer, Boolean>();

        // Generate visibility map of SFMs in scene
        Map<Integer, Boolean> visibilityMap = new HashMap<Integer, Boolean>();
        for (SceneFragmentMember sfm : s.getAllMembers())
        {
            int id = sfm.getEntity().getId();
            visibilityMap.put(id, (visibilityMap.containsKey(id) ? visibilityMap.get(id) || sfm.isVisible() : sfm.isVisible()));
        }

        // Hide new SFMs if same entity already present
        for (SceneFragmentMember sfm : members)
        {
            int id = sfm.getEntity().getId();

            // Preserve previous visibility if set
            if (preserveVisibilityMap.containsKey(id))
                sfm.setVisible(preserveVisibilityMap.get(id));

            // Set invisible if already found in scene
            else if (visibilityMap.containsKey(id) && visibilityMap.get(id))
                sfm.setVisible(false);
        }
    }
}
