package org.memehazard.wheel.core.persist.neo4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.conversion.ResultConverter;
import org.springframework.data.neo4j.mapping.MappingPolicy;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;


/**
 * Demonstrates creation and retrieval of an arbitrary object using a custom ResultConverter
 *
 * @author xorgnz
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class CustomResultConverterDemonstration
{
    // Components
    private Logger        log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Neo4jTemplate template;


    @BeforeTransaction
    public void beforeTransaction()
    {
        log.trace("------------------- BEFORE TX");
    }


    @Test
    @Transactional
    public void sandbox()
    {
        // Act - Save test object using cypher
        Map<String, Object> insertParams = new HashMap<String, Object>();
        insertParams.put("name", "val: name");
        insertParams.put("description", "val: description");
        template.query("CREATE (Curriculum {name:{name}, description:{description}})", insertParams);

        // Act - Retrieve saved curriculum
        Result<Map<String, Object>> result = template.query(
                "START c = node(*) RETURN ID(c) AS id, c.name AS name, c.description AS description", null);

        // Act - Convert result object into target class
        EndResult<ConversionTargetClass> result_reified = result.to(ConversionTargetClass.class, new Converter());
        List<ConversionTargetClass> resultList = new ArrayList<ConversionTargetClass>();
        for (ConversionTargetClass sc : result_reified)
            resultList.add(sc);

        // Test - Verify correct number of objects returned
        Assert.assertEquals(1, resultList.size());

        // Test - Verify that target object has correct values
        Assert.assertEquals("val: name", resultList.get(0).name);
        Assert.assertEquals("val: description", resultList.get(0).desc);
    }


    public class ConversionTargetClass
    {
        public String id;
        public String name;
        public String desc;
    }


    public class Converter implements ResultConverter<Map<String, Object>, ConversionTargetClass>
    {

        @Override
        public ConversionTargetClass convert(Map<String, Object> value, Class<ConversionTargetClass> type)
        {
            ConversionTargetClass sc = new ConversionTargetClass();

            sc.id = value.get("id").toString();
            sc.name = value.get("name").toString();
            sc.desc = value.get("description").toString();

            return sc;
        }


        @Override
        public ConversionTargetClass convert(Map<String, Object> value, Class<ConversionTargetClass> type, MappingPolicy mappingPolicy)
        {
            return convert(value, type);
        }

    }
}
