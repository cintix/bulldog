$(document).foundation();



function buildSpriteView(refId, url, sectionCount, width, height) {
//    var imageWidth = 360;
//    var imageHeight = 322;
    var imageWidth = 357;
    var imageHeight = 299;
    var imageMarginSize = 5;
    var parent = $("#" + refId);

    var columns =  (sectionCount > 1) ? 2 : 1;
    var rows =  Math.ceil(sectionCount / 2);
    
    var scaledHeight = height * rows;
    var scaledWidth = width * columns;    
    
    
    var scaledMarginSize = Math.ceil((width !== imageWidth) ? ((((width - imageWidth) / imageWidth) + 1)  * imageMarginSize) : imageMarginSize );

    if (scaledWidth > width)   scaledWidth += imageMarginSize;
    if (scaledHeight > height) scaledHeight += ((rows - 1) * scaledMarginSize);

    var customTag = $("<div id='" + refId + "_imageChanger'></div>");
    $(customTag).css("width", +width + "px");
    $(customTag).css("height", +height + "px");
    $(customTag).css("display", "block");
    $(customTag).css("background-color", "#000");
    $(customTag).css("background-size", +scaledWidth + "px " + scaledHeight + "px");
    $(customTag).css("background-image", "url(" + url + ")");
    $(customTag).css("background-position", "0px 0px");
    $(customTag).css("background-repeat", "no-repeat");
    $(customTag).appendTo($(parent));
    $("<br/>").appendTo($(parent));


    if (sectionCount > 1) {
        
        var linkTag = $("<a></a>");

        $(linkTag).attr("href", "javascript:changePage('" + refId + "_imageChanger',0,0);");
        $(linkTag).text(" " + (1));
        $(linkTag).appendTo($(parent));

        var top = 0;
        var left = 0;
        for (var index = 1; index < sectionCount; index++) {
            var linkTag = $("<a></a>");
            left += scaledMarginSize + width;

            if (left >= (width * 2)) {
                left = 0;
                top += height + scaledMarginSize;
            }

            $(linkTag).attr("href", "javascript:changePage('" + refId + "_imageChanger'," + top + "," + left + ");");
            $(linkTag).text(" " + (index + 1));
            $(linkTag).appendTo($(parent));
        }
    }

}


function changePage(refId, top, left) {
    $('#' + refId).css("background-position","-" +  left + "px -" + top + "px");
}