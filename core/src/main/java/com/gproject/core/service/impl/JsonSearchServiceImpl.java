package com.gproject.core.service.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.gproject.core.service.JsonSearchService;
import com.gproject.core.utils.ResolverUtil;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = JsonSearchService.class, immediate = true)
public class JsonSearchServiceImpl implements JsonSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(JsonSearchServiceImpl.class);

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Activate
    public void activate() {
    }

    private Map<String, String> createTextSearchQuery(String path) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", path);
        queryMap.put("type", "myTestType");
        return queryMap;
    }

    @Override
    public JSONObject searchResult(String searchPath) {
        JSONObject searchResult = new JSONObject();
        try {
            ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            final Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(createTextSearchQuery(searchPath)), session);
            SearchResult result = query.getResult();

            List<Hit> hits = result.getHits();
            JSONArray resultArray = new JSONArray();
            for (Hit hit : hits) {
                Page page = hit.getResource().adaptTo(Page.class);
                JSONObject resultObject = new JSONObject();
                resultObject.put("title", page.getTitle());
                resultObject.put("path", page.getPath());
                resultArray.put(resultObject);
            }
            searchResult.put("results", resultArray);

        } catch (Exception e) {
            LOG.info("\n ----ERROR -----{} ", e.getMessage());
        }
        return searchResult;
    }
}