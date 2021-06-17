package com.gproject.core.service;

import org.osgi.annotation.versioning.ConsumerType;

import com.day.image.Layer;

import org.apache.sling.api.resource.ValueMap;

@ConsumerType
@SuppressWarnings("squid:S1214")
public interface ImageTransformer {
    /**
     * OSGi Property used to identify the ImageTransformer.
     */
    String PROP_TYPE = "type";

    /**
     * Transform the provided layer using the transformation parameters provided in properties.
     *
     * @param layer the image layer to transform
     * @param properties transformation parameters
     * @return the transformed layer; or if layer could not be transformed (invalid properties) the layer unmodified
     */
    Layer transform(Layer layer, ValueMap properties);
}