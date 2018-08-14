package org.memehazard.wheel.query.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.query.facade.QueryFacade;
import org.memehazard.wheel.query.model.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/query/clearCache")
public class ClearQueryCacheController
{
    @Autowired
    private QueryFacade facade;
    private Logger             log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping("/{id}")
    @Transactional
    public String clearByQuery(
            @PathVariable int id,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Query q = facade.getQuery(id);
        facade.clearCacheByQuery(id);

        // Respond
        flash.addFlashAttribute("msg_good", "Successfully cleared cache for query: " + q.getName());
        return "redirect:/query/query/" + id;
    }


    @RequestMapping("")
    @Transactional
    public String clearAll(
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        facade.clearCache();

        // Respond
        flash.addFlashAttribute("msg_good", "Successfully cleared cache for all queries");
        return "redirect:/admin/blank-main";
    }
}