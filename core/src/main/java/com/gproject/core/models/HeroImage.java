package com.gproject.core.models;

import com.gproject.core.models.impl.HeroImageImpl;

public interface HeroImage {

    HeroImageImpl.Image getImage();
    String getTitle();
    String getText();
    String getLink();

}
