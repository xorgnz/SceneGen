package org.memehazard.wheel.tutoring.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tutoring/studentModel/{curriculumId}/{studentId}/unenrol")
public class UnenrolStudentController
{

    @Autowired
    private TutoringFacade facade;
    @Autowired
    private RbacFacade     facade_rbac;
    private Logger         log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Neo4jUtilities         neo4jUtils;


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public String page(
            @PathVariable long curriculumId,
            @PathVariable long studentId,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        
        User u = facade_rbac.getUser(studentId);
        Curriculum c = facade.getCurriculum(curriculumId);
        if (u == null)
            flash.addFlashAttribute("msg_bad", String.format("Unable to unenrol using user with ID " + studentId + " - does not exist"));
        else if (c == null)
            flash.addFlashAttribute("msg_bad", String.format("Unable to unenrol using curriculum with ID " + studentId + " - does not exist"));
        else
        {
            facade.unenrolStudentFromCurriculum(studentId, curriculumId);
            flash.addFlashAttribute("refresh", "frame-side");
            flash.addFlashAttribute("msg_good", String.format("Unenroled user \"%s\" in curriculum \"%s\"", u.toNameString(), c.getName()));
        }

        // Redirect
        return "redirect:" + request.getHeader("referer");
    }
}
