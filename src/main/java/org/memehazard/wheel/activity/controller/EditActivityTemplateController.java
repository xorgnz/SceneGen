package org.memehazard.wheel.activity.controller;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@RequestMapping("/activity/template/{id}/edit")
@SessionAttributes("query")
public class EditActivityTemplateController
{
    private static final String PAGE_FILE  = "activity/template_form";
    private static final String PAGE_TITLE = "Edit Activity Template";

    @Autowired
    private ActivityFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        ActivityTemplate q = facade.getTemplate(id);

        // Respond
        model.addAttribute("co", new CommandObject(q));
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
            ActivityTemplate at = facade.getTemplate(co.getId());
            co.act(at);
            facade.updateTemplate(at);

            // Respond
            model.addAttribute("msg_good", "Success!!");
            model.addAttribute("refresh", "frame-side");
        }

        // Respond
        model.addAttribute("co", co);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);

        return "admin/base";
    }


    public static class CommandObject
    {
        private String                      description;
        private String                      factsUrl;
        private int                         id;
        @NotEmpty(message = "{Query.name.empty}")
        private String                      name;
        private String[]                    param_label    = new String[] {}; // Parameter values - array of param labels
        private String[]                    param_type     = new String[] {}; // Parameter values - array of param variables
        private String[]                    param_variable = new String[] {}; // Parameter values - array of param variables
        private Parameter[] parameters     = null;           // Pre-existing parameters (get only)
        private String                      playUrl;


        public CommandObject()
        {
        }


        public CommandObject(ActivityTemplate at)
        {
            this.id = at.getId();
            this.name = at.getName();
            this.description = at.getDescription();
            this.factsUrl = at.getFactsUrl();
            this.playUrl = at.getPlayUrl();
            this.parameters = at.getParameters().toArray(new Parameter[] {});
        }


        public void act(ActivityTemplate at)
        {
            // Update query
            at.setName(name);
            at.setDescription(description);
            at.setPlayUrl(playUrl);
            at.setFactsUrl(factsUrl);

            // Create map of existing query parameters
            Map<String, Parameter> oldParamMap = new HashMap<String, Parameter>();
            for (Parameter atp : at.getParameters())
                oldParamMap.put(atp.getVariable(), atp);

            // Delete old queries
            at.clearParameters();

            // Validate - Param arrays of equal size
            Assert.isTrue(param_label.length == param_variable.length && param_label.length == param_type.length,
                    "Parameter arrays of uneven length");

            // Create new query parameters
            for (int i = 0; i < param_label.length; i++)
            {
                String variable = param_variable[i].trim();
                String label = param_label[i].trim();
                String type = param_type[i].trim();

                // Use existing parameter if available
                at.addParameter(new Parameter(label, variable, type));
            }

            this.parameters = at.getParameters().toArray(new Parameter[] {});
        }


        public String getDescription()
        {
            return description;
        }


        public String getFactsUrl()
        {
            return factsUrl;
        }


        public int getId()
        {
            return id;
        }


        public String getName()
        {
            return name;
        }


        public Parameter[] getParameters()
        {
            return parameters;
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


        public void setId(int id)
        {
            this.id = id;
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