package org.memehazard.wheel.als.facade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.memehazard.wheel.activity.dao.ExerciseDAO;
import org.memehazard.wheel.activity.model.Exercise;
import org.memehazard.wheel.rbac.dao.UserDAO;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.tutoring.dao.BayesDAO;
import org.memehazard.wheel.tutoring.dao.CurriculumDAO;
import org.memehazard.wheel.tutoring.dao.EnrolmentDAO;
import org.memehazard.wheel.tutoring.model.BayesValue;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.memehazard.wheel.tutoring.model.Enrolment;
import org.memehazard.wheel.tutoring.utils.TutoringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class AnatomyLearningFacade
{
    @Autowired
    private BayesDAO      dao_bayes;
    @Autowired
    private CurriculumDAO dao_curriculum;
    @Autowired
    private EnrolmentDAO  dao_enrolment;
    @Autowired
    private ExerciseDAO   dao_exercise;
    @Autowired
    private UserDAO       dao_user;


    private Logger        log = LoggerFactory.getLogger(getClass());


    public Map<Exercise, Float> calculateRecommendations(List<Exercise> exercises, long studentId) throws IOException
    {
        // Create response data structure
        Map<Exercise, Float> responseMap = new HashMap<Exercise, Float>();

        // Configure JSON parser
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        for (Exercise ex : exercises)
        {
            try
            {
                // Retrieve and parse facts for exercise
                URL url = new URL(ex.prepareFactsUrl(studentId));
                FactsResponseObject fro = mapper.readValue(url, new TypeReference<FactsResponseObject>()
                {
                });

                List<BayesValue> bayesValues = dao_bayes.list(fro.getFactIds(), studentId);

                float rating = 0;
                for (BayesValue bv : bayesValues)
                    rating += 1 - bv.getP();
                rating /= bayesValues.size();

                responseMap.put(ex, rating);
                System.err.println("" + ex.getActivityInstance().getName() + " -- " + rating);
            }
            catch (URISyntaxException e)
            {
                log.warn("Unable to load facts for " + ex.getActivityInstance().getName());
                e.printStackTrace();
            }
        }

        return responseMap;
    }


    public User getCurrentStudent()
    {
        // Get current user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.isTrue(principal instanceof UserDetails, "Cannot obtain user details");

        return dao_user.getByUsername(((UserDetails) principal).getUsername());
    }


    public Map<Curriculum, Map<User, Double>> loadCurriculumStudentRatings()
    {
        // Create response data structure
        Map<Curriculum, Map<User, Double>> responseMap = new HashMap<Curriculum, Map<User, Double>>();

        List<Curriculum> curricula = dao_curriculum.listAll();

        for (Curriculum c : curricula)
        {
            // Create response data structure
            Map<User, Double> studentMap = new HashMap<User, Double>();

            // Load BayesValues for curriculum
            Map<Long, BayesValue> bvMap = TutoringUtils.mapifyBayesValuesByStudentId(dao_bayes.listForAllStudents(c.getNodeId()));

            // Populate student map
            for (Enrolment e : dao_enrolment.listByCurriculum(c.getNodeId()))
                studentMap.put(e.getStudent(), bvMap.get(e.getStudent().getId()).getP());
            
            responseMap.put(c, studentMap);
        }
        

        return responseMap;
    }


    public Map<Curriculum, List<Exercise>> loadStudentConsoleExercises(long studentId)
    {
        // Retrieve enrolments
        List<Enrolment> enrolments = dao_enrolment.listByStudent(studentId);

        // Load curricula for each enrolment
        List<Curriculum> curricula = new ArrayList<Curriculum>();
        for (Enrolment e : enrolments)
            curricula.add(dao_curriculum.get(e.getCurriculumId()));

        // Load exercises for each curriculum
        Map<Curriculum, List<Exercise>> curriculumExerciseMap = new HashMap<Curriculum, List<Exercise>>();
        for (Curriculum c : curricula)
            curriculumExerciseMap.put(c, dao_exercise.listByCurriculum(c.getNodeId()));

        return curriculumExerciseMap;
    }


    public static class FactsResponseObject
    {
        private List<Long> factIds;
        private String     message;
        private boolean    success;


        public FactsResponseObject()
        {
        }


        public List<Long> getFactIds()
        {
            return factIds;
        }


        public String getMessage()
        {
            return message;
        }


        public boolean isSuccess()
        {
            return success;
        }


        public void setFactIds(List<Long> factIds)
        {
            this.factIds = factIds;
        }


        public void setMessage(String message)
        {
            this.message = message;
        }


        public void setSuccess(boolean success)
        {
            this.success = success;
        }
    }
}
