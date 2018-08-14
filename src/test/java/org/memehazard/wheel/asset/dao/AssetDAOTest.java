package org.memehazard.wheel.asset.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.asset.dao.AssetDAO;
import org.memehazard.wheel.asset.dao.AssetSetDAO;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.asset.model.AssetSet;
import org.memehazard.wheel.asset.test.TestDAO_Asset;
import org.memehazard.wheel.asset.test.TestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class AssetDAOTest
{
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(AssetDAOTest.class);

    @Autowired
    private AssetDAO      dao;

    @Autowired
    private AssetSetDAO   dao_set;


    @Autowired
    private TestDAO_Asset dao_test;


    @Before
    public void before()
    {
        dao_test.createTestData();
    }


    @Test
    public void test_add()
    {
        // Act
        AssetSet set = dao_set.get(1);
        Asset a = new Asset(set,
                TestData.ASSET_NAMES[0] + " alt",
                TestData.ASSET_ENTITY_IDS[0] + 10000,
                TestData.ASSET_OBJ_FILENAME_ALL + " alt",
                TestData.ASSET_X3D_FILENAME_ALL + " alt",
                TestData.ASSET_STATS_ALT,
                TestData.ASSET_STYLE_TAGS_ALT);
        dao.add(a);

        // Test - Did we get an object ID?
        Assert.assertNotNull(a.getId());

        // Test - does it show in list all?
        Assert.assertEquals(7, dao.listAll().size());

        // Test - is it correct?
        Asset a_copy = dao.get(a.getId());
        Assert.assertEquals(set.getId(), a_copy.getAssetSet().getId());
        Assert.assertEquals(TestData.ASSET_NAMES[0] + " alt", a_copy.getName());
        Assert.assertEquals(TestData.ASSET_ENTITY_IDS[0] + 10000, a_copy.getEntityId());
        Assert.assertEquals(TestData.ASSET_OBJ_FILENAME_ALL + " alt", a_copy.getObjFilename());
        Assert.assertEquals(TestData.ASSET_X3D_FILENAME_ALL + " alt", a_copy.getX3dFilename());
        Assert.assertEquals(TestData.ASSET_STATS_ALT, a_copy.getStats());

        // Test - Style tags should not be created (use updateStyleTags for this)
        Assert.assertEquals(0, a_copy.getStyleTags().size());
    }


    @Test
    public void test_delete()
    {
        // Act - by ID
        dao.delete(1);

        // Test - is it gone?
        Assert.assertEquals(5, dao.listAll().size());
    }


    @Test
    public void test_deleteStyleTags()
    {
        // Test - Setup correct
        Assert.assertEquals(3, dao.get(1).getStyleTags().size());

        // Act
        dao.deleteStyleTags(1);

        // Test - tags deleted
        Assert.assertEquals(0, dao.get(1).getStyleTags().size());
    }


    @Test
    public void test_get()
    {
        // Act
        Asset a = dao.get(1);

        // Test - did we get an object back?
        Assert.assertNotNull(a);

        // Test - does that object have expected values?
        Assert.assertEquals(1, a.getId());
        Assert.assertEquals(TestData.ASSET_ASSETSET_IDS[0], a.getAssetSet().getId());
        Assert.assertEquals(TestData.ASSET_NAMES[0], a.getName());
        Assert.assertEquals(TestData.ASSET_ENTITY_IDS[0], a.getEntityId());
        Assert.assertEquals(TestData.ASSET_OBJ_FILENAME_ALL, a.getObjFilename());
        Assert.assertEquals(TestData.ASSET_X3D_FILENAME_ALL, a.getX3dFilename());
        Assert.assertEquals(TestData.ASSET_STATS_ALL, a.getStats());
        Assert.assertEquals(TestData.ASSET_STYLE_TAGS, a.getStyleTags());
    }


    @Test
    public void test_listAll()
    {
        // Act
        List<Asset> assets = dao.listAll();

        // Test - are all objects retrieved?
        Assert.assertEquals(6, assets.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.ASSET_NAMES[5], assets.get(0).getName());
        Assert.assertEquals(TestData.ASSET_NAMES[4], assets.get(1).getName());
        Assert.assertEquals(TestData.ASSET_NAMES[3], assets.get(2).getName());
        Assert.assertEquals(TestData.ASSET_NAMES[2], assets.get(3).getName());
        Assert.assertEquals(TestData.ASSET_NAMES[1], assets.get(4).getName());
        Assert.assertEquals(TestData.ASSET_NAMES[0], assets.get(5).getName());
    }


    @Test
    public void test_listByAssetSet()
    {
        // Act
        List<Asset> assets = dao.listByAssetSet(1);

        // Test - are all objects retrieved?
        Assert.assertEquals(4, assets.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.ASSET_NAMES[3], assets.get(0).getName());
        Assert.assertEquals(TestData.ASSET_NAMES[2], assets.get(1).getName());
        Assert.assertEquals(TestData.ASSET_NAMES[1], assets.get(2).getName());
        Assert.assertEquals(TestData.ASSET_NAMES[0], assets.get(3).getName());
    }


    @Test
    public void test_listIdsByAssetSet()
    {
        // Act
        List<Integer> assets = dao.listIdsByAssetSet(1);

        // Test - are all objects retrieved?
        Assert.assertEquals(4, assets.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.ASSET_ENTITY_IDS[0], assets.get(0).intValue());
        Assert.assertEquals(TestData.ASSET_ENTITY_IDS[1], assets.get(1).intValue());
        Assert.assertEquals(TestData.ASSET_ENTITY_IDS[2], assets.get(2).intValue());
        Assert.assertEquals(TestData.ASSET_ENTITY_IDS[3], assets.get(3).intValue());
    }


    @Test
    public void test_update()
    {
        // Act
        AssetSet set3 = dao_set.get(3);
        Asset a1 = dao.get(1);
        a1.setName(TestData.ASSET_NAMES[0] + " alt");
        a1.setEntityId(TestData.ASSET_ENTITY_IDS[0] + 10000);
        a1.setObjFilename(TestData.ASSET_OBJ_FILENAME_ALL + " alt");
        a1.setX3dFilename(TestData.ASSET_X3D_FILENAME_ALL + " alt");
        a1.setStats(TestData.ASSET_STATS_ALT);
        a1.setStyleTags(TestData.ASSET_STYLE_TAGS_ALT);
        a1.setAssetSet(set3);
        dao.update(a1);

        // Test - have changes been made?
        Asset a1_copy = dao.get(1);
        Assert.assertEquals(TestData.ASSET_NAMES[0] + " alt", a1_copy.getName());
        Assert.assertEquals(TestData.ASSET_ENTITY_IDS[0] + 10000, (int) a1_copy.getEntityId());
        Assert.assertEquals(TestData.ASSET_OBJ_FILENAME_ALL + " alt", a1_copy.getObjFilename());
        Assert.assertEquals(TestData.ASSET_X3D_FILENAME_ALL + " alt", a1_copy.getX3dFilename());
        Assert.assertEquals(TestData.ASSET_STATS_ALT, a1_copy.getStats());
        Assert.assertEquals(set3.getId(), a1_copy.getAssetSet().getId());
    }


    @Test
    public void test_updateStyleTags()
    {
        // Act
        Asset a1 = dao.get(1);
        a1.setStyleTags(TestData.ASSET_STYLE_TAGS_ALT);
        dao.updateStyleTags(a1);

        // Test
        Asset a1_copy = dao.get(1);
        Assert.assertEquals(TestData.ASSET_STYLE_TAGS_ALT, a1_copy.getStyleTags());
    }
}
