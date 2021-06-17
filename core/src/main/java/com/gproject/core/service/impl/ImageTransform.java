package com.gproject.core.service.impl;

import com.day.image.Layer;
import com.gproject.core.configuration.ImageTransformConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = ImageTransform.class)
@Designate(ocd = ImageTransformConfig.class)
public class ImageTransform {
    private static final String RUN_MODE_1 = "mode1";
    private static final String RUN_MODE_2 = "mode2";
    private static final String RUN_MODE_3 = "mode3";
    private static final int DEGREES = 180;
    private String runMode;

    public Layer getLayer(byte[] bytes) {
        try {
            final Layer layer = new Layer(new ByteArrayInputStream(bytes));

            if (runMode.equals(RUN_MODE_1)) {
                layer.grayscale();
            } else if (runMode.equals(RUN_MODE_2)) {
                layer.rotate(DEGREES);
            } else if (runMode.equals(RUN_MODE_3)) {
                layer.rotate(DEGREES);
                layer.grayscale();
            }
            return layer;
        } catch (IOException e) {
            return null;
        }
    }

    @Activate
    @Modified
    protected void activate(ImageTransformConfig config) throws Exception {
        runMode = config.runMode();
    }
}