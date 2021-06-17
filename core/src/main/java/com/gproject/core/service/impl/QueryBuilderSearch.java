package com.gproject.core.service.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.gproject.core.service.SearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component(service = SearchService.class, immediate = true, name = "QB")
public class QueryBuilderSearch implements SearchService {

    @Reference
    QueryBuilder queryBuilder;

    @Override
    public Iterator<Hit> searchResult(List<String> paths, String text, SlingHttpServletRequest slingHttpServletRequest) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("fulltext", text);
        for (int i = 0; i < paths.size(); i++) {
            queryMap.put("group." + (i + 1) + "_path", paths.get(i));
        }
        queryMap.put("group.p.or", "true");
        Session session = slingHttpServletRequest.getResourceResolver().adaptTo(Session.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
        Iterator<Hit> result = query.getResult().getHits().iterator();
        return result;
    }
}
