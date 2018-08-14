package org.memehazard.wheel.query.model;

public class Relationship
{
    private Entity   object;
    private Relation relation;
    private Entity   subject;


    public Relationship(Entity subject, Relation relation, Entity object)
    {
        this.subject = subject;
        this.object = object;
        this.relation = relation;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || !(o instanceof Relationship))
            return false;

        if (subject == null && relation == null && object == null)
            return super.equals(o);


        if (subject != null && !subject.equals(((Relationship) o).getSubject()))
            return false;

        if (relation != null && !relation.equals(((Relationship) o).getRelation()))
            return false;

        if (object != null && !object.equals(((Relationship) o).getObject()))
            return false;

        return true;
    }


    public Entity getObject()
    {
        return object;
    }


    public Relation getRelation()
    {
        return relation;
    }


    public Entity getSubject()
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


    @Override
    public String toString()
    {
        return subject.getResourceString() + " " + relation.getResourceString() + " " + object.getResourceString();
    }
}