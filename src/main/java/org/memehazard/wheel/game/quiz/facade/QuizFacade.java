/**
 *
 */
package org.memehazard.wheel.game.quiz.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.memehazard.wheel.game.quiz.model.Question;
import org.memehazard.wheel.rbac.dao.UserDAO;
import org.memehazard.wheel.scene.dao.SceneDAO;
import org.memehazard.wheel.scene.dao.SceneFragmentDAO;
import org.memehazard.wheel.scene.dao.SceneFragmentMemberDAO;
import org.memehazard.wheel.scene.model.Scene;
import org.memehazard.wheel.scene.model.SceneFragment;
import org.memehazard.wheel.scene.model.SceneFragmentMember;
import org.memehazard.wheel.scene.view.SceneFragmentMemberDescriptor;
import org.memehazard.wheel.tutoring.dao.CurriculumDAO;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.BayesValue;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.memehazard.wheel.tutoring.model.LabelFact;
import org.memehazard.wheel.tutoring.utils.TutoringUtils;
import org.memehazard.wheel.viewer.view.SceneContentDescriptor;
import org.memehazard.wheel.viewer.view.ViewpointDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuizFacade
{
    @Autowired
    private SceneDAO               dao_scn;
    @Autowired
    private SceneFragmentDAO       dao_scnf;
    @Autowired
    private SceneFragmentMemberDAO dao_scnfm;
    @Autowired
    private CurriculumDAO          dao_curriculum;
    @Autowired
    private UserDAO                dao_user;
    @Autowired
    private TutoringFacade         facade_tutoring;

    @SuppressWarnings("unused")
    private Logger                 log = LoggerFactory.getLogger(getClass());


    /**
     * Generate a <code>SceneContentDescriptor</code> from a given <code>Scene</code>.
     * 
     * @param sceneId ID of scene to retrieve
     * @return Generated <code>SceneContentDescriptor</code>
     */
    public SceneContentDescriptor generateSceneContentDescriptor(int sceneId)
    {
        // Load scene, fragments, and members
        Scene scene = dao_scn.get(sceneId);
        scene.setFragments(dao_scnf.listBySceneWithAssociations(sceneId));
        for (SceneFragment scnf : scene.getFragments())
            scnf.setMembers(dao_scnfm.listByFragment(scnf.getId()));

        // Create descriptor
        SceneContentDescriptor scd = new SceneContentDescriptor(scene.getName());
        if (scene.getViewpoint() != null)
            scd.setViewpoint(new ViewpointDescriptor(scene.getViewpoint()));

        // Extract SceneObjects from SceneFragments
        for (SceneFragment scnf : scene.getFragments())
            for (SceneFragmentMember scnfm : scnf.getMembers())
                scd.addSceneObjectDescriptor(new SceneFragmentMemberDescriptor(scnfm));

        return scd;
    }


    public List<Question> generateQuestions(Set<Integer> availableEntityIds, long curriculumId, int studentId)
    {
        List<Question> questions = new ArrayList<Question>();

        // Retrieve label facts and associated bayes values
        Curriculum c = facade_tutoring.buildFullDomainModel(curriculumId);
        List<LabelFact> labelFacts = c.getAllLabelFacts();

        Map<Long, BayesValue> bayesValueMap = TutoringUtils.mapifyBayesValuesByDomainId(facade_tutoring.listBayesValues(labelFacts, studentId));

        // Create questions
        for (LabelFact lf : labelFacts)
        {
            int fmaid = lf.getEntityKnowledge().getFmaId();
            String name = lf.getEntityKnowledge().getFmaLabel();
            BayesValue bv = bayesValueMap.get(lf.getNodeId());
            if (availableEntityIds.contains(fmaid))
                questions.add(new Question(fmaid, name, 1 - bv.getP(), lf.getNodeId()));
        }

        return questions;
    }
}