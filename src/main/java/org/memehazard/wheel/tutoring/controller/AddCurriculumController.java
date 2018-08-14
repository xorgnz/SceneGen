package org.memehazard.wheel.tutoring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tutoring/curriculum/add")
public class AddCurriculumController
{
    private static final String        PAGE_FILE  = "tutoring/curric_form";
    private static final String        PAGE_TITLE = "Add Curriculum";

    @Autowired
    private TutoringFacade             facade;
    private Logger                     log        = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PlatformTransactionManager ptm;

    @Autowired
    private Neo4jUtilities             utils;


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
            Curriculum c = co.buildCurriculum();
            facade.addCurriculum(c);

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
            return "redirect:/tutoring/curriculum/" + c.getNodeId() + "/edit";
        }
    }


    public static class CommandObject
    {
        private String creatorName = "";
        private String description = "";
        @NotEmpty(message = "{Curriculum.name.empty}")
        private String name        = "";


        public CommandObject()
        {
        }


        public Curriculum buildCurriculum()
        {
            Curriculum c = new Curriculum();
            c.setName(name);
            c.setCreatorName(creatorName);
            c.setDescription(description);
            return c;
        }


        public String getCreatorName()
        {
            return creatorName;
        }


        public String getDescription()
        {
            return description;
        }


        public String getName()
        {
            return name;
        }


        public void setCreatorName(String creatorName)
        {
            this.creatorName = creatorName;
        }


        public void setDescription(String description)
        {
            this.description = description;
        }


        public void setName(String name)
        {
            this.name = name;
        }
    }
}