package org.memehazard.wheel.textCompletion.facade;

import java.util.List;

import org.memehazard.wheel.textCompletion.dao.TextCompletionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextCompletionFacade
{
    private static final int DEFAULT_COMPLETIONS_RETURNED = 10;

    @Autowired
    public TextCompletionDAO dao;


    public List<String> listCompletions(String prefix)
    {
        return listCompletions(prefix, DEFAULT_COMPLETIONS_RETURNED);
    }


    public List<String> listCompletions(String prefix, int limit)
    {
        return dao.listCompletions(prefix, limit);
    }
    
    public List<String> listCompletionsWithPrefix(String prefix)
    {
        return listCompletions(prefix, DEFAULT_COMPLETIONS_RETURNED);
    }


    public List<String> listCompletionsWithPrefix(String prefix, int limit)
    {
        return dao.listCompletionsWithPrefix(prefix, limit);
    }
}
