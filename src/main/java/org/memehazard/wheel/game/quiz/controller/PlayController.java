package org.memehazard.wheel.game.quiz.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.game.quiz.facade.QuizFacade;
import org.memehazard.wheel.game.quiz.model.Question;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.viewer.view.SceneContentDescriptor;
import org.memehazard.wheel.viewer.view.SceneObjectDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/game/quiz")
public class PlayController
{
    private static final String PAGE_FILE_QUIZ = "game/quiz/play";

    @Autowired
    private QuizFacade          facade;

    private Logger              log            = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public String ep_scene(
            @ModelAttribute("curriculumId") long curriculumId,
            @ModelAttribute("studentId") int studentId,
            @ModelAttribute("sceneId") int sceneId,
            @ModelAttribute("questionCount") int questionCount,
            @ModelAttribute("redirectUrl") String redirectUrl,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ParserException
    {
        log.trace("Loading game- " + request.getServletPath());

        try
        {
            SceneContentDescriptor scene = facade.generateSceneContentDescriptor(sceneId);
            Set<Integer> entityIds = new TreeSet<Integer>();
            for (SceneObjectDescriptor sod : scene.getSceneObjectDescriptors())
                if (sod != null && sod.getEntity() != null && !sod.isMissing())
                    entityIds.add(sod.getEntity().getId());

            QuizDescriptor quiz = new QuizDescriptor("Quiz - " + scene.getName(), questionCount, studentId, redirectUrl);
            quiz.setQuestions(facade.generateQuestions(entityIds, curriculumId, studentId));

            // Respond
            model.addAttribute("scene", scene);
            model.addAttribute("quiz", quiz);
            return PAGE_FILE_QUIZ;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            model.addAttribute("error_message", "Unable to start quiz: " + e.getMessage());
            return PAGE_FILE_QUIZ;
        }
    }


    public static class QuizDescriptor
    {
        private int            length;
        private String         name;
        private List<Question> questions;
        private String         redirectUrl;
        private int            studentId;


        public QuizDescriptor(String name, int length, int studentId, String redirectUrl)
        {
            this.name = name;
            this.length = length;
            this.studentId = studentId;
            this.redirectUrl = redirectUrl;
        }


        public int getLength()
        {
            return length;
        }


        public String getName()
        {
            return name;
        }


        public List<Question> getQuestions()
        {
            return questions;
        }


        public String getRedirectUrl()
        {
            return redirectUrl;
        }


        public int getStudentId()
        {
            return studentId;
        }


        public void setQuestions(List<Question> questions)
        {
            this.questions = questions;
        }
    }
}
