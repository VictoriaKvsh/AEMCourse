var c = parseInt(prompt("Columns"));
var r = parseInt(prompt("Rows"));
var table = document.getElementById("customTable");

for (var i = 0; i < r; i++) {
    var newRow = document.createElement("tr");
    for (var j = 0; j < c; j++) {
        var newColumn = document.createElement("td");
        newRow.appendChild(newColumn);
    }
    table.appendChild(newRow);
}




"use strict";
use(["/libs/wcm/foundation/components/utils/AuthoringUtils.js"], function (AuthoringUtils) {
    var CONST = {
        PROP_COLUMNS: "columns",
        PROP_ROWS: "rows
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