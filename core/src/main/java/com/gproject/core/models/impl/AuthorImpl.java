package com.gproject.core.models.impl;

import com.gproject.core.models.Author;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Author.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class AuthorImpl implements Author {


    @ValueMapValue
    @Default(values = "AEM")
    private String fname;

    @ValueMapValue
    @Default(values = "GEEKS")
    private String lname;


    @Override
    public String getFirstName() {
        return fname;
    }

    @Override
    public String getLastName() {
        return lname;
    }



}