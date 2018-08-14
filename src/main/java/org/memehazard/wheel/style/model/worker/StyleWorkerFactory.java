package org.memehazard.wheel.style.model.worker;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.model.worker.InequalityStyleWorker.InequalityType;


public class StyleWorkerFactory
{
    public static StyleWorker getInstance(Style style)
    {
        String tag = style.getTag();

        // Inequality - less than
        if (tag.contains("<="))
        {
            String[] parts = StringUtils.split(tag, "<=");
            if (parts.length == 2 && NumberUtils.isNumber(parts[1].trim()))
                return new InequalityStyleWorker(style, InequalityType.INEQ_LESS_THAN_EQUAL, parts[0].trim(), NumberUtils.toFloat(parts[1].trim()));
        }
        else if (tag.contains("<"))
        {
            String[] parts = StringUtils.split(tag, "<");
            if (parts.length == 2 && NumberUtils.isNumber(parts[1].trim()))
                return new InequalityStyleWorker(style, InequalityType.INEQ_LESS_THAN, parts[0].trim(), NumberUtils.toFloat(parts[1].trim()));
        }

        // Inequality - greater than
        if (tag.contains(">="))
        {
            String[] parts = StringUtils.split(tag, ">=");
            if (parts.length == 2 && NumberUtils.isNumber(parts[1].trim()))
                return new InequalityStyleWorker(style, InequalityType.INEQ_MORE_THAN_EQUAL, parts[0].trim(), NumberUtils.toFloat(parts[1].trim()));
        }
        else if (tag.contains(">"))
        {
            String[] parts = StringUtils.split(tag, ">");
            if (parts.length == 2 && NumberUtils.isNumber(parts[1].trim()))
                return new InequalityStyleWorker(style, InequalityType.INEQ_MORE_THAN, parts[0].trim(), NumberUtils.toFloat(parts[1].trim()));
        }


        return new TagMatchingStyleWorker(style);
    }
}
