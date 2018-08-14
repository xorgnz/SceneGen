package org.memehazard.wheel.activity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.text.StrBuilder;

public class ActivityTemplate implements Serializable
{
    private static final long               serialVersionUID = 1L;
    private String                          description      = "";
    private String                          factsUrl         = "";
    private int                             id;
    private String                          name             = "";
    private List<Parameter> parameters       = new ArrayList<Parameter>();
    private String                          playUrl          = "";


    public ActivityTemplate()
    {
    }


    public ActivityTemplate(String name, String description, String playUrl, String factsUrl)
    {
        this.name = name;
        this.description = description;
        this.playUrl = playUrl;
        this.factsUrl = factsUrl;
    }


    public void addParameter(Parameter qp)
    {
        this.parameters.add(qp);
    }


    public void clearParameters()
    {
        this.parameters.clear();
    }


    public String getDescription()
    {
        return description;
    }


    public String getFactsUrl()
    {
        return factsUrl;
    }


    public int getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public List<Parameter> getParameters()
    {
        return parameters;
    }


    public String getPlayUrl()
    {
        return playUrl;
    }


    public void removeParameter(Parameter gtp)
    {
        this.parameters.remove(gtp);
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public void setFactsUrl(String factsUrl)
    {
        this.factsUrl = factsUrl;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setParameters(Parameter... params)
    {
        this.parameters.clear();
        for (Parameter qp : params)
            this.parameters.add(qp);
    }


    public void setParameters(List<Parameter> params)
    {
        this.parameters.clear();
        this.parameters.addAll(params);
    }


    public void setPlayUrl(String playUrl)
    {
        this.playUrl = playUrl;
    }


    public String toString()
    {
        StrBuilder sb = new StrBuilder();
        sb.appendln(String.format("ActivityTemplate (%d): %s\n # %s\n # %s", id, name, playUrl, factsUrl));

        for (Parameter param : parameters)
            sb.appendln(" - " + param.toString());

        return sb.toString();
    }
}
