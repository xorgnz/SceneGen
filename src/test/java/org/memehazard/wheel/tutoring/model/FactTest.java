package org.memehazard.wheel.tutoring.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class FactTest
{
    @SuppressWarnings("unused")
    private Logger                 log          = LoggerFactory.getLogger(this.getClass());

    private static final Integer[] EK_FMA_ID    = { 1001, 1002, 1003, 1004, 1005, 1006 };
    private static final String[]  EK_FMA_LABEL = { "ENTITY 6", "ENTITY 5", "ENTITY 4", "ENTITY 3", "ENTITY 2", "ENTITY 1" };

    private static final String[]  RK_NAME      = { "Relation 0", "Relation 1", "Relation 2" };
    private static final String    RK_NAMESPACE = "ns0";


    /**
     * Check whether Facts update their probability correctly
     *
     * Check:
     * - No update occurs if time interval less than 20 minutes
     * - Positive update halves probability of 'bad' estimate.
     * - Negative update halves probability of 'good' estimate.
     */
    @Test
    public void testUpdateBoolean()
    {
        // PREP - Create timestamps
        Date now = new Date();
        Date past = new Date(now.getTime() - 1210000);

        // PREP - Create test objects
        EntityKnowledge ek0 = new EntityKnowledge(EK_FMA_ID[0], EK_FMA_LABEL[0], null);
        EntityKnowledge ek1 = new EntityKnowledge(EK_FMA_ID[0], EK_FMA_LABEL[0], null);
        RelationKnowledge rk = new RelationKnowledge(RK_NAME[0], RK_NAMESPACE, null);
        RelationFact f = new RelationFact(ek0, rk, ek1);
        BayesValue bv_noTimeInterval = new BayesValue(0, 1000, 0.5, now);
        BayesValue bv_positive = new BayesValue(0, 1000, 0.5, past);
        BayesValue bv_negative = new BayesValue(0, 1000, 0.5, past);

        // ACT - Attempt updates
        f.updateBoolean(bv_noTimeInterval, true);
        f.updateBoolean(bv_positive, true);
        f.updateBoolean(bv_negative, false);

        // TEST - Correct probability values set
        Assert.assertEquals("Insufficient time interval - should not have updated", 0.5, bv_noTimeInterval.getP(), 0.001);
        Assert.assertEquals("Positive update - incorrect P value", 0.75, bv_positive.getP(), 0.001);
        Assert.assertEquals("Negative update - incorrect P value", 0.25, bv_negative.getP(), 0.001);
    }


    @Test
    public void testIsRepresentedBy()
    {
        // ACT - Prepare test objects
        EntityKnowledge ek0 = new EntityKnowledge(EK_FMA_ID[0], EK_FMA_LABEL[0], null);
        ek0.setNodeId(200L);
        EntityKnowledge ek1 = new EntityKnowledge(EK_FMA_ID[1], EK_FMA_LABEL[1], null);
        ek1.setNodeId(201L);
        EntityKnowledge ek2 = new EntityKnowledge(EK_FMA_ID[2], EK_FMA_LABEL[2], null);
        ek2.setNodeId(202L);

        RelationKnowledge rk0 = new RelationKnowledge(RK_NAME[0], RK_NAMESPACE, null);
        rk0.setNodeId(300L);
        RelationKnowledge rk1 = new RelationKnowledge(RK_NAME[1], RK_NAMESPACE, null);
        rk1.setNodeId(301L);

        RelationFact f0 = new RelationFact(ek0, rk0, ek1);
        f0.setNodeId(100L);

        // TEST - Represented by self
        Assert.assertTrue("Not represented by self", f0.isRepresentedBy(f0));

        // TEST - Represented by clone with different node ID
        RelationFact f0_var = new RelationFact(ek0, rk0, ek1);
        f0_var.setNodeId(99L);
        Assert.assertTrue("Not represented by clone with diff node ID", f0.isRepresentedBy(f0_var));

        // TEST - Not represented by clone with different subject
        f0_var = new RelationFact(ek2, rk0, ek1);
        f0_var.setNodeId(200L);
        Assert.assertFalse("Represented by clone with diff FMA ID", f0.isRepresentedBy(f0_var));

        // TEST - Not represented by clone with different relation
        f0_var = new RelationFact(ek0, rk1, ek1);
        f0_var.setNodeId(200L);
        Assert.assertFalse("Represented by clone with diff FMA Label", f0.isRepresentedBy(f0_var));

        // TEST - Not represented by clone with different object
        f0_var = new RelationFact(ek0, rk0, ek2);
        f0_var.setNodeId(200L);
        Assert.assertFalse("Represented by clone with diff FMA Label", f0.isRepresentedBy(f0_var));
    }


    @Test
    public void testIsRepresentedInList()
    {
        // ACT - Prepare test objects
        EntityKnowledge ek0 = new EntityKnowledge(EK_FMA_ID[0], EK_FMA_LABEL[0], null);
        ek0.setNodeId(200L);
        EntityKnowledge ek1 = new EntityKnowledge(EK_FMA_ID[1], EK_FMA_LABEL[1], null);
        ek1.setNodeId(201L);
        EntityKnowledge ek2 = new EntityKnowledge(EK_FMA_ID[2], EK_FMA_LABEL[2], null);
        ek2.setNodeId(202L);

        RelationKnowledge rk0 = new RelationKnowledge(RK_NAME[0], RK_NAMESPACE, null);
        rk0.setNodeId(300L);
        RelationKnowledge rk1 = new RelationKnowledge(RK_NAME[1], RK_NAMESPACE, null);
        rk1.setNodeId(301L);

        RelationFact f0 = new RelationFact(ek0, rk0, ek1);
        f0.setNodeId(100L);
        RelationFact f1 = new RelationFact(ek0, rk0, ek2);
        f0.setNodeId(101L);
        RelationFact f2 = new RelationFact(ek1, rk1, ek2);
        f0.setNodeId(102L);

        RelationFact f0_var = new RelationFact(ek0, rk0, ek1);
        f0_var.setNodeId(99L);

        // TEST - Represented in list containing clone
        List<RelationFact> fList0 = new ArrayList<RelationFact>();
        fList0.add(f0_var);
        fList0.add(f1);
        fList0.add(f2);
        Assert.assertTrue("Not represented in list containing clone", f0.isRepresentedInList(fList0));

        // TEST - Not represented in list
        List<RelationFact> fList1 = new ArrayList<RelationFact>();
        fList1.add(f1);
        fList1.add(f2);
        Assert.assertFalse("Represented in list not containing clones or self", f0.isRepresentedInList(fList1));
    }
}
