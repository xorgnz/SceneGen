package org.memehazard.wheel.style.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.model.Stylesheet;
import org.memehazard.wheel.style.test.TestDAO_Style;
import org.memehazard.wheel.style.test.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class StylesheetDAOTest_crud
{
    @Autowired
    private StylesheetDAO dao;

    @Autowired
    private StyleDAO      dao_style;

    @Autowired
    private TestDAO_Style dao_test;


    @Before
    public void before()
    {
        dao_test.clear();
    }


    @Test
    @Transactional
    public void test()
    {
        // PREP - Create objects
        Stylesheet ss0 = new Stylesheet(TestData.SSHEET_NAMES[0], TestData.SSHEET_DESCRIPTIONS[0], TestData.SSHEET_TAGS[0]);
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
        dao.add(ss0);
        dao_style.add(s0);

        // TEST - Add created object ID
        Assert.assertNotNull(ss0.getId());

        // ACT - Get
        Stylesheet ss0_copy = dao.get(ss0.getId());
        Style s0_copy = ss0_copy.getStyles().get(0);

        // TEST - Get returned correct Stylesheet and parameters
        Assert.assertEquals(ss0.getName(), ss0_copy.getName());
        Assert.assertEquals(ss0.getDescription(), ss0_copy.getDescription());
        Assert.assertEquals(ss0.getTags(), ss0_copy.getTags());
        Assert.assertEquals(s0.getTag(), s0_copy.getTag());
        Assert.assertEquals(s0.getAlpha(), s0_copy.getAlpha(), 0.0005);
        Assert.assertEquals(s0.getAmbient(), s0_copy.getAmbient());
        Assert.assertEquals(s0.getDiffuse(), s0_copy.getDiffuse());
        Assert.assertEquals(s0.getEmissive(), s0_copy.getEmissive());
        Assert.assertEquals(s0.getSpecular(), s0_copy.getSpecular());
        Assert.assertEquals(s0.getShininess(), s0_copy.getShininess());
        Assert.assertEquals(s0.getPriority(), s0_copy.getPriority());

        // TEST - Style count remains 0

        // ACT - Update
        ss0.setName(TestData.SSHEET_NAMES[2]);
        ss0.setDescription(TestData.SSHEET_DESCRIPTIONS[2]);
        dao.update(ss0);

        // TEST - Update seen in retrieved object
        ss0_copy = dao.get(ss0.getId());
        Assert.assertEquals(ss0.getName(), ss0_copy.getName());
        Assert.assertEquals(ss0.getDescription(), ss0_copy.getDescription());
        Assert.assertEquals(ss0.getTags(), ss0_copy.getTags());

        // ACT - Delete
        dao.delete(ss0.getId());

        // TEST - Deleted
        Assert.assertEquals(0, dao.listAll().size());
    }
}
