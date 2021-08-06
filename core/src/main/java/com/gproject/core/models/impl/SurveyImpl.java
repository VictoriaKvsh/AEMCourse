package com.gproject.core.models.impl;

import com.gproject.core.models.Survey;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;


@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Survey.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SurveyImpl implements Survey {

    private static final Logger LOG = LoggerFactory.getLogger(SurveyImpl.class);
    private static final String ALL_ANSWERS = "allanswers";
    private static final String ANSWER = "answer";

    @Inject
    private Resource componentResource;

    @ValueMapValue
    private String question;

    @ValueMapValue
    @Default(values = "1")
    private int numberofanswers;

    @Override
    public String getQuestion() {
        return question;
    }

    @Override
    public String getNumber() {
        switch (numberofanswers) {
            case 1:
                return "One answer";
            case 2:
                return "Two answers";
            case 3:
                return "Three answers";
            case 4:
                return "Four answers";
            case 5:
                return "Five answer";
            case 6:
                return "Six answers";
            default:
                return "One answer";
        }
    }

    @Override
    public List<String> getAnswers() {
        List<String> bookDetailsMap = new ArrayList<>();
        try {
            Resource bookDetail = componentResource.getChild(ALL_ANSWERS);
            if (bookDetail != null) {
                for (Resource book : bookDetail.getChildren()) {
                    bookDetailsMap.add(book.getValueMap().get(ANSWER, String.class));
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return bookDetailsMap;
    }

}
