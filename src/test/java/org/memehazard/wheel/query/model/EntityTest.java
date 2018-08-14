package org.memehazard.wheel.query.model;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.query.model.Entity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class EntityTest
{
    private static final String[]  ENT_RES_STRINGS = { "http://purl.org/sig/fma#Thoracic_vertebra", "http://purl.org/sig/fma#Right_tenth_rib" };
    private static final Integer[] ENT_IDS         = new Integer[] { 9139, 8445 };
    private static final String[]  ENT_NAMES       = new String[] { "Thoracic vertebra", "Right tenth rib" };


    @Test
    public void test_equals() throws IOException
    {
        Entity e0 = new Entity(ENT_RES_STRINGS[0], ENT_IDS[0], ENT_NAMES[0]);
        Entity e1 = new Entity(ENT_RES_STRINGS[0], ENT_IDS[0], ENT_NAMES[0]);
        Entity e2 = new Entity(ENT_RES_STRINGS[1], ENT_IDS[1], ENT_NAMES[1]);

        Assert.assertTrue("Equals - equal objects", e0.equals(e1));
        Assert.assertFalse("Equals - unequal objects", e0.equals(e2));
    }


    @Test
    public void test_hashCode() throws IOException
    {
        Entity e0 = new Entity(ENT_RES_STRINGS[0], ENT_IDS[0], ENT_NAMES[0]);
        Entity e1 = new Entity(ENT_RES_STRINGS[0], ENT_IDS[0], ENT_NAMES[0]);
        Entity e2 = new Entity(ENT_RES_STRINGS[1], ENT_IDS[1], ENT_NAMES[1]);

        Assert.assertEquals(e0.hashCode(), e1.hashCode());
        Assert.assertFalse("Hashcode - unequal objects", e0.hashCode() == e2.hashCode());
    }
}
