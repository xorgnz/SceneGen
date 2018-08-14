package org.memehazard.wheel.query.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.query.facade.QueryFacade;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.parser.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/query/")
public class QueryRestController_GET_All
{
    @Autowired
    private QueryFacade facade;
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
        List<Query> queries = facade.listQueries();

        return new RestResponseObject(true, "Success", queries);
    }


    public static class RestResponseObject extends BasicRestResponseObject
    {
        private List<Query> queries = null;


        public RestResponseObject(boolean success, String message, List<Query> queries)
        {
            super(success, message);
            this.queries = queries;
        }


        public List<Query> getQueries()
        {
            return queries;
        }
    }
}
