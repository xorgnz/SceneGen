package org.memehazard.wheel.query.view;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.query.model.Relation;
import org.memehazard.wheel.query.view.RelationDescriptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class RelationDescriptorTest
{
    private static final String[] REL_RES_STRINGS = { "http://purl.org/sig/fma#articulates_with", "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" };


    @Test
    public void test_equals() throws IOException
    {
        Relation r0 = new Relation(REL_RES_STRINGS[0]);
        Relation r1 = new Relation(REL_RES_STRINGS[0]);
        Relation r2 = new Relation(REL_RES_STRINGS[1]);
        RelationDescriptor rd0 = new RelationDescriptor(r0);
        RelationDescriptor rd1 = new RelationDescriptor(r1);
        RelationDescriptor rd2 = new RelationDescriptor(r2);

        Assert.assertTrue("Equals - equal objects", rd0.equals(rd1));
        Assert.assertFalse("Equals - unequal objects", rd0.equals(rd2));
    }


    @Test
    public void test_hashCode() throws IOException
    {
        Relation r0 = new Relation(REL_RES_STRINGS[0]);
        Relation r1 = new Relation(REL_RES_STRINGS[0]);
        Relation r2 = new Relation(REL_RES_STRINGS[1]);
        RelationDescriptor rd0 = new RelationDescriptor(r0);
        RelationDescriptor rd1 = new RelationDescriptor(r1);
        RelationDescriptor rd2 = new RelationDescriptor(r2);

        Assert.assertEquals(rd0.hashCode(), rd1.hashCode());
        Assert.assertFalse("Hashcode - unequal objects", rd0.hashCode() == rd2.hashCode());
    }
}
