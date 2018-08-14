package org.memehazard.wheel.tutoring.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;


/**
 * A node representing a fact; that is, an assertion that a subject EK node is related to an object EK node by a RK
 * node.
 * 
 * A fact node is considered <code>incomplete</code> if any of its subject, object, or relation nodes are not defined.
 * 
 * @author xorgnz
 */
@NodeEntity
public class RelationFact implements DomainModelNode, Comparable<RelationFact>
{
    @GraphId
    private Long              nodeId;
    @RelatedTo(type = "Fact_Obj")
    @Fetch
    private EntityKnowledge   object;
    @RelatedTo(type = "Fact_Rel")
    @Fetch
    private RelationKnowledge relation;
    @RelatedTo(type = "Fact_Subj")
    @Fetch
    private EntityKnowledge   subject;

    // Length of time interval that must pass between applying updates
    private static final long UPDATE_MINIMUM_TIME_INTERVAL = 0;


    public RelationFact()
    {

    }


    public RelationFact(EntityKnowledge subject, RelationKnowledge relation, EntityKnowledge object)
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

        if (o == null || !(o instanceof RelationFact))
            return false;

        if (nodeId == null)
            return super.equals(o);

        return nodeId.equals(((RelationFact) o).nodeId);
    }


    public Long getNodeId()
    {
        return nodeId;
    }


    public EntityKnowledge getObject()
    {
        return object;
    }


    public RelationKnowledge getRelation()
    {
        return relation;
    }


    public EntityKnowledge getSubject()
    {
        return subject;
    }


    @Override
    public int hashCode()
    {
        return nodeId != null ? nodeId.hashCode() : super.hashCode();
    }


    public boolean isRepresentedBy(RelationFact f)
    {
        if (subject.getNodeId() == null || f.getSubject().getNodeId() == null || !f.getSubject().getNodeId().equals(subject.getNodeId()))
            return false;
        if (relation.getNodeId() == null || f.getRelation().getNodeId() == null || !f.getRelation().getNodeId().equals(relation.getNodeId()))
            return false;
        if (object.getNodeId() == null || f.getObject().getNodeId() == null || !f.getObject().getNodeId().equals(object.getNodeId()))
            return false;

        return true;
    }


    // PERF - Eliminate by defining comparator that can be used to construct collections of distinct Facts (TreeSet, for
    // example)
    public boolean isRepresentedInList(List<RelationFact> facts)
    {
        for (RelationFact f : facts)
            if (this.isRepresentedBy(f))
                return true;

        return false;
    }


    public void setNodeId(Long nodeId)
    {
        this.nodeId = nodeId;
    }


    public void setObject(EntityKnowledge object)
    {
        this.object = object;
    }


    public void setRelation(RelationKnowledge relation)
    {
        this.relation = relation;
    }


    public void setSubject(EntityKnowledge subject)
    {
        this.subject = subject;
    }


    public void updateBoolean(BayesValue bv, boolean b)
    {
        // Calculate time since last update
        Date now = new Date();
        long delta = now.getTime() - bv.getTimestamp().getTime();

        // Update only if minimal time interval has passed
        if (delta > UPDATE_MINIMUM_TIME_INTERVAL)
        {
            double p = bv.getP();

            if (b)
                p = p + (1 - p) / 2;
            else
                p = p / 2;

            bv.setTimestamp(new Date());
            bv.setP(p);
        }
    }


    @Override
    public String toString()
    {
        return "Fact(" + nodeId + "): " + subject.toString() + " -[" + relation.toString() + "]-> " + object.toString();
    }


    @Override
    public int compareTo(RelationFact o)
    {
        return this.getNodeId().compareTo(o.getNodeId());
    }


    @Override
    public void computeBayesValue(Map<Long, BayesValue> bvMap)
    {
        // Do nothing
    }
}
