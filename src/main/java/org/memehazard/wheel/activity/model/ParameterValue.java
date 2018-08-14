package org.memehazard.wheel.activity.model;

public class ParameterValue
{
    private String label;
    private String type;
    private String value;
    private String variable;


    public ParameterValue(String variable, String label, String type, String value)
    {
        this.variable = variable;
        this.label = label;
        this.type = type;
        this.value = value;
    }


    public String getLabel()
    {
        return label;
    }


    public String getType()
    {
        return type;
    }


    public String getValue()
    {
        return value;
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


    public void setValue(String value)
    {
        this.value = value;
    }


    public void setVariable(String variable)
    {
        this.variable = variable;
    }
}
