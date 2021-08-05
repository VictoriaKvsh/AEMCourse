package com.gproject.core.models.impl;

import com.gproject.core.models.HeroImage;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = HeroImage.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroImageImpl implements HeroImage {



    @Self
    private SlingHttpServletRequest request;

    @ScriptVariable
    private ValueMap properties;


    private Image image;

    @PostConstruct
    private void initModel() {

        image = getImage();
    }



    public Image getImage() {
        if (image != null) {
            return image;
        }
        com.adobe.cq.wcm.core.components.models.Image image = request.adaptTo(com.adobe.cq.wcm.core.components.models.Image.class);
        if (image != null) {
            this.image = new Image(image.getSrc());
        }
        return this.image;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public String getLink() {
        return null;
    }

    public class Image {
        private String src;

        public Image(String src) {
            this.src = src;
        }

        public String getSrc() {
            return src;
        }
    }

}