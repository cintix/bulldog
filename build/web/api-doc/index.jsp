<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="bulldog" uri="/WEB-INF/bulldog.tld"%>

<t:genericpage pageTitle="| API DOC" bodyParams="onload=\"displayResult()\" id=\"api-doc\"">
    <jsp:attribute name="htmlHead">
        <link rel="stylesheet" href="css/swadl/screen.css">
        <script src="js/api-doc.js"></script>
    </jsp:attribute>
    <jsp:body>
        <h4>API Endpoints</h4>
        <form id="api_selector">
            <div class="input">
                <input id="input_baseUrl" name="baseUrl" type="hidden">
            </div>
        </form>
        <div id="message" class="error">
        </div>
        <div id="example">
        </div>
        <div id="grammars">
        </div>
    </jsp:body>
</t:genericpage>