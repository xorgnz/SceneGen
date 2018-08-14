package org.memehazard.wheel.query.view;

import org.memehazard.wheel.query.model.Relationship;


public class RelationshipDescriptor
{
    private EntityDescriptor   object;
    private RelationDescriptor relation;
    private EntityDescriptor   subject;


    public RelationshipDescriptor(Relationship r)
    {
        this.subject = new EntityDescriptor(r.getSubject());
        this.object = new EntityDescriptor(r.getObject());
        this.relation = new RelationDescriptor(r.getRelation());
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || !(o instanceof RelationshipDescriptor))
            return false;

        if (subject == null && relation == null && object == null)
            return super.equals(o);

        if (subject != null && !subject.equals(((RelationshipDescriptor) o).getSubject()))
            return false;

        if (relation != null && !relation.equals(((RelationshipDescriptor) o).getRelation()))
            return false;

        if (object != null && !object.equals(((RelationshipDescriptor) o).getObject()))
            return false;

        return true;
    }


    public EntityDescriptor getObject()
    {
        return object;
    }


    public RelationDescriptor getRelation()
    {
        return relation;
    }


    public EntityDescriptor getSubject()
    {
        return subject;
    }


    @Override
    public int hashCode()
    {
        return (""
                + (subject != null ? subject.hashCode() : "null")
                + (relation != null ? relation.hashCode() : "null")
                + (object != null ? object.hashCode() : "null"))
                .hashCode();
    }
}
