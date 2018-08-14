package org.memehazard.wheel.explorer.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.explorer.controller.RestController_RenderableEntity_GET.CommandObject;
import org.memehazard.wheel.explorer.facade.ExplorerFacade;
import org.memehazard.wheel.explorer.view.RenderableEntityDescriptor;
import org.memehazard.wheel.query.parser.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/explorer/entity/byName/{name}")
public class RestController_RenderableEntity_GET_byName
{
    @Autowired
    private ExplorerFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performPost(
            @PathVariable String name,
            @ModelAttribute CommandObject co,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ParserException
    {
        log.trace("Handling REST request - " + request.getServletPath());

        try
        {
            List<RenderableEntityDescriptor> descriptors = facade.retrieveRenderableEntitiesByName(new String[] { name }, co.getAssetSetId(),
                    co.getStylesheetId());

            return new RestResponseObject(true, "", descriptors.size() > 0 ? descriptors.get(0) : null);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return new RestResponseObject(false, e.getMessage(), null);
        }
    }


    public class RestResponseObject extends BasicRestResponseObject
    {
        private RenderableEntityDescriptor entity;


        public RestResponseObject(boolean success, String message, RenderableEntityDescriptor entity)
        {
            super(success, message);

            this.entity = entity;
        }


        public RenderableEntityDescriptor getEntity()
        {
            return entity;
        }
    }
}
