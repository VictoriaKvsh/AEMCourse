package com.gproject.core.models.impl;

import com.gproject.core.models.GridParsys;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = GridParsys.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GridParsysImpl implements GridParsys {

    public List<String> rowsNumber;

    public List<String> colNumber;

    @ValueMapValue
    @Default(values = "1")
    String rows;

    @ValueMapValue
    @Default(values = "1")
    String columns;


    @PostConstruct
    public void init() {
        int intRows = Integer.parseInt(rows);
        int intColumns = Integer.parseInt(columns);
        rowsNumber = new ArrayList<String>();
        colNumber = new ArrayList<String>();
        for (int i = 1; i <= intRows; i++) {
            rowsNumber.add(rows);
        }
        for (int i = 1; i <= intColumns; i++) {
            colNumber.add(columns);
        }
    }

    @Override
    public String getColumns() {
        return columns;
    }

    @Override
    public String getRows() {
        return rows;
    }
}
