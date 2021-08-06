package com.gproject.core.service;

import com.day.image.Layer;
import org.apache.sling.api.resource.ValueMap;

public interface ImageTransformer {

    String PROP_TYPE = "type";

    Layer transform(Layer layer, ValueMap properties);
}