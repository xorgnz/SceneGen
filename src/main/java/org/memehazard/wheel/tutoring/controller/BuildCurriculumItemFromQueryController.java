package org.memehazard.wheel.tutoring.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.memehazard.wheel.query.facade.QueryFacade;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.query.view.QueryDescriptor;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/tutoring/curriculumItem/{ciId}/build/fromQuery")
public class BuildCurriculumItemFromQueryController
{
    private static final String PAGE_FILE  = "tutoring/ci_build_from_query";
    private static final String PAGE_TITLE = "Build Curriculum Item From Query";

    @Autowired
    private TutoringFacade      facade;
    @Autowired
    private QueryFacade  facade_queryMgr;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    public String form(
            @PathVariable Long ciId,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Generating page - " + request.getServletPath());

        return out_form(ciId, null, model);
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable Long ciId,
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult bResult,
            RedirectAttributes flash,
            Model model,
            HttpServletRequest request,
            SessionStatus status,
            HttpServletResponse response)
            throws ParserException, IOException
    {
        log.trace("Processing form - " + request.getServletPath());

        try
        {
            // Interpret parameter string
            Map<String, Object> paramObjMap = WebUtils.getParametersStartingWith(request, "qparam_");
            Map<String, String> paramMap = new HashMap<String, String>();
            for (String key : paramObjMap.keySet())
                paramMap.put(key, paramObjMap.get(key).toString());

            // Act
            facade.addEntityKnowledgeFromQueryResults(ciId, co.getQueryId(), paramMap);

            // Redirect
            flash.addFlashAttribute("msg_good", "Successfully added entity knowledge from query.");
            flash.addFlashAttribute("refresh", "frame-side");
            status.setComplete();
            return "redirect:/tutoring/curriculumItem/" + ciId + "/build";
        }
        catch (ParserException e)
        {
            // Message - failure
            model.addAttribute("msg_bad", "Unable to parse response from Query Integrator. " +
                                          "This could be caused by a mis-formed query, a problem in query execution, " +
                                          "or a failure within the Query Integrator. See the query log for more information.");

            return out_form(ciId, co.getQueryId(), model);
        }
        catch (IOException e)
        {
            // Message - failure
            model.addAttribute("msg_bad", "A problem occured while communication with the Query Integrator. " +
                                          "Either the Query Integrator is down or a network problem has occurred. " +
                                          "Consider trying again as this might be a short term problem");

            return out_form(ciId, co.getQueryId(), model);
        }
    }


    @Transactional(readOnly = true)
    public String out_form(Long ciId, Integer queryId, Model model)
    {
        CurriculumItem ci = facade.getCurriculumItem(ciId);

        // Prepare model attributes - query
        List<QueryDescriptor> queryDescriptors = new ArrayList<QueryDescriptor>();

        for (Query q : facade_queryMgr.listQueries())
            queryDescriptors.add(new QueryDescriptor(q));

        // Respond
        model.addAttribute("queries", queryDescriptors);
        model.addAttribute("co", new CommandObject());
        model.addAttribute("curriculumItem", ci);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    public static class CommandObject
    {
        private String  parameters;
        private Integer queryId;


        public String getParameters()
        {
            return parameters;
        }


        public Integer getQueryId()
        {
            return queryId;
        }


        public void setParameters(String parameters)
        {
            this.parameters = parameters;
        }


        public void setQueryId(Integer queryId)
        {
            this.queryId = queryId;
        }
    }
}
