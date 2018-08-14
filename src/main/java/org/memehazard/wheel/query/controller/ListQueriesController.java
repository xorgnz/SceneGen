package org.memehazard.wheel.query.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.query.facade.QueryFacade;
import org.memehazard.wheel.query.model.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ListQueriesController
{
    @Autowired
    private QueryFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping("/query/query/list")
    @Transactional(readOnly = true)
    public String listSets(
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        List<Query> objs = facade.listQueries();

        model.addAttribute("objs", objs);
        model.addAttribute("pageTitle", "List Queries");
        model.addAttribute("pageFile", "query/query_list");
        return "admin/base";
    }
}
