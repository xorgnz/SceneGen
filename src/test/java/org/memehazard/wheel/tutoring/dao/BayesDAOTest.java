package org.memehazard.wheel.tutoring.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.AbstractDomainModelNode;
import org.memehazard.wheel.tutoring.model.BayesValue;
import org.memehazard.wheel.tutoring.model.DomainModelNode;
import org.memehazard.wheel.tutoring.test.TestData;
import org.memehazard.wheel.tutoring.test.TutoringTestMyBatisDAO;
import org.memehazard.wheel.tutoring.utils.TutoringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class BayesDAOTest
{
    @Autowired
    private BayesDAO               dao;

    @Autowired
    private EntityKnowledgeDAO     dao_ek;

    @Autowired
    private RelationFactDAO        dao_fact;

    @Autowired
    private RelationKnowledgeDAO   dao_rk;

    @Autowired
    private TutoringTestMyBatisDAO dao_test;

    @Autowired
    private TutoringFacade         facade;
    
    @SuppressWarnings("unused")
    private Logger                 log = LoggerFactory.getLogger(this.getClass());


    @BeforeTransaction
    public void before()
    {
        dao_test.createTestData();
    }


    @Test
    @Transactional
    public void test_delete()
    {
        // Act - delete a record
        dao.delete(TestData.ITSS_DOMAIN_ID[0], TestData.ITSS_STUDENT_ID[0]);

        // Test - correct number of records remain
        Assert.assertEquals(5, dao_test.listAllBayes().size());
    }


    @Test
    @Transactional
    public void test_deleteAllByDomainId()
    {
        // Act - delete several records
        dao.deleteAllByDomainId(TestData.ITSS_DOMAIN_ID[0]);

        // Test - correct number of records remain
        Assert.assertEquals(4, dao_test.listAllBayes().size());
    }


    @Test
    @Transactional
    public void test_deleteAllByMultipleDomainIds()
    {
        // Act - delete several records
        List<Long> idsToDelete = new ArrayList<Long>();
        idsToDelete.add(TestData.ITSS_DOMAIN_ID[0]);
        idsToDelete.add(TestData.ITSS_DOMAIN_ID[1]);
        dao.deleteAllByMultipleDomainIds(idsToDelete);

        // Test - correct number of records remain
        Assert.assertEquals(2, dao_test.listAllBayes().size());
    }


    @Test
    @Transactional
    public void test_deleteAllByMultipleDomainModelNodes()
    {
        // PREP - Prepare test objects
        DomainModelNode dmn0 = new AbstractDomainModelNode()
        {
            public Long getNodeId()
            {
                return TestData.ITSS_DOMAIN_ID[0];
            }
        };
        DomainModelNode dmn1 = new AbstractDomainModelNode()
        {
            public Long getNodeId()
            {
                return TestData.ITSS_DOMAIN_ID[1];
            }
        };

        // ACT - Delete bayes values given a list of domain model nodes
        dao.deleteAllByMultipleDomainModelNodes(new DomainModelNode[] { dmn0, dmn1 });

        // Test - correct number of records remain
        Assert.assertEquals(2, dao_test.listAllBayes().size());
    }


    @Test
    @Transactional
    public void test_deleteByMultipleDomainModelNodes()
    {
        // PREP - Prepare test objects
        DomainModelNode dmn0 = new AbstractDomainModelNode()
        {
            public Long getNodeId()
            {
                return TestData.ITSS_DOMAIN_ID[0];
            }
        };
        DomainModelNode dmn1 = new AbstractDomainModelNode()
        {
            public Long getNodeId()
            {
                return TestData.ITSS_DOMAIN_ID[1];
            }
        };

        // ACT - Delete bayes values given a list of domain model nodes
        dao.deleteByMultipleDomainModelNodes(new DomainModelNode[] { dmn0, dmn1 }, TestData.ITSS_STUDENT_ID[0]);

        // Test - correct number of records remain
        Assert.assertEquals(4, dao_test.listAllBayes().size());
    }


    @Test
    @Transactional
    public void test_get()
    {
        // Test - correct P value retrieved
        Assert.assertEquals(TestData.ITSS_P_VAL[0], dao.get(TestData.ITSS_DOMAIN_ID[0], TestData.ITSS_STUDENT_ID[0]).getP(), 0.000001);
        Assert.assertEquals(TestData.ITSS_P_VAL[1], dao.get(TestData.ITSS_DOMAIN_ID[1], TestData.ITSS_STUDENT_ID[1]).getP(), 0.000001);
        Assert.assertEquals(TestData.ITSS_P_VAL[2], dao.get(TestData.ITSS_DOMAIN_ID[2], TestData.ITSS_STUDENT_ID[2]).getP(), 0.000001);
        Assert.assertEquals(TestData.ITSS_P_VAL[3], dao.get(TestData.ITSS_DOMAIN_ID[3], TestData.ITSS_STUDENT_ID[3]).getP(), 0.000001);
        Assert.assertEquals(TestData.ITSS_P_VAL[4], dao.get(TestData.ITSS_DOMAIN_ID[4], TestData.ITSS_STUDENT_ID[4]).getP(), 0.000001);
        Assert.assertEquals(TestData.ITSS_P_VAL[5], dao.get(TestData.ITSS_DOMAIN_ID[5], TestData.ITSS_STUDENT_ID[5]).getP(), 0.000001);

        // Test - correct timestamp values retrieved
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(0).getTime(), dao.get(TestData.ITSS_DOMAIN_ID[0], TestData.ITSS_STUDENT_ID[0]).getTimestamp());
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(1).getTime(), dao.get(TestData.ITSS_DOMAIN_ID[1], TestData.ITSS_STUDENT_ID[1]).getTimestamp());
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(2).getTime(), dao.get(TestData.ITSS_DOMAIN_ID[2], TestData.ITSS_STUDENT_ID[2]).getTimestamp());
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(3).getTime(), dao.get(TestData.ITSS_DOMAIN_ID[3], TestData.ITSS_STUDENT_ID[3]).getTimestamp());
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(4).getTime(), dao.get(TestData.ITSS_DOMAIN_ID[4], TestData.ITSS_STUDENT_ID[4]).getTimestamp());
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(5).getTime(), dao.get(TestData.ITSS_DOMAIN_ID[5], TestData.ITSS_STUDENT_ID[5]).getTimestamp());
    }


    @Test
    @Transactional
    public void test_list()
    {
        // Act - List all bayes values for student 0
        List<Long> ids = new ArrayList<Long>();
        for (long id : TestData.ITSS_DOMAIN_ID)
            ids.add(id);
        List<BayesValue> result = dao.list(ids, TestData.ITSS_STUDENT_ID[0]);
        Map<Long, BayesValue> resultMap = new HashMap<Long, BayesValue>();
        for (BayesValue bv : result)
            resultMap.put(bv.getDomainId(), bv);

        // Test - correct number of bayes values returned
        Assert.assertEquals(4, result.size());

        // Test - Correct P values returned
        Assert.assertEquals(TestData.ITSS_P_VAL[0], resultMap.get(TestData.ITSS_DOMAIN_ID[0]).getP(), 0.000001);
        Assert.assertEquals(TestData.ITSS_P_VAL[1], resultMap.get(TestData.ITSS_DOMAIN_ID[1]).getP(), 0.000001);
        Assert.assertEquals(TestData.ITSS_P_VAL[2], resultMap.get(TestData.ITSS_DOMAIN_ID[2]).getP(), 0.000001);
        Assert.assertEquals(TestData.ITSS_P_VAL[3], resultMap.get(TestData.ITSS_DOMAIN_ID[3]).getP(), 0.000001);

        // Test - Correct timestampts returned
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(0).getTime(), resultMap.get(TestData.ITSS_DOMAIN_ID[0]).getTimestamp());
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(1).getTime(), resultMap.get(TestData.ITSS_DOMAIN_ID[1]).getTimestamp());
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(2).getTime(), resultMap.get(TestData.ITSS_DOMAIN_ID[2]).getTimestamp());
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(3).getTime(), resultMap.get(TestData.ITSS_DOMAIN_ID[3]).getTimestamp());
    }


    @Test
    @Transactional
    public void test_listByMultipleDomainModelNodes()
    {
        // PREP - Prepare test objects
        DomainModelNode dmn0 = new AbstractDomainModelNode()
        {
            public Long getNodeId()
            {
                return TestData.ITSS_DOMAIN_ID[0];
            }
        };
        DomainModelNode dmn1 = new AbstractDomainModelNode()
        {
            public Long getNodeId()
            {
                return TestData.ITSS_DOMAIN_ID[1];
            }
        };

        // ACT - Delete bayes values given a list of domain model nodes
        List<BayesValue> bayesValues = dao.listByMultipleDomainModelNodes(new DomainModelNode[] { dmn0, dmn1 }, TestData.ITSS_STUDENT_ID[0]);
        Map<Long, BayesValue> bvMap = TutoringUtils.mapifyBayesValuesByDomainId(bayesValues);

        // Test - correct number of records remain
        Assert.assertEquals(2, bayesValues.size());
        Assert.assertTrue(bvMap.containsKey(TestData.ITSS_DOMAIN_ID[0]));
        Assert.assertTrue(bvMap.containsKey(TestData.ITSS_DOMAIN_ID[1]));
    }


    @Test
    @Transactional
    public void test_set()
    {
        // Act - Set a bayes value with a different P value and timestamp
        dao.set(TestData.ITSS_DOMAIN_ID[0], TestData.ITSS_STUDENT_ID[0], TestData.ITSS_P_VAL[0] + 0.05, TestData.ITSS_TIMESTAMPS(5).getTime());

        // Act - retrieve updated bayes value
        BayesValue bv = dao.get(TestData.ITSS_DOMAIN_ID[0], TestData.ITSS_STUDENT_ID[0]);

        // Test - are values correct?
        Assert.assertEquals(TestData.ITSS_P_VAL[0] + 0.05, bv.getP(), 0.000001);
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(5).getTime(), bv.getTimestamp());
    }


    @Test
    @Transactional
    public void test_setForMultipleStudents()
    {
        // PREP - Create test BVs
        List<Long> studentIds = new ArrayList<Long>();
        studentIds.add(1001l);
        studentIds.add(1002l);
        dao.setForMultipleStudents(1, studentIds, 0.4, new Date());

        // TEST - Correct BVs
        Assert.assertEquals(8, dao_test.listAllBayes().size());
        Assert.assertNotNull(dao.get(1, 1001));
        Assert.assertNotNull(dao.get(1, 1002));
    }


    @Test
    @Transactional
    public void test_setMultiple()
    {
        // Act - Set a group of bayes values with different P values and timestamps
        List<BayesValue> bvals = new ArrayList<BayesValue>();
        bvals.add(new BayesValue(TestData.ITSS_DOMAIN_ID[0], TestData.ITSS_STUDENT_ID[0], TestData.ITSS_P_VAL[0] + 0.05, TestData.ITSS_TIMESTAMPS(
                1).getTime()));
        bvals.add(new BayesValue(TestData.ITSS_DOMAIN_ID[1], TestData.ITSS_STUDENT_ID[1], TestData.ITSS_P_VAL[1] + 0.05, TestData.ITSS_TIMESTAMPS(
                2).getTime()));
        bvals.add(new BayesValue(TestData.ITSS_DOMAIN_ID[2], TestData.ITSS_STUDENT_ID[2], TestData.ITSS_P_VAL[2] + 0.05, TestData.ITSS_TIMESTAMPS(
                3).getTime()));
        dao.setMultiple(bvals);

        // Act - Retrieve updated bayes values
        BayesValue bv0 = dao.get(TestData.ITSS_DOMAIN_ID[0], TestData.ITSS_STUDENT_ID[0]);
        BayesValue bv1 = dao.get(TestData.ITSS_DOMAIN_ID[1], TestData.ITSS_STUDENT_ID[1]);
        BayesValue bv2 = dao.get(TestData.ITSS_DOMAIN_ID[2], TestData.ITSS_STUDENT_ID[2]);

        // Test - Are P values correct?
        Assert.assertEquals(TestData.ITSS_P_VAL[0] + 0.05, bv0.getP(), 0.000001);
        Assert.assertEquals(TestData.ITSS_P_VAL[1] + 0.05, bv1.getP(), 0.000001);
        Assert.assertEquals(TestData.ITSS_P_VAL[2] + 0.05, bv2.getP(), 0.000001);

        // Test - Are timestamps correct?
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(1).getTime(), bv0.getTimestamp());
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(2).getTime(), bv1.getTimestamp());
        Assert.assertEquals(TestData.ITSS_TIMESTAMPS(3).getTime(), bv2.getTimestamp());
    }
}
