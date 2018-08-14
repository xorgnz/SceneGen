package org.memehazard.wheel.viewer.controller;

import org.memehazard.wheel.viewer.view.SceneContentDescriptor;
import org.springframework.ui.Model;

public class AVAControllerHelper
{
    private static final String PAGE_FILE_VIEW_WEBGL = "viewer/ava";


    public static String generateControllerResponse(SceneContentDescriptor scnd, Model model)
    {
        // Compose scene
        model.addAttribute("scnd", scnd);
        return PAGE_FILE_VIEW_WEBGL;
    }
}
