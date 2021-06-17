package com.gproject.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(name = "Image Transform Config")
public @interface ImageTransformConfig {
    static final String RUN_MODE_1 = "mode1";

    @AttributeDefinition(
            name = "Run Modes",
            type = AttributeType.STRING)
    String runMode() default RUN_MODE_1;
}
