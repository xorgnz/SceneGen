package org.memehazard.wheel.tutoring.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.memehazard.wheel.tutoring.model.EntityKnowledge;
import org.memehazard.wheel.tutoring.model.RelationKnowledge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tutoring/curriculumItem/{id}/build")
public class BuildCurriculumItemController
{
    private static final String PAGE_FILE  = "tutoring/ci_build";
    private static final String PAGE_TITLE = "Build Curriculum Item";

    @Autowired
    private TutoringFacade    facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    public String form(
            @PathVariable Long id,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Generating page - " + request.getServletPath());

        CurriculumItem ci = facade.getCurriculumItem(id);

        boolean labelKnowledgeIncluded = facade.getLabelKnowledgeByCurriculumItem(id) != null;
        List<EntityKnowledge> entityKnowledge = facade.listEntityKnowledgeByCurriculumItem(id);
        List<RelationKnowledge> relationKnowledge = facade.listRelationKnowledgeByCurriculumItem(id);

        // Respond
        model.addAttribute("curriculumItem", ci);
        model.addAttribute("labelKnowledgeIncluded", labelKnowledgeIncluded);
        model.addAttribute("entitySubItems", entityKnowledge);
        model.addAttribute("relationSubItems", relationKnowledge);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }
}
