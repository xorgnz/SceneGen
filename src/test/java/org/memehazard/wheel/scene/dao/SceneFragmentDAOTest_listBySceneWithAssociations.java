package org.memehazard.wheel.scene.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.scene.model.SceneFragment;
import org.memehazard.wheel.scene.test.TestDAO_Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class SceneFragmentDAOTest_listBySceneWithAssociations
{
    private static final String[] ASSET_SET_NAME = new String[] { "Asset Set A", "Asset Set B" };
    private static final String[] QUERY_NAME     = new String[] { "Query A", "Query B" };
    private static final String[] SCN_NAME       = new String[] { "Scene A", "Scene B" };
    private static final String[] SCNF_NAME      = new String[] { "Scene fragment X", "Scene fragment Y", "Scene fragment Z" };
    private static final String[] SCNF_PARAMS    = new String[] { "Params X", "Params Y", "Params Z" };
    private static final String[] SSHEET_NAME    = new String[] { "Stylesheet A", "Stylesheet B" };

    @Autowired
    private SceneFragmentDAO      dao;
    @Autowired
    private TestDAO_Scene         dao_test;


    @Before
    public void before()
    {
        dao_test.prep_SceneFragmentDAO_listBySceneWithAssociations();
    }


    @Test
    public void test()
    {
        // Act
        List<SceneFragment> fragments = dao.listBySceneWithAssociations(0);

        // Test - are all objects retrieved?
        Assert.assertEquals(2, fragments.size());

        // TEST - Did objects come back in the correct order?
        Assert.assertEquals(SCNF_NAME[1], fragments.get(0).getName());
        Assert.assertEquals(SCNF_NAME[2], fragments.get(1).getName());

        // TEST - Are all associations returned correctly?
        SceneFragment fragment0 = fragments.get(0);
        Assert.assertEquals(SCNF_PARAMS[1], fragment0.getQueryParamString());
        Assert.assertEquals(SCN_NAME[0], fragment0.getScene().getName());
        Assert.assertEquals(ASSET_SET_NAME[1], fragment0.getAssetSet().getName());
        Assert.assertEquals(QUERY_NAME[1], fragment0.getQuery().getName());
        Assert.assertEquals(SSHEET_NAME[1], fragment0.getStylesheet().getName());

        SceneFragment fragment1 = fragments.get(1);
        Assert.assertEquals(SCNF_PARAMS[2], fragment1.getQueryParamString());
        Assert.assertEquals(SCN_NAME[0], fragment1.getScene().getName());        
        Assert.assertEquals(ASSET_SET_NAME[0], fragment1.getAssetSet().getName());
        Assert.assertEquals(QUERY_NAME[0], fragment1.getQuery().getName());
        Assert.assertEquals(SSHEET_NAME[0], fragment1.getStylesheet().getName());
    }
}
