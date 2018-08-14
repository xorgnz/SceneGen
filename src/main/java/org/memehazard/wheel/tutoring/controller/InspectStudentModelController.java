package org.memehazard.wheel.tutoring.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.BayesValue;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.memehazard.wheel.tutoring.utils.TutoringUtils;
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
@RequestMapping("/tutoring/studentModel/{curriculumId}/{studentId}/inspect")
public class InspectStudentModelController
{

    @Autowired
    private TutoringFacade facade;
    @Autowired
    private RbacFacade     facade_rbac;
    private Logger         log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Neo4jUtilities         neo4jUtils;


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String page(
            @PathVariable long curriculumId,
            @PathVariable long studentId,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Curriculum c = facade.buildFullDomainModel(curriculumId);
        User u = facade_rbac.getUser(studentId);
        Map<Long, BayesValue> bvMap = TutoringUtils.mapifyBayesValuesByDomainId(facade.listBayesValues(c.getAllDomainNodes(), studentId));

        // Respond
        model.addAttribute("curriculum", c);
        model.addAttribute("student", u);
        model.addAttribute("bayes", bvMap);
        return "tutoring/sm_inspect";
    }
}
