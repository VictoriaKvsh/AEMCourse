/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.gproject.core.listeners;

import com.day.cq.wcm.api.PageManager;
import com.gproject.core.utils.ResolverUtil;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.VersionManager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        immediate = true,
        service = ResourceChangeListener.class,
        property = {
                ResourceChangeListener.PATHS + "=/content/gproject/us/en/test11",
                ResourceChangeListener.CHANGES + "=ADDED",
                ResourceChangeListener.CHANGES + "=CHANGED"
        }
)
public class ResourceEventHandler implements ResourceChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceEventHandler.class);

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public void onChange(List<ResourceChange> list) {
        for (ResourceChange resourceChange : list) {
            try (ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory)) {
                Session session = resourceResolver.adaptTo(Session.class);
                Node node = session.getNode(resourceChange.getPath());
                boolean isContainer = resourceChange.getPath().contains("container");

                String primaryType = node.getProperty("jcr:primaryType").getString();

                String description = null;
                description =  node.getProperty("jcr:description").getString();


                LOG.info("\n primaryType 11111111" + primaryType + "description!!!!!" + description);


                if (primaryType.equals("cq:PageContent") && description != null) {


                    if (!node.hasProperty("mix:versionable")) {
                        node.addMixin("mix:versionable");
                    }

                    VersionManager vm = session.getWorkspace().getVersionManager();
                    vm.checkout(resourceChange.getPath());
                    session.save();
                    vm.checkin(resourceChange.getPath());

                    LOG.info("\n Property 11111111" + primaryType + "11111111111111" + "\n Property 11111111" + description + "11111111111111");
                }
            } catch (Exception e) {
                LOG.info("\n Exception : {} ", e.getMessage());
            }
        }
    }
}
