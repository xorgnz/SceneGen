package org.memehazard.wheel.style.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.style.facade.StyleFacade;
import org.memehazard.wheel.style.model.Stylesheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/stylesheet/")
public class StylesheetRestController_GET_All
{
    @Autowired
    private StyleFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ParserException, IOException

    {
        log.trace("Handling REST request - " + request.getServletPath() + " - " + request.getMethod());

        // Act
        List<Stylesheet> sheets = facade.listStylesheets();
        
        return new RestResponseObject(true, "Success", sheets);
    }


    public static class RestResponseObject extends BasicRestResponseObject
    {
        private List<Stylesheet> stylesheets = null;


        public RestResponseObject(boolean success, String message, List<Stylesheet> stylesheets)
        {
            super(success, message);
            this.stylesheets = stylesheets;
        }


        public List<Stylesheet> getStylesheets()
        {
            return stylesheets;
        }
    }
}
