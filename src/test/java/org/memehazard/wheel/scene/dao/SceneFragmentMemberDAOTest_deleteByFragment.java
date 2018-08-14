package org.memehazard.wheel.scene.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.scene.model.SceneFragmentMember;
import org.memehazard.wheel.scene.test.TestDAO_Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class SceneFragmentMemberDAOTest_deleteByFragment
{
    @SuppressWarnings("unused")
    private Logger                 log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SceneFragmentMemberDAO dao;
    @Autowired
    private TestDAO_Scene          dao_test;


    @Before
    public void before()
    {
        dao_test.prep_SceneFragmentMemberDAO_deleteByFragment();
    }


    @Test
    public void test_deleteByFragment()
    {
        // Act - by ID
        dao.deleteByFragment(0);

        // Test - is it gone?
        List<SceneFragmentMember> members = dao.listAll();
        Assert.assertEquals(1, dao.listAll().size());
        Assert.assertEquals("Member W", members.get(0).getEntity().getName());
    }
}
