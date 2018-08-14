package org.memehazard.wheel.query.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.query.facade.QueryFacade;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.view.QueryDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/query/query/{queryId}/delete")
public class DeleteQueryController
{
    private static final String PAGE_FILE  = "query/query_delete";
    private static final String PAGE_TITLE = "Delete Query";

    @Autowired
    private QueryFacade         facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int queryId, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Query q = facade.getQueryWithCacheLines(queryId);

        // Respond
        model.addAttribute("query", new QueryDescriptor(q));
        model.addAttribute("cacheLines", q.getCacheLines());
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable int queryId,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Act
        Query q = facade.getQuery(queryId);
        try
        {
            facade.deleteQuery(queryId);

            flash.addFlashAttribute("msg_good", "Successfully deleted query " + q.toString());
            flash.addFlashAttribute("refresh", "frame-side");
        }
        catch (DataIntegrityViolationException e)
        {
            e.printStackTrace();

            flash.addFlashAttribute("msg_bad", "Unable to delete query '" + q.getName() + "'. It appears to be in use elsewhere in the system");
        }

        // Redirect
        return "redirect:/admin/blank-main";
    }
}