package org.memehazard.wheel.style.dao;

import java.util.List;

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
public class StyleDAOTest_listByStylesheet
{
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(StyleDAOTest_listByStylesheet.class);

    @Autowired
    private StyleDAO      dao;

    @Autowired
    private StylesheetDAO dao_sheet;

    @Autowired
    private TestDAO_Style dao_test;


    @Before
    public void before()
    {
        dao_test.clear();
    }


    @Test
    @Transactional
    public void test_listByStylesheet()
    {
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
        Style s1 = new Style(ss0,
                TestData.STYLE_TAG[1],
                TestData.STYLE_ALPHA[1],
                TestData.STYLE_AMBIENT[1],
                TestData.STYLE_DIFFUSE[1],
                TestData.STYLE_EMISSIVE[1],
                TestData.STYLE_SPECULAR[1],
                TestData.STYLE_SHININESS[1],
                TestData.STYLE_PRIORITY[1]);
        Style s2 = new Style(ss0,
                TestData.STYLE_TAG[2],
                TestData.STYLE_ALPHA[2],
                TestData.STYLE_AMBIENT[2],
                TestData.STYLE_DIFFUSE[2],
                TestData.STYLE_EMISSIVE[2],
                TestData.STYLE_SPECULAR[2],
                TestData.STYLE_SHININESS[2],
                TestData.STYLE_PRIORITY[2]);
        Style s3 = new Style(ss1,
                TestData.STYLE_TAG[3],
                TestData.STYLE_ALPHA[3],
                TestData.STYLE_AMBIENT[3],
                TestData.STYLE_DIFFUSE[3],
                TestData.STYLE_EMISSIVE[3],
                TestData.STYLE_SPECULAR[3],
                TestData.STYLE_SHININESS[3],
                TestData.STYLE_PRIORITY[3]);
        dao_sheet.add(ss0);
        dao_sheet.add(ss1);
        dao.add(s0);
        dao.add(s1);
        dao.add(s2);
        dao.add(s3);

        // Act
        List<Style> styles = dao.listByStylesheet(ss0.getId());

        // Test - are all objects retrieved?
        Assert.assertEquals(3, styles.size());
        Style s0_copy = styles.get(2);
        Style s1_copy = styles.get(1);
        Style s2_copy = styles.get(0);

        // Test - are objects ordered by name?
        Assert.assertEquals(s0.getTag(), s0_copy.getTag());
        Assert.assertEquals(s1.getTag(), s1_copy.getTag());
        Assert.assertEquals(s2.getTag(), s2_copy.getTag());
    }
}
