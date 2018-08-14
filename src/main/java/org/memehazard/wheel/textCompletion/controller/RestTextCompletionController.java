package org.memehazard.wheel.textCompletion.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.textCompletion.facade.TextCompletionFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/textCompletion")
public class RestTextCompletionController
{
    @Autowired
    private TextCompletionFacade facade;
    private Logger               log = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    RestResponseObject performRequest(
            @ModelAttribute CommandObject co,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, XMLException
    {
        log.trace("Handling REST request - " + request.getServletPath());

        try
        {
            // Assemble list of all completions. Include prefix conclusions first
            List<String> prefixCompletions = facade.listCompletionsWithPrefix(co.getPrefix(), co.getLimit());
            List<String> anywhereCompletions = facade.listCompletions(co.getPrefix(), co.getLimit());
            anywhereCompletions.removeAll(prefixCompletions);
            prefixCompletions.addAll(anywhereCompletions);

            return new RestResponseObject(true, "success", prefixCompletions);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return new RestResponseObject(false, e.getMessage(), null);
        }
    }


    public static class CommandObject
    {
        private Integer limit;
        private String  prefix;


        public Integer getLimit()
        {
            return limit;
        }


        public String getPrefix()
        {
            return prefix;
        }


        public void setLimit(Integer limit)
        {
            this.limit = limit;
        }


        public void setPrefix(String prefix)
        {
            this.prefix = prefix;
        }
    }


    public class RestResponseObject extends BasicRestResponseObject
    {
        private List<String> completions   = new ArrayList<String>();


        public RestResponseObject(boolean success, String message, List<String> completions)
        {
            super(success, message);
            if (completions != null)
                this.completions.addAll(completions);
        }


        public List<String> getCompletions()
        {
            return completions;
        }
    }
}
