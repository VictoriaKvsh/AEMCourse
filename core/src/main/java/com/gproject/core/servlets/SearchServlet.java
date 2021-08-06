package com.gproject.core.servlets;

import com.gproject.core.service.JsonSearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component(service = Servlet.class)
@SlingServletPaths(
        value = {"/bin/pages"}
)
public class SearchServlet extends SlingSafeMethodsServlet {
    private static final String REQUEST_PARAMETER_PATH = "path";
    private static final String RESPONSE_CONTENT_TYPE = "application/json";

    @Reference
    private JsonSearchService searchService;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
        JSONObject searchResult = null;
        try {
            String searchtext = req.getRequestParameter(REQUEST_PARAMETER_PATH).getString();
            searchResult = searchService.searchResult(searchtext);
            resp.setContentType(RESPONSE_CONTENT_TYPE);
            resp.getWriter().write(searchResult.toString());

        } catch (NullPointerException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }

}