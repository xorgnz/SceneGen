package org.memehazard.wheel.game.quiz.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
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
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/game/quiz/facts")
public class FactsController
{
    @Autowired
    private QuizFacade facade;

    private Logger     log = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    RestResponseObject ep_scene(
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
            // Load scene
            SceneContentDescriptor scene = facade.generateSceneContentDescriptor(sceneId);
            Set<Integer> entityIds = new TreeSet<Integer>();
            for (SceneObjectDescriptor sod : scene.getSceneObjectDescriptors())
                if (sod != null && sod.getEntity() != null && !sod.isMissing())
                    entityIds.add(sod.getEntity().getId());

            // Create quiz item pool
            List<Question> questions = facade.generateQuestions(entityIds, curriculumId, studentId);

            // Extract list of fact IDs
            List<Long> factIds = new ArrayList<Long>();
            for (Question q : questions)
                factIds.add(q.getFactId());

            return new RestResponseObject(true, "Success", factIds);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            model.addAttribute("error_message", "Unable to generate quiz facts: " + e.getMessage());
            return new RestResponseObject(false, "Unable to generate quiz facts: " + e.getMessage(), null);
        }
    }


    public static class RestResponseObject extends BasicRestResponseObject
    {
        private List<Long> factIds;


        public RestResponseObject(boolean success, String message, List<Long> factIds)
        {
            super(success, message);
            this.factIds = factIds;
        }


        public List<Long> getFactIds()
        {
            return factIds;
        }
    }
}
