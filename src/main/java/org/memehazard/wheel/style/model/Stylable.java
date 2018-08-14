package org.memehazard.wheel.style.model;

import java.util.Map;

public interface Stylable
{
    public Map<String, ? extends Object> getData();


    public String[] getStyleTags();


    public boolean needsStyle();


    public void setStyle(Style style);
}
