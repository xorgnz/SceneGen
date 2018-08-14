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
 * A node representing knowledge about the labels of entities within a curriculum. In shorthand, instances of
 * this class are referred to as LK nodes.
 * 
 * A LK node is considered <code>orphaned</code> if it is not attached to a curriculum item.
 * 
 * @author xorgnz
 */
@NodeEntity
public class LabelKnowledge implements Comparable<LabelKnowledge>, DomainModelNode
{
    @RelatedTo(direction = Direction.OUTGOING, type = "LKPart_Of")
    @Fetch
    private CurriculumItem           curriculumItem;
    private transient Set<LabelFact> labelFacts = new TreeSet<LabelFact>();
    @GraphId
    private Long                     nodeId;


    public LabelKnowledge()
    {
    }


    public LabelKnowledge(CurriculumItem curriculumItem)
    {
        this.curriculumItem = curriculumItem;
    }


    public void addLabelFact(LabelFact lf)
    {
        // Necessary because Neo4J somehow bypasses initialization
        if (this.labelFacts == null)
            this.labelFacts = new TreeSet<LabelFact>();
        
        this.labelFacts.add(lf);
    }


    @Override
    public int compareTo(LabelKnowledge o)
    {
        return this.nodeId.intValue() - o.getNodeId().intValue();
    }


    public void computeBayesValue(Map<Long, BayesValue> bvMap)
    {
        // Sum P() for all facts
        double sum = 0;
        int count = 0;
        for (LabelFact f : getLabelFacts())
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

        if (o == null || !(o instanceof LabelKnowledge))
            return false;

        if (nodeId == null)
            return super.equals(o);

        return nodeId.equals(((LabelKnowledge) o).nodeId);
    }


    public CurriculumItem getCurriculumItem()
    {
        return curriculumItem;
    }


    public Set<LabelFact> getLabelFacts()
    {
        // Necessary because Neo4J somehow bypasses initialization
        if (this.labelFacts == null)
            this.labelFacts = new TreeSet<LabelFact>();

        return labelFacts;
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


    public boolean isRepresentedBy(LabelKnowledge lk)
    {
        if (lk == null)
            return false;

        if (lk.getCurriculumItem() == null || curriculumItem == null || !lk.getCurriculumItem().equals(curriculumItem))
            return false;

        return true;
    }


    public boolean isRepresentedInList(List<LabelKnowledge> lkList)
    {
        for (LabelKnowledge lk : lkList)
            if (this.isRepresentedBy(lk))
                return true;

        return false;
    }


    public void setCurriculumItem(CurriculumItem curriculumItem)
    {
        this.curriculumItem = curriculumItem;
    }


    public void setLabelFacts(List<LabelFact> facts)
    {
        // Necessary because Neo4J somehow bypasses initialization
        if (this.labelFacts == null)
            this.labelFacts = new TreeSet<LabelFact>();
        
        if (facts != null)
        {
            this.labelFacts.clear();
            this.labelFacts.addAll(facts);
        }
    }


    public void setNodeId(Long nodeId)
    {
        this.nodeId = nodeId;
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("LabelKnowledge(" + this.getNodeId() + ") - ");

        if (curriculumItem == null)
            sb.append("CI null");
        else
            sb.append("CI " + this.getCurriculumItem().getNodeId());

        return sb.toString();
    }
}