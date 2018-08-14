package org.memehazard.util.spring;

import org.apache.commons.lang.text.StrBuilder;
import org.springframework.ui.Model;

public class LogHelper
{

    public static String dumpModel(Model model)
    {
        StrBuilder sb = new StrBuilder();
        
        sb.appendln("Dumping model:");
        for (String s : model.asMap().keySet())
        {
            sb.appendln(s + ":");
            sb.appendln(model.asMap().get(s).getClass().getCanonicalName());
            sb.appendln(model.asMap().get(s).toString());
        }
        
        return sb.toString();
    }
}
