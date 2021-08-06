package com.gproject.core.models;

import java.util.List;

public interface Survey {

    String getQuestion();

    List<String> getAnswers();

    String getNumber();
}