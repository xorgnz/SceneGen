package org.memehazard.wheel.tutoring.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.model.EntityKnowledge;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.helpers.collection.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;


@Component
public class EntityKnowledgeDAO
{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Neo4jUtilities utils;


    public EntityKnowledge add(EntityKnowledge obj)
    {
        log.info(".add " + obj.toString());

        return utils.getTemplate().save(obj);
    }


    public long count()
    {
        log.info(".count");

        return utils.getTemplate().count(EntityKnowledge.class);
    }


    public void delete(EntityKnowledge obj)
    {
        log.info(".delete " + obj.toString());

        utils.getTemplate().delete(obj);
    }


    public void deleteMultiple(Collection<EntityKnowledge> eks)
    {
        log.info(".deleteMultiple " + eks.toString());

        for (EntityKnowledge ek : eks)
            utils.getTemplate().delete(ek);
    }


    public List<EntityKnowledge> findOrphanedInList(Collection<EntityKnowledge> eks)
    {
        log.info(".findDetachedInList " + eks.toString());

        try
        {
            // Assemble parameters
            List<String> ids = new ArrayList<String>();
            for (EntityKnowledge ek : eks)
                ids.add(ek.getNodeId().toString());
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ids", ids);

            // Issue query
            List<EntityKnowledge> result = new ArrayList<EntityKnowledge>();
            for (EntityKnowledge ek : utils.getTemplate().query("START ek = node({ids}) WHERE NOT (ek -- ()) RETURN ek;", params)
                    .to(EntityKnowledge.class))
            {
                result.add(ek);
            }

            return result;
        }
        catch (NotFoundException nfe)
        {
            log.debug("Unable to find any detached EntityKnowledge in given list", nfe);
            return new ArrayList<EntityKnowledge>();
        }
    }


    public EntityKnowledge findLinkedByCurriculumItemAndEntity(Long ciId, Integer fmaId)
    {
        log.info(".findByCurriculumItemAndEntity ciId: " + ciId + ", fmaId: " + fmaId);

        try
        {
            // Assemble parameters
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ciId", ciId);
            params.put("fmaId", fmaId);

            // Issue query
            EntityKnowledge result = utils.getTemplate()
                    .query("START ci = node({ciId}) MATCH ci <-[:EKPart_Of]- ek WHERE ek.fmaId = {fmaId} RETURN ek", params)
                    .to(EntityKnowledge.class).singleOrNull();

            return result;

        }
        catch (NotFoundException nfe)
        {
            log.debug("Unable to find EntityKnowledge with associated CI " + ciId + " and fmaId " + fmaId, nfe);
            return null;
        }
    }


    public EntityKnowledge findHangingByCurriculumItemAndEntity(Long ciId, Integer fmaId)
    {
        log.info(".findHangingByCurriculumItemAndEntity ciId: " + ciId + ", fmaId: " + fmaId);

        try
        {
            // Assemble parameters
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ciId", ciId);
            params.put("fmaId", fmaId);

            // Issue query
            EntityKnowledge result = utils
                    .getTemplate()
                    .query("START ci = node({ciId}) "
                           +
                           "MATCH (ci) <-[:RKPart_Of]- (rk) <-[:Fact_Rel]- (fact) -[:Fact_Obj|Fact_Subj]-> (ek) WHERE NOT(ek --> ci) AND ek.fmaId = {fmaId} RETURN DISTINCT ek",
                            params)
                    .to(EntityKnowledge.class)
                    .singleOrNull();

            return result;
        }
        catch (NotFoundException nfe)
        {
            log.debug("Unable to find hanging EntityKnowledge with associated CI " + ciId + " and fmaId " + fmaId, nfe);
            return null;
        }
        catch (DataAccessException e)
        {
            log.debug("Unable to find hanging EntityKnowledge with associated CI " + ciId + " and fmaId " + fmaId, e);
            return null;
        }
    }


    public EntityKnowledge get(Long id)
    {
        log.trace(".get " + id);

        try
        {
            final EntityKnowledge ek = utils.getTemplate().findOne(id, EntityKnowledge.class);

            // If a node exists but is of the wrong type
            if (!EntityKnowledge.class.equals(utils.getTemplate().getStoredJavaType(ek)))
                throw new WrongNodeTypeException("Unable to find EntityKnowledge with ID " + id);

            // Success
            else
                return ek;
        }

        // Thrown when no node can be found
        catch (DataRetrievalFailureException e)
        {
            throw new NotFoundException("Unable to find EntityKnowledge with ID " + id, e);
        }
        
        // Thrown when node has been been deleted in this transaction
        catch (IllegalStateException e)
        {
            throw new NotFoundException("Unable to find EntityKnowledge with ID " + id, e);
        }
    }


    public List<EntityKnowledge> listLinkedByCurriculumItem(Long id)
    {
        try
        {
            log.info(".listLinkedByCurriculumItem " + id);

            // Assemble parameters
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);

            // Issue query
            Iterable<EntityKnowledge> result = utils.getTemplate()
                    .query("START ci = node({id}) MATCH (ci) <-[:EKPart_Of]- (ek) RETURN ek ORDER BY ek.fmaLabel;", params)
                    .to(EntityKnowledge.class);

            // Create result list
            List<EntityKnowledge> list = Iterables.toList(result);

            return list;
        }
        catch (DataAccessException e)
        {
            log.debug("Unable to find linked EK nodes with associated CI " + id, e);
            return new ArrayList<EntityKnowledge>();
        }
    }


    public List<EntityKnowledge> listHangingByCurriculumItem(Long id)
    {
        try
        {
            log.info(".listHangingByCurriculumItem " + id);

            // Assemble parameters
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);

            String query = "START ci = node({id}) " +
                           "MATCH (ci) <-[:RKPart_Of]- (rk) <-[:Fact_Rel]- (fact) -[:Fact_Obj|Fact_Subj]-> (ek) " +
                           "WHERE NOT(ek --> ci) " +
                           "RETURN DISTINCT ek " +
                           "ORDER BY ek.fmaLabel;";

            // Issue query
            Iterable<EntityKnowledge> result = utils
                    .getTemplate()
                    .query(query, params)
                    .to(EntityKnowledge.class);

            // Create result list
            List<EntityKnowledge> list = Iterables.toList(result);

            return list;
        }
        catch (DataAccessException e)
        {
            log.debug("Unable to find hanging EK nodes with associated CI " + id, e);
            return new ArrayList<EntityKnowledge>();
        }
    }


    public void update(EntityKnowledge obj)
    {
        log.info(".update " + obj.toString());

        utils.getTemplate().save(obj);
    }
}
