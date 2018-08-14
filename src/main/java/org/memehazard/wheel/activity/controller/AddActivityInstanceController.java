package org.memehazard.wheel.activity.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.memehazard.wheel.activity.facade.ActivityFacade;
import org.memehazard.wheel.activity.model.ActivityInstance;
import org.memehazard.wheel.activity.model.ActivityTemplate;
import org.memehazard.wheel.activity.model.Parameter;
import org.memehazard.wheel.activity.model.ParameterValue;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/activity/instance/add/{templateId}")
public class AddActivityInstanceController
{
    private static final String PAGE_FILE  = "activity/instance_form";
    private static final String PAGE_TITLE = "Add Activity Instance";

    @Autowired
    private ActivityFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    public String form(
            @PathVariable int templateId,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Retrieve template
        ActivityTemplate template = facade.getTemplate(templateId);

        // Respond
        model.addAttribute("vo", new ViewObject(template));
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
            ActivityInstance ai = co.act(facade);
            facade.addInstance(ai);

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
            return "redirect:/activity/template/" + ai.getTemplate().getId();
        }
    }


    public static class CommandObject
    {
        private String   description;
        @NotEmpty(message = "{ActivityInstance.name.empty}")
        private String   name;
        private String[] parameterValue;
        private String[] parameterVariable;
        private int      templateId;


        public CommandObject()
        {
        }


        public ActivityInstance act(ActivityFacade facade)
        {
            // Retrieve template
            ActivityTemplate template = facade.getTemplate(templateId);

            // Update query
            ActivityInstance instance = new ActivityInstance(name, description, template, "");

            // Validate - Param arrays of equal size
            Assert.isTrue(parameterVariable.length == parameterValue.length, "Parameter arrays of uneven length");

            // Create new query parameters
            Map<String, String> paramValueMap = new HashMap<String, String>();
            for (int i = 0; i < parameterVariable.length; i++)
            {
                String variable = parameterVariable[i].trim();
                String value = parameterValue[i].trim();

                paramValueMap.put(variable, value);
            }
            instance.setParameterValues(paramValueMap);

            return instance;
        }


        public String getDescription()
        {
            return description;
        }


        public String getName()
        {
            return name;
        }


        public String[] getParameterValue()
        {
            return parameterValue;
        }


        public String[] getParameterVariable()
        {
            return parameterVariable;
        }


        public int getTemplateId()
        {
            return templateId;
        }


        public void setDescription(String description)
        {
            this.description = description;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void setParameterValue(String[] parameterValue)
        {
            this.parameterValue = parameterValue;
        }


        public void setParameterVariable(String[] parameterVariable)
        {
            this.parameterVariable = parameterVariable;
        }


        public void setTemplateId(int templateId)
        {
            this.templateId = templateId;
        }
    }


    public static class ViewObject
    {
        private String               description;
        private String               name;
        private List<ParameterValue> paramValues = new ArrayList<ParameterValue>();
        private int                  templateId;


        /**
         * Create blank view from template instance is to be added to
         * 
         * @param template
         */
        public ViewObject(ActivityTemplate template)
        {
            // Save template ID
            this.templateId = template.getId();

            // Create blank parameter values
            for (Parameter atp : template.getParameters())
                paramValues.add(new ParameterValue(atp.getVariable(), atp.getLabel(), atp.getType(), ""));
        }


        public ViewObject(CommandObject co, ActivityTemplate template)
        {
            // Copy fields
            this.description = co.getDescription();
            this.name = co.getName();
            this.templateId = co.getTemplateId();

            // Prepare parameters and value fields for conversion
            String[] variables = co.getParameterVariable();
            String[] values = co.getParameterValue();

            // Validate - Parameter value arrays of same size
            Assert.isTrue(variables.length == values.length, "Parameter arrays of uneven length");

            // Create map of values.
            Map<String, String> valueMap = new HashMap<String, String>();
            for (int i = 0; i < variables.length; i++)
                valueMap.put(variables[i], values[i]);

            // Create parameter values from template parameters and value map from command object
            for (Parameter atp : template.getParameters())
                paramValues.add(new ParameterValue(atp.getVariable(), atp.getLabel(), atp.getType(), valueMap.get(atp.getVariable())));
        }


        public String getDescription()
        {
            return description;
        }


        public String getName()
        {
            return name;
        }


        public List<ParameterValue> getParamValues()
        {
            return paramValues;
        }


        public int getTemplateId()
        {
            return templateId;
        }
    }
}