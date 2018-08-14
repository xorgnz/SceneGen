package org.memehazard.wheel.core.persist.neo4j;

import java.util.List;

import org.neo4j.helpers.collection.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SimpleTestNodeDAO
{
    @Autowired
    Neo4jUtilities utils;


    public SimpleTestNode add(SimpleTestNode c)
    {
        return utils.getTemplate().save(c);
    }


    public long count()
    {
        return utils.getTemplate().count(SimpleTestNode.class);
    }


    public void delete(Long id)
    {
        utils.getTemplate().delete(id);
    }


    public SimpleTestNode get(Long id)
    {
        return utils.getTemplate().findOne(id, SimpleTestNode.class);
    }


    public List<SimpleTestNode> listAll()
    {
        return Iterables.toList(utils.getTemplate().findAll(SimpleTestNode.class));
    }


    public void update(SimpleTestNode c)
    {
        utils.getTemplate().save(c);
    }
}
