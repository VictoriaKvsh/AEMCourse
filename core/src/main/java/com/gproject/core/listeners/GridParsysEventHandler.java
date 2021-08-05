package com.gproject.core.listeners;

import com.gproject.core.utils.ResolverUtil;
import org.apache.sling.api.SlingConstants;
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
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@Component(immediate = true,
        property = {
                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/CHANGED",
                EventConstants.EVENT_FILTER + "=(resourceType=gproject/components/training/gridparsys)"
        })
public class GridParsysEventHandler implements EventHandler {

    private static final String PROPERTY_ROWS = "rows";
    private static final String PROPERTY_COLUMNS = "columns";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    public void handleEvent(final Event event) {
        try (ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory)) {    // change to resourse
            Session session = resourceResolver.adaptTo(Session.class);
            String propertyPath = event.getProperty(SlingConstants.PROPERTY_PATH).toString();
            Node node = session.getNode(propertyPath);
            int rows = Integer.parseInt(node.getProperty(PROPERTY_ROWS).getString());
            int columns = Integer.parseInt(node.getProperty(PROPERTY_COLUMNS).getString());
            removeNotUsedNodes(node, rows, 0);
            removeNotUsedNodes(node, 0, columns);
            session.save();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeNotUsedNodes(Node node, int rowNum, int columnNum) throws RepositoryException {
        for (int j = 10; j > columnNum; j--) {
            for (int i = 10; i > rowNum; i--) {
                if (node.hasNode(j + "." + i)) {
                    node.getNode(j + "." + i).remove();
                }
            }
        }
    }
}