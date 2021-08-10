package com.gproject.core.models.impl;

import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.gproject.core.models.ExtendedBreadcrumbModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = ExtendedBreadcrumbModel.class)
public class ExtendedBreadcrumbModelImpl implements ExtendedBreadcrumbModel {

    @RequestAttribute(name = "requestAttributes")
    private Collection<NavigationItem> collection;

    public Collection<NavigationItem> getItems() {
        List<NavigationItem> items = new ArrayList<>(collection);
        Collections.reverse(items);
        return Collections.unmodifiableList(items);
    }
}
