package org.memehazard.wheel.query.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class RelationRegistry
{
    private final Map<String, String> LABEL_MAP = new HashMap<String, String>();
    private final Map<String, String> NS_MAP    = new HashMap<String, String>();
    private static RelationRegistry   instance  = new RelationRegistry();


    private RelationRegistry()
    {
        LABEL_MAP.put("fma:arterial_supply", "Has arterial supply");       
        LABEL_MAP.put("fma:arterial_supply_of", "Is arterial supply of");       
        LABEL_MAP.put("fma:articulates_with", "Articulates with");
        LABEL_MAP.put("fma:attaches_to", "Attaches to");
        LABEL_MAP.put("fma:bounded_by", "Bounded by");   
        LABEL_MAP.put("fma:bounds", "Bounds");         
        LABEL_MAP.put("fma:branch", "Has branch");
        LABEL_MAP.put("fma:branch_of", "Is branch of");
        LABEL_MAP.put("fma:branch__continuity_", "Has branch continuity");
        LABEL_MAP.put("fma:constitutional_part", "Has constitutional part");
        LABEL_MAP.put("fma:constitutional_part_of", "Constitutional part of");
        LABEL_MAP.put("fma:contained_in", "Is contained in");
        LABEL_MAP.put("fma:contains", "Contains");
        LABEL_MAP.put("fma:continuous_distally_with", "Is continuous distally with");
        LABEL_MAP.put("fma:continuous_proximally_with", "Is continuous proximally with");
        LABEL_MAP.put("fma:continuous_with", "Continuous with");
        LABEL_MAP.put("fma:develops_from", "Develops from");
        LABEL_MAP.put("fma:direct_anterior_to", "Is directly anterior to");
        LABEL_MAP.put("fma:direct_inferior_to", "Is directly inferior to");
        LABEL_MAP.put("fma:direct_left_of", "Is directly left of");
        LABEL_MAP.put("fma:direct_right_of", "Is directly right of");
        LABEL_MAP.put("fma:direct_superior_to", "Is directly superior to");        
        LABEL_MAP.put("fma:has_insertion", "Has insertion");   
        LABEL_MAP.put("fma:insertion_of", "Is insertion of");
        LABEL_MAP.put("fma:lymphatic_drainage", "Has lymphatic drainage");           
        LABEL_MAP.put("fma:lymphatic_drainage_of", "Is lymphatic drainage of");           
        LABEL_MAP.put("fma:member", "Has member");                
        LABEL_MAP.put("fma:member_of", "Is member of");                        
        LABEL_MAP.put("fma:muscle_insertion", "Has muscle insertion");
        LABEL_MAP.put("fma:muscle_origin", "Has muscle origin");
        LABEL_MAP.put("fma:nerve_supply", "Has nerve supply");
        LABEL_MAP.put("fma:nerve_supply_of", "Is nerve supply of");
        LABEL_MAP.put("fma:primary_segmental_supply", "Has primary segmental supply");
        LABEL_MAP.put("fma:primary_segmental_supply_of", "Is primary segmental supply of");
        LABEL_MAP.put("fma:receives_attachment_from", "Receives attachment from");
        LABEL_MAP.put("fma:receives_input_from", "Receives input from");
        LABEL_MAP.put("fma:regional_part", "Has regional part");
        LABEL_MAP.put("fma:regional_part_of", "Regional part of");
        LABEL_MAP.put("fma:related_object", "Has related object");
        LABEL_MAP.put("fma:related_part", "Has related part");
        LABEL_MAP.put("fma:secondary_segmental_supply", "Has secondary segmental supply");
        LABEL_MAP.put("fma:secondary_segmental_supply_of", "Is secondary segmental supply of");
        LABEL_MAP.put("fma:segmental_supply_of", "Is segmental supply of");
        LABEL_MAP.put("fma:sends_output_to", "Sends output to");
        LABEL_MAP.put("fma:systemic_part_of", "Systemic part of");
        LABEL_MAP.put("fma:venous_drainage", "Has venous drainage");           
        LABEL_MAP.put("fma:venous_drainage_of", "Is venous drainage of");   
        LABEL_MAP.put("rdf:type", "Type");
        LABEL_MAP.put("rdfs:subClassOf", "Subclass of");

        NS_MAP.put("http://sig.uw.edu/fma#", "fma");
        NS_MAP.put("http://purl.org/sig/fma#", "fma");
        NS_MAP.put("http://purl.org/sig/ont/fma/", "fma");
        NS_MAP.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf");
        NS_MAP.put("http://www.w3.org/2000/01/rdf-schema#", "rdfs");
        NS_MAP.put("http://www.w3.org/2002/07/owl#", "owl");
    }


    public static String getLabelForRelation(String relation)
    {
        if (instance.LABEL_MAP.containsKey(relation))
            return instance.LABEL_MAP.get(relation);
        else
            return relation;
    }


    public static String replaceURLWithNSPrefix(String relation)
    {
        for (String url : instance.NS_MAP.keySet())
            if (StringUtils.startsWith(relation, url))
                relation = StringUtils.replace(relation, url, instance.NS_MAP.get(url) + ":", 1);

        return relation;
    }


    public static List<Relation> getRegisteredRelations()
    {
        List<Relation> relations = new ArrayList<Relation>();

        for (String key : instance.LABEL_MAP.keySet())
            relations.add(new Relation(key));

        return relations;
    }
}
