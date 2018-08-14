package org.memehazard.wheel.tutoring.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.model.RelationFact;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.helpers.collection.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;

@Component
public class RelationFactDAO
{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Neo4jUtilities utils;


    public RelationFact add(RelationFact obj)
    {
        log.trace(".add " + obj.toString());

        return utils.getTemplate().save(obj);
    }


    public long count()
    {
        log.trace(".count");

        return utils.getTemplate().count(RelationFact.class);
    }


    public void delete(RelationFact obj)
    {
        log.trace(".delete " + obj.toString());

        utils.getTemplate().delete(obj);
    }


    public void deleteMultiple(Collection<RelationFact> facts)
    {
        log.trace(".deleteMultiple " + facts.toString());

        for (RelationFact f : facts)
            utils.getTemplate().delete(f);
    }


    public RelationFact find(long subjectId, long relationId, long objectId)
    {
        log.trace(".find");

        // Assemble parameters
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("subjectId", subjectId);
        params.put("relationId", relationId);
        params.put("objectId", objectId);

        // Issue query
        return utils.getTemplate()
                .query("START subj=node({subjectId}), obj=node({objectId}), rel=node({relationId})" +
                       " MATCH (subj) <-[:Fact_Subj]- (fact), (obj) <-[:Fact_Obj]- (fact), (rel) <-[:Fact_Rel]- (fact)" +
                       " RETURN fact;", params)
                .to(RelationFact.class).singleOrNull();
    }


    public RelationFact get(Long id)
    {
        log.trace(".get " + id);

        try
        {
            final RelationFact f = utils.getTemplate().findOne(id, RelationFact.class);

            // If a node exists but is of the wrong type
            if (!RelationFact.class.equals(utils.getTemplate().getStoredJavaType(f)))
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


    public List<RelationFact> listByAnyEntity(Long id)
    {
        log.trace(".listByAnyEntity " + id);

        // Assemble parameters
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        // Issue query
        Iterable<RelationFact> result = utils.getTemplate()
                .query("START n=node({id}) MATCH (n) <-[:Fact_Subj|Fact_Obj]- (fact) RETURN fact;", params)
                .to(RelationFact.class);

        // Create result list
        List<RelationFact> facts = Iterables.toList(result);

        if (facts.size() == 1 && facts.get(0) == null)
            facts.remove(0);

        return facts;
    }


    public List<RelationFact> listByCurriculumItem(Long id)
    {
        log.trace(".listByCurriculumItem");

        // Assemble parameters
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        String query = "START n=node({id}) " +
                       "MATCH (n) <-[:RKPart_Of|EKPart_Of]- (rel) <-[:Fact_Rel|Fact_Subj|Fact_Obj]- (fact)" +
                       "RETURN DISTINCT fact;";

        // Issue query
        Iterable<RelationFact> result = utils.getTemplate()
                .query(query, params)
                .to(RelationFact.class);

        // Create result list
        return Iterables.toList(result);
    }


    public List<RelationFact> listByObjectEntity(Long id)
    {
        log.trace(".listByObjectEntity " + id);

        // Assemble parameters
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        // Issue query
        Iterable<RelationFact> result = utils.getTemplate()
                .query("START n=node({id}) MATCH (n) <-[:Fact_Obj]- (fact) RETURN fact;", params)
                .to(RelationFact.class);

        // Create result list
        return Iterables.toList(result);
    }


    public List<RelationFact> listByRelation(Long id)
    {
        log.trace(".listByRelation " + id);

        // Assemble parameters
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        // Issue query
        Iterable<RelationFact> result = utils.getTemplate()
                .query("START n=node({id}) MATCH (n) <-[:Fact_Rel]- (fact) RETURN fact;", params)
                .to(RelationFact.class);

        // Create result list
        return Iterables.toList(result);
    }


    public List<RelationFact> listBySubjectEntity(Long id)
    {
        log.trace(".listBySubjectEntity " + id);

        // Assemble parameters
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        // Issue query
        Iterable<RelationFact> result = utils.getTemplate()
                .query("START n=node({id}) MATCH (n) <-[:Fact_Subj]- (fact) RETURN fact;", params)
                .to(RelationFact.class);

        // Create result list
        return Iterables.toList(result);
    }


    public void update(RelationFact obj)
    {
        log.trace(".add " + obj.toString());

        utils.getTemplate().save(obj);
    }
}
