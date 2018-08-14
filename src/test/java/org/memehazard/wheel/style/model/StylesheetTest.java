package org.memehazard.wheel.style.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.style.test.TestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class StylesheetTest
{
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(StylesheetTest.class);


    /**
     * Test whether a stylesheet can correctly style a group of stylable objects
     * 
     * See StyleWorker tests for testing of particular styling logic - this test focuses on the ability of a stylesheet
     * to respond correctly to a list of objects
     */
    @Test
    public void test_styleObjects()
    {
        // PREP - Prepare stylesheet
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

        ss0.addStyle(s0);
        ss0.addStyle(s1);
        ss0.addStyle(s2);

        // PREP - Prepare a mix of stylable objects
        StylableImpl stylable1 = new StylableImpl(false, TestData.STYLE_TAG);
        StylableImpl stylable2 = new StylableImpl(true, new String[] { "foo", "bar", "bas" });
        StylableImpl stylable3 = new StylableImpl(true, new String[] { TestData.STYLE_TAG[0] });
        StylableImpl stylable4 = new StylableImpl(true, new String[] { TestData.STYLE_TAG[1], TestData.STYLE_TAG[2], TestData.STYLE_TAG[0] });
        StylableImpl stylable5 = new StylableImpl(true, new String[] { TestData.STYLE_TAG[2] });

        // PREP - Style objects
        List<Stylable> objects = new ArrayList<Stylable>();
        objects.add(stylable1);
        objects.add(stylable2);
        objects.add(stylable3);
        objects.add(stylable4);
        objects.add(stylable5);
        ss0.styleObjects(objects);

        // TEST - Verify all objects are styled correctly
        Assert.assertTrue("Style not needed", stylable1.getStyle() == null);
        Assert.assertTrue("No matching style found", stylable2.getStyle() == null);
        Assert.assertTrue("Simple match", stylable3.getStyle().getTag().equalsIgnoreCase(TestData.STYLE_TAG[0]));
        Assert.assertTrue("Match out of order", stylable4.getStyle().getTag().equalsIgnoreCase(TestData.STYLE_TAG[0]));
        Assert.assertTrue("Match other than first", stylable5.getStyle().getTag().equalsIgnoreCase(TestData.STYLE_TAG[2]));
    }


    public class StylableImpl implements Stylable
    {
        private boolean  need;
        private String[] tags;
        private Style    style;


        public StylableImpl(boolean need, String[] tags)
        {
            this.need = need;
            this.tags = tags;

        }


        public Map<String, Object> getData()
        {
            return null;
        }


        public Style getStyle()
        {
            return style;
        }


        @Override
        public void setStyle(Style style)
        {
            this.style = style;
        }


        @Override
        public boolean needsStyle()
        {
            return need;
        }


        @Override
        public String[] getStyleTags()
        {
            return tags;
        }
    }
}
