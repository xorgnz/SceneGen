package org.memehazard.wheel.scene.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.asset.dao.AssetSetDAO;
import org.memehazard.wheel.asset.model.AssetSet;
import org.memehazard.wheel.query.dao.QueryDAO;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.scene.model.Scene;
import org.memehazard.wheel.scene.model.SceneFragment;
import org.memehazard.wheel.scene.test.TestDAO_Scene;
import org.memehazard.wheel.style.dao.StylesheetDAO;
import org.memehazard.wheel.style.model.Stylesheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class SceneFragmentDAOTest_crud
{
    @SuppressWarnings("unused")
    private Logger    log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SceneFragmentDAO dao;

    @Autowired
    private SceneDAO         dao_scn;

    @Autowired
    private StylesheetDAO    dao_ssheet;

    @Autowired
    private AssetSetDAO      dao_assetSet;

    @Autowired
    private QueryDAO         dao_query;


    @Autowired
    private TestDAO_Scene    dao_test;


    @Before
    public void before()
    {
        dao_test.prep_SceneFragmentDAO_crud();
    }


    @Test
    public void test_crud()
    {
        // PREP - Obtain depended objects
        Scene scene0 = dao_scn.get(0);
        Scene scene1 = dao_scn.get(1);
        AssetSet assetSet0 = dao_assetSet.get(0);
        AssetSet assetSet1 = dao_assetSet.get(1);
        Stylesheet ssheet0 = dao_ssheet.get(0);
        Stylesheet ssheet1 = dao_ssheet.get(1);
        Query query0 = dao_query.get(0);
        Query query1 = dao_query.get(0);

        // ACT - Add
        SceneFragment fragment0 = new SceneFragment(scene0, "Scene Fragment Z", assetSet0, ssheet0, 52, query0, "Params");
        dao.addQueryType(fragment0);

        // Test - Did we get an object ID?
        Assert.assertNotNull(fragment0.getId());

        // ACT - Get
        SceneFragment fragment0_copy = dao.get(fragment0.getId());

        // TEST - Retrieved object has correct values
        Assert.assertEquals(fragment0.getId(), fragment0_copy.getId());
        Assert.assertEquals(fragment0.getName(), fragment0_copy.getName());
        Assert.assertEquals(0, fragment0_copy.getType());
        Assert.assertEquals(fragment0.getQueryParamString(), fragment0_copy.getQueryParamString());
        Assert.assertEquals(scene0.getId(), fragment0_copy.getScene().getId());
        Assert.assertEquals(ssheet0.getId(), fragment0_copy.getStylesheet().getId());
        Assert.assertEquals(assetSet0.getId(), fragment0_copy.getAssetSet().getId());
        Assert.assertEquals(query0.getId(), fragment0_copy.getQuery().getId());

        // ACT - List all
        SceneFragment fragment1 = new SceneFragment(scene0, "Scene Fragment Y", assetSet0, ssheet0, 52, query0, "Params");
        dao.addQueryType(fragment1);
        List<SceneFragment> fragments = dao.listAll();

        // TEST - Did list all return fragments correctly?
        Assert.assertEquals(2, fragments.size());
        Assert.assertEquals(fragment1.getName(), fragments.get(0).getName());
        Assert.assertEquals(fragment0.getName(), fragments.get(1).getName());

        // ACT - Update
        fragment0.setName(fragment0.getName() + "alt");
        fragment0.setType(fragment0.getType() + 16);
        fragment0.setQueryParamString(fragment0.getQueryParamString() + "alt");
        fragment0.setAssetSet(assetSet1);
        fragment0.setQuery(query1);
        fragment0.setStylesheet(ssheet1);
        fragment0.setScene(scene1);
        dao.updateQueryType(fragment0);

        // TEST - Update seen in retrieved object
        fragment0_copy = dao.get(fragment0.getId());
        Assert.assertEquals(fragment0.getName(), fragment0_copy.getName());
        Assert.assertEquals(0, fragment0_copy.getType());
        Assert.assertEquals(fragment0.getQueryParamString(), fragment0_copy.getQueryParamString());
        Assert.assertEquals(scene1.getId(), fragment0_copy.getScene().getId());
        Assert.assertEquals(ssheet1.getId(), fragment0_copy.getStylesheet().getId());
        Assert.assertEquals(assetSet1.getId(), fragment0_copy.getAssetSet().getId());
        Assert.assertEquals(query1.getId(), fragment0_copy.getQuery().getId());

        // ACT - Delete
        dao.delete(fragment1.getId());

        // TEST - Deleted
        Assert.assertEquals(1, dao.listAll().size());
    }
}