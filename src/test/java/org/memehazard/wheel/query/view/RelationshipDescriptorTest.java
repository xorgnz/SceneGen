package org.memehazard.wheel.query.view;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Relation;
import org.memehazard.wheel.query.model.Relationship;
import org.memehazard.wheel.query.view.RelationshipDescriptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class RelationshipDescriptorTest
{
    private static final String[]  ENT_RES_STRINGS = { "http://purl.org/sig/fma#Thoracic_vertebra", "http://purl.org/sig/fma#Right_tenth_rib" };
    private static final Integer[] ENT_FMAIDS      = new Integer[] { 9139, 8445 };
    private static final String[]  ENT_NAMES       = new String[] { "Thoracic vertebra", "Right tenth rib" };
    private static final String[]  REL_RES_STRINGS = { "http://purl.org/sig/fma#articulates_with", "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" };


    @Test
    public void test_equals() throws IOException
    {
        Entity e0 = new Entity(ENT_RES_STRINGS[0], ENT_FMAIDS[0], ENT_NAMES[0]);
        Entity e1 = new Entity(ENT_RES_STRINGS[1], ENT_FMAIDS[1], ENT_NAMES[1]);
        Relation r0 = new Relation(REL_RES_STRINGS[0]);
        Relation r1 = new Relation(REL_RES_STRINGS[1]);
        Relationship rs0 = new Relationship(e0, r0, e0);
        Relationship rs1 = new Relationship(e0, r0, e0);
        Relationship rs2 = new Relationship(e1, r0, e0);
        Relationship rs3 = new Relationship(e0, r1, e0);
        Relationship rs4 = new Relationship(e0, r0, e1);
        RelationshipDescriptor rsd0 = new RelationshipDescriptor(rs0);
        RelationshipDescriptor rsd1 = new RelationshipDescriptor(rs1);
        RelationshipDescriptor rsd2 = new RelationshipDescriptor(rs2);
        RelationshipDescriptor rsd3 = new RelationshipDescriptor(rs3);
        RelationshipDescriptor rsd4 = new RelationshipDescriptor(rs4);

        Assert.assertTrue("Equals - equal objects", rsd0.equals(rsd1));
        Assert.assertFalse("Equals - unequal objects", rsd0.equals(rsd2));
        Assert.assertFalse("Equals - unequal objects", rsd0.equals(rsd3));
        Assert.assertFalse("Equals - unequal objects", rsd0.equals(rsd4));
    }


    @Test
    public void test_hashCode() throws IOException
    {
        Entity e0 = new Entity(ENT_RES_STRINGS[0], ENT_FMAIDS[0], ENT_NAMES[0]);
        Entity e1 = new Entity(ENT_RES_STRINGS[1], ENT_FMAIDS[1], ENT_NAMES[1]);
        Relation r0 = new Relation(REL_RES_STRINGS[0]);
        Relation r1 = new Relation(REL_RES_STRINGS[1]);

        Relationship rs0 = new Relationship(e0, r0, e0);
        Relationship rs1 = new Relationship(e0, r0, e0);
        Relationship rs2 = new Relationship(e1, r0, e0);
        Relationship rs3 = new Relationship(e0, r1, e0);
        Relationship rs4 = new Relationship(e0, r0, e1);
        RelationshipDescriptor rsd0 = new RelationshipDescriptor(rs0);
        RelationshipDescriptor rsd1 = new RelationshipDescriptor(rs1);
        RelationshipDescriptor rsd2 = new RelationshipDescriptor(rs2);
        RelationshipDescriptor rsd3 = new RelationshipDescriptor(rs3);
        RelationshipDescriptor rsd4 = new RelationshipDescriptor(rs4);


        Assert.assertEquals(rsd0.hashCode(), rsd1.hashCode());
        Assert.assertFalse("Hashcode - unequal objects", rsd0.hashCode() == rsd2.hashCode());
        Assert.assertFalse("Hashcode - unequal objects", rsd0.hashCode() == rsd3.hashCode());
        Assert.assertFalse("Hashcode - unequal objects", rsd0.hashCode() == rsd4.hashCode());
    }
}
