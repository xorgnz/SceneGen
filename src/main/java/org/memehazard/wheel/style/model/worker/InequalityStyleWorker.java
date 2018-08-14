package org.memehazard.wheel.style.model.worker;

import org.apache.commons.lang.math.NumberUtils;
import org.memehazard.wheel.style.model.Stylable;
import org.memehazard.wheel.style.model.Style;


public class InequalityStyleWorker implements StyleWorker
{
    private String         dataKey;
    private Style          style;
    private InequalityType type;
    private Float value;


    public InequalityStyleWorker(Style style, InequalityType type, String dataKey, Float value)
    {
        if (style == null)
            throw new IllegalArgumentException("Style cannot be null");
        if (type == null)
            throw new IllegalArgumentException("Inequality type cannot be null");
        if (dataKey == null)
            throw new IllegalArgumentException("Data key cannot be null");
        if (value == null)
            throw new IllegalArgumentException("Value cannot be null");

        this.style = style;
        this.type = type;
        this.dataKey = dataKey;
        this.value = value;
    }


    @Override
    public boolean apply(Stylable s)
    {
        if (s.getData().keySet().contains(this.dataKey))
        {
            String data = s.getData().get(this.dataKey).toString();
            if (NumberUtils.isNumber(data))
            {
                Float dataValue = NumberUtils.toFloat(data);
                boolean match = false;
                if (this.type == InequalityType.INEQ_LESS_THAN_EQUAL && dataValue <= value)
                    match = true;
                else if (this.type == InequalityType.INEQ_LESS_THAN && dataValue < value)
                    match = true;
                else if (this.type == InequalityType.INEQ_MORE_THAN_EQUAL && dataValue >= value)
                    match = true;
                else if (this.type == InequalityType.INEQ_MORE_THAN && dataValue > value)
                    match = true;

                if (match)
                {
                    s.setStyle(style);
                    return true;
                }
            }
        }
        return false;
    }


    public String getDataKey()
    {
        return dataKey;
    }


    public InequalityType getType()
    {
        return type;
    }


    public Float getValue()
    {
        return value;
    }


    public static enum InequalityType
    {
        INEQ_LESS_THAN,
        INEQ_LESS_THAN_EQUAL,
        INEQ_MORE_THAN,
        INEQ_MORE_THAN_EQUAL,
    }
}
