package org.memehazard.wheel.explorer.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.asset.dao.AssetDAO;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.explorer.view.RenderableEntityDescriptor;
import org.memehazard.wheel.query.facade.QueryDispatchFacadeImpl;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Relationship;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.style.dao.StylesheetDAO;
import org.memehazard.wheel.style.model.Stylesheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExplorerFacade
{
    @Autowired
    private AssetDAO                dao_asset;
    @Autowired
    private StylesheetDAO           dao_stylesheet;
    // private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QueryDispatchFacadeImpl svc_query;


    /**
     * Generates a <code>SceneContentDescriptor</code> from a list of entities using the given asset set and
     * stylesheet.
     * 
     * @param entities
     * @param assetSetId
     * @param stylesheetId
     * @return
     * @throws IOException
     * @throws XMLException
     */
    public List<RenderableEntityDescriptor> buildRenderableEntityDescriptors(List<? extends Entity> entities, int assetSetId, int stylesheetId)
    {
        List<RenderableEntityDescriptor> descriptors = new ArrayList<RenderableEntityDescriptor>();

        // Retrieve assets
        Map<Integer, Asset> assetMap = new HashMap<Integer, Asset>();
        for (Asset a : dao_asset.listByAssetSet(assetSetId))
            assetMap.put(a.getEntityId(), a);

        // Create RenderableEntityDescriptors
        for (Entity e : entities)
        {
            System.err.println(e);
            descriptors.add(new RenderableEntityDescriptor(e, assetMap.get(e.getId())));
        }


        // Style RenderableEntityDescriptors
        Stylesheet stylesheet = dao_stylesheet.get(stylesheetId);
        if (stylesheet != null)
            stylesheet.styleObjects(descriptors);

        return descriptors;
    }


    /**
     * Generate a list of <code>RelationDescriptor</code>s representing the relations in which a given
     * entity is the subject
     * 
     * @param entityId ID of entity to find relations from
     * @return List of <code>RelationDescriptor</code>s
     */
    public List<Relationship> generateRelationshipsByEntity(int entityId)
            throws ParserException, IOException
    {
        // Execute query
        List<Relationship> relationships = svc_query.retrieveRelationshipsByEntity(entityId);

        // Prepare and return list of descriptors
        return relationships;
    }


    public List<Relationship> generateRelationshipsByEntityRelationCascade(Integer entityId, String relation)
            throws ParserException, IOException
    {
        // Execute
        List<Relationship> relationships = svc_query.retrieveRelationshipsByEntityRelationCascade(entityId, relation);

        return relationships;
    }


    /**
     * Generate <code>SceneContentDescriptor</code> from a given subject and relation pair.
     * 
     * @param relation Relationship to explore
     * @param subject Subject of relationship
     * @param assetSetId ID of asset set to map query response to
     * @param stylesheetId ID of style sheet to apply. If null, apply no styles
     * @return Generated <code>SceneContentDescriptor</code>
     */
    // public List<RenderableEntityDescriptor> retrieveRenderableEntitiesByRelation(
    // String relation,
    // Integer entityId,
    // Integer assetSetId,
    // Integer stylesheetId)
    // throws ParserException, IOException
    // {
    // // Retrieve entities
    // List<Entity> entities = svc_query.retrieveRelatedEntities(entityId, relation);
    //
    // // Generate renderable entity descriptors
    // return this.buildRenderableEntityDescriptors(entities, assetSetId, stylesheetId);
    // }


    public List<RenderableEntityDescriptor> retrieveRenderableEntities(int[] ids, int assetSetId, int stylesheetId)
            throws ParserException, IOException
    {
        List<Entity> entities = new ArrayList<Entity>();

        // Retrieve entities from server
        for (int id : ids)
            entities.add(svc_query.retrieveEntity(id));

        return this.buildRenderableEntityDescriptors(entities, assetSetId, stylesheetId);
    }


    public List<RenderableEntityDescriptor> retrieveRenderableEntitiesByName(String[] names, int assetSetId, int stylesheetId)
            throws ParserException, IOException
    {
        List<Entity> entities = new ArrayList<Entity>();

        // Retrieve entities from server
        for (String name : names)
        {
            Entity e = svc_query.retrieveEntityByName(name);
            if (e != null)
                entities.add(e);
        }

        return this.buildRenderableEntityDescriptors(entities, assetSetId, stylesheetId);
    }
}
