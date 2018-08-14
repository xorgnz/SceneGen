package org.memehazard.wheel.tutoring.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.tutoring.test.TestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class RelationKnowledgeTest
{
    private static final String[] RK_NAME        = { "Relation 0", "Relation 1", "Relation 2" };
    private static final String[] RK_NAMESPACE   = { "ns0", "ns1", "ns2" };


    @SuppressWarnings("unused")
    private Logger                log            = LoggerFactory.getLogger(this.getClass());


    @Test
    public void testIsRepresentedBy()
    {
        // ACT - Prepare test objects
        CurriculumItem ci0 = new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]);
        ci0.setNodeId(100L);
        CurriculumItem ci1 = new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1]);
        ci1.setNodeId(101L);
        RelationKnowledge rk0 = new RelationKnowledge(RK_NAME[0], RK_NAMESPACE[0], ci0);
        rk0.setNodeId(200L);

        // TEST - Represented by self
        Assert.assertTrue("Not represented by self", rk0.isRepresentedBy(rk0));

        // TEST - Represented by clone with different node ID
        RelationKnowledge rk0_var = new RelationKnowledge(RK_NAME[0], RK_NAMESPACE[0], ci0);
        rk0_var.setNodeId(299L);
        Assert.assertTrue("Not represented by clone with diff node ID", rk0.isRepresentedBy(rk0_var));

        // TEST - Not represented by clone with different FMA ID
        rk0_var = new RelationKnowledge(RK_NAME[1], RK_NAMESPACE[0], ci0);
        rk0_var.setNodeId(200L);
        Assert.assertFalse("Represented by clone with diff FMA ID", rk0.isRepresentedBy(rk0_var));

        // TEST - Not represented by clone with different FMA Label
        rk0_var = new RelationKnowledge(RK_NAME[0], RK_NAMESPACE[1], ci0);
        rk0_var.setNodeId(200L);
        Assert.assertFalse("Represented by clone with diff FMA Label", rk0.isRepresentedBy(rk0_var));

        // TEST - Not represented by clone with different CurriculumItem
        rk0_var = new RelationKnowledge(RK_NAME[0], RK_NAMESPACE[0], ci1);
        rk0_var.setNodeId(200L);
        Assert.assertFalse("Represented by clone with diff FMA Label", rk0.isRepresentedBy(rk0_var));

        // TEST - Not represented by clone with null CurriculumItem
        rk0_var = new RelationKnowledge(RK_NAME[0], RK_NAMESPACE[0], null);
        rk0_var.setNodeId(200L);
        Assert.assertFalse("Represented by clone with diff FMA Label", rk0.isRepresentedBy(rk0_var));

        // TEST - Not represented by null
        Assert.assertFalse("Represented by clone with diff FMA Label", rk0.isRepresentedBy(null));
    }


    @Test
    public void testIsRepresentedInList()
    {
        // ACT - Prepare test objects
        CurriculumItem ci0 = new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]);
        ci0.setNodeId(100L);
        CurriculumItem ci1 = new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1]);
        ci1.setNodeId(101L);
        RelationKnowledge rk0 = new RelationKnowledge(RK_NAME[0], RK_NAMESPACE[0], ci0);
        rk0.setNodeId(200L);
        RelationKnowledge rk1 = new RelationKnowledge(RK_NAME[1], RK_NAMESPACE[1], ci1);
        rk0.setNodeId(201L);
        RelationKnowledge rk2 = new RelationKnowledge(RK_NAME[2], RK_NAMESPACE[2], ci0);
        rk0.setNodeId(202L);
        RelationKnowledge rk0_var = new RelationKnowledge(RK_NAME[0], RK_NAMESPACE[0], ci0);
        rk0_var.setNodeId(299L);

        // TEST - Represented in list containing clone
        List<RelationKnowledge> rkList0 = new ArrayList<RelationKnowledge>();
        rkList0.add(rk0_var);
        rkList0.add(rk1);
        rkList0.add(rk2);
        Assert.assertTrue("Not represented in list containing clone", rk0.isRepresentedInList(rkList0));

        // TEST - Not represented in list
        List<RelationKnowledge> rkList1 = new ArrayList<RelationKnowledge>();
        rkList1.add(rk1);
        rkList1.add(rk2);
        Assert.assertFalse("Represented in list not containing clones or self", rk0.isRepresentedInList(rkList1));
    }
}
