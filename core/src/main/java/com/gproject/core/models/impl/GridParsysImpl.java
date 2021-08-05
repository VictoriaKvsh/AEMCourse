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
        adapters = GridParsys.class)
public class GridParsysImpl implements GridParsys {

    public List<String> rowsNumber;

    public List<String> colNumber;

    @ValueMapValue
    @Default(values = "1")
    private int rows;

    @ValueMapValue
    @Default(values = "1")
    private int columns;


    @PostConstruct
    public void init() {
        rowsNumber = new ArrayList<>(getList(rows));
        colNumber = new ArrayList<>(getList(columns));

    }

    private ArrayList getList(int numberOfFields) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= numberOfFields; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public int getRows() {
        return rows;
    }
}
