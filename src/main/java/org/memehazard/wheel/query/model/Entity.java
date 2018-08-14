package org.memehazard.wheel.query.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrBuilder;

public class Entity implements Serializable
{
    private static final long   serialVersionUID = -8050622983246730085L;
    private Map<String, Object> data             = new HashMap<String, Object>();
    private Integer             id               = null;
    private String              name             = null;
    private String              resourceString;


    public Entity()
    {
    }


    public Entity(String resourceString, Integer id, String name)
    {
        this.resourceString = resourceString;
        this.id = id;
        this.name = name;
    }


    public void addDataItem(String key, Object value)
    {
        this.data.put(key, value);
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || !(o instanceof Entity))
            return false;

        if (resourceString == null)
            return super.equals(o);

        return resourceString.equals(((Entity) o).resourceString);
    }


    public Map<String, Object> getData()
    {
        return data;
    }


    public Integer getId()
    {
        return id;
    }


    public String getName()
    {

        return name;
    }


    public String getResourceString()
    {
        return resourceString;
    }


    @Override
    public int hashCode()
    {
        return resourceString != null ? resourceString.hashCode() : super.hashCode();
    }


    public void setData(Map<String, Object> data)
    {
        this.data = data;
    }


    public void setResourceString(String resourceString)
    {
        this.resourceString = resourceString;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    @Override
    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.append(resourceString + " - " + name + "(" + id + ")");

        return sb.toString();
    }
}
