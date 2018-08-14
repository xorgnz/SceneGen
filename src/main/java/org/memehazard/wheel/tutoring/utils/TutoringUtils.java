package org.memehazard.wheel.tutoring.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.tutoring.model.BayesValue;
import org.memehazard.wheel.tutoring.model.DomainModelNode;
import org.memehazard.wheel.tutoring.model.EntityKnowledge;
import org.memehazard.wheel.tutoring.model.LabelFact;
import org.memehazard.wheel.tutoring.model.RelationKnowledge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TutoringUtils
{

    private static Logger log = LoggerFactory.getLogger(TutoringUtils.class);


    public static Map<Long, BayesValue> mapifyBayesValuesByDomainId(List<BayesValue> bvList)
    {
        Map<Long, BayesValue> map = new HashMap<Long, BayesValue>();

        for (BayesValue bv : bvList)
            map.put(bv.getDomainId(), bv);

        return map;
    }


    public static Map<Long, BayesValue> mapifyBayesValuesByStudentId(List<BayesValue> bvList)
    {
        Map<Long, BayesValue> map = new HashMap<Long, BayesValue>();

        for (BayesValue bv : bvList)
            map.put(bv.getStudentId(), bv);

        return map;
    }


    public static <T extends DomainModelNode> Map<Long, T> mapifyDomainModelNodes(Collection<T> list)
    {
        Map<Long, T> map = new HashMap<Long, T>();

        for (T n : list)
            map.put(n.getNodeId(), n);

        return map;
    }


    /**
     * Convert a list of EK chunks into a map of EK chunks indexed by each EK's FMA ID
     * 
     * @param ekList
     * @return
     */
    public static Map<Integer, EntityKnowledge> mapifyEntityKnowledge(Collection<EntityKnowledge> ekList)
    {
        Map<Integer, EntityKnowledge> ekMap = new HashMap<Integer, EntityKnowledge>();
        for (EntityKnowledge ek : ekList)
        {
            if (ekMap.containsKey(ek.getFmaId()))
                log.warn("mapifyEntityKnowledge - duplicate key " + ek.getFmaId());
            ekMap.put(ek.getFmaId(), ek);
        }

        return ekMap;
    }


    public static Map<Long, LabelFact> mapifyLabelFactByEntityKnowledgeId(Collection<LabelFact> list)
    {
        Map<Long, LabelFact> map = new HashMap<Long, LabelFact>();
        for (LabelFact lf : list)
        {
            if (map.containsKey(lf.getEntityKnowledge().getNodeId()))
                log.warn("mapifyEntityKnowledge - duplicate key " + lf.getEntityKnowledge().getNodeId());
            map.put(lf.getEntityKnowledge().getNodeId(), lf);
        }

        return map;
    }


    /**
     * Convert a list of RK chunks into a map of RK chunks indexed by each RK's relation string
     * 
     * @param ekList
     * @return
     */
    public static Map<String, RelationKnowledge> mapifyRelationKnowledge(Collection<RelationKnowledge> rkList)
    {
        Map<String, RelationKnowledge> rkMap = new HashMap<String, RelationKnowledge>();
        for (RelationKnowledge rk : rkList)
        {
            String key = rk.toRelationString();
            if (rkMap.containsKey(key))
                log.warn("mapifyRelationKNowledge - duplicate key " + key);
            rkMap.put(key, rk);
        }

        return rkMap;
    }

}
