package org.memehazard.wheel.query.model;



public class Relation
{
    private String       label;
    private String       relation;
    private String       resourceString;


    public Relation(String resourceString)
    {
        this.resourceString = resourceString;
        this.relation = RelationRegistry.replaceURLWithNSPrefix(resourceString);
        this.label = RelationRegistry.getLabelForRelation(this.relation);
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || !(o instanceof Relation))
            return false;

        if (resourceString == null)
            return super.equals(o);

        return resourceString.equals(((Relation) o).resourceString);
    }


    public String getLabel()
    {
        return label;
    }


    public String getRelation()
    {
        return relation;
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


    public void setResourceString(String resourceString)
    {
        this.resourceString = resourceString;
    }
}
