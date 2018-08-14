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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/style/sheet/{id}/edit")
public class EditStylesheetController
{
    private static final String PAGE_FILE  = "style/sheet_form";
    private static final String PAGE_TITLE = "Edit Stylesheet";

    @Autowired
    private StyleFacade         facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Stylesheet stylesheet = facade.getStylesheet(id);

        // Respond
        model.addAttribute("co", new CommandObject(id, stylesheet));
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult result,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Passed binding & validation
        if (!result.hasErrors())
        {
            // Act
            Stylesheet stylesheet = facade.getStylesheet(co.getId());
            stylesheet.setName(co.getName());
            stylesheet.setDescription(co.getDescription());
            stylesheet.setTags(co.getTags());
            facade.updateStylesheet(stylesheet);
            model.addAttribute("msg_good", "Success!!");
            model.addAttribute("refresh", "frame-side");
        }

        // Respond
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);

        return "admin/base";
    }


    public static class CommandObject
    {
        private String description;
        @Valid
        private int    id;
        @NotEmpty(message = "{Stylesheet.name.empty}")
        private String name;
        private String tags;


        public CommandObject()
        {

        }


        public CommandObject(int id, Stylesheet ssheet)
        {
            this.id = id;
            this.name = ssheet.getName();
            this.description = ssheet.getDescription();
            this.tags = ssheet.getTags();
        }


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