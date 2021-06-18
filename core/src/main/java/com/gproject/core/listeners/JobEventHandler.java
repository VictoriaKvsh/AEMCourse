package com.gproject.core.listeners;

import com.gproject.core.utils.ResolverUtil;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.util.HashMap;
import java.util.Map;


//@Component(immediate = true)
public class JobEventHandler implements EventListener {

    private static final Logger log = LoggerFactory.getLogger(JobEventHandler.class);
    private static final String PROPERTY_PATH = "/content/gproject/us/en";
    public static final String JOB_CONSUMER_TOPIC = "task/job";
    private Session session;

    @Reference
    SlingRepository slingRepository;
    @Reference
    private JobManager jobManager;

    @Activate
    public void activate() throws Exception {
        try {
            session = slingRepository.loginService(ResolverUtil.PROJECT_USER, null);
            session.getWorkspace().getObservationManager().addEventListener(
                    this,
                    Event.PROPERTY_REMOVED,
                    PROPERTY_PATH,
                    true,
                    null,
                    null,
                    false);

        } catch (RepositoryException e) {
            log.info(" \n Error while adding Event Listener : {} ", e.getMessage());
        }
    }

    public void onEvent(EventIterator eventIterator) {
        try {
            while (eventIterator.hasNext()) {
                Map<String, Object> jobProperties = new HashMap<>();
                jobProperties.put(SlingConstants.PROPERTY_PATH, eventIterator.nextEvent().getPath());
                jobManager.addJob(JOB_CONSUMER_TOPIC, jobProperties);
            }
        } catch (Exception e) {
            log.error("\n Error while processing events : {} ", e.getMessage());
        }
    }
}