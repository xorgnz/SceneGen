package org.memehazard.wheel.activity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.springframework.util.Assert;

public class ActivityInstance implements Serializable
{
    private static final long serialVersionUID     = 1L;
    private String            description          = "";
    private int               id;
    private String            name                 = "";
    private String            parameterValueString = "";


    private ActivityTemplate  template             = null;


    public ActivityInstance()
    {
    }


    public ActivityInstance(String name, String description, ActivityTemplate template, String parameterString)
    {
        this.setName(name);
        this.setDescription(description);
        this.setTemplate(template);
        this.parameterValueString = parameterString;
    }


    public String getDescription()
    {
        return description;
    }


    public int getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public String getParameterValueString()
    {
        return parameterValueString;
    }


    public ActivityTemplate getTemplate()
    {
        return template;
    }


    public List<ParameterValue> getParameterValues()
    {
        // Validate - Param arrays of equal size
        Assert.isTrue(template != null, "Cannot create parameter values for activity instance with null activity template");

        // Create parameter values from value map and template parameters
        List<ParameterValue> paramValues = new ArrayList<ParameterValue>();
        Map<String, String> valueMap = this.parseParameterValueMap();
        for (Parameter atp : this.getTemplate().getParameters())
        {
            String value = valueMap.get(atp.getVariable());
            paramValues.add(new ParameterValue(atp.getVariable(), atp.getLabel(), atp.getType(), value != null ? value : ""));
        }

        return paramValues;
    }


    public Map<String, String> parseParameterValueMap()
    {
        Map<String, String> map = new TreeMap<String, String>();

        if (parameterValueString != null)
        {
            String[] parameters = StringUtils.split(parameterValueString, "&");

            for (String parameter : parameters)
            {
                String[] parts = StringUtils.split(parameter, "=", 2);

                if (parts.length == 2)
                    map.put(parts[0], parts[1]);
                else
                    map.put(parts[0], "");
            }
        }
        return map;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setParameterValues(Map<String, String> paramValueMap)
    {
        String[] parameters = new String[paramValueMap.size()];

        int i = 0;
        for (String key : paramValueMap.keySet())
        {
            parameters[i] = key + "=" + paramValueMap.get(key);
            i++;
        }

        this.parameterValueString = StringUtils.join(parameters, "&");
    }


    public void setParameterValueString(String parameterValueString)
    {
        this.parameterValueString = parameterValueString;
    }


    public void setTemplate(ActivityTemplate template)
    {
        this.template = template;
    }


    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.appendln(String.format("ActivityInstance (%d): %s with Template-%s", id, name, template.getName()));

        Map<String, String> parameters = parseParameterValueMap();
        for (String key : parameters.keySet())
            sb.appendln(" - " + key + " = " + parameters.get(key));

        return sb.toString();
    }
}
