package org.memehazard.wheel.query.view;

import org.memehazard.wheel.query.model.Relation;


public class RelationDescriptor
{
    private String label;
    private String relation;


    public RelationDescriptor(Relation r)
    {
        this.label = r.getLabel();
        this.relation = r.getRelation();
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || !(o instanceof RelationDescriptor))
            return false;

        if (label == null && relation == null)
            return super.equals(o);

        if (label != null && !label.equals(((RelationDescriptor) o).getLabel()))
            return false;

        if (relation != null && !relation.equals(((RelationDescriptor) o).getRelation()))
            return false;

        return true;
    }


    public String getLabel()
    {
        return label;
    }


    public String getRelation()
    {
        return relation;
    }


    @Override
    public int hashCode()
    {
        return (""
                + (label != null ? label.hashCode() : "null")
                + (relation != null ? relation.hashCode() : "null"))
                .hashCode();
    }
}
