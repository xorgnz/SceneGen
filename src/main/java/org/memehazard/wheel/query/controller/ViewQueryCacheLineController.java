package org.memehazard.wheel.query.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.query.facade.QueryFacade;
import org.memehazard.wheel.query.model.QueryCacheLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewQueryCacheLineController
{
    @Autowired
    private QueryFacade facade;
    private Logger             log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping("/query/cacheLine/{id}")
    @Transactional(readOnly = true)
    public String listSets(
            @PathVariable int id,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        QueryCacheLine obj = facade.getQueryCacheLine(id);

        // Respond
        model.addAttribute("obj", obj);
        return "query/qcl_view";
    }
}