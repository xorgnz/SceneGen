package org.memehazard.wheel.query.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public class CsvParser implements Parser
{
    private Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public List<Entity> parseEntities(String s) throws ParserException
    {
        List<Entity> entities = new ArrayList<Entity>();

        try
        {
            // Parse CSV
            CSVReader reader = new CSVReader(new StringReader(s));
            String[] headers = reader.readNext();
            List<String[]> lines = reader.readAll();
            reader.close();

            // Validate file (uses headers to validate)
            if (headers.length < 2)
                throw new ParserException("Cannot parse CSV - insufficient headers");

            // Identify label column
            int labelColumn = -1;
            if ("fmalabel".equalsIgnoreCase(headers[0]))
                labelColumn = 0;
            else if ("fmalabel".equalsIgnoreCase(headers[1]))
                labelColumn = 1;
            else
                throw new ParserException("Cannot parse CSV - missing label column");

            // Identify ID column
            int idColumn = -1;
            if ("fmaid".equalsIgnoreCase(headers[0]))
                idColumn = 0;
            else if ("fmaid".equalsIgnoreCase(headers[1]))
                idColumn = 1;
            else
                throw new ParserException("Cannot parse CSV - missing id column");

            // Create entities
            for (String[] line : lines)
            {
                if (line.length != headers.length)
                    log.trace("Skipping row - has different column count than header row");
                else
                {
                    Integer id = Integer.parseInt(line[idColumn]);
                    Entity e = new Entity("", id, line[labelColumn]);

                    if (headers.length > 2)
                    {
                        for (int i = 2; i < headers.length; i++)
                            e.addDataItem(headers[i], line[i]);
                    }

                    entities.add(e);
                }
            }

        }
        catch (IOException ioe)
        {
            throw new ParserException("Unable to read input", ioe);
        }


        return entities;
    }


    @Override
    public List<Relationship> parseRelationships(String s) throws ParserException
    {
        // TODO - Implement CsvParser.parseRelationships
        // TODO - Test CsvParser.parseRelationships
        return null;
    }

}
