package org.memehazard.wheel.tutoring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tutoring/studentModel/{curriculumId}/{studentId}/console")
@Transactional
public class ViewUpdateConsoleController
{

    @Autowired
    private TutoringFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping
    @Transactional(readOnly = true)
    public String page(
            @PathVariable long curriculumId,
            @PathVariable long studentId,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Generating page - " + request.getServletPath());
        
        boolean exists = facade.getCurriculum(curriculumId) != null;
        boolean enrolled = facade.isEnrolled(curriculumId, studentId);
        if (!exists)
            model.addAttribute("msg_bad", "No curriculum found with ID " + curriculumId);
        else if (!enrolled)
            model.addAttribute("msg_bad", "Student with ID " + studentId + " not enrolled");
        else
        {
            model.addAttribute("curriculum", facade.buildFullDomainModel(curriculumId));
            model.addAttribute("studentId", studentId);
        }

        // Respond
        model.addAttribute("pageTitle", "Tutoring - Student Model Update Console");
        model.addAttribute("pageFile", "tutoring/sm_console");
        return "admin/base";
    }
}
