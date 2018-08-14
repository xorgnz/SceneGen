package org.memehazard.wheel.tutoring.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.EntityKnowledge;
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
@RequestMapping("/tutoring/curriculumItem/{id}/build/addEntityKnowledge")
public class RestAddEntityKnowledgeController
{

    @Autowired
    private TutoringFacade facade;
    private Logger           log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject addEntityKnowledge(
            @PathVariable Long id,
            @ModelAttribute("co") CommandObject co,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Handling REST request - " + request.getServletPath());

        try
        {
            EntityKnowledge ek = facade.addEntityKnowledge(id, co.getFmaId(), co.getFmaLabel());
            return new RestResponseObject(true, "Added EntityKnowledge - " + ek.toString(), ek.getNodeId());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return new RestResponseObject(false, e.getMessage(), null);
        }
    }


    public static class CommandObject
    {
        private Integer fmaId;
        private String  fmaLabel;


        public Integer getFmaId()
        {
            return fmaId;
        }


        public String getFmaLabel()
        {
            return fmaLabel;
        }


        public void setFmaId(Integer fmaId)
        {
            this.fmaId = fmaId;
        }


        public void setFmaLabel(String fmaLabel)
        {
            this.fmaLabel = fmaLabel;
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
