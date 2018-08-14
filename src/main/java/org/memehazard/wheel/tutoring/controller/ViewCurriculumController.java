package org.memehazard.wheel.tutoring.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
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
@RequestMapping("/tutoring/curriculum/{id}")
public class ViewCurriculumController
{

    @Autowired
    private TutoringFacade facade;
    @Autowired
    private RbacFacade     facade_rbac;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String page(
            @PathVariable Long id,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Curriculum c = facade.getCurriculum(id);
        Collection<CurriculumItem> items = facade.listCurriculumItemsByCurriculum(id);
        Collection<Enrolment> enrolments = facade.listEnrolmentsByCurriculum(id);
        
        System.err.println(enrolments);
        List<User> allUsers = facade_rbac.listUsers();
        
        
        System.err.println(allUsers);
        for (Enrolment e : enrolments)
            allUsers.remove(e.getStudent());
        System.err.println(allUsers);
        
        // Respond
        model.addAttribute("curriculum", c);
        model.addAttribute("curriculumItems", items);
        model.addAttribute("enrolments", enrolments);
        model.addAttribute("users", allUsers);
        model.addAttribute("pageTitle", "View Curriculum");
        model.addAttribute("pageFile", "tutoring/curric_view");
        return "admin/base";
    }
}
