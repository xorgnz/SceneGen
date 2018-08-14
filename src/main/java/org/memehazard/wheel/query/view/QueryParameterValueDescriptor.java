package org.memehazard.wheel.query.view;


public class QueryParameterValueDescriptor
{
    private String value;
    private String variable;


    public QueryParameterValueDescriptor(String variable, String value)
    {
        this.value = value;
        this.variable = variable;
    }


    public String getValue()
    {
        return value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getVariable()
    {
        return variable;
    }


    public void setVariable(String variable)
    {
        this.variable = variable;
    }
}
