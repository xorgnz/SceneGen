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
public class LabelFact implements Comparable<LabelFact>, DomainModelNode
{
    @GraphId
    private Long              nodeId;

    @RelatedTo(type = "Fact_Lbl_Entity")
    @Fetch
    private EntityKnowledge   entity;
    @RelatedTo(type = "Fact_Lbl_Knowl")
    @Fetch
    private LabelKnowledge    labelKnowledge;

    // Length of time interval that must pass between applying updates
    private static final long UPDATE_MINIMUM_TIME_INTERVAL = 0;


    public LabelFact()
    {

    }


    public LabelFact(EntityKnowledge entity, LabelKnowledge lk)
    {
        this.entity = entity;
        this.labelKnowledge = lk;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || !(o instanceof LabelFact))
            return false;

        if (nodeId == null)
            return super.equals(o);

        return nodeId.equals(((LabelFact) o).nodeId);
    }


    public Long getNodeId()
    {
        return nodeId;
    }


    public EntityKnowledge getEntityKnowledge()
    {
        return entity;
    }


    public LabelKnowledge getLabelKnowledge()
    {
        return labelKnowledge;
    }


    @Override
    public int hashCode()
    {
        return nodeId != null ? nodeId.hashCode() : super.hashCode();
    }


    public boolean isRepresentedBy(LabelFact f)
    {
        if (labelKnowledge.getNodeId() == null || f.getLabelKnowledge().getNodeId() == null
            || !f.getLabelKnowledge().getNodeId().equals(labelKnowledge.getNodeId()))
            return false;

        if (entity.getNodeId() == null || f.getEntityKnowledge().getNodeId() == null
            || !f.getEntityKnowledge().getNodeId().equals(entity.getNodeId()))
            return false;

        return true;
    }


    // PERF - Eliminate by defining comparator that can be used to construct collections of distinct Facts (TreeSet, for
    // example)
    public boolean isRepresentedInList(List<LabelFact> facts)
    {
        for (LabelFact f : facts)
            if (this.isRepresentedBy(f))
                return true;

        return false;
    }


    public void setNodeId(Long nodeId)
    {
        this.nodeId = nodeId;
    }


    public void setEntityKnowledge(EntityKnowledge object)
    {
        this.entity = object;
    }


    public void setLabelKnowledge(LabelKnowledge lk)
    {
        this.labelKnowledge = lk;
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
        return "LabelFact(" + nodeId + "): " + entity.toString() + " -- " + labelKnowledge.toString();
    }


    @Override
    public int compareTo(LabelFact o)
    {
        return this.getNodeId().compareTo(o.getNodeId());
    }


    @Override
    public void computeBayesValue(Map<Long, BayesValue> bvMap)
    {
        // Do nothing
    }

}
