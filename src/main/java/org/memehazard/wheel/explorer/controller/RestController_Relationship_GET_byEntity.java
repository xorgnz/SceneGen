package org.memehazard.wheel.explorer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.explorer.facade.ExplorerFacade;
import org.memehazard.wheel.query.model.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/explorer/exploreEntity/{id}")
public class RestController_Relationship_GET_byEntity
{
    @Autowired
    private ExplorerFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    RestResponseObject performPost(
            @PathVariable int id,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, XMLException
    {
        log.trace("Handling REST request - " + request.getServletPath());

        try
        {
            List<Relationship> rels = facade.generateRelationshipsByEntity(id);
            return new RestResponseObject(true, "success", rels);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return new RestResponseObject(false, e.getMessage(), null);
        }
    }


    public class RestResponseObject extends BasicRestResponseObject
    {
        private List<Relationship> relationships = new ArrayList<Relationship>();


        public RestResponseObject(boolean success, String message, List<Relationship> relationships)
        {
            super(success, message);

            if (relationships != null)
                this.relationships.addAll(relationships);
        }


        public List<Relationship> getRelationships()
        {
            return relationships;
        }
    }
}
