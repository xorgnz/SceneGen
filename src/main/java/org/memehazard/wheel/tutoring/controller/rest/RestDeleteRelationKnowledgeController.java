package org.memehazard.wheel.tutoring.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.text.StrBuilder;
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
@RequestMapping("/tutoring/curriculumItem/{id}/build/removeRelationKnowledge")
public class RestDeleteRelationKnowledgeController
{

    @Autowired
    private TutoringFacade facade;
    private Logger           log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject removeEntityKnowledge(
            @PathVariable Long id,
            @ModelAttribute("co") CommandObject co,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Handling REST request - " + request.getServletPath());

        // Act - remove relation knowledge
        StrBuilder sb = new StrBuilder();
        for (Long nodeId : co.getNodeIds())
        {
            RelationKnowledge rk = facade.getRelationKnowledge(nodeId);
            facade.deleteRelationKnowledge(rk);

            sb.appendln(rk.toString());
        }

        return new BasicRestResponseObject(true, "Removed RelationKnowledge - " + sb.toString());
    }


    public static class CommandObject
    {
        public long[] nodeIds;


        public long[] getNodeIds()
        {
            return nodeIds;
        }


        public void setNodeIds(long[] nodeIds)
        {
            this.nodeIds = nodeIds;
        }
    }
}
