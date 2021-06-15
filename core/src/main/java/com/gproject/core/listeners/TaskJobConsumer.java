package com.gproject.core.listeners;

import com.day.cq.commons.jcr.JcrUtil;
import com.gproject.core.utils.ResolverUtil;
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

@Component(service = JobConsumer.class,
        immediate = true,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=task/job"
        })
public class TaskJobConsumer implements JobConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(TaskJobConsumer.class);

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public JobResult process(Job job) {
        try (ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory)){

            Session session = resourceResolver.adaptTo(Session.class);

            String propertyPath = (String) job.getProperty("path");

            String newPath = "/var/log/removedProperties/deletedNode";

            Node feedNode = JcrUtil.createPath(newPath, true, "sling:OrderedFolder", "cq:Page", session, false);
            Node dataNode = feedNode.addNode("jcr:content", "cq:PageContent");
            dataNode.setProperty("propertyPath",propertyPath);
            dataNode.setProperty("propertyName",propertyPath.substring(66));


            session.save();



            LOG.info("\n Job executing for  : {} ", propertyPath);
            return JobResult.OK;
        } catch (Exception e) {
            LOG.info("\n Error in Job Consumer : {}  ", e.getMessage());
            return JobResult.FAILED;
        }
    }
}