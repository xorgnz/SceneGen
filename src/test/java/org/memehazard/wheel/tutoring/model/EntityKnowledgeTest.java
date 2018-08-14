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
public class EntityKnowledgeTest
{
    // Parameters for test objects
    private static final Integer[] EK_FMA_ID      = { 1001, 1002, 1003, 1004, 1005, 1006 };
    private static final String[]  EK_FMA_LABEL   = { "EK Label 6", "EK Label 5", "EK Label 4", "EK Label 3", "EK Label 2", "EK Label 1" };


    @SuppressWarnings("unused")
    private Logger                 log            = LoggerFactory.getLogger(this.getClass());


    @Test
    public void testIsRepresentedBy()
    {
        // ACT - Prepare test objects
        CurriculumItem ci0 = new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]);
        ci0.setNodeId(100L);
        CurriculumItem ci1 = new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1]);
        ci1.setNodeId(101L);
        EntityKnowledge ek0 = new EntityKnowledge(EK_FMA_ID[0], EK_FMA_LABEL[0], ci0);
        ek0.setNodeId(200L);

        // TEST - Represented by self
        Assert.assertTrue("Not represented by self", ek0.isRepresentedBy(ek0));

        // TEST - Represented by clone with different node ID
        EntityKnowledge ek0_var = new EntityKnowledge(EK_FMA_ID[0], EK_FMA_LABEL[0], ci0);
        ek0_var.setNodeId(299L);
        Assert.assertTrue("Not represented by clone with diff node ID", ek0.isRepresentedBy(ek0_var));

        // TEST - Not represented by clone with different FMA ID
        ek0_var = new EntityKnowledge(EK_FMA_ID[1], EK_FMA_LABEL[0], ci0);
        ek0_var.setNodeId(200L);
        Assert.assertFalse("Represented by clone with diff FMA ID", ek0.isRepresentedBy(ek0_var));

        // TEST - Not represented by clone with different FMA Label
        ek0_var = new EntityKnowledge(EK_FMA_ID[0], EK_FMA_LABEL[1], ci0);
        ek0_var.setNodeId(200L);
        Assert.assertFalse("Represented by clone with diff FMA Label", ek0.isRepresentedBy(ek0_var));

        // TEST - Not represented by clone with different CurriculumItem
        ek0_var = new EntityKnowledge(EK_FMA_ID[0], EK_FMA_LABEL[0], ci1);
        ek0_var.setNodeId(200L);
        Assert.assertFalse("Represented by clone with diff FMA Label", ek0.isRepresentedBy(ek0_var));
        
        // TEST - Not represented by clone with null CurriculumItem
        ek0_var = new EntityKnowledge(EK_FMA_ID[0], EK_FMA_LABEL[0], null);
        ek0_var.setNodeId(200L);
        Assert.assertFalse("Represented by clone with diff FMA Label", ek0.isRepresentedBy(ek0_var));
        
        // TEST - Not represented by null
        Assert.assertFalse("Represented by clone with diff FMA Label", ek0.isRepresentedBy(null));        
    }


    @Test
    public void testIsRepresentedInList()
    {
        // ACT - Prepare test objects
        CurriculumItem ci0 = new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]);
        ci0.setNodeId(100L);
        CurriculumItem ci1 = new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1]);
        ci1.setNodeId(101L);
        EntityKnowledge ek0 = new EntityKnowledge(EK_FMA_ID[0], EK_FMA_LABEL[0], ci0);
        ek0.setNodeId(200L);
        EntityKnowledge ek1 = new EntityKnowledge(EK_FMA_ID[1], EK_FMA_LABEL[1], ci1);
        ek0.setNodeId(201L);
        EntityKnowledge ek2 = new EntityKnowledge(EK_FMA_ID[2], EK_FMA_LABEL[2], ci0);
        ek0.setNodeId(202L);
        EntityKnowledge ek0_var = new EntityKnowledge(EK_FMA_ID[0], EK_FMA_LABEL[0], ci0);
        ek0_var.setNodeId(299L);

        // TEST - Represented in list containing clone
        List<EntityKnowledge> ekList0 = new ArrayList<EntityKnowledge>();
        ekList0.add(ek0_var);
        ekList0.add(ek1);
        ekList0.add(ek2);
        Assert.assertTrue("Not represented in list containing clone", ek0.isRepresentedInList(ekList0));

        // TEST - Not represented in list
        List<EntityKnowledge> ekList1 = new ArrayList<EntityKnowledge>();
        ekList1.add(ek1);
        ekList1.add(ek2);
        Assert.assertFalse("Represented in list not containing clones or self", ek0.isRepresentedInList(ekList1));
    }
}
