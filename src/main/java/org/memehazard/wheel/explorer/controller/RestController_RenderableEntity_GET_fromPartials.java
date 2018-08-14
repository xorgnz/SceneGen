package org.memehazard.wheel.explorer.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.explorer.facade.ExplorerFacade;
import org.memehazard.wheel.explorer.view.RenderableEntityDescriptor;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.parser.ParserException;
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
@RequestMapping("/REST/explorer/entity/fromPartials")
public class RestController_RenderableEntity_GET_fromPartials
{
    @Autowired
    private ExplorerFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performGet(
            @RequestBody CommandObject co,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ParserException
    {
        log.trace("Handling REST request - " + request.getServletPath());

        try
        {
            List<RenderableEntityDescriptor> objects = facade.buildRenderableEntityDescriptors(
                    co.getPartials(), 
                    co.getAssetSetId(),
                    co.getStylesheetId());

            return new RestResponseObject(true, "", objects);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return new RestResponseObject(false, e.getMessage(), null);
        }
    }


    public static class CommandObject
    {
        private Integer             assetSetId;
        private List<PartialEntity> partials;
        private Integer             stylesheetId;


        public Integer getAssetSetId()
        {
            return assetSetId;
        }


        public List<PartialEntity> getPartials()
        {
            return partials;
        }


        public Integer getStylesheetId()
        {
            return stylesheetId;
        }


        public void setAssetSetId(Integer assetSetId)
        {
            this.assetSetId = assetSetId;
        }


        public void setPartials(List<PartialEntity> partials)
        {
            this.partials = partials;
        }


        public void setStylesheetId(Integer stylesheetId)
        {
            this.stylesheetId = stylesheetId;
        }
    }


    public static class PartialEntity extends Entity
    {
        private static final long serialVersionUID = 4752759680597444279L;
    }


    public class RestResponseObject extends BasicRestResponseObject
    {
        private List<RenderableEntityDescriptor> entities;


        public RestResponseObject(boolean success, String message, List<RenderableEntityDescriptor> entities)
        {
            super(success, message);

            this.entities = entities;
        }


        public List<RenderableEntityDescriptor> getEntities()
        {
            return entities;
        }
    }
}
