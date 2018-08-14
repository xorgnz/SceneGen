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
import org.memehazard.wheel.query.parser.CsvParser;
import org.memehazard.wheel.query.parser.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class FmaParserCsvTest
{
    @SuppressWarnings("unused")
    private Logger                 log                       = LoggerFactory.getLogger(this.getClass());
    private static final String    FN_TEST_CSV               = "org/memehazard/wheel/query/parser/test.csv";
    private static final String    FN_TEST_CSV_MISSING_ID    = "org/memehazard/wheel/query/parser/test_idmissing.csv";
    private static final String    FN_TEST_CSV_MISSING_LABEL = "org/memehazard/wheel/query/parser/test_labelmissing.csv";
    private static final String    FN_TEST_CSV_DIFF_ORDER    = "org/memehazard/wheel/query/parser/test_differentorder.csv";
    private static final String    FN_TEST_CSV_BROKEN_ROWS   = "org/memehazard/wheel/query/parser/test_brokenrows.csv";
    private static final Integer[] ENT_FMAIDS                = new Integer[] { 10014, 10037, 10059, 9139, 8445, 8472 };
    private static final String[]  ENT_NAMES                 = new String[] {
                                                             "Ninth thoracic vertebra",
                                                             "Tenth thoracic vertebra",
                                                             "Eleventh thoracic vertebra",
                                                             "Thoracic vertebra",
                                                             "Right tenth rib",
                                                             "Left tenth rib" };

    private static final String[]  ENT_DATA1                 = new String[] { "data1_0", "data1_1", "data1_2", "data1_3", "data1_4", "data1_5" };
    private static final String[]  ENT_DATA2                 = new String[] { "data2_0", "data2_1", "data2_2", "data2_3", "data2_4", "data2_5" };


    @Test
    public void test_parseEntities()
            throws ParserException, IOException
    {
        // ACT - Load entities
        CsvParser parser = new CsvParser();
        InputStream is = getClass().getClassLoader().getResourceAsStream(FN_TEST_CSV);
        List<Entity> entities = parser.parseEntities(new String(IOUtils.toByteArray(is), StandardCharsets.UTF_8));

        // ACT - Mapify entity list
        Map<Integer, Entity> entityMap = new HashMap<Integer, Entity>();
        for (Entity e : entities)
            entityMap.put(e.getId(), e);

        // TEST - Correct number of entities
        Assert.assertEquals(6, entities.size());

        // TEST - All expected entities are present
        for (int i = 0; i < 6; i++)
        {
            Assert.assertTrue("Find Entity " + ENT_NAMES[i], entityMap.containsKey(ENT_FMAIDS[i]));
            Assert.assertEquals(ENT_NAMES[i], entityMap.get(ENT_FMAIDS[i]).getName());
            Assert.assertEquals(ENT_FMAIDS[i], entityMap.get(ENT_FMAIDS[i]).getId());
        }

        // TEST - Entities have correct data payload
        for (int i = 0; i < 6; i++)
        {
            Entity e = entityMap.get(ENT_FMAIDS[i]);
            Assert.assertEquals(ENT_DATA1[i], e.getData().get("data1"));
            Assert.assertEquals(ENT_DATA2[i], e.getData().get("data2"));
        }
    }


    @Test(expected = ParserException.class)
    public void test_parseEntities_MissingIdHeader()
            throws ParserException, IOException
    {
        // ACT - Load entities
        CsvParser parser = new CsvParser();
        InputStream is = getClass().getClassLoader().getResourceAsStream(FN_TEST_CSV_MISSING_ID);
        parser.parseEntities(new String(IOUtils.toByteArray(is), StandardCharsets.UTF_8));
    }


    @Test(expected = ParserException.class)
    public void test_parseEntities_MissingLabelHeader()
            throws ParserException, IOException
    {
        // ACT - Load entities
        CsvParser parser = new CsvParser();
        InputStream is = getClass().getClassLoader().getResourceAsStream(FN_TEST_CSV_MISSING_LABEL);
        parser.parseEntities(new String(IOUtils.toByteArray(is), StandardCharsets.UTF_8));
    }


    @Test
    public void test_parseEntities_DifferentHeaderOrder()
            throws ParserException, IOException
    {
        // ACT - Load entities
        CsvParser parser = new CsvParser();
        InputStream is = getClass().getClassLoader().getResourceAsStream(FN_TEST_CSV_DIFF_ORDER);
        List<Entity> entities = parser.parseEntities(new String(IOUtils.toByteArray(is), StandardCharsets.UTF_8));

        // ACT - Mapify entity list
        Map<Integer, Entity> entityMap = new HashMap<Integer, Entity>();
        for (Entity e : entities)
            entityMap.put(e.getId(), e);

        // TEST - Correct number of entities
        Assert.assertEquals(6, entities.size());

        // TEST - All expected entities are present
        for (int i = 0; i < ENT_FMAIDS.length; i++)
        {
            Assert.assertTrue("Find Entity " + ENT_NAMES[i], entityMap.containsKey(ENT_FMAIDS[i]));
            Assert.assertEquals(ENT_NAMES[i], entityMap.get(ENT_FMAIDS[i]).getName());
            Assert.assertEquals(ENT_FMAIDS[i], entityMap.get(ENT_FMAIDS[i]).getId());
        }
    }


    @Test
    public void test_parseEntities_BrokenRows()
            throws ParserException, IOException
    {
        // ACT - Load entities
        CsvParser parser = new CsvParser();
        InputStream is = getClass().getClassLoader().getResourceAsStream(FN_TEST_CSV_BROKEN_ROWS);
        List<Entity> entities = parser.parseEntities(new String(IOUtils.toByteArray(is), StandardCharsets.UTF_8));

        // ACT - Mapify entity list
        Map<Integer, Entity> entityMap = new HashMap<Integer, Entity>();
        for (Entity e : entities)
            entityMap.put(e.getId(), e);

        // TEST - Correct number of entities
        Assert.assertEquals(6, entities.size());

        // TEST - All expected entities are present
        for (int i = 0; i < 4; i++)
        {
            Assert.assertTrue("Find Entity " + ENT_NAMES[i], entityMap.containsKey(ENT_FMAIDS[i]));
            Assert.assertEquals(ENT_NAMES[i], entityMap.get(ENT_FMAIDS[i]).getName());
            Assert.assertEquals(ENT_FMAIDS[i], entityMap.get(ENT_FMAIDS[i]).getId());
        }
    }
}
