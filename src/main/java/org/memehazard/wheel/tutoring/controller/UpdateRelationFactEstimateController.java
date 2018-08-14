package org.memehazard.wheel.tutoring.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.neo4j.graphdb.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/tutoring/studentModel/rFact/{factId}/")
@Transactional
public class UpdateRelationFactEstimateController
{

    @Autowired
    private TutoringFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping("/b/{studentId}/{value}")
    @Transactional
    public String respond(
            @PathVariable long factId,
            @PathVariable long studentId,
            @PathVariable boolean value,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act - update fact
        try
        {
            // Update fact
            double p_delta = facade.updateRelationFactEstimate(factId, studentId, value);
            
            // Store event
            facade.addRelationFactUpdateEvent(studentId, factId, request.getServletPath(), "Relation Fact updated", p_delta);
        }
        catch (NotFoundException e)
        {
            e.printStackTrace();
            flash.addFlashAttribute("msg_bad", "No fact with ID: " + factId + ". Cannot update estimate");
        }

        // Redirect
        return "redirect:" + request.getHeader("referer");
    }
}
