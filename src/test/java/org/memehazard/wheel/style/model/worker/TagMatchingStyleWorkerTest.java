package org.memehazard.wheel.style.model.worker;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.style.model.Stylable;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.model.Stylesheet;
import org.memehazard.wheel.style.test.TestData;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class TagMatchingStyleWorkerTest
{

    @Test
    public void test()
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
        TagMatchingStyleWorker worker = new TagMatchingStyleWorker(s0);

        // PREP - Prepare test objects
        StylableImpl stylable1 = new StylableImpl(true, TestData.STYLE_TAG);
        StylableImpl stylable2 = new StylableImpl(true, new String[] { "foo", "bar", "bas" });
        worker.apply(stylable1);
        worker.apply(stylable2);

        // TEST - Verify all objects are styled correctly
        Assert.assertTrue("Tag match should exist", stylable1.getStyle() != null);
        Assert.assertTrue("Tag match should not exist", stylable2.getStyle() == null);
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
