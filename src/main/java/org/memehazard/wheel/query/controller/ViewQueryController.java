package org.memehazard.wheel.query.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.query.facade.QueryFacade;
import org.memehazard.wheel.query.model.Query;
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

@Controller
@RequestMapping("/query/query/{id}")
public class ViewQueryController
{
    @Autowired
    private QueryFacade facade;
    private Logger             log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String performGet(
            @PathVariable int id,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Query q = facade.getQueryWithCacheLines(id);

        // Respond
        model.addAttribute("query", new QueryDescriptor(q));
        model.addAttribute("pageTitle", "View Query");
        model.addAttribute("pageFile", "query/query_view");
        return "admin/base";
    }
}