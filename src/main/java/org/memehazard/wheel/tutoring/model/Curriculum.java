package org.memehazard.wheel.tutoring.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;


/**
 * A node representing a curriculum, defined as a list of curriculum items.
 * 
 * Curriculum nodes are the roots of the ITS domain model, and are usually looked up directly by ID.
 * 
 * @author xorgnz
 */
@NodeEntity(useShortNames = true)
public class Curriculum implements Comparable<Curriculum>, DomainModelNode, Serializable
{
    private static final long              serialVersionUID = 7628599172991946928L;
    private String                         creatorName;
    private transient List<CurriculumItem> curriculumItems  = new ArrayList<CurriculumItem>();
    private String                         description;
    @Indexed
    private String                         name;
    @GraphId
    private Long                           nodeId;


    public Curriculum()
    {
    }


    public Curriculum(String name, String creatorName, String description)
    {
        this.name = name;
        this.creatorName = creatorName;
        this.description = description;
    }


    @Override
    public int compareTo(Curriculum o)
    {
        return this.getName().compareTo(o.getName());
    }


    public void computeBayesValue(Map<Long, BayesValue> bvMap)
    {
        // Sum P() for all facts
        double sum = 0;
        double count = 0;

        for (CurriculumItem ci : curriculumItems)
        {
            sum += bvMap.get(ci.getNodeId()).getP() * ci.getWeight();
            count += ci.getWeight();
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

        if (o == null || !(o instanceof Curriculum))
            return false;

        if (nodeId == null)
            return super.equals(o);

        return nodeId.equals(((Curriculum) o).nodeId);
    }


    public String getCreatorName()
    {
        return creatorName;
    }


    public List<CurriculumItem> getCurriculumItems()
    {
        return curriculumItems;
    }


    public String getDescription()
    {
        return description;
    }


    public String getName()
    {
        return name;
    }


    public Long getNodeId()
    {
        return nodeId;
    }


    @Override
    public int hashCode()
    {
        return nodeId != null ? nodeId.hashCode() : super.hashCode();
    }


    public void setCreatorName(String creatorName)
    {
        this.creatorName = creatorName;
    }


    public void setCurriculumItems(Collection<CurriculumItem> curriculumItems)
    {
        if (this.curriculumItems == null)
            this.curriculumItems = new ArrayList<CurriculumItem>();

        if (curriculumItems != null)
        {
            this.curriculumItems.clear();
            this.curriculumItems.addAll(curriculumItems);
        }
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setNodeId(Long nodeId)
    {
        this.nodeId = nodeId;
    }


    @Override
    public String toString()
    {
        return "Curriculum: " + name + " (" + nodeId + ")";
    }


    public List<DomainModelNode> getAllDomainNodes()
    {
        Set<DomainModelNode> nodes = new HashSet<DomainModelNode>();
        nodes.add(this);

        nodes.addAll(this.getCurriculumItems());
        for (CurriculumItem ci : this.getCurriculumItems())
        {
            // Add nodes associated with relation knowledge
            nodes.addAll(ci.getRelationKnowledge());
            for (RelationKnowledge rk : ci.getRelationKnowledge())
                nodes.addAll(rk.getRelationFacts());

            System.err.println("foo");
            for (DomainModelNode dmn : nodes)
                System.err.println(dmn);

            // Add nodes associated with entity knowledge
            nodes.addAll(ci.getEntityKnowledge());
            for (EntityKnowledge ek : ci.getEntityKnowledge())
            {
                nodes.addAll(ek.getRelationFacts());
                if (ek.getLabelFact() != null)
                    nodes.add(ek.getLabelFact());
            }

            System.err.println("foo");
            for (DomainModelNode dmn : nodes)
                System.err.println(dmn);

            // Add nodes associated with label knowledge
            if (ci.getLabelKnowledge() != null)
            {
                nodes.add(ci.getLabelKnowledge());
                nodes.addAll(ci.getLabelKnowledge().getLabelFacts());
            }

            System.err.println("foo");
            for (DomainModelNode dmn : nodes)
                System.err.println(dmn);
        }
        return new ArrayList<DomainModelNode>(nodes);
    }


    public List<LabelFact> getAllLabelFacts()
    {
        List<LabelFact> labelFacts = new ArrayList<LabelFact>();

        for (CurriculumItem ci : this.getCurriculumItems())
            if (ci.getLabelKnowledge() != null)
                labelFacts.addAll(ci.getLabelKnowledge().getLabelFacts());

        return labelFacts;
    }


    public CurriculumItem findCurriculumItem(Long id)
    {
        for (CurriculumItem ci : curriculumItems)
            if (ci.getNodeId().equals(id))
                return ci;

        return null;
    }
}
