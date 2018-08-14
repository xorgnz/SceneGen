package org.memehazard.wheel.sandbox.graph;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.tutoring.model.Curriculum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/sandbox/graph/first")
public class FirstGraphController
{
    @Autowired
    private Neo4jTemplate template;
    private Logger        log = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public String form(@RequestBody String body,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Generating page - " + request.getServletPath());

        template.save(new Curriculum("The Curriculum", "Test User", "Test Description"));
        EndResult<Curriculum> result = template.findAll(Curriculum.class);

        String output = "";
        for (Curriculum cr_retrieved : result)
        {
            output += "Retrieved " + cr_retrieved.getName() + " - " + cr_retrieved.getNodeId();
        }
        // Respond
        model.addAttribute("output", output);
        model.addAttribute("pageTitle", "Sandbox - Graph add / retrieve");
        model.addAttribute("pageFile", "sandbox/output_dump");
        return "admin/base";
    }
}