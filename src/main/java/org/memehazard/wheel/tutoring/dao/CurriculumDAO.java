package org.memehazard.wheel.tutoring.dao;

import java.util.Collections;
import java.util.List;

import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.helpers.collection.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;


@Component
public class CurriculumDAO
{
    @Autowired
    private Neo4jUtilities utils;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    // private static final String NODE_CLASS_NAME = Curriculum.class.getCanonicalName();


    public Curriculum add(Curriculum c)
    {
        log.trace(".add " + c.toString());

        return utils.getTemplate().save(c);
    }


    public long count()
    {
        log.trace(".count");

        return utils.getTemplate().count(Curriculum.class);
    }


    public void delete(Curriculum c)
    {
        log.trace(".delete " + c.toString());

        utils.getTemplate().delete(c);
    }


    public Curriculum get(Long id)
    {
        log.trace(".get " + id);

        try
        {
            final Curriculum c = utils.getTemplate().findOne(id, Curriculum.class);

            // If a node exists but is of the wrong type
            if (!Curriculum.class.equals(utils.getTemplate().getStoredJavaType(c)))
                throw new WrongNodeTypeException("Unable to find Curriculum with ID " + id);

            // Success
            else
                return c;
        }

        // Thrown when no node can be found
        catch (DataRetrievalFailureException e)
        {
            throw new NotFoundException("Unable to find Curriculum with ID " + id, e);
        }

        // Thrown when node has been been deleted in this transaction
        catch (IllegalStateException e)
        {
            throw new NotFoundException("Unable to find Curriculum with ID " + id, e);
        }
    }


    public List<Curriculum> listAll()
    {
        log.trace(".listAll");

        List<Curriculum> list = Iterables.toList(utils.getTemplate().findAll(Curriculum.class));
        Collections.sort(list);

        return list;
    }


    public void update(Curriculum c)
    {
        log.trace(".update " + c.toString());

        utils.getTemplate().save(c);
    }
}
