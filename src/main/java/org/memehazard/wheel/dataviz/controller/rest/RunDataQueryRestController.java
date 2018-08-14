package org.memehazard.wheel.dataviz.controller.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.dataviz.facade.VisualizationFacade;
import org.memehazard.wheel.query.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/REST/visualizer/dataQuery")
public class RunDataQueryRestController
{
    @Autowired
    private VisualizationFacade facade;
    private Logger              log = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    RestResponseObject performPost(
            @RequestBody CommandObject co,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, XMLException
    {
        log.trace("Handling REST request - " + request.getServletPath());

        try
        {
            System.err.println(co.getQueryParamValueMap());
            
            Collection<? extends Entity> entities = facade.retrieveDataFromQuery(co.getQueryId(), co.getQueryParamValueMap(), co.getEntityIds());

            return new RestResponseObject(true, "success", entities);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return new RestResponseObject(false, e.getMessage(), null);
        }
    }


    public static class CommandObject
    {
        public List<Map<String, String>> queryParams;
        private Integer[]                entityIds;
        private Integer                  queryId;


        public Integer[] getEntityIds()
        {
            return entityIds;
        }


        public Integer getQueryId()
        {
            return queryId;
        }


        public List<Map<String, String>> getQueryParams()
        {
            return queryParams;
        }


        public Map<String, String> getQueryParamValueMap()

        {
            Map<String, String> qpvMap = new TreeMap<String, String>();

            if (queryParams != null)
                for (Map<String, String> co : queryParams)
                    qpvMap.put(co.get("name"), co.get("value"));

            return qpvMap;
        }


        public void setEntityIds(Integer[] entityIds)
        {
            this.entityIds = entityIds;
        }


        public void setQueryId(Integer queryId)
        {
            this.queryId = queryId;
        }


        public void setQueryParams(List<Map<String, String>> queryParams)
        {
            this.queryParams = queryParams;
        }
    }


    public class RestResponseObject extends BasicRestResponseObject
    {
        private List<Entity> entities = new ArrayList<Entity>();


        public RestResponseObject(boolean success, String message, Collection<? extends Entity> entities)
        {
            super(success, message);

            this.entities.clear();
            this.entities.addAll(entities);
        }


        public List<Entity> getEntities()
        {
            return entities;
        }
    }
}
