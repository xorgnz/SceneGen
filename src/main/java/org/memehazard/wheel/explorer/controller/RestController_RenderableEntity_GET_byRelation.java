//package org.memehazard.wheel.explorer.controller;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.memehazard.exceptions.XMLException;
//import org.memehazard.wheel.core.controller.BasicRestResponseObject;
//import org.memehazard.wheel.explorer.facade.ExplorerFacade;
//import org.memehazard.wheel.explorer.view.RenderableEntityDescriptor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Controller
//@RequestMapping("/REST/explorer/entities/byRelation")
//public class RestController_RenderableEntity_GET_byRelation
//{
//    @Autowired
//    private ExplorerFacade facade;
//    private Logger         log = LoggerFactory.getLogger(getClass());
//
//
//    @RequestMapping(method = RequestMethod.GET)
//    @Transactional
//    public @ResponseBody
//    RestResponseObject performPost(
//            @ModelAttribute CommandObject co,
//            HttpServletRequest request,
//            HttpServletResponse response)
//            throws IOException, XMLException
//    {
//        log.trace("Handling REST request - " + request.getServletPath());
//
//        try
//        {
//            List<RenderableEntityDescriptor> entities = facade.retrieveRenderableEntitiesByRelation(co.getRelation(), co.getEntityId(),
//                    co.getAssetSetId(), co.getStylesheetId());
//
//            return new RestResponseObject(true, "success", entities);
//        }
//        catch (Exception e)
//        {
//            log.error(e.getMessage(), e);
//            return new RestResponseObject(false, e.getMessage(), null);
//        }
//    }
//
//
//    public class RestResponseObject extends BasicRestResponseObject
//    {
//        private List<RenderableEntityDescriptor> entities = new ArrayList<RenderableEntityDescriptor>();
//
//
//        public RestResponseObject(boolean success, String message, List<RenderableEntityDescriptor> entities)
//        {
//            super(success, message);
//            if (entities != null)
//                this.entities.addAll(entities);
//        }
//
//
//        public List<RenderableEntityDescriptor> getEntities()
//        {
//            return entities;
//        }
//    }
//
//
//    public static class CommandObject
//    {
//        private Integer assetSetId;
//        private String  relation;
//        private Integer stylesheetId;
//        private Integer entityId;
//
//
//        public Integer getAssetSetId()
//        {
//            return assetSetId;
//        }
//
//
//        public String getRelation()
//        {
//            return relation;
//        }
//
//
//        public Integer getStylesheetId()
//        {
//            return stylesheetId;
//        }
//
//
//        public Integer getEntityId()
//        {
//            return entityId;
//        }
//
//
//        public void setAssetSetId(Integer assetSetId)
//        {
//            this.assetSetId = assetSetId;
//        }
//
//
//        public void setRelation(String relation)
//        {
//            this.relation = relation;
//        }
//
//
//        public void setStylesheetId(Integer stylesheetId)
//        {
//            this.stylesheetId = stylesheetId;
//        }
//
//
//        public void setEntityId(Integer entityId)
//        {
//            this.entityId = entityId;
//        }
//    }
//}
