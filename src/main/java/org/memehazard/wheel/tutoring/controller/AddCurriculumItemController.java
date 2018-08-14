package org.memehazard.wheel.tutoring.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.core.validator.ValidFloat;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/tutoring/curriculum/{id}/addCurriculumItem")
public class AddCurriculumItemController
{
    private static final String PAGE_FILE  = "tutoring/ci_form";
    private static final String PAGE_TITLE = "Add Curriculum Item";

    @Autowired
    private TutoringFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable("id") Long id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        CommandObject co = new CommandObject();

        // Respond
        model.addAttribute("co", co);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable("id") Long id,
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult result,
            SessionStatus status,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
            throws IOException, XMLException
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
            Curriculum c = facade.getCurriculum(id);
            CurriculumItem ci = co.buildCurriculumItem();
            ci.setCurriculum(c);
            facade.addCurriculumItem(ci);

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
            status.setComplete();
            return "redirect:/tutoring/curriculumItem/" + ci.getNodeId() + "/build";
        }
    }


    public static class CommandObject
    {
        private String description = "";
        @NotEmpty(message = "{CurriculumItem.name.empty}")
        private String name        = "";
        @NotEmpty(message = "{CurriculumItem.weight.empty}")
        @ValidFloat(message = "{CurriculumItem.weight.invalid}", min = 0)
        private String weight      = "";


        public CommandObject()
        {
        }


        public CurriculumItem buildCurriculumItem()
        {
            CurriculumItem ci = new CurriculumItem();
            ci.setName(name);
            ci.setDescription(description);
            ci.setWeight(Double.parseDouble(weight));
            return ci;
        }


        public String getDescription()
        {
            return description;
        }


        public String getName()
        {
            return name;
        }


        public String getWeight()
        {
            return weight;
        }


        public void setDescription(String description)
        {
            this.description = description;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void setWeight(String weight)
        {
            this.weight = weight;
        }
    }
}