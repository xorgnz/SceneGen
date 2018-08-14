package org.memehazard.wheel.tutoring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.memehazard.wheel.core.validator.ValidFloat;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/tutoring/curriculumItem/{id}/edit")
@SessionAttributes("co")
public class EditCurriculumItemController
{
    private static final String PAGE_FILE  = "tutoring/ci_form";
    private static final String PAGE_TITLE = "Edit Curriculum Item";

    @Autowired
    private TutoringFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable Long id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        CurriculumItem ci = facade.getCurriculumItem(id);
        CommandObject co = new CommandObject(ci);

        // Respond
        model.addAttribute("co", co);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult result,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Passed binding & validation
        if (!result.hasErrors())
        {
            // Act
            CurriculumItem ci = facade.getCurriculumItem(co.getNodeId());
            co.updateCurriculumItem(ci);
            facade.updateCurriculumItem(ci);
            model.addAttribute("msg_good", "Success!!");
            model.addAttribute("refresh", "frame-side");
        }

        // Respond
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);

        return "admin/base";
    }


    public static class CommandObject
    {
        private String description;
        @NotEmpty(message = "{CurriculumItem.name.empty}")
        private String name;
        private Long   nodeId;
        @NotEmpty(message = "{CurriculumItem.weight.empty}")
        @ValidFloat(message = "{CurriculumItem.weight.invalid}", min = 0)
        private String weight;


        public CommandObject()
        {
        }


        public CommandObject(CurriculumItem ci)
        {
            this.nodeId = ci.getNodeId();
            this.description = ci.getDescription();
            this.name = ci.getName();
            this.weight = "" + ci.getWeight();
        }


        public String getDescription()
        {
            return description;
        }


        public String getName()
        {
            return name;
        }


        public Long getNodeId()
        {
            return nodeId;
        }


        public String getWeight()
        {
            return weight;
        }


        public void setDescription(String description)
        {
            this.description = description;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void setWeight(String weight)
        {
            this.weight = weight;
        }


        public void updateCurriculumItem(CurriculumItem ci)
        {
            ci.setName(name);
            ci.setDescription(description);
            ci.setWeight(Double.parseDouble(weight));
        }
    }
}