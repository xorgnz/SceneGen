package org.memehazard.wheel.als.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.activity.model.Exercise;
import org.memehazard.wheel.als.facade.AnatomyLearningFacade;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/als/student")
public class StudentConsoleController
{
    private static final String   PAGE_FILE_QUIZ = "als/student_console";

    @Autowired
    private AnatomyLearningFacade facade;

    private Logger                log            = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public String ep_scene(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ParserException
    {
        log.trace("Generating page - " + request.getServletPath());

        System.err.println(request.getRequestURL());

        // Load student
        User student = facade.getCurrentStudent();

        // Load exercises based on student enrolment
        Map<Curriculum, List<Exercise>> curriculumExerciseMap;
        if (student != null)
            curriculumExerciseMap = facade.loadStudentConsoleExercises(student.getId());
        else
            curriculumExerciseMap = new HashMap<Curriculum, List<Exercise>>();

        // Calculate recommendations
        List<Exercise> exercises = new ArrayList<Exercise>();
        for (Curriculum c : curriculumExerciseMap.keySet())
            exercises.addAll(curriculumExerciseMap.get(c));
        Map<Exercise, Float> ratingsMap = facade.calculateRecommendations(exercises, student.getId());

        // Create view objects
        List<CurriculumDescriptor> ccds = new ArrayList<CurriculumDescriptor>();
        for (Curriculum c : curriculumExerciseMap.keySet())
            ccds.add(new CurriculumDescriptor(c, curriculumExerciseMap.get(c), student, ratingsMap, request.getRequestURL().toString()));

        // Respond
        model.addAttribute("student", student);
        model.addAttribute("ccds", ccds);
        return PAGE_FILE_QUIZ;
    }


    public static class CurriculumDescriptor
    {
        private Curriculum               curriculum;
        private List<ExerciseDescriptor> exercises = new ArrayList<ExerciseDescriptor>();


        public CurriculumDescriptor(Curriculum curriculum, List<Exercise> exercises, User student, Map<Exercise, Float> ratingsMap, String redirectUrl)
        {
            this.curriculum = curriculum;

            for (Exercise e : exercises)
                this.exercises.add(new ExerciseDescriptor(e, student.getId(), ratingsMap.get(e), redirectUrl));
        }


        public Curriculum getCurriculum()
        {
            return curriculum;
        }


        public List<ExerciseDescriptor> getExercises()
        {
            return exercises;
        }
    }


    public static class ExerciseDescriptor
    {
        private String factsUrl;
        private String name;
        private String playUrl;
        private String priority;
        private float  rating;


        public ExerciseDescriptor(Exercise exercise, long studentId, float rating, String redirectUrl)
        {
            try
            {
                this.name = exercise.getActivityInstance().getName();
                this.factsUrl = exercise.prepareFactsUrl(studentId);
                this.playUrl = exercise.preparePlayUrl(studentId, redirectUrl);
                this.rating = rating;
                if (rating < 0.2)
                    priority = "low";
                else if (rating < 0.6)
                    priority = "medium";
                else
                    priority = "high";
            }
            catch (URISyntaxException e)
            {
                e.printStackTrace();
            }
        }


        public String getFactsUrl()
        {
            return factsUrl;
        }


        public String getName()
        {
            return name;
        }


        public String getPlayUrl()
        {
            return playUrl;
        }


        public String getPriority()
        {
            return priority;
        }


        public float getRating()
        {
            return rating;
        }
    }
}
