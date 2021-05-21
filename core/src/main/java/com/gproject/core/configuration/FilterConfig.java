package com.gproject.core.configuration;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;


@ObjectClassDefinition(name = "DemoConfig - OSGi Config")
public @interface FilterConfig {
    @AttributeDefinition(
            name = "Run Modes",
            type = AttributeType.STRING)
    String runMode() default "mode1";
}
