package com.gproject.core.service;

import com.day.cq.search.result.Hit;
import org.apache.sling.api.SlingHttpServletRequest;

import javax.jcr.RepositoryException;
import java.util.Iterator;
import java.util.List;

public interface SearchService {

    Iterator<Hit> searchResult(List<String> paths, String text, SlingHttpServletRequest slingHttpServletRequest) throws RepositoryException;

}
