package org.memehazard.wheel.tutoring.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.RelationKnowledge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tutoring/curriculumItem/{id}/build/addRelationKnowledge")
public class RestAddRelationKnowledgeController
{

    @Autowired
    private TutoringFacade facade;
    private Logger           log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject addRelationKnowledge(
            @PathVariable Long id,
            @ModelAttribute("co") CommandObject co,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Handling REST request - " + request.getServletPath());

        try
        {
            RelationKnowledge rk = facade.addRelationKnowledge(id, co.getName(), co.getNamespace());
            return new RestResponseObject(true, "Add Relation REST - " + rk.toString(), rk.getNodeId());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return new RestResponseObject(false, e.getMessage(), null);
        }

    }


    public static class CommandObject
    {
        private String name;
        private String namespace;


        public String getName()
        {
            return name;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public String getNamespace()
        {
            return namespace;
        }


        public void setNamespace(String namespace)
        {
            this.namespace = namespace;
        }
    }


    public class RestResponseObject extends BasicRestResponseObject
    {

        private Long nodeId;


        public RestResponseObject(boolean success, String message, Long nodeId)
        {
            super(success, message);
            this.nodeId = nodeId;
        }


        public Long getNodeId()
        {
            return nodeId;
        }
    }
}
