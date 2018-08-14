package org.memehazard.wheel.query.view;

import java.util.HashMap;
import java.util.Map;

import org.memehazard.wheel.query.model.Entity;


public class EntityDescriptor
{
    private Map<String, Object> data           = new HashMap<String, Object>(); ;
    private Integer             id             = 0;
    private String              name           = "";
    private String              resourceString = "";


    public EntityDescriptor()
    {
    }


    public EntityDescriptor(Entity entity)
    {
        if (entity != null)
        {
            this.name = entity.getName();
            this.resourceString = entity.getResourceString();
            this.id = entity.getId();
            this.data = entity.getData();
        }
    }


    public EntityDescriptor(Integer id, String name, String resourceString)
    {
        this.id = id;
        this.name = name;
        this.resourceString = resourceString;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || !(o instanceof EntityDescriptor))
            return false;

        if (id == null && name == null && resourceString == null)
            return super.equals(o);

        if (id != null && !id.equals(((EntityDescriptor) o).getId()))
            return false;

        if (name != null && !name.equals(((EntityDescriptor) o).getName()))
            return false;

        if (resourceString != null && !resourceString.equals(((EntityDescriptor) o).getResourceString()))
            return false;

        return true;
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
        return (""
                + (id != null ? id.hashCode() : "null")
                + (name != null ? name.hashCode() : "null")
                + (resourceString != null ? resourceString.hashCode() : "null"))
                .hashCode();
    }


    public boolean isValid()
    {
        return this.id != null;
    }


    @Override
    public String toString()
    {
        return "Entity - " + name + " [" + id + "]";
    }
}
