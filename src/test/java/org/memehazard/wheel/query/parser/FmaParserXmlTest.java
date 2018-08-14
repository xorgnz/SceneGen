package org.memehazard.wheel.query.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Relation;
import org.memehazard.wheel.query.model.Relationship;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.query.parser.QueryIntegratorXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class FmaParserXmlTest
{
    // Entity values
    private static final String[]  ENT_DATA1         = new String[] { "data1_0", "data1_1", "data1_2", "data1_3", "data1_4", "data1_5" };
    private static final String[]  ENT_DATA2         = new String[] { "data2_0", "data2_1", "data2_2", "data2_3", "data2_4", "data2_5" };
    private static final Integer[] ENT_FMAIDS        = new Integer[] { 10014, 10037, 10059, 9139, 8445, 8472 };
    private static final String[]  ENT_NAMES         = new String[] {
                                                     "Ninth thoracic vertebra",
                                                     "Tenth thoracic vertebra",
                                                     "Eleventh thoracic vertebra",
                                                     "Thoracic vertebra",
                                                     "Right tenth rib",
                                                     "Left tenth rib" };
    private static final String[]  ENT_RES_STRINGS   = new String[] {
                                                     "http://purl.org/sig/ont/fma/Ninth_thoracic_vertebra",
                                                     "http://purl.org/sig/ont/fma/Tenth_thoracic_vertebra",
                                                     "http://purl.org/sig/ont/fma/Eleventh_thoracic_vertebra",
                                                     "http://purl.org/sig/ont/fma/Thoracic_vertebra",
                                                     "http://purl.org/sig/ont/fma/Right_tenth_rib",
                                                     "http://purl.org/sig/ont/fma/Left_tenth_rib" };
    // Test file                                                         
    private static final String    FILENAME_TEST_XML = "org/memehazard/wheel/query/parser/test.xml";

    // Relationship values
    private static final int[]     RELS_OBJECTS      = new int[] { 1, 0, 2, 4, 5, 1, 1, 1 };
    private static final String[]  RELS_RELATIONS    = new String[] {
                                                     "http://purl.org/sig/ont/fma/articulates_with",
                                                     "http://purl.org/sig/ont/fma/articulates_with",
                                                     "http://purl.org/sig/ont/fma/articulates_with",
                                                     "http://purl.org/sig/ont/fma/articulates_with",
                                                     "http://purl.org/sig/ont/fma/articulates_with",
                                                     "http://purl.org/sig/ont/fma/articulates_with",
                                                     "http://purl.org/sig/ont/fma/articulates_with",
                                                     "http://purl.org/sig/ont/fma/articulates_with" };
    private static final int[]     RELS_SUBJECTS     = new int[] { 0, 1, 1, 1, 1, 2, 4, 5 };
    
    @SuppressWarnings("unused")
    private Logger                 log               = LoggerFactory.getLogger(this.getClass());


    @Test
    public void test_parseEntities()
            throws ParserException, IOException
    {
        // ACT - Load entities
        String xml = loadXml();
        QueryIntegratorXmlParser parser = new QueryIntegratorXmlParser();
        List<Entity> entities = parser.parseEntities(xml);

        // ACT - Mapify entity list
        Map<String, Entity> entityMap = new HashMap<String, Entity>();
        for (Entity e : entities)
            entityMap.put(e.getResourceString(), e);

        // TEST - Correct number of entities
        Assert.assertEquals(6, entities.size());

        // TEST - All expected entities are present
        for (int i = 0; i < ENT_FMAIDS.length; i++)
        {
            Assert.assertTrue("Find Entity " + ENT_NAMES[i], entityMap.containsKey(ENT_RES_STRINGS[i]));
            Assert.assertEquals(ENT_NAMES[i], entityMap.get(ENT_RES_STRINGS[i]).getName());
            Assert.assertEquals(ENT_FMAIDS[i], entityMap.get(ENT_RES_STRINGS[i]).getId());
        }
        
        // TEST - Entities have correct data payload
        for (int i = 0; i < 6; i++)
        {
            Entity e = entityMap.get(ENT_RES_STRINGS[i]);
            Assert.assertEquals(ENT_DATA1[i], e.getData().get("data1"));
            Assert.assertEquals(ENT_DATA2[i], e.getData().get("data2"));
        }
    }


    @Test
    public void test_parseRelationships()
            throws ParserException, IOException
    {
        // ACT - Load relationships and entities
        String xml = loadXml();
        QueryIntegratorXmlParser parser = new QueryIntegratorXmlParser();
        List<Relationship> relationships = parser.parseRelationships(xml);

        // TEST - Correct number of relationships
        Assert.assertEquals(8, relationships.size());

        for (int i = 0; i < RELS_SUBJECTS.length; i++)
        {
            Entity subject = new Entity(ENT_RES_STRINGS[RELS_SUBJECTS[i]], 0, "Name");
            Entity object = new Entity(ENT_RES_STRINGS[RELS_OBJECTS[i]], 0, "Name");
            Relation relation = new Relation(RELS_RELATIONS[i]);
            Relationship r = new Relationship(subject, relation, object);

            Assert.assertTrue("Find Relationship " + r.toString(), relationships.contains(r));
        }
    }


    private String loadXml()
            throws IOException
    {
        InputStream is = getClass().getClassLoader().getResourceAsStream(FILENAME_TEST_XML);
        return new String(IOUtils.toByteArray(is), StandardCharsets.UTF_8);
    }
}
