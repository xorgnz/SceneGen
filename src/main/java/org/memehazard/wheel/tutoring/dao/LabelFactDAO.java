package org.memehazard.wheel.tutoring.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.model.LabelFact;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.helpers.collection.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;

@Component
public class LabelFactDAO
{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Neo4jUtilities utils;


    public LabelFact add(LabelFact obj)
    {
        log.trace(".add " + obj.toString());

        return utils.getTemplate().save(obj);
    }


    public long count()
    {
        log.trace(".count");

        return utils.getTemplate().count(LabelFact.class);
    }


    public void delete(LabelFact obj)
    {
        log.trace(".delete " + obj.toString());

        utils.getTemplate().delete(obj);
    }


    public void deleteMultiple(Collection<LabelFact> facts)
    {
        log.trace(".deleteMultiple " + facts.toString());

        for (LabelFact f : facts)
            utils.getTemplate().delete(f);
    }


    public LabelFact findByEntityKnowledge(Long nodeId)
    {
        log.trace(".findByEntityKnowledge");

        // Assemble parameters
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", nodeId);

        String query = "START n=node({id}) " +
                       "MATCH (n) <-[:Fact_Lbl_Entity]- (fact)" +
                       "RETURN DISTINCT fact;";

        // Issue query
        return utils.getTemplate()
                .query(query, params)
                .to(LabelFact.class).singleOrNull();
    }


    public LabelFact get(Long id)
    {
        log.trace(".get " + id);

        try
        {
            final LabelFact f = utils.getTemplate().findOne(id, LabelFact.class);

            // If a node exists but is of the wrong type
            if (!LabelFact.class.equals(utils.getTemplate().getStoredJavaType(f)))
                throw new WrongNodeTypeException("Unable to find Fact with ID " + id);

            // Success
            else
                return f;
        }

        // Thrown when no node can be found
        catch (DataRetrievalFailureException e)
        {
            throw new NotFoundException("Unable to find Fact with ID " + id, e);
        }

        // Thrown when node has been been deleted in this transaction
        catch (IllegalStateException e)
        {
            throw new NotFoundException("Unable to find Fact with ID " + id, e);
        }
    }


    public List<LabelFact> listByCurriculumItem(Long id)
    {
        log.trace(".listByCurriculumItem");

        // Assemble parameters
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        String query = "START n=node({id}) " +
                       "MATCH (n) <-[:LKPart_Of|EKPart_Of]- (k) <-[:Fact_Lbl_Entity|Fact_Lbl_Knowl]- (fact)" +
                       "RETURN DISTINCT fact;";

        // Issue query
        Iterable<LabelFact> result = utils.getTemplate()
                .query(query, params)
                .to(LabelFact.class);

        // Create result list
        return Iterables.toList(result);
    }


    public List<LabelFact> listByLabelKnowledge(Long nodeId)
    {
        log.trace(".listByLabelKnowledge");

        // Assemble parameters
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", nodeId);

        String query = "START n=node({id}) " +
                       "MATCH (n) <-[:Fact_Lbl_Entity|Fact_Lbl_Knowl]- (fact)" +
                       "RETURN DISTINCT fact;";

        // Issue query
        Iterable<LabelFact> result = utils.getTemplate()
                .query(query, params)
                .to(LabelFact.class);

        // Create result list
        return Iterables.toList(result);
    }


    public void update(LabelFact obj)
    {
        log.trace(".add " + obj.toString());

        utils.getTemplate().save(obj);
    }


}
