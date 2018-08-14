package org.memehazard.wheel.activity.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.activity.facade.ActivityFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/activity/template/list")
public class ListActivityTemplateController
{
    private static final String PAGE_FILE  = "activity/template_list";
    private static final String PAGE_TITLE = "List Activity Templates";

    @Autowired
    private ActivityFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String page(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        model.addAttribute("templates", facade.listTemplates());
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }
}