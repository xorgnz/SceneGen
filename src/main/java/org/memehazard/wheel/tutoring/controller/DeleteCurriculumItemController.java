package org.memehazard.wheel.tutoring.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/tutoring/curriculumItem/{id}/delete")
public class DeleteCurriculumItemController
{
    private static final String PAGE_FILE  = "tutoring/ci_delete";
    private static final String PAGE_TITLE = "Delete Curriculum Item";

    @Autowired
    private TutoringFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(
            @PathVariable Long id,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        CurriculumItem ci = facade.getCurriculumItem(id);

        // Respond
        model.addAttribute("curriculumItem", ci);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable Long id,
            SessionStatus status,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Act
        CurriculumItem ci = facade.getCurriculumItem(id);
        facade.deleteCurriculumItem(ci);

        // Redirect
        flash.addFlashAttribute("msg_good", "Successfully deleted curriculum item " + ci.getName());
        flash.addFlashAttribute("refresh", "frame-side");
        status.setComplete();
        return "redirect:/tutoring/curriculum/" + ci.getCurriculum().getNodeId();
    }
}