package org.memehazard.wheel.style.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.style.facade.StyleFacade;
import org.memehazard.wheel.style.model.Stylesheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/style/sheet/{sheetId}/delete")
public class DeleteStylesheetController
{
    private static final String PAGE_FILE  = "style/sheet_delete";
    private static final String PAGE_TITLE = "Delete Stylesheet";

    @Autowired
    private StyleFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int sheetId, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Stylesheet obj = facade.getStylesheet(sheetId);

        // Respond
        model.addAttribute("obj", obj);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable int sheetId,
            SessionStatus status,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Act
        Stylesheet obj = facade.getStylesheet(sheetId);
        facade.deleteStylesheet(sheetId);

        // Redirect
        flash.addFlashAttribute("msg_good", "Successfully deleted " + obj.getName());
        flash.addFlashAttribute("refresh", "frame-side");
        status.setComplete();
        return "redirect:/admin/blank-main";
    }
}