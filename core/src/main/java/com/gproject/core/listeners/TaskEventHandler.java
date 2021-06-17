package com.gproject.core.listeners;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

//@Component(immediate = true,
//        property = {
//                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/REMOVED",
//                EventConstants.EVENT_FILTER + "=(path=/content/gproject/us/en/*)"
//        })
public class TaskEventHandler implements EventHandler {


    @Override
    public void handleEvent(final Event event) {



    }
}