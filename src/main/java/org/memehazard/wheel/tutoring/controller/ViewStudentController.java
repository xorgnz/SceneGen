package org.memehazard.wheel.tutoring.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.memehazard.wheel.tutoring.model.Enrolment;
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
@RequestMapping("/tutoring/student/{id}")
public class ViewStudentController
{
    private static final String PAGE_FILE  = "tutoring/student_view";
    private static final String PAGE_TITLE = "View Student";

    @Autowired
    private TutoringFacade      facade;
    @Autowired
    private RbacFacade          facade_rbac;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String page(
            @PathVariable Long id,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act - Retrieve view objects
        User u = facade_rbac.getUser(id);
        Collection<Enrolment> enrolments = facade.listEnrolmentsByStudent(id);
        List<Curriculum> allCurricula = facade.listCurricula();

        System.err.println(enrolments);
        
        // Filter curricula to show only those in which student is not enrolled.
        Map<Long, Curriculum> allCurriculaMap = new HashMap<Long, Curriculum>();
        for (Curriculum c : allCurricula)
            allCurriculaMap.put(c.getNodeId(), c);
        Map<Long, Curriculum> filteredCurriculaMap = new HashMap<Long, Curriculum>(allCurriculaMap);
        for (Enrolment e : enrolments)
            filteredCurriculaMap.remove(e.getCurriculumId());

        System.err.println(allCurriculaMap);
        System.err.println(filteredCurriculaMap);
        
        // Respond
        model.addAttribute("student", u);
        model.addAttribute("enrolments", enrolments);
        model.addAttribute("filteredCurriculaMap", filteredCurriculaMap);
        model.addAttribute("allCurriculaMap", allCurriculaMap);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }
}
