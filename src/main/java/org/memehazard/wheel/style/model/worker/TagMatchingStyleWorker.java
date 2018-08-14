package org.memehazard.wheel.style.model.worker;

import org.apache.commons.lang.ArrayUtils;
import org.memehazard.wheel.style.model.Stylable;
import org.memehazard.wheel.style.model.Style;

public class TagMatchingStyleWorker implements StyleWorker
{
    private Style style;


    public TagMatchingStyleWorker(Style style)
    {
        this.style = style;
    }


    @Override
    public boolean apply(Stylable obj)
    {
        if (ArrayUtils.contains(obj.getStyleTags(), style.getTag()))
        {
            obj.setStyle(style);
            return true;
        }
        return false;
    }
}
