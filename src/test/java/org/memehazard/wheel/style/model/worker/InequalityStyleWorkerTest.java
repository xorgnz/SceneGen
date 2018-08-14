package org.memehazard.wheel.style.model.worker;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.style.model.Stylable;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.model.Stylesheet;
import org.memehazard.wheel.style.model.worker.InequalityStyleWorker.InequalityType;
import org.memehazard.wheel.style.test.TestData;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class InequalityStyleWorkerTest
{

    @Test
    public void test_gt()
    {
        // PREP - Prepare styles
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
        InequalityStyleWorker worker = new InequalityStyleWorker(s0, InequalityType.INEQ_MORE_THAN, "data", 10.0f);

        // PREP - Prepare and style test objects
        StylableImpl stylable_none = new StylableImpl();
        StylableImpl stylable_notnum = new StylableImpl();
        StylableImpl stylable_less = new StylableImpl("0.0");
        StylableImpl stylable_equal = new StylableImpl("10.0");
        StylableImpl stylable_more = new StylableImpl("15.0");
        worker.apply(stylable_none);
        worker.apply(stylable_notnum);
        worker.apply(stylable_less);
        worker.apply(stylable_equal);
        worker.apply(stylable_more);

        // TEST - Verify all objects are styled correctly
        Assert.assertTrue("Should not have applied", stylable_none.getStyle() == null);
        Assert.assertTrue("Should not have applied", stylable_notnum.getStyle() == null);
        Assert.assertTrue("Should not have applied", stylable_less.getStyle() == null);
        Assert.assertTrue("Should not have applied", stylable_equal.getStyle() == null);
        Assert.assertTrue("Should have applied", stylable_more.getStyle() != null);
    }


    @Test
    public void test_gte()
    {
        // PREP - Prepare styles
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
        InequalityStyleWorker worker = new InequalityStyleWorker(s0, InequalityType.INEQ_MORE_THAN_EQUAL, "data", 10.0f);

        // PREP - Prepare and style test objects
        StylableImpl stylable_none = new StylableImpl();
        StylableImpl stylable_notnum = new StylableImpl();
        StylableImpl stylable_less = new StylableImpl("0.0");
        StylableImpl stylable_equal = new StylableImpl("10.0");
        StylableImpl stylable_more = new StylableImpl("15.0");
        worker.apply(stylable_none);
        worker.apply(stylable_notnum);
        worker.apply(stylable_less);
        worker.apply(stylable_equal);
        worker.apply(stylable_more);

        // TEST - Verify all objects are styled correctly
        Assert.assertTrue("Should not have applied", stylable_none.getStyle() == null);
        Assert.assertTrue("Should not have applied", stylable_notnum.getStyle() == null);
        Assert.assertTrue("Should not have applied", stylable_less.getStyle() == null);
        Assert.assertTrue("Should have applied", stylable_equal.getStyle() != null);
        Assert.assertTrue("Should have applied", stylable_more.getStyle() != null);
    }


    @Test
    public void test_lt()
    {
        // PREP - Prepare styles
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
        InequalityStyleWorker worker = new InequalityStyleWorker(s0, InequalityType.INEQ_LESS_THAN, "data", 10.0f);

        // PREP - Prepare and style test objects
        StylableImpl stylable_none = new StylableImpl();
        StylableImpl stylable_notnum = new StylableImpl();
        StylableImpl stylable_less = new StylableImpl("0.0");
        StylableImpl stylable_equal = new StylableImpl("10.0");
        StylableImpl stylable_more = new StylableImpl("15.0");
        worker.apply(stylable_none);
        worker.apply(stylable_notnum);
        worker.apply(stylable_less);
        worker.apply(stylable_equal);
        worker.apply(stylable_more);

        // TEST - Verify all objects are styled correctly
        Assert.assertTrue("Should not have applied", stylable_none.getStyle() == null);
        Assert.assertTrue("Should not have applied", stylable_notnum.getStyle() == null);
        Assert.assertTrue("Should have applied", stylable_less.getStyle() != null);
        Assert.assertTrue("Should not have applied", stylable_equal.getStyle() == null);
        Assert.assertTrue("Should not have applied", stylable_more.getStyle() == null);
    }


    @Test
    public void test_lte()
    {
        // PREP - Prepare styles
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
        InequalityStyleWorker worker = new InequalityStyleWorker(s0, InequalityType.INEQ_LESS_THAN_EQUAL, "data", 10.0f);

        // PREP - Prepare and style test objects
        StylableImpl stylable_none = new StylableImpl();
        StylableImpl stylable_notnum = new StylableImpl();
        StylableImpl stylable_less = new StylableImpl("0.0");
        StylableImpl stylable_equal = new StylableImpl("10.0");
        StylableImpl stylable_more = new StylableImpl("15.0");
        worker.apply(stylable_none);
        worker.apply(stylable_notnum);
        worker.apply(stylable_less);
        worker.apply(stylable_equal);
        worker.apply(stylable_more);

        // TEST - Verify all objects are styled correctly
        Assert.assertTrue("Should not have applied", stylable_none.getStyle() == null);
        Assert.assertTrue("Should not have applied", stylable_notnum.getStyle() == null);
        Assert.assertTrue("Should have applied", stylable_less.getStyle() != null);
        Assert.assertTrue("Should have applied", stylable_equal.getStyle() != null);
        Assert.assertTrue("Should not have applied", stylable_more.getStyle() == null);
    }


    public class StylableImpl implements Stylable
    {
        private Style               style;
        private Map<String, Object> data = new HashMap<String, Object>();


        public StylableImpl()
        {

        }


        public StylableImpl(String s)
        {
            this.data.put("data", s);
        }


        @Override
        public Map<String, Object> getData()
        {
            return data;
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
            return true;
        }


        @Override
        public String[] getStyleTags()
        {
            return new String[] { "foo", "bar", "bas" };
        }
    }
}
