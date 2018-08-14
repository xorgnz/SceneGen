package org.memehazard.wheel.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.core.facade.AdminFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/admin/initialize-db")
public class InitializeDBController
{
    private static final String PAGE_FILE  = "admin/confirm";
    private static final String PAGE_TITLE = "Initialize Database";

    @Autowired
    private AdminFacade facade;
    
    private Logger              log        = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Respond
        model.addAttribute("message", "This will <b>add extensive content</b> to the database. Are you sure?</b>");
        model.addAttribute("url", "admin/initialize-db");
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String home(
            @ModelAttribute("confirm") String confirm, 
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        log.trace("Creating base DB objects");
        facade.initializeDB();
        
        // Redirect
        model.addAttribute("msg_good", "Successfully created new objects!!");
        model.addAttribute("refresh", "frame-side");
        model.addAttribute("pageFile", "admin/blank-inner");        
        return "admin/base";
    }
}
