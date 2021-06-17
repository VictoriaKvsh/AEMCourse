package com.gproject.core.listeners;

import com.day.cq.commons.jcr.JcrUtil;
import com.gproject.core.utils.ResolverUtil;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;

@Component(immediate = true,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + JobEventHandler.JOB_CONSUMER_TOPIC
        })
public class TaskJobConsumer implements JobConsumer {
    private static final String PATH_TO_ADD = "/var/log/removedProperties/deletedNode";
    private static final Logger LOG = LoggerFactory.getLogger(TaskJobConsumer.class);
    private static final String DELETED_NODE_PROPERTY_PATH = "propertyPath";
    private static final String DELETED_NODE_PROPERTY_NAME = "propertyName";
    private static final String INTERMEDIATE_NODE_TYPE = "sling:Folder";
    private static final String NEW_NODE_TYPE = "nt:unstructured";


    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public JobResult process(Job job) {
        try (ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory)) {
            Session session = resourceResolver.adaptTo(Session.class);
            String deletedNodePath = (String) job.getProperty(SlingConstants.PROPERTY_PATH);
            Node newNode = JcrUtil.createPath(PATH_TO_ADD, true, INTERMEDIATE_NODE_TYPE, NEW_NODE_TYPE, session, false);
            newNode.setProperty(DELETED_NODE_PROPERTY_PATH, deletedNodePath);
            newNode.setProperty(DELETED_NODE_PROPERTY_NAME, deletedNodePath.substring(deletedNodePath.indexOf("jcr:content")+12));

            session.save();
            LOG.info("\n Job executing for  : {} ", deletedNodePath);
            return JobResult.OK;
        } catch (Exception e) {
            LOG.info("\n Error in Job Consumer : {}  ", e.getMessage());
            return JobResult.FAILED;
        }
    }
}