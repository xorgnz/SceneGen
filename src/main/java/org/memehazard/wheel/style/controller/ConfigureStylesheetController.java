package org.memehazard.wheel.style.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.wheel.style.facade.StyleFacade;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.model.Stylesheet;
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


@Controller
@RequestMapping("/style/sheet/{id}/configure")
public class ConfigureStylesheetController
{
    private static final String PAGE_FILE  = "style/sheet_configure";
    private static final String PAGE_TITLE = "Configure Stylesheet ";

    @Autowired
    private StyleFacade         facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Stylesheet sheet = facade.getStylesheet(id);
        List<Style> styles = facade.listStylesByStylesheet(id);

        // Respond
        model.addAttribute("co", new CommandObject(sheet, styles));
        model.addAttribute("pageTitle", PAGE_TITLE + " - " + sheet.getName());
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
        log.trace("Received command: " + co.toString());

        // Passed binding & validation
        if (!result.hasErrors())
        {
            // Act
            Stylesheet sheet = facade.getStylesheet(co.getId());
            facade.configureStylesheet(sheet, co.createStyles());
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
        private double[] alpha;
        @Valid
        private int      id;
        private String   sheetName;
        String[]         ambient;
        String[]         diffuse;
        String[]         emissive;
        int[]            shininess;
        String[]         specular;
        int[]            styleId;
        String[]         tag;


        public CommandObject()
        {
        }


        public CommandObject(Stylesheet stylesheet, List<Style> styles)
        {
            this.id = stylesheet.getId();
            this.setSheetName(stylesheet.getName());

            styleId = new int[styles.size()];
            tag = new String[styles.size()];

            ambient = new String[styles.size()];
            diffuse = new String[styles.size()];
            emissive = new String[styles.size()];
            specular = new String[styles.size()];
            shininess = new int[styles.size()];

            for (int i = 0; i < styles.size(); i++)
            {
                Style style = styles.get(i);

                styleId[i] = style.getId();
                tag[i] = style.getTag();
                ambient[i] = style.getAmbient();
                diffuse[i] = style.getDiffuse();
                emissive[i] = style.getEmissive();
                specular[i] = style.getSpecular();
                shininess[i] = style.getShininess();
            }
        }


        public List<Style> createStyles()
        {
            List<Style> styles = new ArrayList<Style>();

            for (int i = 0; i < styleId.length; i++)
            {
                Style style = new Style(null, tag[i], alpha[i], ambient[i], diffuse[i], emissive[i], specular[i],
                        shininess[i], i);

                style.setId(styleId[i]);
                styles.add(style);
            }

            return styles;
        }


        public double[] getAlpha()
        {
            return alpha;
        }


        public String[] getAmbient()
        {
            return ambient;
        }


        public String[] getDiffuse()
        {
            return diffuse;
        }


        public String[] getEmissive()
        {
            return emissive;
        }


        public int getId()
        {
            return id;
        }


        public String getSheetName()
        {
            return sheetName;
        }


        public int[] getShininess()
        {
            return shininess;
        }


        public String[] getSpecular()
        {
            return specular;
        }


        public int[] getStyleId()
        {
            return styleId;
        }


        public String[] getTag()
        {
            return tag;
        }


        public void setAlpha(double[] alpha)
        {
            this.alpha = alpha;
        }


        public void setAmbient(String[] ambient)
        {
            this.ambient = ambient;
        }


        public void setDiffuse(String[] diffuse)
        {
            this.diffuse = diffuse;
        }


        public void setEmissive(String[] emissive)
        {
            this.emissive = emissive;
        }


        public void setId(int id)
        {
            this.id = id;
        }


        public void setSheetName(String sheetName)
        {
            this.sheetName = sheetName;
        }


        public void setShininess(int[] shininess)
        {
            this.shininess = shininess;
        }


        public void setSpecular(String[] specular)
        {
            this.specular = specular;
        }


        public void setStyleId(int[] styleId)
        {
            this.styleId = styleId;
        }


        public void setTag(String[] tag)
        {
            this.tag = tag;
        }


        @Override
        public String toString()
        {
            StrBuilder sb = new StrBuilder();

            sb.appendln("ConfigureStylesheetController.CommandObject ");

            for (int i = 0; i < styleId.length; i++)
            {
                sb.appendln(tag[i] + "(" + styleId[i] + ") : a" + ambient[i] + ", d" + diffuse[i] + ", e" + emissive[i]
                            + ", s"
                            + specular[i] + ", sh" + shininess[i]);
            }

            return sb.toString();
        }
    }
}