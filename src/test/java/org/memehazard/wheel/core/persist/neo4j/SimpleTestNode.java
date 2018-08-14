package org.memehazard.wheel.core.persist.neo4j;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class SimpleTestNode
{
    @GraphId
    public Long id;


    public SimpleTestNode()
    {

    }


    public Long getId()
    {
        return id;
    }


    public void setId(Long id)
    {
        this.id = id;
    }


    @Override
    public String toString()
    {
        return "SimpleTestNode - " + id;
    }
}