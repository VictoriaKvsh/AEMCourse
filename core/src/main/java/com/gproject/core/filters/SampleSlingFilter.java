package com.gproject.core.filters;


import com.day.cq.commons.ImageResource;
import com.day.cq.wcm.foundation.Image;
import com.gproject.core.service.imp.ImgRender;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import javax.jcr.RepositoryException;
import javax.servlet.*;
import javax.servlet.http.*;



@Component(service = Filter.class)
@SlingServletFilter(scope = {SlingServletFilterScope.COMPONENT},
        pattern = "/content/we-retail/fr/fr.*")
@ServiceRanking(500)
@ServiceDescription("Filter incoming img")
public class SampleSlingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SampleSlingFilter.class.getName());

    @Reference
    ImgRender imgRender;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
        SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        ImgResponseWrapper wrapper = new ImgResponseWrapper(slingResponse);

        final Resource resource = slingRequest.getResource();

        // to proceed with the rest of the Filter chain
        chain.doFilter(request, wrapper);

        try {
            imgRender.getLayer(resource);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Deactivate
    protected void deactivate(ComponentContext ctx) {

    }


    class ImgResponseWrapper extends HttpServletResponseWrapper {

        public ImgResponseWrapper(HttpServletResponse response) {
            super(response);
        }


    }
}
