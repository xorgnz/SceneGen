package org.memehazard.wheel.dataviz.controller.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.dataviz.facade.VisualizationFacade;
import org.memehazard.wheel.style.facade.StyleFacade;
import org.memehazard.wheel.style.model.Stylable;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.model.Stylesheet;
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
@RequestMapping("/REST/visualizer/applyStylesheet")
public class ApplyStylesheetRestController
{
    @Autowired
    private VisualizationFacade facade;
    @Autowired
    private StyleFacade         facade_style;
    private Logger              log = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performPost(
            @RequestBody CommandObject co,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, XMLException
    {
        log.trace("Handling REST request - " + request.getServletPath());

        try
        {
            // Retrieve stylesheet
            Stylesheet stylesheet = facade_style.getStylesheet(co.getStylesheetId());

            System.err.println(stylesheet.toString());
            
            // Retrieve stylable objects
            List<DataStylable> stylables = new ArrayList<DataStylable>();
            stylables.addAll(co.getStylables());

            // Apply styles
            stylesheet.styleObjects(co.getStylables());

            // Prepare map of styles for response
            Map<Integer, Style> styleMap = new HashMap<Integer, Style>();
            for (DataStylable ds : stylables)
                styleMap.put(ds.getId(), ds.getStyle());

            // Respond
            return new RestResponseObject(true, "success", styleMap);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return new RestResponseObject(false, e.getMessage(), null);
        }
    }


    public static class CommandObject
    {
        private List<DataStylable> stylables;
        private Integer            stylesheetId;


        public List<DataStylable> getStylables()
        {
            return stylables;
        }


        public Integer getStylesheetId()
        {
            return stylesheetId;
        }


        public void setStylables(List<DataStylable> stylables)
        {
            this.stylables = stylables;
        }


        public void setStylesheetId(Integer stylesheetId)
        {
            this.stylesheetId = stylesheetId;
        }
    }


    public static class DataStylable implements Stylable
    {
        private Map<String, String> data;
        private Integer             id;
        private Style               style = null;
        private String[]            styleTags;


        public DataStylable()
        {
        }


        @Override
        public Map<String, String> getData()
        {
            return data;
        }


        public Integer getId()
        {
            return id;
        }


        public Style getStyle()
        {
            return style;
        }


        @Override
        public String[] getStyleTags()
        {
            return styleTags;
        }


        @Override
        public boolean needsStyle()
        {
            return true;
        }


        public void setData(Map<String, String> data)
        {
            this.data = data;
        }


        public void setId(Integer id)
        {
            this.id = id;
        }


        @Override
        public void setStyle(Style style)
        {
            this.style = style;
        }


        public void setStyleTags(String[] styleTags)
        {
            this.styleTags = styleTags;
        }


        public String toString()
        {
            return "ID: " + id + ". Style Tags: " + styleTags + ". Data " + data;
        }
    }


    public class RestResponseObject extends BasicRestResponseObject
    {
        private Map<Integer, Style> styleMap = new HashMap<Integer, Style>();


        public RestResponseObject(boolean success, String message, Map<Integer, Style> styleMap)
        {
            super(success, message);

            this.styleMap.clear();
            this.styleMap.putAll(styleMap);
        }


        public Map<Integer, Style> getStyleMap()
        {
            return styleMap;
        }
    }
}
