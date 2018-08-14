package org.memehazard.wheel.activity.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.memehazard.wheel.activity.facade.ActivityFacade;
import org.memehazard.wheel.activity.model.ActivityTemplate;
import org.memehazard.wheel.activity.model.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/activity/template/add")
public class AddActivityTemplateController
{
    private static final String PAGE_FILE  = "activity/template_form";
    private static final String PAGE_TITLE = "Add Activity Template";

    @Autowired
    private ActivityFacade      facade;
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
            model.addAttribute(co);
            model.addAttribute("pageTitle", PAGE_TITLE);
            model.addAttribute("pageFile", PAGE_FILE);
            return "admin/base";
        }

        // Passed binding & validation
        else
        {
            log.trace("Validation succeeded - saving object");

            // Act
            ActivityTemplate at = co.act();
            facade.addTemplate(at);

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
            return "redirect:/activity/template/" + at.getId() + "/edit";
        }
    }


    public static class CommandObject
    {
        private String   description;
        @NotEmpty(message = "{ActivityTemplate.factsUrl.empty}")
        private String   factsUrl;
        @NotEmpty(message = "{ActivityTemplate.name.empty}")
        private String   name;
        private String[] param_label    = new String[] {}; // Parameter values - array of param labels
        private String[] param_type     = new String[] {}; // Parameter values - array of param labels
        private String[] param_variable = new String[] {}; // Parameter values - array of param variables
        @NotEmpty(message = "{ActivityTemplate.playUrl.empty}")
        private String   playUrl;


        public ActivityTemplate act()
        {
            // Update query
            ActivityTemplate at = new ActivityTemplate(name, description, playUrl, factsUrl);

            // Validate - Param arrays of equal size
            Assert.isTrue(param_label.length == param_variable.length && param_label.length == param_type.length,
                    "Parameter arrays of uneven length");

            // Create new query parameters
            for (int i = 0; i < param_label.length; i++)
            {
                String variable = param_variable[i].trim();
                String label = param_label[i].trim();
                String type = param_type[i].trim();

                at.addParameter(new Parameter(label, variable, type));
            }

            return at;
        }


        public String getDescription()
        {
            return description;
        }


        public String getFactsUrl()
        {
            return factsUrl;
        }


        public String getName()
        {
            return name;
        }


        public Parameter[] getParameters()
        {
            return new Parameter[] {};
        }


        public String getPlayUrl()
        {
            return playUrl;
        }


        public void setDescription(String description)
        {
            this.description = description;
        }


        public void setFactsUrl(String factsUrl)
        {
            this.factsUrl = factsUrl;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void setParam_label(String[] param_label)
        {
            this.param_label = param_label;
        }


        public void setParam_type(String[] param_type)
        {
            this.param_type = param_type;
        }


        public void setParam_variable(String[] param_variable)
        {
            this.param_variable = param_variable;
        }


        public void setPlayUrl(String playUrl)
        {
            this.playUrl = playUrl;
        }
    }
}