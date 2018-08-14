package org.memehazard.wheel.query.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.memehazard.wheel.query.facade.QueryFacade;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryParameter;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/query/query/add")
public class AddQueryController
{
    private static final String PAGE_FILE  = "query/query_form";
    private static final String PAGE_TITLE = "Add Query";

    @Autowired
    private QueryFacade         facade;
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
            // SessionStatus status,
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
            Query q = co.act();
            facade.addQuery(q);

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
            // status.setComplete(); - Eliminated. Not sure why it's needed.
            return "redirect:/query/query/" + q.getId() + "/edit";
        }
    }


    public static class CommandObject
    {
        private String       description;
        @NotEmpty(message = "{Query.name.empty}")
        private String       name;
        private Set<String> param_flag_entityId   = new HashSet<String>(); // Parameter values - array of param indexes with entity ID flag
        private Set<String> param_flag_entityName = new HashSet<String>(); // Parameter values - array of param indexes with entity Name flag
        private String[]     param_label           = new String[] {};       // Parameter values - array of param labels
        private String[]     param_variable        = new String[] {};       // Parameter values - array of param variables
        @NotNull(message = "{Query.queryId.null}")
        private Integer      queryId;
        private String       tags;


        public Query act()
        {
            // Update query
            Query q = new Query(name, description, queryId, tags);

            // Create new query parameters
            for (int i = 0; i < param_label.length; i++)
            {
                String variable = param_variable[i].trim();
                String label = param_label[i].trim();

                // Use existing parameter if available
                QueryParameter qp = new QueryParameter();

                // Set parameter values
                qp.setVariable(variable);
                qp.setLabel(label);
                
                System.err.println(variable);
                System.err.println(param_flag_entityId.contains(variable));
                System.err.println(param_flag_entityId);
                
                qp.setFlag_entityId(param_flag_entityId.contains(variable));
                qp.setFlag_entityName(param_flag_entityName.contains(variable));

                // Save parameter if variable and label are set
                if (!variable.equals("") && !label.equals(""))
                    q.addParameter(qp);
            }

            return q;
        }


        public String getDescription()
        {
            return description;
        }


        public String getName()
        {
            return name;
        }


        public QueryParameter[] getParameters()
        {
            return new QueryParameter[] {};
        }


        public Integer getQueryId()
        {
            return queryId;
        }


        public String getTags()
        {
            return tags;
        }


        public void setDescription(String description)
        {
            this.description = description;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void setParam_flag_entityId(String[] param_flag_entityId)
        {
            for (String s : param_flag_entityId)
                this.param_flag_entityId.add(s);
        }


        public void setParam_flag_entityName(String[] param_flag_entityName)
        {
            for (String s : param_flag_entityName)
                this.param_flag_entityName.add(s);
        }


        public void setParam_label(String[] param_label)
        {
            this.param_label = param_label;
        }


        public void setParam_variable(String[] param_variable)
        {
            this.param_variable = param_variable;
        }


        public void setQueryId(Integer queryId)
        {
            this.queryId = queryId;
        }


        public void setTags(String tags)
        {
            this.tags = tags;
        }
    }
}