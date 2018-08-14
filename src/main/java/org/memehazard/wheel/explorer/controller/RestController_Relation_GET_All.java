package org.memehazard.wheel.explorer.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.explorer.facade.ExplorerFacade;
import org.memehazard.wheel.query.model.Relation;
import org.memehazard.wheel.query.model.RelationRegistry;
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
@RequestMapping("/REST/explorer/relation")
public class RestController_Relation_GET_All
{
    @Autowired
    private ExplorerFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ParserException
    {
        log.trace("Handling REST request - " + request.getServletPath());

        try
        {
            return new RestResponseObject(true, "", RelationRegistry.getRegisteredRelations());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return new RestResponseObject(false, e.getMessage(), null);
        }
    }


    public class RestResponseObject extends BasicRestResponseObject
    {
        private List<Relation> relations;


        public RestResponseObject(boolean success, String message, List<Relation> relations)
        {
            super(success, message);

            this.relations = relations;
        }


        public List<Relation> getRelations()
        {
            return relations;
        }
    }


}
