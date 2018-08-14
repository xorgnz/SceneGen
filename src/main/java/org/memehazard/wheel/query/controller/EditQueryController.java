package org.memehazard.wheel.query.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@RequestMapping("/query/query/{id}/edit")
@SessionAttributes("query")
public class EditQueryController
{
    private static final String PAGE_FILE  = "query/query_form";
    private static final String PAGE_TITLE = "Edit Query";

    @Autowired
    private QueryFacade         facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Query q = facade.getQuery(id);

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
            Query q = facade.getQuery(co.getId());
            co.act(q);
            facade.updateQuery(q);

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
        private String           description;
        private int              id;
        @NotEmpty(message = "{Query.name.empty}")
        private String           name;
        private QueryParameter[] parameters            = null;                  // Pre-existing parameters (get only)
        private Set<String>     param_flag_entityId   = new HashSet<String>(); // Parameter values - array of param variables with entity ID flag
        private Set<String>     param_flag_entityName = new HashSet<String>(); // Parameter values - array of param variables with entity Name flag
        private String[]         param_label           = new String[] {};       // Parameter values - array of param labels
        private String[]         param_variable        = new String[] {};       // Parameter values - array of param variables
        @NotNull(message = "{Query.queryId.null}")
        private Integer          queryId;
        private String           tags;


        public CommandObject()
        {

        }


        public CommandObject(Query q)
        {
            this.id = q.getId();
            this.name = q.getName();
            this.queryId = q.getQueryId();
            this.description = q.getDescription();
            this.tags = q.getTags();

            this.parameters = q.getParameters().toArray(new QueryParameter[] {});
        }


        public void act(Query q)
        {
            // Update query
            q.setName(name);
            q.setQueryId(queryId);
            q.setTags(tags);
            q.setDescription(description);

            // Create map of existing query parameters
            Map<String, QueryParameter> oldParamMap = new HashMap<String, QueryParameter>();
            for (QueryParameter qp : q.getParameters())
                oldParamMap.put(qp.getVariable(), qp);

            // Delete old queries
            q.clearParameters();

            // Create new query parameters
            for (int i = 0; i < param_label.length; i++)
            {
                String variable = param_variable[i].trim();
                String label = param_label[i].trim();

                // Use existing parameter if available
                QueryParameter qp = oldParamMap.get(variable);
                if (qp == null)
                    qp = new QueryParameter();

                // Set parameter values
                qp.setVariable(variable);
                qp.setLabel(label);
                qp.setFlag_entityId(param_flag_entityId.contains(variable));
                qp.setFlag_entityName(param_flag_entityName.contains(variable));

                // Save parameter if variable and label are set
                if (!variable.equals("") && !label.equals(""))
                    q.addParameter(qp);
            }
            
            this.parameters = q.getParameters().toArray(new QueryParameter[] {});
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


        public QueryParameter[] getParameters()
        {
            return parameters;
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


        public void setId(int id)
        {
            this.id = id;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void setParam_flag_entityId(String[] param_flag_entityId)
        {
            for (String i : param_flag_entityId)
                this.param_flag_entityId.add(i);
        }


        public void setParam_flag_entityName(String[] param_flag_entityName)
        {
            for (String i : param_flag_entityName)
                this.param_flag_entityName.add(i);

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