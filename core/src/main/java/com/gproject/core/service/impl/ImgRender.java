package com.gproject.core.service.impl;

import com.gproject.core.configuration.FilterConfig;
import com.day.image.Layer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = ImgRender.class)
@Designate(ocd = FilterConfig.class)
public class ImgRender {

    private String runMode;


    public Layer getLayer(byte[] bytes) {
        try {
            final Layer layer = new Layer(new ByteArrayInputStream(bytes));

            if (runMode.equals("mode1")) {
                layer.grayscale();
            } else if (runMode.equals("mode2")) {
                layer.rotate(180);
            } else if (runMode.equals("mode3")) {
                layer.rotate(180);
                layer.grayscale();
            }
            return layer;
        } catch (IOException e) {
            return null;
        }


    }

    @Activate
    @Modified
    protected void activate(FilterConfig config) throws Exception {
        runMode = config.runMode();
    }
}