package org.memehazard.wheel.scene.facade;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.scene.dao.SceneFragmentDAO;
import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.scene.model.Scene;
import org.memehazard.wheel.scene.test.TestDAO_Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class SceneFacadeTest_getFullScene
{
    public static final String   ASSET_NAME     = "Asset";
    public static final String   ASSET_SET_NAME = "Asset Set";
    public static final String   QUERY_NAME     = "Query";
    public static final String   SCN_NAME       = "Scene";
    public static final String[] SCNF_NAME      = new String[] { "Scene fragment 2", "Scene fragment 1" };
    public static final String   SSHEET_NAME    = "Stylesheet";
    @Autowired
    private SceneFacade          facade;
    @Autowired
    private SceneFragmentDAO     dao_sp;
    @Autowired
    private TestDAO_Scene        dao_test;


    @Before
    public void before()
    {
        dao_test.prep_SceneFacade_getFullScene();
    }


    @Test
    public void test()
    {
        Scene scn = facade.getFullScene(0);

        // Test - did we get an object back?
        Assert.assertNotNull(scn);

        // Test - does that object have expected values?
        Assert.assertEquals(0, (int) scn.getId());
        Assert.assertEquals(SCN_NAME, scn.getName());

        // Test - are fragments linked?
        Assert.assertEquals(2, scn.getFragments().size());
        Assert.assertEquals(SCNF_NAME[1], scn.getFragments().get(0).getName());
        Assert.assertEquals(SCNF_NAME[0], scn.getFragments().get(1).getName());

        // Test - are fragment associations linked?
        Assert.assertEquals(ASSET_SET_NAME, scn.getFragments().get(0).getAssetSet().getName());
        Assert.assertEquals(SSHEET_NAME, scn.getFragments().get(0).getStylesheet().getName());
        Assert.assertEquals(QUERY_NAME, scn.getFragments().get(0).getQuery().getName());

        // Test - are fragment members linked?
        Assert.assertEquals(2, scn.getFragments().get(0).getMembers().size());
        Assert.assertEquals(1, scn.getFragments().get(1).getMembers().size());

        // Test - are fragment member assets linked?
        Assert.assertEquals(ASSET_NAME, scn.getFragments().get(0).getMembers().get(0).getAsset().getName());
    }
}
