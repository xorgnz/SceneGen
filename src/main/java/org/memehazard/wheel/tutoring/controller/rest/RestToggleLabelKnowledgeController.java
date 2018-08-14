package org.memehazard.wheel.tutoring.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.LabelKnowledge;
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
@RequestMapping("/tutoring/curriculumItem/{ciId}/build/toggleLabelKnowledge")
public class RestToggleLabelKnowledgeController
{

    @Autowired
    private TutoringFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject form(
            @PathVariable Long ciId,
            @ModelAttribute("co") CommandObject co,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Handling REST request - " + request.getServletPath());

        try
        {
            // TODO - addLabelKnowledge - is this idempotent?

            LabelKnowledge lk = facade.getLabelKnowledgeByCurriculumItem(ciId);

            if (co.isIncluded())
            {
                if (lk == null)
                    facade.addLabelKnowledge(ciId);
            }
            else
            {
                if (lk != null)
                    facade.deleteLabelKnowledge(lk);
            }

            return new BasicRestResponseObject(true, "Toggle Label Knowledge REST - " + co.isIncluded());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return new BasicRestResponseObject(false, e.getMessage());
        }

    }


    public static class CommandObject
    {
        private boolean included;


        public boolean isIncluded()
        {
            return included;
        }


        public void setIncluded(boolean included)
        {
            this.included = included;
        }
    }
}
