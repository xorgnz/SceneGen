package org.memehazard.wheel.tutoring.controller;


import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/tutoring/studentModel/lFact/{factId}/")
@Transactional
public class UpdateLabelFactEstimateController
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
            double p_delta = facade.updateLabelFactEstimate(factId, studentId, value);

            // Store event
            facade.addLabelFactUpdateEvent(studentId, factId, request.getServletPath(), "Label Fact updated", p_delta);
        }
        catch (NotFoundException e)
        {
            e.printStackTrace();
            flash.addFlashAttribute("msg_bad", "No fact with ID: " + factId + ". Cannot update estimate");
        }

        // Redirect
        return "redirect:" + request.getHeader("referer");
    }


    public static class CommandObject
    {
        private long id;
        private int  type;


        public int getType()
        {
            return type;
        }


        public void setType(int type)
        {
            this.type = type;
        }


        public long getId()
        {
            return id;
        }


        public void setId(long id)
        {
            this.id = id;
        }


        @Override
        public String toString()
        {
            return "ID: " + id + ", type: " + type;
        }
    }


    class RestResponseObject extends BasicRestResponseObject
    {
        private List<ResponseItem> items = new ArrayList<ResponseItem>();


        public RestResponseObject(boolean success, String message, List<ResponseItem> items)
        {
            super(success, message);
            this.items = items;
        }


        public List<ResponseItem> getItems()
        {
            return items;
        }


        public void setItems(List<ResponseItem> items)
        {
            this.items = items;
        }
    }


    class ResponseItem
    {
        private long  id;
        public String name;
        public int    type;
        public float  p;


        public ResponseItem(long id, String name, int type, float p)
        {
            this.id = id;
            this.name = name;
            this.type = type;
            this.p = p;
        }


        public String getName()
        {
            return name;
        }


        public int getType()
        {
            return type;
        }


        public long getId()
        {
            return id;
        }


        public float getP()
        {
            return p;
        }
    }
}
