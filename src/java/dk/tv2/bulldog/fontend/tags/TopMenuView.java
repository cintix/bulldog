package dk.tv2.bulldog.fontend.tags;

import dk.tv2.bulldog.backend.io.Configuration;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * @author hjep
 */
public class TopMenuView extends SimpleTagSupport {

    private final StringWriter stringWriter = new StringWriter();
    private String applicationRoot;
    private String requestPage;
    private Map<String, String> breadcrumbs;

    @Override
    public void setJspContext(JspContext pc) {
        super.setJspContext(pc); //To change body of generated methods, choose Tools | Templates.
        PageContext context = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();

        applicationRoot = context.getServletContext().getContextPath();
        requestPage = request.getRequestURI();
        breadcrumbs = buildBreadcrumbs(request);
    }

    @Override
    public void doTag() throws JspException, IOException {
        PageContext context = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();

        boolean signedIn = Configuration.isUserSignedIn(request);
        signedIn = true;

        stringWriter.append("<div class=\"title-bar\" data-responsive-toggle=\"main-menu\" data-hide-for=\"medium\">\n"
                + "            <button class=\"menu-icon\" type=\"button\" data-toggle></button>\n"
                + "            <div class=\"title-bar-title\">Menu</div>\n"
                + "        </div>\n"
                + "\n"
                + "        <div class=\"top-bar\" id=\"main-menu\">\n"
                + "            <div class=\"top-bar-left\">\n"
                + "                <ul class=\"dropdown menu\" data-dropdown-menu>\n"
                + "                    <li class=\"menu-text\" style=\"width: 400px;\"><img src=\"img/tv2_n_RGB.svg\" >&nbsp;<img src=\"img/bulldog.png\">  Bulldog Backend </li>\n"
                + "                </ul>\n"
                + "            </div>\n"
                + "            <div class=\"top-bar-right\">\n"
                + "                <ul class=\"menu\" data-responsive-menu=\"drilldown medium-dropdown\">\n");
        if (signedIn) {
//            stringWriter.append("<li><a class=\"user-name\"><i class=\"fi-torso\"></i> ").append(Configuration.getSignedInUserFullname(request)).append("</a></li>");
        }

        stringWriter.append("                    <li><a href=\"" + applicationRoot + "\">Home</a></li>\n");
        if (signedIn) {
            stringWriter.append(
                    "                    <li class=\"has-submenu\">\n"
                    + "                        <a href=\"javascript:;\">Settings</a>\n"
                    + "                        <ul class=\"submenu menu vertical\" data-submenu>\n"
                    + "                            <li><a href=\"settings/clients\">Clients</a></li>\n"
                    + "                            <li><a href=\"settings/mapping\">Mapping</a></li>\n"
                    + "                        </ul>\n"
                    + "                    </li>");
        }
        stringWriter.append(
                "                    <li><a href=\"api-doc/\">API doc</a></li>\n"
                + "                </ul>\n"
                + "            </div>\n"
                + "        </div>\n");

        stringWriter.append("<div class=\"row\"><nav class=\"breadcrumbs\" role=\"menubar\" aria-label=\"breadcrumbs\">\n");
        int breadcrumbCount = breadcrumbs.size();
        int breadcrumbNr = 0;
        for (String breadcrumb : breadcrumbs.keySet()) {
            breadcrumbNr++;
            if (breadcrumbNr == breadcrumbCount) {
                stringWriter.append("  <li role=\"menuitem\" class=\"current\"><span class=\"show-for-sr\">Current: </span>" + breadcrumb + "</span></li>\n");
            } else {
                stringWriter.append("  <li role=\"menuitem\"><a href=\"" + breadcrumbs.get(breadcrumb) + "\">" + breadcrumb + "</a></li>\n");
            }

        }
        stringWriter.append("</nav>\n</div>\n");
        JspWriter out = getJspContext().getOut();
        out.print(stringWriter.toString());
    }

    private Map<String, String> buildBreadcrumbs(HttpServletRequest request) {
        Map<String, String> links = new LinkedHashMap<>();
        Map<String, String> requestParams = new TreeMap<>();
        String filename = new File(request.getRequestURI()).getName();
        if (request.getQueryString() == null) {
            return links;
        }
        String[] requestMap = request.getQueryString().split("&");

        // read values from request
        for (String queryParam : requestMap) {
            String[] requestPart = queryParam.split("=");
            requestParams.put(requestPart[0], requestPart[1]);
        }
        switch (filename) {
            case "candidat.jsp":
                links.put("Election", applicationRoot + "/election/" + requestParams.get("__valg"));
                links.put("Party", applicationRoot + "/election/" + requestParams.get("__valg") + "/party/" + requestParams.get("__parti"));
                links.put("Candidat", "#");
                break;
            case "party.jsp":
                links.put("Election", applicationRoot + "/election/" + requestParams.get("__valg"));
                links.put("Party", "#");
                break;
            case "greaterconstituency.jsp":
                links.put("Election", applicationRoot + "/election/" + requestParams.get("__valg"));
                links.put("Greater Constituency", "#");
                break;
            case "election.jsp":
                links.put("Election", "#");
                break;
        }

        return links;
    }

}
