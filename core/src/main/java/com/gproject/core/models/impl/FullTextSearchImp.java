package com.gproject.core.models.impl;

import com.day.cq.search.result.Hit;
import com.gproject.core.models.FullTextSearch;
import com.gproject.core.service.SearchService;
import com.gproject.core.service.impl.QueryBuilderSearch;
import com.gproject.core.service.impl.QueryManagerSearch;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import java.util.*;


@Model(adaptables = SlingHttpServletRequest.class,
        adapters = FullTextSearch.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FullTextSearchImp implements FullTextSearch {

    public Iterator<Hit> result;

    @ValueMapValue
    private List<String> paths = new ArrayList<String>();

    @ValueMapValue
    @Default(values = "successful product")
    String text;

    @ValueMapValue
    @Default(values = "qb")
    String searchapi;

    @Self
    SlingHttpServletRequest slingHttpServletRequest;

    @OSGiService(filter = "(component.name=QB)")
    SearchService searchServiceQB;

    @OSGiService(filter = "(component.name=QOM)")
    SearchService searchServiceQOM;


    @PostConstruct
    public void result() throws RepositoryException {

        if (searchapi.equals("qb")) {
            result = searchServiceQB.searchResult(paths, text, slingHttpServletRequest);
        } else
            result = searchServiceQOM.searchResult(paths, text, slingHttpServletRequest);
    }

    @Override
    public List<String> getPaths() {

            return paths;

    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getSearchAPI() {
        return searchapi;
    }

}
