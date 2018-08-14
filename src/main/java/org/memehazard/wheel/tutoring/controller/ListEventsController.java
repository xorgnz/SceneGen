package org.memehazard.wheel.tutoring.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.memehazard.wheel.tutoring.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tutoring/event/list/{curriculumId}/{studentId}")
public class ListEventsController
{
    private static final String PAGE_FILE  = "tutoring/event_list";
    private static final String PAGE_TITLE = "List Events for student %s and curriculum %s";

    @Autowired
    private TutoringFacade      facade;
    @Autowired
    private RbacFacade          facade_rbac;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String page(
            @PathVariable("curriculumId") long curriculumId,
            @PathVariable("studentId") long studentId,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());
        
        // Act - retrieve objects
        User u = facade_rbac.getUser(studentId);
        Curriculum c = facade.getCurriculum(curriculumId);
        List<Event> events = facade.listEvents(curriculumId, studentId);

        model.addAttribute("events", events);
        model.addAttribute("pageTitle", String.format(PAGE_TITLE, u.toNameString(), c.getName()));
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }
}