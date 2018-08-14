package org.memehazard.wheel.activity.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.activity.facade.ActivityFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/activity/exercise/{id}/delete")
public class DeleteExerciseController
{
    @Autowired
    private ActivityFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public String form(@PathVariable int id, RedirectAttributes flash, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        facade.deleteExercise(id);

        flash.addFlashAttribute("msg_good", "Deleted exercise");

        // Redirect
        return "redirect:" + request.getHeader("referer");
    }

}