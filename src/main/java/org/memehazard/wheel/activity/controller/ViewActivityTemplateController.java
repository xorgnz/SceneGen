package org.memehazard.wheel.activity.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.activity.facade.ActivityFacade;
import org.memehazard.wheel.activity.model.ActivityInstance;
import org.memehazard.wheel.activity.model.ActivityTemplate;
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
@RequestMapping("/activity/template/{id}")
public class ViewActivityTemplateController
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
        ActivityTemplate at = facade.getTemplate(id);
        List<ActivityInstance> instances = facade.listInstancesByTemplate(id);

        // Respond
        model.addAttribute("template", at);
        model.addAttribute("instances", instances);
        model.addAttribute("pageTitle", "View Activity Template");
        model.addAttribute("pageFile", "activity/template_view");
        return "admin/base";
    }
}