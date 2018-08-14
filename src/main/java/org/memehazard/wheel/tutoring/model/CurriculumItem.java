package org.memehazard.wheel.tutoring.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;


/**
 * A node representing an item within a curriculum. A curriculum item is defined as a named collection of Entity and
 * Relation Knowledge, and is assigned to a curriculum.
 * 
 * A CurriculumItem is considered <b>orphaned</b> if it is not assigned a curriculum
 * 
 * @author xorgnz
 */
@NodeEntity
public class CurriculumItem implements Comparable<CurriculumItem>, DomainModelNode
{
    @RelatedTo(direction = Direction.OUTGOING, type = "Part_Of")
    private Curriculum                        curriculum;
    private String                            description;
    private transient List<EntityKnowledge>   entityKnowledge   = new ArrayList<EntityKnowledge>();
    private transient LabelKnowledge          labelKnowledge;
    private String                            name;
    @GraphId
    private Long                              nodeId;
    private transient List<RelationKnowledge> relationKnowledge = new ArrayList<RelationKnowledge>();
    private Double                            weight            = 1.0;


    public CurriculumItem()
    {
    }


    public CurriculumItem(String name, String description, Double weight)
    {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }


    public CurriculumItem(String name, String description, Double weight, Curriculum curriculum)
    {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.curriculum = curriculum;
    }


    @Override
    public int compareTo(CurriculumItem o)
    {
        return this.getName().compareTo(o.getName());
    }


    public void computeBayesValue(Map<Long, BayesValue> bvMap)
    {
        // Sum P() for all facts
        double sum = 0;
        int count = 0;

        // P for entity knowledge not counted - already feeds in via relation knowledge

        // Add P for relation knowledge
        for (RelationKnowledge rk : relationKnowledge)
        {
            sum += bvMap.get(rk.getNodeId()).getP() * rk.getRelationFacts().size();
            count += rk.getRelationFacts().size();
        }

        // Add P for label knowledge
        if (labelKnowledge != null)
        {
            sum += bvMap.get(labelKnowledge.getNodeId()).getP() * labelKnowledge.getLabelFacts().size();
            count += labelKnowledge.getLabelFacts().size();
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

        if (o == null || !(o instanceof CurriculumItem))
            return false;

        if (nodeId == null)
            return super.equals(o);

        return nodeId.equals(((CurriculumItem) o).nodeId);
    }


    public EntityKnowledge findEntityKnowledge(Long id)
    {
        for (EntityKnowledge ek : entityKnowledge)
            if (ek.getNodeId().equals(id))
                return ek;

        return null;
    }


    public RelationKnowledge findRelationKnowledge(Long id)
    {
        for (RelationKnowledge rk : relationKnowledge)
            if (rk.getNodeId().equals(id))
                return rk;

        return null;
    }


    public Curriculum getCurriculum()
    {
        return curriculum;
    }


    public String getDescription()
    {
        return description;
    }


    public List<EntityKnowledge> getEntityKnowledge()
    {
        // Necessary because Neo4J somehow bypasses initialization
        if (this.entityKnowledge == null)
            this.entityKnowledge = new ArrayList<EntityKnowledge>();

        return entityKnowledge;
    }


    public LabelKnowledge getLabelKnowledge()
    {
        return labelKnowledge;
    }


    public String getName()
    {
        return name;
    }


    public Long getNodeId()
    {
        return nodeId;
    }


    public List<RelationKnowledge> getRelationKnowledge()
    {
        // Necessary because Neo4J somehow bypasses initialization
        if (this.relationKnowledge == null)
            this.relationKnowledge = new ArrayList<RelationKnowledge>();

        return relationKnowledge;
    }


    public Double getWeight()
    {
        return weight;
    }


    @Override
    public int hashCode()
    {
        return nodeId != null ? nodeId.hashCode() : super.hashCode();
    }


    public void setCurriculum(Curriculum curriculum)
    {
        this.curriculum = curriculum;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public void setEntityKnowledge(List<EntityKnowledge> entityKnowledge)
    {
        // Necessary because Neo4J somehow bypasses initialization
        if (this.entityKnowledge == null)
            this.entityKnowledge = new ArrayList<EntityKnowledge>();

        if (entityKnowledge != null)
        {
            this.entityKnowledge.clear();
            this.entityKnowledge.addAll(entityKnowledge);

        }
    }


    public void setLabelKnowledge(LabelKnowledge labelKnowledge)
    {
        this.labelKnowledge = labelKnowledge;
    }


    public void setName(String title)
    {
        this.name = title;
    }


    public void setNodeId(Long nodeId)
    {
        this.nodeId = nodeId;
    }


    public void setRelationKnowledge(List<RelationKnowledge> relationKnowledge)
    {
        // Necessary because Neo4J somehow bypasses initialization
        if (this.relationKnowledge == null)
            this.relationKnowledge = new ArrayList<RelationKnowledge>();

        if (relationKnowledge != null)
            this.relationKnowledge = relationKnowledge;
    }


    public void setWeight(Double weight)
    {
        this.weight = weight;
    }


    @Override
    public String toString()
    {
        return "CurriculumItem: " + name + " (" + nodeId + ")";
    }
}
