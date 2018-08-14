package org.memehazard.wheel.tutoring.model;

import java.util.Map;

public interface DomainModelNode
{

    public void computeBayesValue(Map<Long, BayesValue> bvMap);


    public Long getNodeId();
}
