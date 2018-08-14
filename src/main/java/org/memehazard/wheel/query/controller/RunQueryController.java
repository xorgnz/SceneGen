package org.memehazard.wheel.query.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.memehazard.wheel.query.facade.QueryFacade;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryParameter;
import org.memehazard.wheel.query.view.QueryDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.WebUtils;


@Controller
@RequestMapping("/query/query/{id}/run")
@SessionAttributes("query")
public class RunQueryController
{
    private static final String PAGE_FILE_FORM     = "query/query_run";
    private static final String PAGE_FILE_RESPONSE = "query/query_response";

    @Autowired
    private QueryFacade  facade;
    private Logger              log                = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        QueryDescriptor obj = new QueryDescriptor(facade.getQuery(id));

        // Respond
        model.addAttribute("query", obj);
        model.addAttribute("pageTitle", "Run Query: " + obj.getName());
        model.addAttribute("pageFile", PAGE_FILE_FORM);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable int id,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        Query q = facade.getQuery(id);

        // Interpret parameter string
        List<QueryParameter> parameters = q.getParameters();
        Map<String, Object> paramValueObjectMap = WebUtils.getParametersStartingWith(request, "qparam_");
        Map<String, String> paramValueStringMap = new HashMap<String, String>();
        Map<String, String> paramValueLabeledMap = new HashMap<String, String>();
        for (QueryParameter qp : parameters)
        {
            paramValueStringMap.put(qp.getVariable(), paramValueObjectMap.get(qp.getVariable()).toString());
            paramValueLabeledMap.put(qp.getLabel(), paramValueObjectMap.get(qp.getVariable()).toString());
        }

        try
        {
            String response = facade.runQuery(q, paramValueStringMap);
            model.addAttribute("response", response);
            model.addAttribute("pageTitle", "Query Response: " + q.getName());
            model.addAttribute("params", paramValueLabeledMap);
            model.addAttribute("pageFile", PAGE_FILE_RESPONSE);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ExceptionUtils.getFullStackTrace(e);
            model.addAttribute("msg_bad", "Error occured while issuing query");
            model.addAttribute("exception", ExceptionUtils.getFullStackTrace(e));
        }

        return "admin/base";
    }
}