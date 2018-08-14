package org.memehazard.wheel.als.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@RequestMapping(value = "/als/educator")
public class EducatorConsoleController
{
    private static final String   PAGE_FILE_QUIZ = "als/educator_console";

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

        // Load curricula along with all students and their current ratings
        Map<Curriculum, Map<User, Double>> csrMap = facade.loadCurriculumStudentRatings();

        // Create view objects
        List<CurriculumDescriptor> ccds = new ArrayList<CurriculumDescriptor>();
        for (Curriculum c : csrMap.keySet())
            ccds.add(new CurriculumDescriptor(c, csrMap.get(c)));


        // Respond
        model.addAttribute("ccds", ccds);
        return PAGE_FILE_QUIZ;
    }


    public static class CurriculumDescriptor
    {
        private long                    id;
        private String                  name;
        private List<StudentDescriptor> students = new ArrayList<StudentDescriptor>();


        public CurriculumDescriptor(Curriculum curriculum, Map<User, Double> studentRatings)
        {
            this.id = curriculum.getNodeId();
            this.name = curriculum.getName();

            for (User student : studentRatings.keySet())
                this.students.add(new StudentDescriptor(student, studentRatings.get(student)));
        }


        public long getId()
        {
            return id;
        }


        public String getName()
        {
            return name;
        }


        public List<StudentDescriptor> getStudents()
        {
            return students;
        }
    }


    public static class StudentDescriptor
    {
        private String achievement;
        private long   id;
        private String name;
        private double  rating;


        public StudentDescriptor(User student, double rating)
        {
            super();
            this.name = student.getFullName();
            this.id = student.getId();
            this.rating = rating;
            if (rating < 0.2)
                achievement = "low";
            else if (rating < 0.6)
                achievement = "medium";
            else
                achievement = "high";
        }


        public String getAchievement()
        {
            return achievement;
        }


        public long getId()
        {
            return id;
        }


        public String getName()
        {
            return name;
        }


        public double getRating()
        {
            return rating;
        }
    }
}
