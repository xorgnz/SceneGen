package org.memehazard.wheel.style.dao;

import java.util.List;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.style.model.Stylesheet;


public interface StylesheetDAO extends MyBatisDAO
{
    public void add(Stylesheet obj);


    public void delete(int id);


    public Stylesheet get(int id);


    public List<Stylesheet> listAll();


    public void update(Stylesheet obj);
}
