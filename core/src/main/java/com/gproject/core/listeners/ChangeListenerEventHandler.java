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

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.gproject.core.utils.ResolverUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.VersionManager;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component(
//        immediate = true,
//        service = ResourceChangeListener.class,
//        property = {
//                ResourceChangeListener.PATHS + "=/content/gproject/us/en",
//                ResourceChangeListener.CHANGES + "=ADDED",
//                ResourceChangeListener.CHANGES + "=CHANGED"
//        }
//)
public class ChangeListenerEventHandler implements ResourceChangeListener {
    private static final String PAGE_CONTENT_TYPE = "cq:PageContent";
    private static final String JCR_DESCRIPTION_PROPERTY = "jcr:description";
    private static final Logger LOG = LoggerFactory.getLogger(ChangeListenerEventHandler.class);
    private static final String MIXIN_TYPE = "jcr:mixinTypes";
    private static final String MIXIN_NAME = "mix:versionable";

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public void onChange(List<ResourceChange> list) {
        for (ResourceChange resourceChange : list) {
            try (ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory)) {
                LOG.info("\n Event : {} , Resource : {} ", resourceChange.getType(), resourceChange.getPath());
                Session session = resourceResolver.adaptTo(Session.class);
                PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                Node node = session.getNode(resourceChange.getPath());

                String primaryType = node.getProperty("jcr:primaryType").getString();
                LOG.info("\n primaryType 11111111" + primaryType);

                if (primaryType.equals(PAGE_CONTENT_TYPE) && node.hasProperty(JCR_DESCRIPTION_PROPERTY)) {

                    if (!node.hasProperty(MIXIN_TYPE)) {
                        node.addMixin(MIXIN_NAME);
                        session.save();
                    }
                }

                if (!primaryType.equals(PAGE_CONTENT_TYPE)) {
                    Optional.ofNullable(pageManager.getContainingPage(resourceChange.getPath()))
                            .map(Page::getContentResource)
                            .map(Resource::getPath)
                            .ifPresent(path -> { // переменная Path

                                try {
                                    VersionManager vm = null;
                                    vm = session.getWorkspace().getVersionManager();
                                    vm.checkin(path);
                                    session.logout();
                                } catch (RepositoryException e) {
                                    e.printStackTrace();
                                }
                            });
                }
                LOG.info("\n Property 11111111" + primaryType + "1111111111111111111111111111");

            } catch (Exception e) {
                LOG.info("\n Exception : {} ", e.getMessage());
            }
        }
    }
}
