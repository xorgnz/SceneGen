package org.memehazard.wheel.style.view;

import org.memehazard.wheel.style.model.Stylesheet;

public class StylesheetDescriptor
{
    private String description;
    private int    id;
    private String name;
    private String tags;


    public StylesheetDescriptor(Stylesheet ss)
    {
        this.setId(ss.getId());
        this.setName(ss.getName());
        this.setDescription(ss.getDescription());
        this.setTags(ss.getTags());
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


    public String getTags()
    {
        return tags;
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


    public void setTags(String tags)
    {
        this.tags = tags;
    }

}
