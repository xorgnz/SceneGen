package org.memehazard.wheel.sandbox.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.style.facade.StyleFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sandbox/restComplex")
public class RestComplexDatastructureController
{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StyleFacade facade_style;


    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    @Transactional
    Foo form(
            @RequestBody String body,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Generating page - " + request.getServletPath());

        Foo foo = new Foo();

        // Respond
        return foo;
    }


    public class Foo
    {
        public String name = "name";
        public String name2 = "name2";

        public Bar    bar = new Bar();


        public String getName2()
        {
            return name2;
        }


        public Bar getBar()
        {
            return bar;
        }

    }


    public class Bar
    {
        private String name = "barname";


        public String getName()
        {
            return name;
        }
    }
}
