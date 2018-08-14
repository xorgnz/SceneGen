package org.memehazard.wheel.tutoring.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * A node representing knowledge about a particular (FMA) relation. RK nodes are sometimes referred to in comments as
 * 'RK
 * chunks'. The proper term is RK node.
 * 
 * A RK node is considered <code>orphaned</code> if it is not attached to a curriculum item.
 * 
 * @author xorgnz
 */
@NodeEntity
public class RelationKnowledge implements Comparable<RelationKnowledge>, DomainModelNode
{
    @RelatedTo(direction = Direction.OUTGOING, type = "RKPart_Of")
    @Fetch
    private CurriculumItem              curriculumItem;
    private String                      name;
    private String                      namespace;
    @GraphId
    private Long                        nodeId;
    private transient Set<RelationFact> relationFacts = new TreeSet<RelationFact>();


    public RelationKnowledge()
    {
    }


    public RelationKnowledge(String relationString)
    {
        this(relationString, (CurriculumItem) null);
    }


    public RelationKnowledge(String relationString, CurriculumItem curriculumItem)
    {
        if (!relationString.contains(":"))
            throw new IllegalArgumentException("Relation string must contain namespace and name");

        String[] bits = relationString.split(":");

        this.namespace = bits[0];
        this.name = bits[1];
        this.curriculumItem = curriculumItem;
    }


    public RelationKnowledge(String name, String namespace)
    {
        this(name, namespace, null);
    }


    public RelationKnowledge(String name, String namespace, CurriculumItem curriculumItem)
    {
        this.name = name;
        this.namespace = namespace;
        this.curriculumItem = curriculumItem;
    }


    public void addRelationFact(RelationFact rf)
    {
        // Necessary because Neo4J somehow bypasses initialization
        if (this.relationFacts == null)
            this.relationFacts = new TreeSet<RelationFact>();

        relationFacts.add(rf);
    }


    @Override
    public int compareTo(RelationKnowledge o)
    {
        return (this.namespace + ":" + this.getName()).compareTo(o.getNamespace() + ":" + o.getName());
    }


    public void computeBayesValue(Map<Long, BayesValue> bvMap)
    {
        // Sum P() for all facts
        double sum = 0;
        int count = 0;
        for (RelationFact f : getRelationFacts())
        {
            sum += bvMap.get(f.getNodeId()).getP();
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

        if (o == null || !(o instanceof RelationKnowledge))
            return false;

        if (nodeId == null)
            return super.equals(o);

        return nodeId.equals(((RelationKnowledge) o).nodeId);
    }


    public CurriculumItem getCurriculumItem()
    {
        return curriculumItem;
    }


    public String getName()
    {
        return name;
    }


    public String getNamespace()
    {
        return namespace;
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


    public boolean isRepresentedBy(RelationKnowledge rk)
    {
        if (rk == null)
            return false;

        if (!rk.getName().equals(name))
            return false;
        if (!rk.getNamespace().equals(namespace))
            return false;
        if (rk.getCurriculumItem() == null || curriculumItem == null || !rk.getCurriculumItem().equals(curriculumItem))
            return false;

        return true;
    }


    public boolean isRepresentedInList(List<RelationKnowledge> rkList)
    {
        for (RelationKnowledge rk : rkList)
            if (this.isRepresentedBy(rk))
                return true;

        return false;
    }


    public void setCurriculumItem(CurriculumItem curriculumItem)
    {
        this.curriculumItem = curriculumItem;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
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


    public String toRelationString()
    {
        return this.namespace + ":" + this.name;
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("RelationKnowledge(" + this.getNodeId() + "): " + this.namespace + ":" + this.name + " - ");
        if (curriculumItem == null)
            sb.append("CI null");
        else
            sb.append("CI " + this.getCurriculumItem().getNodeId());

        return sb.toString();
    }
}