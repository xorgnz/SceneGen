package org.memehazard.wheel.scene.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.scene.model.Scene;
import org.memehazard.wheel.scene.test.TestDAO_Scene;
import org.memehazard.wheel.viewer.model.Viewpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class SceneDAOTest_crud
{
    @Autowired
    private SceneDAO         dao;

    @Autowired
    private SceneFragmentDAO dao_sp;

    @Autowired
    private TestDAO_Scene    dao_test;


    @Before
    public void before()
    {
        dao_test.prep_SceneDAO_crud();
    }


    @Test
    public void test()
    {
        // ACT - Add
        Scene scn0 = new Scene("Scene Z",
                new Viewpoint(new double[][] { { 0.0, 0.1, 0.2 }, { 1.0, 1.1, 1.2 }, { 2.0, 2.1, 2.2 }, { 3.0, 3.1, 3.2 } }));
        dao.add(scn0);

        // TEST - Did we get an object ID?
        Assert.assertNotNull(scn0.getId());

        // ACT - Get
        Scene scn0_copy = dao.get(scn0.getId());

        // TEST - Retrieved object has correct values
        Assert.assertEquals(scn0.getId(), scn0_copy.getId());
        Assert.assertEquals(scn0.getName(), scn0_copy.getName());
        Assert.assertEquals(scn0.getViewpoint(), scn0_copy.getViewpoint());

        // ACT - List All
        Scene scn1 = new Scene("Scene Y", new Viewpoint(new double[][] { { 0.0, 0.1, 0.2 }, { 1.0, 1.1, 1.2 }, { 2.0, 2.1, 2.2 }, { 3.0, 3.1, 3.2 } }));
        dao.add(scn1);
        List<Scene> scenes = dao.listAll();

        // TEST - Did list all return fragment correctly?
        Assert.assertEquals(2, scenes.size());
        Assert.assertEquals(scn1.getName(), scenes.get(0).getName());
        Assert.assertEquals(scn0.getName(), scenes.get(1).getName());

        // ACT - Update
        scn0.setName(scn0.getName() + "alt");
        scn0.setViewpoint(new Viewpoint(new double[][] { { 0.05, 0.15, 0.25 }, { 1.05, 1.15, 1.25 }, { 2.05, 2.15, 2.25 }, { 3.05, 3.15, 3.25 } }));
        dao.update(scn0);

        // TEST - Update seen in retrieved object
        scn0_copy = dao.get(scn0.getId());
        Assert.assertEquals(scn0.getName(), scn0_copy.getName());
        Assert.assertEquals(scn0.getViewpoint(), scn0_copy.getViewpoint());

        // ACT - Delete
        dao.delete(scn1.getId());

        // TEST - Deleted
        Assert.assertEquals(1, dao.listAll().size());
    }
}