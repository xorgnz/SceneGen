package org.memehazard.wheel.style.dao;

import java.util.List;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class StylesheetDAOTest_listAll
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
    public void test_listAll()
    {
        // PREP - Create objects
        Stylesheet ss0 = new Stylesheet(TestData.SSHEET_NAMES[0], TestData.SSHEET_DESCRIPTIONS[0], TestData.SSHEET_TAGS[0]);
        Stylesheet ss1 = new Stylesheet(TestData.SSHEET_NAMES[1], TestData.SSHEET_DESCRIPTIONS[1], TestData.SSHEET_TAGS[1]);
        Stylesheet ss2 = new Stylesheet(TestData.SSHEET_NAMES[2], TestData.SSHEET_DESCRIPTIONS[2], TestData.SSHEET_TAGS[2]);
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
        Style s2 = new Style(ss1,
                TestData.STYLE_TAG[2],
                TestData.STYLE_ALPHA[2],
                TestData.STYLE_AMBIENT[2],
                TestData.STYLE_DIFFUSE[2],
                TestData.STYLE_EMISSIVE[2],
                TestData.STYLE_SPECULAR[2],
                TestData.STYLE_SHININESS[2],
                TestData.STYLE_PRIORITY[2]);
        dao.add(ss0);
        dao.add(ss1);
        dao.add(ss2);
        dao_style.add(s0);
        dao_style.add(s1);
        dao_style.add(s2);

        // ACT - listAll
        List<Stylesheet> ssheets = dao.listAll();
        Stylesheet ss0_copy = ssheets.get(2);
        Stylesheet ss1_copy = ssheets.get(1);
        Stylesheet ss2_copy = ssheets.get(0);

        // Test - are all objects retrieved?
        Assert.assertEquals(3, ssheets.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(ss0.getName(), ss0_copy.getName());
        Assert.assertEquals(ss0.getDescription(), ss0_copy.getDescription());
        Assert.assertEquals(ss0.getTags(), ss0_copy.getTags());        
        Assert.assertEquals(ss1.getName(), ss1_copy.getName());
        Assert.assertEquals(ss2.getName(), ss2_copy.getName());

        // Test - do stylesheets have correct style counts?
        Assert.assertEquals(2, ss0_copy.getStyleCount());
        Assert.assertEquals(1, ss1_copy.getStyleCount());
        Assert.assertEquals(0, ss2_copy.getStyleCount());
    }

}
