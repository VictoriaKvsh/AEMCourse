package com.gproject.core.service.impl;

import com.gproject.core.service.SearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.jcr.query.QueryManager;
import javax.jcr.query.RowIterator;
import javax.jcr.query.qom.QueryObjectModel;
import javax.jcr.query.qom.QueryObjectModelFactory;
import java.util.List;


@Component(service = SearchService.class, immediate = true, name = "QOM")
public class QueryManagerSearch implements SearchService {
    private static final String SELECTOR_NAME = "s";
    private static final String NODE_TYPE_NAME = "nt:base";

    @Override
    public RowIterator searchResult(List<String> paths, String text, SlingHttpServletRequest slingHttpServletRequest) throws RepositoryException {

        Session session = slingHttpServletRequest.getResourceResolver().adaptTo(Session.class);
        QueryManager qm = session.getWorkspace().getQueryManager();
        QueryObjectModelFactory qf = qm.getQOMFactory();
        ValueFactory vf = session.getValueFactory();
        QueryObjectModel query = null;

        if (paths.size() == 1) {
            query = qf.createQuery(
                    qf.selector(NODE_TYPE_NAME, SELECTOR_NAME),
                    qf.and(
                            qf.fullTextSearch(SELECTOR_NAME, null,
                                    qf.literal(vf.createValue(text))),
                            qf.descendantNode(SELECTOR_NAME, paths.get(0))),
                    null,
                    null
            );
        } else if (paths.size() == 2) {
            query = qf.createQuery(
                    qf.selector(NODE_TYPE_NAME, SELECTOR_NAME),
                    qf.and(
                            qf.fullTextSearch(SELECTOR_NAME, null,
                                    qf.literal(vf.createValue(text))),
                            qf.or(qf.descendantNode(SELECTOR_NAME, paths.get(0)),
                                    qf.descendantNode(SELECTOR_NAME, paths.get(1)))
                    ),
                    null,
                    null
            );
        } else {
            query = qf.createQuery(
                    qf.selector(NODE_TYPE_NAME, SELECTOR_NAME),
                    qf.fullTextSearch(SELECTOR_NAME, null,
                            qf.literal(vf.createValue(text))),
                    null,
                    null
            );
        }
        RowIterator result = query.execute().getRows();
        return result;
    }
}
