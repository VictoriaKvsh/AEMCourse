package com.gproject.core.models;

import java.util.List;

public interface FullTextSearch {
    List<String> getPaths ();
    String getText();
    String getSearchAPI();

}