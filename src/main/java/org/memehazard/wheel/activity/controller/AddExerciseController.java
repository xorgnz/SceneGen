package org.memehazard.wheel.activity.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.activity.facade.ActivityFacade;
import org.memehazard.wheel.activity.model.ActivityInstance;
import org.memehazard.wheel.activity.model.Exercise;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/activity/exercise/add")
public class AddExerciseController
{
    @Autowired
    private ActivityFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.POST)
    public String form(
            @ModelAttribute("curriculumId") long curriculumId,
            @ModelAttribute("instanceId") int instanceId,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        ActivityInstance instance = facade.getInstance(instanceId);
        Curriculum c = facade.getCurriculum(curriculumId);
        if (instance == null)
            flash.addFlashAttribute("msg_bad", "Unable to create exercise for instance with ID " + instanceId + " - does not exist");
        else if (c == null)
            flash.addFlashAttribute("msg_bad", "Unable to create exercise for curriculum with ID " + curriculumId + " - does not exist");
        else
        {
            facade.addExercise(new Exercise(instance, c));
            flash.addFlashAttribute("msg_good",
                    String.format("Created exercise from instance \"%s\" and curriculum \"%s\"", instance.getName(), c.getName()));
        }

        // Redirect
        return "redirect:" + request.getHeader("referer");
    }
}