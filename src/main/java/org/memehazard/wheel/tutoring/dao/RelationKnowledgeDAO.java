package org.memehazard.wheel.tutoring.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.model.RelationKnowledge;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.helpers.collection.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;


@Component
public class RelationKnowledgeDAO
{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Neo4jUtilities utils;


    public RelationKnowledge add(RelationKnowledge obj)
    {
        log.trace(".add " + obj.toString());

        return utils.getTemplate().save(obj);
    }


    public long count()
    {
        log.trace(".count");

        return utils.getTemplate().count(RelationKnowledge.class);
    }


    public void delete(RelationKnowledge obj)
    {
        log.trace(".delete " + obj.toString());

        utils.getTemplate().delete(obj);
    }


    public RelationKnowledge find(Long ciId, String name, String namespace)
    {
        log.trace(".findMatching " + namespace + ":" + name + " with ciId: " + ciId);

        try
        {

            // Assemble parameters
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ciId", ciId);
            params.put("name", name);
            params.put("namespace", namespace);

            // Issue query
            RelationKnowledge result = utils
                    .getTemplate()
                    .query("START ci = node({ciId}) MATCH ci <-[:RKPart_Of]- rk WHERE rk.name = {name} AND rk.namespace = {namespace} RETURN rk",
                            params)
                    .to(RelationKnowledge.class).singleOrNull();

            return result;
        }
        catch (NotFoundException e)
        {
            log.debug("Unable to find matching node", e);
            return null;
        }
    }


    public RelationKnowledge get(Long id)
    {
        log.trace(".get " + id);

        try
        {
            final RelationKnowledge rk = utils.getTemplate().findOne(id, RelationKnowledge.class);

            // If a node exists but is of the wrong type
            if (!RelationKnowledge.class.equals(utils.getTemplate().getStoredJavaType(rk)))
                throw new WrongNodeTypeException("Unable to find RelationKnowledge with ID " + id);

            // Success
            else
                return rk;
        }

        // Thrown when no node can be found
        catch (DataRetrievalFailureException e)
        {
            throw new NotFoundException("Unable to find RelationKnowledge with ID " + id, e);
        }
        
        // Thrown when node has been been deleted in this transaction
        catch (IllegalStateException e)
        {
            throw new NotFoundException("Unable to find RelationKnowledge with ID " + id, e);
        }
    }


    public List<RelationKnowledge> listAll()
    {
        log.trace(".listAll");

        List<RelationKnowledge> list = Iterables.toList(utils.getTemplate().findAll(RelationKnowledge.class));
        Collections.sort(list);
        return list;
    }


    public List<RelationKnowledge> listByCurriculumItem(Long id)
    {
        log.trace(".listByCurriculumItem " + id);

        // Assemble parameters
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        // Issue query
        Iterable<RelationKnowledge> result = utils.getTemplate()
                .query("START n=node({id}) MATCH (n) <-[:RKPart_Of]- (rk) RETURN rk ORDER BY rk.name;", params)
                .to(RelationKnowledge.class);

        // Create result list
        List<RelationKnowledge> list = Iterables.toList(result);

        return list;
    }


    public void update(RelationKnowledge obj)
    {
        log.trace(".update " + obj.toString());

        utils.getTemplate().save(obj);
    }
}
