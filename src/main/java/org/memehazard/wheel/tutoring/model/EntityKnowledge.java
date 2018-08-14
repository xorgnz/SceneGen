package org.memehazard.wheel.tutoring.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.neo4j.graphdb.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * A node representing knowledge about a particular (FMA) entity. EK nodes are sometimes referred to in comments as 'EK
 * chunks'. The proper term is EK node.
 * 
 * An EK node is considered <b>orphaned</b> if it is not attached to a curriculum item directly or indirectly through
 * a fact and associated EK node.
 * 
 * An EK node is considered <b>hanging</b> if it is not attached to a curriculum item directly, but is attached
 * indirectly via a fact and its other associated EK node. An EK node that is not <b>hanging</b> is considered
 * <b>linked</b>.
 * 
 * @author xorgnz
 */
@NodeEntity
public class EntityKnowledge implements Comparable<EntityKnowledge>, DomainModelNode
{
    @SuppressWarnings("unused")
    private static Logger               log           = LoggerFactory.getLogger(EntityKnowledge.class);
    @RelatedTo(direction = Direction.OUTGOING, type = "EKPart_Of")
    @Fetch
    private CurriculumItem              curriculumItem;
    private Integer                     fmaId;
    private String                      fmaLabel;
    @Transient
    private transient LabelFact         labelFact;
    @GraphId
    private Long                        nodeId;
    private transient Set<RelationFact> relationFacts = new TreeSet<RelationFact>();


    public EntityKnowledge()
    {
    }


    public EntityKnowledge(Integer fmaId, String fmaLabel)
    {
        this.setFmaId(fmaId);
        this.setFmaLabel(fmaLabel);
    }


    public EntityKnowledge(Integer fmaId, String fmaLabel, CurriculumItem curriculumItem)
    {
        this.setFmaId(fmaId);
        this.setFmaLabel(fmaLabel);
        this.setCurriculumItem(curriculumItem);
    }


    public void addRelationFact(RelationFact rf)
    {
        // Necessary because Neo4J somehow bypasses initialization
        if (this.relationFacts == null)
            this.relationFacts = new TreeSet<RelationFact>();

        this.relationFacts.add(rf);
    }


    @Override
    public int compareTo(EntityKnowledge o)
    {
        return this.getFmaLabel().compareTo(o.getFmaLabel());
    }


    public void computeBayesValue(Map<Long, BayesValue> bvMap)
    {
        double sum = 0;
        int count = 0;

        // Add up P for relation facts
        for (RelationFact f : getRelationFacts())
        {
            sum += bvMap.get(f.getNodeId()).getP();
            count++;
        }
        
        // Add P for label fact (if one exists)
        if (getLabelFact() != null)
        {
            sum += bvMap.get(getLabelFact().getNodeId()).getP();
            count++;
        }

        // Calculate P = Mean ( P(Facts) )
        if (count > 0)
            bvMap.get(getNodeId()).setP(sum / count);
        else
            bvMap.get(getNodeId()).setP(1);
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || !(o instanceof EntityKnowledge))
            return false;

        if (nodeId == null)
            return super.equals(o);

        return nodeId.equals(((EntityKnowledge) o).nodeId);
    }


    public CurriculumItem getCurriculumItem()
    {
        return curriculumItem;
    }


    public Integer getFmaId()
    {
        return fmaId;
    }


    public String getFmaLabel()
    {
        return fmaLabel;
    }


    public LabelFact getLabelFact()
    {
        return labelFact;
    }


    public Long getNodeId()
    {
        return nodeId;
    }


    public Set<RelationFact> getRelationFacts()
    {
        // Necessary because Neo4J somehow bypasses initialization
        if (this.relationFacts == null)
            this.relationFacts = new TreeSet<RelationFact>();

        return relationFacts;
    }


    @Override
    public int hashCode()
    {
        return nodeId != null ? nodeId.hashCode() : super.hashCode();
    }


    public boolean isLinked()
    {
        return curriculumItem != null;
    }


    public boolean isRepresentedBy(EntityKnowledge ek)
    {
        if (ek == null)
            return false;

        if (!ek.getFmaId().equals(fmaId))
            return false;
        if (!ek.getFmaLabel().equals(fmaLabel))
            return false;
        if (ek.getCurriculumItem() == null || curriculumItem == null || !ek.getCurriculumItem().equals(curriculumItem))
            return false;

        return true;
    }


    public boolean isRepresentedInList(List<EntityKnowledge> ekList)
    {
        for (EntityKnowledge ek : ekList)
            if (this.isRepresentedBy(ek))
                return true;

        return false;
    }


    public void setCurriculumItem(CurriculumItem curriculumItem)
    {
        this.curriculumItem = curriculumItem;
    }


    public void setFmaId(Integer fmaId)
    {
        this.fmaId = fmaId;
    }


    public void setFmaLabel(String fmaLabel)
    {
        this.fmaLabel = fmaLabel;
    }


    public void setLabelFact(LabelFact labelFact)
    {
        this.labelFact = labelFact;
    }


    public void setNodeId(Long nodeId)
    {
        this.nodeId = nodeId;
    }


    public void setRelationFacts(List<RelationFact> facts)
    {
        // Necessary because Neo4J somehow bypasses initialization
        if (this.relationFacts == null)
            this.relationFacts = new TreeSet<RelationFact>();

        if (facts != null)
        {
            this.relationFacts.clear();
            this.relationFacts.addAll(facts);
        }
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("EntityKnowledge(" + this.getNodeId() + "): " + this.getFmaLabel() + " [" + this.getFmaId() + "] ");
        if (curriculumItem == null)
            sb.append("CI null");
        else
            sb.append("CI " + this.getCurriculumItem().getNodeId());

        return sb.toString();
    }
}