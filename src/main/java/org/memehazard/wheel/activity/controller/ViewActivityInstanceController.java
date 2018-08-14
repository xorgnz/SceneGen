package org.memehazard.wheel.activity.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.activity.facade.ActivityFacade;
import org.memehazard.wheel.activity.model.ActivityInstance;
import org.memehazard.wheel.activity.model.Exercise;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/activity/instance/{id}")
public class ViewActivityInstanceController
{
    @Autowired
    private ActivityFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String performGet(
            @PathVariable int id,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        ActivityInstance instance = facade.getInstance(id);
        List<Exercise> exercises = facade.listExercisesByInstance(id);
        List<Curriculum> curricula = facade.listCurriculaUnusedByInstance(id);

        // Respond
        model.addAttribute("instance", instance);
        model.addAttribute("exercises", exercises);
        model.addAttribute("curricula", curricula);
        model.addAttribute("pageTitle", "View Activity Instance");
        model.addAttribute("pageFile", "activity/instance_view");
        return "admin/base";
    }
}