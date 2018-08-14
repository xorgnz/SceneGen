package org.memehazard.wheel.tutoring.controller.rest;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.neo4j.graphdb.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/tutoring/REST/studentModel/lFact/update")
@Transactional
public class RestUpdateLabelFactEstimateController
{

    @Autowired
    private TutoringFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping
    @Transactional
    public @ResponseBody
    BasicRestResponseObject respond(
            @ModelAttribute("co") CommandObject co,
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
            double p_delta = facade.updateLabelFactEstimate(co.getFactId(), co.getStudentId(), co.isCorrect());

            // Store event
            facade.addLabelFactUpdateEvent(co.getStudentId(), co.getFactId(), request.getServletPath(), "Label Fact updated", p_delta);

            return new BasicRestResponseObject(true, "Updated!");
        }
        catch (NotFoundException e)
        {
            e.printStackTrace();
            return new BasicRestResponseObject(false, "No fact with ID: " + co.getFactId() + ". Cannot update estimate");
        }
    }


    public static class CommandObject
    {
        public int     factId;
        public int     studentId;
        public boolean correct;


        public int getFactId()
        {
            return factId;
        }


        public int getStudentId()
        {
            return studentId;
        }


        public boolean isCorrect()
        {
            return correct;
        }


        public void setFactId(int factId)
        {
            this.factId = factId;
        }


        public void setStudentId(int studentId)
        {
            this.studentId = studentId;
        }


        public void setCorrect(boolean correct)
        {
            this.correct = correct;
        }
    }
}
