package org.memehazard.wheel.asset.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class RetrieveFileController
{
    public static final String FILE_PATH = "/asset/file/";

    @Autowired
    private AssetFacade        facade;
    private Logger             log       = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(FILE_PATH + "{name}.{ext}")
    @Transactional(readOnly = true)
    public void retrieveFile(
            @PathVariable String name,
            @PathVariable String ext,
            HttpServletRequest request,
            HttpServletResponse response)
            throws
            IOException
    {
        log.trace("Returning file " + name + "." + ext);

        // Retrieve file
        File f = facade.retrieveFile(name + "." + ext);
        response.setContentLength((int) f.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
        FileCopyUtils.copy(new FileInputStream(f), response.getOutputStream());
    }


    public static String getAssetRetrievalPath(HttpServletRequest request)
    {
        return String.format("http://%s:%s%s%s",
                request.getServerName(),
                request.getServerPort(),
                request.getContextPath(),
                RetrieveFileController.FILE_PATH);
    }
}
