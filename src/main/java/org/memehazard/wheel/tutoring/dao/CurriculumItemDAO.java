package org.memehazard.wheel.tutoring.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.helpers.collection.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;


@Repository
public class CurriculumItemDAO
{
    @Autowired
    private Neo4jUtilities utils;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    public CurriculumItem add(CurriculumItem obj)
    {
        log.trace(".add " + obj.toString());

        return utils.getTemplate().save(obj);
    }


    public long count()
    {
        log.trace(".count");

        return utils.getTemplate().count(CurriculumItem.class);
    }


    public void delete(CurriculumItem obj)
    {
        log.trace(".delete " + obj.toString());

        utils.getTemplate().delete(obj);
    }


    public CurriculumItem get(Long id)
    {
        log.trace(".get " + id);

        try
        {
            final CurriculumItem ci = utils.getTemplate().findOne(id, CurriculumItem.class);

            // If a node exists but is of the wrong type
            if (!CurriculumItem.class.equals(utils.getTemplate().getStoredJavaType(ci)))
                throw new WrongNodeTypeException("Unable to find CurriculumItem with ID " + id);

            // Success
            else
                return ci;
        }

        // Thrown when no node can be found
        catch (DataRetrievalFailureException e)
        {
            throw new NotFoundException("Unable to find CurriculumItem with ID " + id, e);
        }

        // Thrown when node has been been deleted in this transaction
        catch (IllegalStateException e)
        {
            throw new NotFoundException("Unable to find CurriculumItem with ID " + id, e);
        }
    }


    public List<CurriculumItem> listByCurriculum(Long id)
    {
        log.trace(".listByCurriculum" + id);

        // Assemble Parameters
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("c_id", id);

        // Issue query
        Iterable<CurriculumItem> result = utils.getTemplate().query("START n=node({c_id}) MATCH (n) <-[]- (ci) RETURN ci ORDER BY ci.name;", params)
                .to(CurriculumItem.class);

        // Create result list
        List<CurriculumItem> list = Iterables.toList(result);

        return list;
    }


    public void update(CurriculumItem obj)
    {
        log.trace(".update " + obj.toString());

        utils.getTemplate().save(obj);
    }
}
