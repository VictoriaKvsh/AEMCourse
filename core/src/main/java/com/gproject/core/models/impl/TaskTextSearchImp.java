package com.gproject.core.models.impl;

import com.day.cq.search.result.Hit;
import com.gproject.core.models.TaskTextSearch;
import com.gproject.core.service.SearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Model(adaptables = SlingHttpServletRequest.class,
        adapters = TaskTextSearch.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TaskTextSearchImp implements TaskTextSearch {

    public Iterator<Hit> result;

    @ValueMapValue
    String text;

    @ValueMapValue
    String searchengine;

    @Self
    SlingHttpServletRequest slingHttpServletRequest;

    @OSGiService(filter = "(component.name=QB)")
    SearchService searchServiceQB;

    @OSGiService(filter = "(component.name=QOM)")
    SearchService searchServiceQOM;

//    @PostConstruct
//    public void result() throws RepositoryException {
//        if (searchengine.equals("sql")) {
//            result  ;
//        } else
//            result  ;
//    }


    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getSearchAPI() {
        return searchengine;
    }

}
