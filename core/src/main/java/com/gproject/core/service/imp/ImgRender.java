package com.gproject.core.service.imp;

import com.day.cq.wcm.foundation.Image;
import com.gproject.core.configuration.FilterConfig;
import com.day.image.Layer;
import com.gproject.core.servlets.ImageServlet;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.JcrConstants;

import com.day.cq.commons.ImageHelper;

import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = ImgRender.class)
@Designate(ocd = FilterConfig.class)
public class ImgRender {

    private String runMode;

       public Layer getLayer(Resource res)
            throws IOException, RepositoryException {
        Layer layer = null;
        Session session = res.getResourceResolver().adaptTo(Session.class);
        Resource contentRes = res.getChild(JcrConstants.JCR_CONTENT);
        Node node = session.getNode(contentRes.getPath());
        Property data = node.getProperty(JcrConstants.JCR_DATA);
        if (data != null) {
            layer = ImageHelper.createLayer(data);
            if (runMode.equals("mode1")) {
                layer.rotate(180);
            } else if (runMode.equals("mode2")) {
                layer.grayscale();
            } else if (runMode.equals("mode3")) {
                layer.rotate(180);
                layer.grayscale();
            }
        }
        return layer;
    }




    public void writeLayer(
            SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException, RepositoryException {

        final ImageServlet.ImageWrapper wrapper = new ImageServlet.ImageWrapper(request);

        final Image image = wrapper.getImage();

        if (!image.hasContent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // get pure layer
        final Layer imageLayer = image.getLayer(false, false, false);

        imageLayer.rotate(180);


//        if (modeType.equals("mode1")) {
//
//            layer.rotate(180);
//        } else if (modeType.equals("mode2")) {
//            layer.grayscale();
//        } else if (modeType.equals("mode3")) {
//            layer.rotate(180);
//            layer.grayscale();
//        }


        // image.rotate(layer);


        response.flushBuffer();
    }


    @Activate
    @Modified
    protected void activate(FilterConfig config) throws Exception {
        runMode = config.runMode();
    }
}