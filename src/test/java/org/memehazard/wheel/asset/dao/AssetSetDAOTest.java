package org.memehazard.wheel.asset.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.asset.dao.AssetDAO;
import org.memehazard.wheel.asset.dao.AssetSetDAO;
import org.memehazard.wheel.asset.model.AssetSet;
import org.memehazard.wheel.asset.test.TestDAO_Asset;
import org.memehazard.wheel.asset.test.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class AssetSetDAOTest
{
    @Autowired
    private AssetSetDAO     dao;

    @Autowired
    private AssetDAO        dao_asset;


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
        AssetSet a = new AssetSet(TestData.SET_NAMES[0] + " alt", TestData.SET_MAINTAINER_ALL + " alt");
        dao.add(a);

        // Test - Did we get an object ID?
        Assert.assertNotNull(a.getId());

        // Test - does it show in list all?
        List<AssetSet> sets = dao.listAll();
        Assert.assertEquals(5, sets.size());

        // Test - is it correct?
        AssetSet a2 = sets.get(4);
        Assert.assertEquals(TestData.SET_NAMES[0] + " alt", a2.getName());
        Assert.assertEquals(TestData.SET_MAINTAINER_ALL + " alt", a2.getMaintainer());
    }


    @Test
    public void test_delete()
    {
        // Act - by ID
        dao.delete(1);

        // Test - is it gone?
        Assert.assertEquals(3, dao.listAll().size());

        // Test - have associated assets been cascade deleted?
        Assert.assertEquals(2, dao_asset.listAll().size());
    }


    @Test
    public void test_findByName()
    {
        // ACT - attempt to find asset set by name
        AssetSet a = dao.getByName(TestData.SET_NAMES[2]);

        // TEST - did we get an object back?
        Assert.assertNotNull(a);

        // TEST - does that object have expected values?
        Assert.assertEquals(3, (int) a.getId());
        Assert.assertEquals(TestData.SET_NAMES[2], a.getName());
        Assert.assertEquals(TestData.SET_MAINTAINER_ALL, a.getMaintainer());
    }


    @Test
    public void test_get()
    {
        // Act
        AssetSet a = dao.get(1);

        // Test - did we get an object back?
        Assert.assertNotNull(a);

        // Test - does that object have expected values?
        Assert.assertEquals(1, a.getId());
        Assert.assertEquals(TestData.SET_NAMES[0], a.getName());
        Assert.assertEquals(TestData.SET_MAINTAINER_ALL, a.getMaintainer());
    }


    @Test
    public void test_listAll()
    {
        // Act
        List<AssetSet> sets = dao.listAll();

        // Test - are all objects retrieved?
        Assert.assertEquals(4, sets.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.SET_IDS[0], sets.get(3).getId());
        Assert.assertEquals(TestData.SET_IDS[1], sets.get(2).getId());
        Assert.assertEquals(TestData.SET_IDS[2], sets.get(1).getId());
        Assert.assertEquals(TestData.SET_IDS[3], sets.get(0).getId());
        Assert.assertEquals(TestData.SET_NAMES[0], sets.get(3).getName());
        Assert.assertEquals(TestData.SET_NAMES[1], sets.get(2).getName());
        Assert.assertEquals(TestData.SET_NAMES[2], sets.get(1).getName());
        Assert.assertEquals(TestData.SET_NAMES[3], sets.get(0).getName());
    }


    @Test
    public void test_listAllWithCounts()
    {
        // Act
        List<AssetSet> sets = dao.listAllWithCounts();

        // Test - are all objects retrieved?
        Assert.assertEquals(4, sets.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.SET_IDS[0], sets.get(3).getId());
        Assert.assertEquals(TestData.SET_IDS[1], sets.get(2).getId());
        Assert.assertEquals(TestData.SET_IDS[2], sets.get(1).getId());
        Assert.assertEquals(TestData.SET_IDS[3], sets.get(0).getId());
        Assert.assertEquals(TestData.SET_NAMES[0], sets.get(3).getName());
        Assert.assertEquals(TestData.SET_NAMES[1], sets.get(2).getName());
        Assert.assertEquals(TestData.SET_NAMES[2], sets.get(1).getName());
        Assert.assertEquals(TestData.SET_NAMES[3], sets.get(0).getName());

        // Test - are counts correct?
        Assert.assertEquals(0, (int) sets.get(0).getAssetCount());
        Assert.assertEquals(0, (int) sets.get(1).getAssetCount());
        Assert.assertEquals(2, (int) sets.get(2).getAssetCount());
        Assert.assertEquals(4, (int) sets.get(3).getAssetCount());
    }

    
    // TODO - Test AssetSetDAO.listAllWithAssets()
    

    @Test
    public void test_update()
    {
        // Act
        AssetSet a1 = dao.get(1);
        a1.setName(TestData.SET_NAMES[0] + " alt");
        a1.setMaintainer(TestData.SET_MAINTAINER_ALL + " alt");
        dao.update(a1);

        // Test - have changes been made?
        AssetSet a1_copy = dao.get(1);
        Assert.assertEquals(1, (int) a1_copy.getId());
        Assert.assertEquals(TestData.SET_NAMES[0] + " alt", a1_copy.getName());
        Assert.assertEquals(TestData.SET_MAINTAINER_ALL + " alt", a1_copy.getMaintainer());
    }
}
