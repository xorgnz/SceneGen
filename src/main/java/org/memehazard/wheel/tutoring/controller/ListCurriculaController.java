package org.memehazard.wheel.tutoring.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tutoring/curriculum/list")
public class ListCurriculaController
{
    private static final String PAGE_FILE  = "tutoring/curric_list";
    private static final String PAGE_TITLE = "List Curricula";

    @Autowired
    private TutoringFacade    facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String page(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        model.addAttribute("curricula", facade.listCurricula());
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }
}