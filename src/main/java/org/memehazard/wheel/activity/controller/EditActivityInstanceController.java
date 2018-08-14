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
@RequestMapping("/activity/instance/{id}/edit")
public class EditActivityInstanceController
{
    private static final String PAGE_FILE  = "activity/instance_form";
    private static final String PAGE_TITLE = "Edit Activity Instance";

    @Autowired
    private ActivityFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    public String form(
            @PathVariable int id,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Retrieve template
        ActivityInstance instance = facade.getInstance(id);

        // Respond
        model.addAttribute("vo", new ViewObject(instance));
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable int id,
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult result,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Retrieve target instance
        ActivityInstance instance = facade.getInstance(id);

        // Failed binding & validation
        if (result.hasErrors())
        {
            log.trace("Validation failed - redisplaying form");

            // Respond
            model.addAttribute("vo", new ViewObject(co, instance));
            model.addAttribute("pageTitle", PAGE_TITLE);
            model.addAttribute("pageFile", PAGE_FILE);
            return "admin/base";
        }

        // Passed binding & validation
        else
        {
            log.trace("Validation succeeded - saving object");

            // Act - Apply update
            co.act(facade, instance);
            facade.updateInstance(instance);

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
            // status.setComplete(); - Eliminated. Not sure why it's needed.
            return "redirect:/activity/instance/" + instance.getId() + "/edit";
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


        public void act(ActivityFacade facade, ActivityInstance instance)
        {
            // Update fields
            instance.setName(this.name);
            instance.setDescription(this.description);

            // Validate - Param arrays of equal size
            Assert.isTrue(parameterVariable.length == parameterValue.length, "Parameter arrays of uneven length");

            // Re-create new parameter values
            Map<String, String> paramValueMap = new HashMap<String, String>();
            for (int i = 0; i < parameterVariable.length; i++)
            {
                String variable = parameterVariable[i].trim();
                String value = parameterValue[i].trim();

                paramValueMap.put(variable, value);
            }
            instance.setParameterValues(paramValueMap);
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
        public ViewObject(ActivityInstance instance)
        {
            // Populate fields
            this.templateId = instance.getTemplate().getId();
            this.name = instance.getName();
            this.description = instance.getDescription();
            this.paramValues = instance.getParameterValues();
        }


        public ViewObject(CommandObject co, ActivityInstance instance)
        {
            // Copy fields
            this.description = co.getDescription();
            this.name = co.getName();
            this.templateId = instance.getTemplate().getId();

            // Prepare parameters and value fields for conversion
            String[] variables = co.getParameterVariable();
            String[] values = co.getParameterValue();

            // Validate - Parameter value arrays of same size
            Assert.isTrue(variables.length == values.length, "Parameter arrays of uneven length");

            // Create map of values.
            Map<String, String> valueMap = new HashMap<String, String>();
            for (int i = 0; i < variables.length; i++)
                valueMap.put(variables[i], values[i]);

            // Create Parameter values from parameters and value map
            for (Parameter atp : instance.getTemplate().getParameters())
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