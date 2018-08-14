package org.memehazard.wheel.style.dao;

import java.util.List;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.style.model.Style;


public interface StyleDAO extends MyBatisDAO
{
    public void add(Style obj);


    public void delete(int id);


    public Style get(int id);


    public List<Style> listAll();


    public List<Style> listByStylesheet(int id);


    public void update(Style obj);
}
