package org.memehazard.wheel.query.view;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.view.EntityDescriptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class EntityDescriptorTest
{
    private static final String[]  ENT_RES_STRINGS = { "http://purl.org/sig/fma#Thoracic_vertebra", "http://purl.org/sig/fma#Right_tenth_rib" };
    private static final Integer[] ENT_FMAIDS         = new Integer[] { 9139, 8445 };
    private static final String[]  ENT_NAMES          = new String[] { "Thoracic vertebra", "Right tenth rib" };


    @Test
    public void test_equals() throws IOException
    {
        Entity e0 = new Entity(ENT_RES_STRINGS[0], ENT_FMAIDS[0], ENT_NAMES[0]);
        Entity e1 = new Entity(ENT_RES_STRINGS[0], ENT_FMAIDS[0], ENT_NAMES[0]);
        Entity e2 = new Entity(ENT_RES_STRINGS[1], ENT_FMAIDS[1], ENT_NAMES[1]);
        EntityDescriptor ed0 = new EntityDescriptor(e0);
        EntityDescriptor ed1 = new EntityDescriptor(e1);
        EntityDescriptor ed2 = new EntityDescriptor(e2);

        Assert.assertTrue("Equals - equal objects", ed0.equals(ed1));
        Assert.assertFalse("Equals - unequal objects", ed0.equals(ed2));
    }


    @Test
    public void test_hashCode() throws IOException
    {
        Entity e0 = new Entity(ENT_RES_STRINGS[0], ENT_FMAIDS[0], ENT_NAMES[0]);
        Entity e1 = new Entity(ENT_RES_STRINGS[0], ENT_FMAIDS[0], ENT_NAMES[0]);
        Entity e2 = new Entity(ENT_RES_STRINGS[1], ENT_FMAIDS[1], ENT_NAMES[1]);
        EntityDescriptor ed0 = new EntityDescriptor(e0);
        EntityDescriptor ed1 = new EntityDescriptor(e1);
        EntityDescriptor ed2 = new EntityDescriptor(e2);

        Assert.assertEquals(ed0.hashCode(), ed1.hashCode());
        Assert.assertFalse("Hashcode - unequal objects", ed0.hashCode() == ed2.hashCode());
    }
}
