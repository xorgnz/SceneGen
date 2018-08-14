package org.memehazard.wheel.style.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.style.facade.StyleFacade;
import org.memehazard.wheel.style.model.Stylesheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ListStylesheetsController
{
    @Autowired
    private StyleFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @Transactional(readOnly = true)
    @RequestMapping("/style/sheet/list")
    public String listSets(
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        List<Stylesheet> objs = facade.listStylesheets();

        // Respond
        model.addAttribute("objs", objs);
        model.addAttribute("pageTitle", "List Stylesheets");
        model.addAttribute("pageFile", "style/sheet_list");
        return "admin/base";
    }
}
