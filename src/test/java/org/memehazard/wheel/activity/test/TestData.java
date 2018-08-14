package org.memehazard.wheel.activity.test;

import org.memehazard.wheel.activity.model.ActivityInstance;
import org.memehazard.wheel.activity.model.ActivityTemplate;
import org.memehazard.wheel.activity.model.Parameter;
import org.memehazard.wheel.activity.model.Exercise;
import org.memehazard.wheel.tutoring.model.Curriculum;

public class TestData
{
    public static final String   C_CREATOR_NAME = "Creator Name";
    public static final String   C_DESCRIPTION  = "Description";
    public static final String[] C_NAME         = { "Name 3", "Name 2", "Name 1" };

    public static final String[] AT_NAME        = { "AT Name 0", "AT Name 1", "AT Name 2", "AT Name 3", "AT Name 4", "AT Name 5" };
    public static final String[] AT_DESCRIPTION = { "AT Desc 0", "AT Desc 1", "AT Desc 2", "AT Desc 3", "AT Desc 4", "AT Desc 5" };
    public static final String[] AT_URL_PLAY    = { "AT playURL 0", "AT playURL 1", "AT playURL 2", "AT playURL 3", "AT playURL 4", "AT playURL 5" };
    public static final String[] AT_URL_FACTS   = { "AT factURL 0", "AT factURL 1", "AT factURL 2", "AT factURL 3", "AT factURL 4", "AT factURL 5" };

    public static final String[] ATP_LABEL      = { "ATP Label 0", "ATP Label 1", "ATP Label 2", "ATP Label 3", "ATP Label 4", "ATP Label 5" };
    public static final String[] ATP_VARIABLE   = { "ATP_var_0", "ATP_var_1", "ATP_var_2", "ATP_var_3", "ATP_var_4", "ATP_var_5" };
    public static final String[] ATP_TYPE       = { "Type0", "Type1", "Type2", "Type3", "Type4", "Type5" };

    public static final String[] AI_NAME        = { "AI Name 0", "AI Name 1", "AI Name 2", "AI Name 3", "AI Name 4", "AI Name 5" };
    public static final String[] AI_DESCRIPTION = { "AI Desc 0", "AI Desc 1", "AI Desc 2", "AI Desc 3", "AI Desc 4", "AI Desc 5" };
    public static final String[] AI_PARAM_STR   = { "param=foo0", "param=foo1", "param=foo2", "param=foo3", "param=foo4", "param=foo5", };


    public static ActivityInstance activityInstance(int id, ActivityTemplate gt)
    {
        return new ActivityInstance(AI_NAME[id], AI_DESCRIPTION[id], gt, AI_PARAM_STR[id]);
    }


    public static ActivityTemplate activityTemplate(int id)
    {
        return new ActivityTemplate(AT_NAME[id], AT_DESCRIPTION[0], AT_URL_PLAY[0], AT_URL_FACTS[0]);
    }


    public static Parameter activityTemplateParameter(int id)
    {
        return new Parameter(ATP_VARIABLE[id], ATP_LABEL[id], ATP_TYPE[id]);
    }


    public static Curriculum curriculum(int id)
    {
        return new Curriculum(C_NAME[id], C_CREATOR_NAME, C_DESCRIPTION);
    }


    public static void update(ActivityTemplate at, int id)
    {
        at.setName(AT_NAME[id]);
        at.setDescription(AT_DESCRIPTION[id]);
        at.setPlayUrl(AT_URL_PLAY[id]);
        at.setFactsUrl(AT_URL_FACTS[id]);
    }


    public static void update(Parameter atp, int id)
    {
        atp.setLabel(ATP_LABEL[id]);
        atp.setVariable(ATP_VARIABLE[id]);
        atp.setType(ATP_TYPE[id]);
    }


    public static void update(ActivityInstance ai, int id, ActivityTemplate gt)
    {
        ai.setName(AI_NAME[id]);
        ai.setDescription(AI_DESCRIPTION[id]);
        ai.setParameterValueString(AI_PARAM_STR[id]);
        ai.setTemplate(gt);
    }


    public static void update(Exercise ex, ActivityInstance ai, Curriculum c)
    {
        ex.setActivityInstance(ai);
        ex.setCurriculum(c);
    }
}
