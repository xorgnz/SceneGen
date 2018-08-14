package org.memehazard.wheel.style.dao;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.model.Stylesheet;
import org.memehazard.wheel.style.test.TestDAO_Style;
import org.memehazard.wheel.style.test.TestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class StyleDAOTest_crud
{
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(StyleDAOTest_crud.class);

    @Autowired
    private StyleDAO      dao;

    @Autowired
    private StylesheetDAO dao_ssheet;

    @Autowired
    private TestDAO_Style dao_test;


    @Before
    public void before()
    {
        dao_test.clear();
    }


    @Test
    @Transactional
    public void test_crud()
    {
        // PREP - Create objects
        Stylesheet ss0 = new Stylesheet(TestData.SSHEET_NAMES[0], TestData.SSHEET_DESCRIPTIONS[0], TestData.SSHEET_TAGS[0]);
        Stylesheet ss1 = new Stylesheet(TestData.SSHEET_NAMES[1], TestData.SSHEET_DESCRIPTIONS[1], TestData.SSHEET_TAGS[1]);
        Style s0 = new Style(ss0,
                TestData.STYLE_TAG[0],
                TestData.STYLE_ALPHA[0],
                TestData.STYLE_AMBIENT[0],
                TestData.STYLE_DIFFUSE[0],
                TestData.STYLE_EMISSIVE[0],
                TestData.STYLE_SPECULAR[0],
                TestData.STYLE_SHININESS[0],
                TestData.STYLE_PRIORITY[0]);

        // ACT - Add
        dao_ssheet.add(ss0);
        dao_ssheet.add(ss1);
        dao.add(s0);

        // TEST - Add created object ID
        Assert.assertNotNull(s0.getId());

        // ACT - Get
        Style s0_copy = dao.get(s0.getId());

        Assert.assertNotNull(s0_copy.getStylesheet());
        Assert.assertEquals(s0.getStylesheet().getId(), s0_copy.getStylesheet().getId());
        Assert.assertEquals(s0.getTag(), s0_copy.getTag());
        Assert.assertEquals(s0.getAlpha(), s0_copy.getAlpha(), 0.0005);
        Assert.assertEquals(s0.getAmbient(), s0_copy.getAmbient());
        Assert.assertEquals(s0.getDiffuse(), s0_copy.getDiffuse());
        Assert.assertEquals(s0.getEmissive(), s0_copy.getEmissive());
        Assert.assertEquals(s0.getSpecular(), s0_copy.getSpecular());
        Assert.assertEquals(s0.getShininess(), s0_copy.getShininess());
        Assert.assertEquals(s0.getPriority(), s0_copy.getPriority());

        // ACT - Update
        s0.setTag(TestData.STYLE_TAG[1]);
        s0.setAmbient(TestData.STYLE_AMBIENT[1]);
        s0.setDiffuse(TestData.STYLE_DIFFUSE[1]);
        s0.setEmissive(TestData.STYLE_EMISSIVE[1]);
        s0.setSpecular(TestData.STYLE_SPECULAR[1]);
        s0.setShininess(TestData.STYLE_SHININESS[1]);
        s0.setPriority(TestData.STYLE_PRIORITY[1]);
        s0.setStylesheet(ss1);
        dao.update(s0);

        // TEST - Update seen in retrieved object
        s0_copy = dao.get(s0.getId());
        Assert.assertNotNull(s0_copy.getStylesheet());
        Assert.assertEquals(s0.getStylesheet().getId(), s0_copy.getStylesheet().getId());
        Assert.assertEquals(s0.getTag(), s0_copy.getTag());
        Assert.assertEquals(s0.getAlpha(), s0_copy.getAlpha(), 0.0005);
        Assert.assertEquals(s0.getAmbient(), s0_copy.getAmbient());
        Assert.assertEquals(s0.getDiffuse(), s0_copy.getDiffuse());
        Assert.assertEquals(s0.getEmissive(), s0_copy.getEmissive());
        Assert.assertEquals(s0.getSpecular(), s0_copy.getSpecular());
        Assert.assertEquals(s0.getShininess(), s0_copy.getShininess());
        Assert.assertEquals(s0.getPriority(), s0_copy.getPriority());

        // ACT - Delete
        dao.delete(s0.getId());

        // TEST - Deleted
        Assert.assertEquals(0, dao.listAll().size());
    }
}
