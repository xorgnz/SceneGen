package org.memehazard.wheel.query.parser;

import java.util.List;

import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Relationship;

public interface Parser
{
    public List<Entity> parseEntities(String s) throws ParserException;


    public List<Relationship> parseRelationships(String s) throws ParserException;
}
