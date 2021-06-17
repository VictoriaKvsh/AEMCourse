package com.gproject.core.listeners;

import com.gproject.core.utils.ResolverUtil;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.version.VersionManager;

@Component(immediate = true,
        property = {
                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/ADDED",
                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/CHANGED",
                EventConstants.EVENT_FILTER + "=(path=/content/gproject/us/en*)"
        })
public class OSGiEventHandler implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(OSGiEventHandler.class);
    private static final String PAGE_CONTENT_TYPE = "cq:PageContent";
    private static final String JCR_DESCRIPTION_PROPERTY = "jcr:description";

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    public void handleEvent(final Event event) {
        LOG.info("\n Resource event: {} at: {}", event.getTopic(), event.getProperty(SlingConstants.PROPERTY_PATH));
        try (ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory)) {
            Session session = resourceResolver.adaptTo(Session.class);
            String propertyPath = event.getProperty(SlingConstants.PROPERTY_PATH).toString();
            Node node = session.getNode(propertyPath);
            String primaryType = node.getProperty("jcr:primaryType").getString();

            if (primaryType.equals(PAGE_CONTENT_TYPE)) {
                if (!node.hasProperty("mix:versionable")) {
                    node.addMixin("mix:versionable");
                    session.save();
                }
                VersionManager vm = session.getWorkspace().getVersionManager();
                vm.checkin(propertyPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}