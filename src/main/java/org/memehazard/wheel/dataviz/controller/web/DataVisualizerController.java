package org.memehazard.wheel.dataviz.controller.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.query.parser.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 */

/**
 * @author xorgnz
 * 
 */
@Controller
@RequestMapping(value = "/visualizer")
public class DataVisualizerController
{
    private static final String PAGE_FILE_DV = "dataviz/dv";

    private Logger              log                 = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public String ep_scene(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ParserException
    {
        log.trace("Processing form - " + request.getServletPath());

        // Respond
        return PAGE_FILE_DV;
    }
}
