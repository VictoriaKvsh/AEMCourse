
"use strict";
use(["/libs/wcm/foundation/components/utils/AuthoringUtils.js"], function (AuthoringUtils) {
    var CONST = {
        PROP_TITLE: "jcr:title",
        PROP_REF_HERO_IMAGE: "fileReference",
        PROP_UPLOAD_HERO_IMAGE: "file",
        PROP_UPPERCASE: "uppercase",
        PROP_TEXT: "text",
        PROP_LINK: "linkURL"
    }

    var hero = {}

    //Get the title
    hero.title = properties.get(CONST.PROP_TITLE)
            || pageProperties.get(CONST.PROP_TITLE)
            || currentPage.name;


    //Get the text
    hero.text = properties.get(CONST.PROP_TEXT);

//Get the link
    hero.link = properties.get(CONST.PROP_LINK);

    //Check for file reference from the DAM
    var image = properties.get(CONST.PROP_REF_HERO_IMAGE, String.class);
    if(image == "undefined"){
    	//Check for file upload
    	var res = resource.getChild("file");
    	if(res != null){
    		image = res.getPath();
    	}
    }

    if(image != "undefined"){
    	hero.style = "background-image:url(" + image + ");";
    }

    //Add uppercase design to hero text
    if(currentStyle.get(CONST.PROP_UPPERCASE)){
    	hero.uppercase = "text-transform:uppercase;";
    }



    return hero;
});