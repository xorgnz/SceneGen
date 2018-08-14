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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/query/cacheLine/{id}/delete")
public class DeleteQueryCacheLineController
{
    @Autowired
    private QueryFacade facade;
    private Logger             log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public String submit(@PathVariable int id, Model model, HttpServletRequest request,
            RedirectAttributes flash)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        QueryCacheLine qcl = facade.getQueryCacheLine(id);
        facade.deleteQueryCacheLine(id);

        // Respond
        flash.addFlashAttribute("msg_good", "Successfully deleted query cache line: " + qcl.getQuery().getName()
                                            + " - " + qcl.getParameterValueString());
        return "redirect:/query/query/" + qcl.getQuery().getId();
    }
}