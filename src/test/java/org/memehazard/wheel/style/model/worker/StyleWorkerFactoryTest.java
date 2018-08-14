package org.memehazard.wheel.style.model.worker;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.model.Stylesheet;
import org.memehazard.wheel.style.test.TestData;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class StyleWorkerFactoryTest
{
    private static final String EXPR_GT_TAG  = "Foo > 3.0";
    private static final String EXPR_GTE_TAG = "Foo >= 3.0";
    private static final String EXPR_LT_TAG  = "Foo < 3.0";
    private static final String EXPR_LTE_TAG = "Foo <= 3.0";


    @Test
    public void test_gt()
    {
        // PREP - Create stylesheet, style, and style worker
        Stylesheet ss0 = new Stylesheet(TestData.SSHEET_NAMES[0], TestData.SSHEET_DESCRIPTIONS[0], TestData.SSHEET_TAGS[0]);
        Style s0 = new Style(ss0,
                EXPR_GT_TAG,
                TestData.STYLE_ALPHA[0],
                TestData.STYLE_AMBIENT[0],
                TestData.STYLE_DIFFUSE[0],
                TestData.STYLE_EMISSIVE[0],
                TestData.STYLE_SPECULAR[0],
                TestData.STYLE_SHININESS[0],
                TestData.STYLE_PRIORITY[0]);
        StyleWorker sw = StyleWorkerFactory.getInstance(s0);

        // TEST - Verify correct worker was created
        Assert.assertTrue("StyleWorker Java type", sw instanceof InequalityStyleWorker);
        InequalityStyleWorker isw = (InequalityStyleWorker) sw;
        Assert.assertEquals("Foo", isw.getDataKey());
        Assert.assertEquals(InequalityStyleWorker.InequalityType.INEQ_MORE_THAN, isw.getType());
        Assert.assertEquals(3.0, isw.getValue(), 0.0);
    }


    @Test
    public void test_gte()
    {
        // PREP - Create stylesheet, style, and style worker
        Stylesheet ss0 = new Stylesheet(TestData.SSHEET_NAMES[0], TestData.SSHEET_DESCRIPTIONS[0], TestData.SSHEET_TAGS[0]);
        Style s0 = new Style(ss0,
                EXPR_GTE_TAG,
                TestData.STYLE_ALPHA[0],
                TestData.STYLE_AMBIENT[0],
                TestData.STYLE_DIFFUSE[0],
                TestData.STYLE_EMISSIVE[0],
                TestData.STYLE_SPECULAR[0],
                TestData.STYLE_SHININESS[0],
                TestData.STYLE_PRIORITY[0]);
        StyleWorker sw = StyleWorkerFactory.getInstance(s0);

        // TEST - Verify correct worker was created
        Assert.assertTrue("StyleWorker type", sw instanceof InequalityStyleWorker);
        InequalityStyleWorker isw = (InequalityStyleWorker) sw;
        Assert.assertEquals("Foo", isw.getDataKey());
        Assert.assertEquals(InequalityStyleWorker.InequalityType.INEQ_MORE_THAN_EQUAL, isw.getType());
        Assert.assertEquals(3.0, isw.getValue(), 0.0);
    }


    @Test
    public void test_lt()
    {
        // PREP - Create stylesheet, style, and style worker
        Stylesheet ss0 = new Stylesheet(TestData.SSHEET_NAMES[0], TestData.SSHEET_DESCRIPTIONS[0], TestData.SSHEET_TAGS[0]);
        Style s0 = new Style(ss0,
                EXPR_LT_TAG,
                TestData.STYLE_ALPHA[0],
                TestData.STYLE_AMBIENT[0],
                TestData.STYLE_DIFFUSE[0],
                TestData.STYLE_EMISSIVE[0],
                TestData.STYLE_SPECULAR[0],
                TestData.STYLE_SHININESS[0],
                TestData.STYLE_PRIORITY[0]);
        StyleWorker sw = StyleWorkerFactory.getInstance(s0);

        // TEST - Verify correct worker was created
        Assert.assertTrue("StyleWorker type", sw instanceof InequalityStyleWorker);
        InequalityStyleWorker isw = (InequalityStyleWorker) sw;
        Assert.assertEquals("Foo", isw.getDataKey());
        Assert.assertEquals(InequalityStyleWorker.InequalityType.INEQ_LESS_THAN, isw.getType());
        Assert.assertEquals(3.0, isw.getValue(), 0.0);
    }


    @Test
    public void test_lte()
    {
        // PREP - Create stylesheet, style, and style worker
        Stylesheet ss0 = new Stylesheet(TestData.SSHEET_NAMES[0], TestData.SSHEET_DESCRIPTIONS[0], TestData.SSHEET_TAGS[0]);
        Style s0 = new Style(ss0,
                EXPR_LTE_TAG,
                TestData.STYLE_ALPHA[0],
                TestData.STYLE_AMBIENT[0],
                TestData.STYLE_DIFFUSE[0],
                TestData.STYLE_EMISSIVE[0],
                TestData.STYLE_SPECULAR[0],
                TestData.STYLE_SHININESS[0],
                TestData.STYLE_PRIORITY[0]);
        StyleWorker sw = StyleWorkerFactory.getInstance(s0);

        // TEST - Verify correct worker was created
        Assert.assertTrue("StyleWorker type", sw instanceof InequalityStyleWorker);
        InequalityStyleWorker isw = (InequalityStyleWorker) sw;
        Assert.assertEquals("Foo", isw.getDataKey());
        Assert.assertEquals(InequalityStyleWorker.InequalityType.INEQ_LESS_THAN_EQUAL, isw.getType());
        Assert.assertEquals(3.0, isw.getValue(), 0.0);
    }
}
