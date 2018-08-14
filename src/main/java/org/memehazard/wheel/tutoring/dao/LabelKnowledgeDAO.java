package org.memehazard.wheel.tutoring.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.model.LabelKnowledge;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.helpers.collection.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;


@Component
public class LabelKnowledgeDAO
{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Neo4jUtilities utils;


    public LabelKnowledge add(LabelKnowledge obj)
    {
        log.trace(".add " + obj.toString());

        return utils.getTemplate().save(obj);
    }


    public long count()
    {
        log.trace(".count");

        return utils.getTemplate().count(LabelKnowledge.class);
    }


    public void delete(LabelKnowledge obj)
    {
        log.trace(".delete " + obj.toString());

        utils.getTemplate().delete(obj);
    }


    public LabelKnowledge get(Long id)
    {
        log.trace(".get " + id);

        try
        {
            final LabelKnowledge lk = utils.getTemplate().findOne(id, LabelKnowledge.class);

            // If a node exists but is of the wrong type
            if (!LabelKnowledge.class.equals(utils.getTemplate().getStoredJavaType(lk)))
                throw new WrongNodeTypeException("Unable to find LabelKnowledge with ID " + id);

            // Success
            else
                return lk;
        }

        // Thrown when no node can be found
        catch (DataRetrievalFailureException e)
        {
            throw new NotFoundException("Unable to find LabelKnowledge with ID " + id, e);
        }

        // Thrown when node has been been deleted in this transaction
        catch (IllegalStateException e)
        {
            throw new NotFoundException("Unable to find LabelKnowledge with ID " + id, e);
        }
    }


    public LabelKnowledge findByCurriculumItem(Long ciId)
    {
        log.trace(".findByCurriculumItem with ciId: " + ciId);

        try
        {
            // Assemble parameters
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ciId", ciId);

            // Issue query
            LabelKnowledge result = utils
                    .getTemplate()
                    .query("START ci = node({ciId}) MATCH ci <-[:LKPart_Of]- rk RETURN rk",
                            params)
                    .to(LabelKnowledge.class).singleOrNull();

            return result;
        }
        catch (NotFoundException e)
        {
            log.debug("Unable to find matching node", e);
            return null;
        }
    }


    public List<LabelKnowledge> listAll()
    {
        log.trace(".listAll");

        List<LabelKnowledge> list = Iterables.toList(utils.getTemplate().findAll(LabelKnowledge.class));
        Collections.sort(list);
        return list;
    }


    // public List<LabelKnowledge> listByCurriculumItem(Long id)
    // {
    // log.trace(".listByCurriculumItem " + id);
    //
    // // Assemble parameters
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("id", id);
    //
    // // Issue query
    // Iterable<LabelKnowledge> result = utils.getTemplate()
    // .query("START n=node({id}) MATCH (n) <-[:LKPart_Of]- (lk) RETURN lk;", params)
    // .to(LabelKnowledge.class);
    //
    // // Create result list
    // List<LabelKnowledge> list = Iterables.toList(result);
    //
    // return list;
    // }


    public void update(LabelKnowledge obj)
    {
        log.trace(".update " + obj.toString());

        utils.getTemplate().save(obj);
    }
}
