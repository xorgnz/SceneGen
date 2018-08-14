package org.memehazard.wheel.activity.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.activity.dao.ActivityInstanceDAO;
import org.memehazard.wheel.activity.dao.ActivityTemplateDAO;
import org.memehazard.wheel.activity.dao.ExerciseDAO;
import org.memehazard.wheel.activity.model.ActivityInstance;
import org.memehazard.wheel.activity.model.ActivityTemplate;
import org.memehazard.wheel.activity.model.Exercise;
import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.tutoring.dao.CurriculumDAO;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Performs actions on the tutoring model.
 * 
 * Open issues:
 * - When a orphaned hanging EKs are deleted, no care is taken to ensure no facts exists between pairs of hanging EKs.
 * It should be impossible for
 * this to occur, but it's worth noting.
 * - SUGG - Method for locating completely orphaned nodes (that is, nodes that cannot trace any link to a curriculum.
 * Nodes of this sort should not
 * occur if everything's working properly, but their presence if they can be detected is a good canary indicated a
 * problem in some of the algorithms
 * in this facade.
 * 
 * 
 * @author xorgnz
 */
@Component
public class ActivityFacade
{
    @SuppressWarnings("unused")
    private Logger      log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Neo4jUtilities      utils;


    @Autowired
    ActivityTemplateDAO dao_at;

    @Autowired
    ActivityInstanceDAO dao_ai;

    @Autowired
    ExerciseDAO         dao_ex;

    @Autowired
    CurriculumDAO       dao_curriculum;


    public void addExercise(Exercise exercise)
    {
        System.err.println(exercise);

        dao_ex.add(exercise);
    }


    public void addInstance(ActivityInstance ai)
    {
        dao_ai.add(ai);
    }


    public void addTemplate(ActivityTemplate at)
    {
        dao_at.add(at);
        if (at.getParameters().size() > 0)
            dao_at.addParameters(at);
    }


    public void deleteExercise(int id)
    {
        dao_ex.delete(id);
    }


    public void deleteInstance(int id)
    {
        dao_ai.delete(id);
    }


    public void deleteTemplate(int id)
    {
        dao_at.delete(id);
    }


    public Curriculum getCurriculum(long nodeId)
    {
        return dao_curriculum.get(nodeId);
    }


    public ActivityInstance getInstance(int id)
    {
        return dao_ai.get(id);
    }


    public ActivityTemplate getTemplate(int id)
    {
        return dao_at.get(id);
    }


    public List<Curriculum> listCurriculaUnusedByInstance(int instanceId)
    {
        // Retrieve and marshal curricula
        List<Curriculum> curricula = dao_curriculum.listAll();
        Map<Long, Curriculum> curriculumMap = new HashMap<Long, Curriculum>();
        for (Curriculum c : curricula)
            curriculumMap.put(c.getNodeId(), c);

        // Cull curricula that are already used
        List<Exercise> exercises = dao_ex.listByActivityInstance(instanceId);
        for (Exercise ex : exercises)
            curriculumMap.remove(ex.getCurriculum().getNodeId());

        // Respond
        curricula.clear();
        curricula.addAll(curriculumMap.values());
        return curricula;
    }


    public List<Exercise> listExercisesByInstance(int instanceId)
    {
        // Retrieve exercises
        List<Exercise> exercises = dao_ex.listByActivityInstance(instanceId);

        // Retrieve and marshal curricula for linking
        List<Curriculum> curricula = dao_curriculum.listAll();
        Map<Long, Curriculum> curriculumMap = new HashMap<Long, Curriculum>();
        for (Curriculum c : curricula)
            curriculumMap.put(c.getNodeId(), c);

        // Link curricula to exercises by ID
        for (Exercise ex : exercises)
        {
            ex.setCurriculum(curriculumMap.get(ex.getCurriculum().getNodeId()));
        }

        // Respond
        return exercises;
    }


    public List<ActivityInstance> listInstancesByTemplate(int templateId)
    {
        return dao_ai.listByTemplate(templateId);
    }


    public List<ActivityTemplate> listTemplates()
    {
        return dao_at.listAll();
    }


    public void updateInstance(ActivityInstance ai)
    {
        dao_ai.update(ai);
    }


    public void updateTemplate(ActivityTemplate at)
    {
        dao_at.update(at);
        dao_at.deleteParameters(at.getId());
        if (at.getParameters().size() > 0)
            dao_at.addParameters(at);
    }
}
