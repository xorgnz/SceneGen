package org.memehazard.wheel.activity.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.activity.facade.ActivityFacade;
import org.memehazard.wheel.activity.model.ActivityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/activity/instance/{id}/delete")
public class DeleteActivityInstanceController
{
    private static final String PAGE_FILE  = "activity/instance_delete";
    private static final String PAGE_TITLE = "Delete Activity Instance";

    @Autowired
    private ActivityFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        ActivityInstance instance = facade.getInstance(id);

        // Respond
        model.addAttribute("instance", instance);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable int id,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Act
        ActivityInstance instance = facade.getInstance(id);
        facade.deleteInstance(id);

        flash.addFlashAttribute("msg_good", "Successfully deleted instance " + instance.getName() + " from template " + instance.getTemplate().getId());
        flash.addFlashAttribute("refresh", "frame-side");

        // Redirect
        return "redirect:/activity/template/" + instance.getTemplate().getId();
    }
}