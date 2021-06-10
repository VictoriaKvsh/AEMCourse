package com.gproject.core.filters;



import com.adobe.acs.commons.util.ServletOutputStreamWrapper;
import com.day.image.Layer;
import com.gproject.core.service.impl.ImgRender;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;


import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

@Component(service = Filter.class, property = {"service.ranking=-500"})
@SlingServletFilter(scope = {SlingServletFilterScope.REQUEST}, pattern = "/content/we-retail/de/de.*",
        extensions = {"jpg", "jpeg", "png"})
public class SampleSlingFilter implements Filter {

    @Reference
    ImgRender imgRender;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        ImgResponseWrapper wrapper = new ImgResponseWrapper((HttpServletResponse) response);

        chain.doFilter(request, wrapper);
        final Layer layer = imgRender.getLayer(wrapper.getBytes());

        if (layer != null) {
            layer.write(layer.getMimeType(), 1.0, response.getOutputStream());
            response.flushBuffer();
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

        private ByteArrayOutputStream bos = new ByteArrayOutputStream();

        public ImgResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStreamWrapper(bos);
        }

        public byte[] getBytes() {
            return bos.toByteArray();
        }
    }


}
