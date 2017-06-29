<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@taglib prefix="bulldog" uri="/WEB-INF/bulldog.tld"%>
<%@tag trimDirectiveWhitespaces="true" %>
<%@attribute name="pageTitle" required="true"%>
<%@attribute name="bodyParams" required="false"%>
<%@attribute name="htmlHead" fragment="true" %>
<%@attribute name="footerJSScripts" fragment="true" %>
<!doctype html>
<html class="no-js" lang="en" dir="ltr">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <bulldog:Base />
        <title>Bulldog Backend ${pageTitle}</title>
        <link rel="icon" type="image/png" href="img/favicon/favicon-32x32.png" sizes="32x32">
        <link rel="icon" type="image/png" href="img/favicon/favicon-96x96.png" sizes="96x96">
        <link rel="icon" type="image/png" href="img/favicon/favicon-16x16.png" sizes="16x16">
        <link rel="stylesheet" href="css/foundation.css">
        <link rel="stylesheet" href="css/foundation-icons.css" />
        <link rel="stylesheet" href="css/app.css">
        <jsp:invoke fragment="htmlHead"/>
    </head>
    <body ${bodyParams}>
        <bulldog:TopMenuView />

        <div class="row">
            <div class="medium-12 columns">
                <jsp:doBody/>
            </div>
        </div>

        <script src="js/vendor/jquery.js"></script>
        <script src="js/vendor/what-input.js"></script>
        <script src="js/vendor/foundation.js"></script>
        <script src="js/app.js"></script>
        <jsp:invoke fragment="footerJSScripts"/>
       
    </body>
</html>