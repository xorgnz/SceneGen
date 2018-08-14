package org.memehazard.wheel.style.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.memehazard.wheel.style.facade.StyleFacade;
import org.memehazard.wheel.style.model.Stylesheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/style/sheet/add")
public class AddStylesheetController
{
    private static final String PAGE_FILE  = "style/sheet_form";
    private static final String PAGE_TITLE = "Add Stylesheet";

    @Autowired
    private StyleFacade         facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    public String form(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Respond
        model.addAttribute("co", new CommandObject());
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult result,
            SessionStatus status,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Failed binding & validation
        if (result.hasErrors())
        {
            log.trace("Validation failed - redisplaying form");

            // Respond
            model.addAttribute("pageTitle", PAGE_TITLE);
            model.addAttribute("pageFile", PAGE_FILE);
            return "admin/base";
        }

        // Passed binding & validation
        else
        {
            log.trace("Validation succeeded - saving object");

            // Act
            Stylesheet stylesheet = new Stylesheet(co.getName(), co.getDescription(), co.getTags());
            facade.addStylesheet(stylesheet);

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
            status.setComplete();
            return "redirect:/style/sheet/" + stylesheet.getId() + "/edit";
        }
    }


    public static class CommandObject
    {
        private String description;
        @Valid
        private int    id;
        @NotEmpty(message = "{Stylesheet.name.empty}")
        private String name;
        private String tags;


        public String getDescription()
        {
            return description;
        }


        public int getId()
        {
            return id;
        }


        public String getName()
        {
            return name;
        }


        public String getTags()
        {
            return tags;
        }


        public void setDescription(String description)
        {
            this.description = description;
        }


        public void setId(int id)
        {
            this.id = id;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void setTags(String tags)
        {
            this.tags = tags;
        }
    }
}