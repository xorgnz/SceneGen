package org.memehazard.wheel.style.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.style.dao.StyleDAO;
import org.memehazard.wheel.style.dao.StylesheetDAO;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.model.Stylesheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StyleFacade
{
    @Autowired
    private StyleDAO      dao_style;
    @Autowired
    private StylesheetDAO dao_stylesheet;


    public void addStylesheet(Stylesheet obj)
    {
        dao_stylesheet.add(obj);
    }


    public void deleteStyle(int id)
    {
        dao_style.delete(id);
    }


    public void deleteStylesheet(int id)
    {
        // Delete Stylesheet
        dao_stylesheet.delete(id);
    }


    public Stylesheet getStylesheet(int id)
    {
        return dao_stylesheet.get(id);
    }


    public List<Stylesheet> listStylesheets()
    {
        return dao_stylesheet.listAll();
    }


    public void updateStylesheet(Stylesheet obj)
    {
        dao_stylesheet.update(obj);
    }


    // TODO - Test StyleFacade.configureStylesheet
    public void configureStylesheet(Stylesheet ssheet, List<Style> styles)
    {
        // Grab styles from DB
        List<Style> dbStyles = dao_style.listByStylesheet(ssheet.getId());
        Map<Integer, Style> dbStyleMap = new HashMap<Integer, Style>();
        for (Style style : dbStyles)
            dbStyleMap.put(style.getId(), style);

        // Sort through submitted style to make adjustments
        for (Style style : styles)
        {
            // Assign to given stylesheet
            style.setStylesheet(ssheet);

            // Find equivalent style from database
            // If not found, add.
            // If found, update
            Style style_db = dbStyleMap.get(style.getId());
            if (style_db == null)
            {
                dao_style.add(style);
            }
            else
            {
                // Copy newly submitted style over DB version
                style_db.copy(style);
                dao_style.update(style_db);

                // Remove from db map (to prevent deletion)
                dbStyleMap.remove(style_db.getId());
            }
        }

        // Remove all db styles not submitted (they are considered deleted)
        for (Integer i : dbStyleMap.keySet())
            dao_style.delete(i);
    }


    public List<Style> listStylesByStylesheet(int ssheetId)
    {
        return dao_style.listByStylesheet(ssheetId);
    }
}
