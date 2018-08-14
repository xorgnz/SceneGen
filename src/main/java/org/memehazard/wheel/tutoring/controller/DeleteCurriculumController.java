package org.memehazard.wheel.tutoring.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.Curriculum;
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
@RequestMapping("/tutoring/curriculum/{id}/delete")
public class DeleteCurriculumController
{
    private static final String PAGE_FILE  = "tutoring/curric_delete";
    private static final String PAGE_TITLE = "Delete Curriculum";

    @Autowired
    private TutoringFacade    facade;
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
        Curriculum c = facade.getCurriculum(id);
        Collection<CurriculumItem> items = facade.listCurriculumItemsByCurriculum(id);

        // Respond
        model.addAttribute("curriculum", c);
        model.addAttribute("curriculumItems", items);
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
        Curriculum c = facade.getCurriculum(id);
        facade.deleteCurriculum(c);

        // Redirect
        flash.addFlashAttribute("msg_good", "Successfully deleted curriculum " + c.getName());
        flash.addFlashAttribute("refresh", "frame-side");
        status.setComplete();
        return "redirect:/admin/blank-main";
    }
}
