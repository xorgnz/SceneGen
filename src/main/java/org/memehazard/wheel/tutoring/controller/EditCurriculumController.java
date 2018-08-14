package org.memehazard.wheel.tutoring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.Curriculum;
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
@RequestMapping("/tutoring/curriculum/{id}/edit")
@SessionAttributes("co")
public class EditCurriculumController
{
    private static final String PAGE_FILE  = "tutoring/curric_form";
    private static final String PAGE_TITLE = "Edit Curriculum";

    @Autowired
    private TutoringFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable Long id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Curriculum c = facade.getCurriculum(id);
        CommandObject co = new CommandObject(c);

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
            Curriculum c = facade.getCurriculum(co.getNodeId());
            co.updateCurriculum(c);
            facade.updateCurriculum(c);
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
        private String creatorName = "";
        private String description = "";
        @NotEmpty(message = "{Curriculum.name.empty}")
        private String name        = "";
        private Long   nodeId;


        public CommandObject(Curriculum c)
        {
            this.name = c.getName();
            this.description = c.getDescription();
            this.creatorName = c.getCreatorName();
            this.nodeId = c.getNodeId();
        }


        public String getCreatorName()
        {
            return creatorName;
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


        public void setCreatorName(String creatorName)
        {
            this.creatorName = creatorName;
        }


        public void setDescription(String description)
        {
            this.description = description;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void updateCurriculum(Curriculum c)
        {
            c.setName(name);
            c.setCreatorName(creatorName);
            c.setDescription(description);
        }
    }
}