package org.memehazard.wheel.sandbox.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.servlet.http.HttpServletUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sandbox/httpRequestDump")
public class HttpRequestDumpController
{
    @RequestMapping(method = RequestMethod.GET)
    public String form(@RequestBody String body,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        String output = HttpServletUtils.requestToString(request);
        
        // Respond
        model.addAttribute("output", output);
        model.addAttribute("pageTitle", "Sandbox - HTTP Request Dump");
        model.addAttribute("pageFile", "sandbox/output_dump");
        return "admin/base";
    }
}
