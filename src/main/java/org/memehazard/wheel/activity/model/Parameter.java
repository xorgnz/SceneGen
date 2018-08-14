package org.memehazard.wheel.activity.model;

import java.io.Serializable;

public class Parameter implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String            label            = "";
    private String            type             = "";
    private String            variable         = "";


    public Parameter()
    {

    }


    public Parameter(String label, String variable, String type)
    {
        this.label = label;
        this.type = type;
        this.variable = variable;
    }


    public String getLabel()
    {
        return label;
    }


    public String getType()
    {
        return type;
    }


    public String getVariable()
    {
        return variable;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public void setVariable(String variable)
    {
        this.variable = variable;
    }


    public String toString()
    {
        return String.format("ActivityTemplateParameter: %s (%s) - %s", variable, type, label);
    }

}
