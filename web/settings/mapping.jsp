<%@page import="java.util.Date"%>
<%@page import="dk.tv2.bulldog.backend.io.Actions"%>
<%@page import="dk.tv2.bulldog.backend.db.entities.ClientMapping"%>
<%@page import="dk.tv2.bulldog.backend.db.EntityManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="bulldog" uri="/WEB-INF/bulldog.tld"%>

<%
    int mappingId = 0;
    String mappingName = "";
    String formHeadline = "Create new mapping";
    String formInfoText = "";
    String mappingUrl = "";
    String mappingPath = "";
    String mappingPattern = "";
    int mappingActions = 0;
    int clientId = 0;

    if (request.getParameter("__mapping_id") != null) {
        mappingId = Integer.parseInt(request.getParameter("__mapping_id"));
        ClientMapping mapping = EntityManager.create(ClientMapping.class).load(mappingId);
        if (mapping != null) {
            mappingName = mapping.getName();
            mappingUrl = mapping.getUrl();
            mappingPath = mapping.getPath();
            mappingPattern = mapping.getPattern();
            mappingActions = mapping.getActions();
            
            clientId = mapping.getClientId();
            formHeadline = "Edit mapping";
        } else {
            formInfoText = "<div class=\"callout alert\"><h5>Not Found</h5><p>The mapping with ID " + mappingId + " was not found!</p></div>";
            mappingId = 0;
        }
    }

    String customJavascript = "$('#form-delete').prop('disabled', " + ((mappingId > 0) ? "false" : "true") + ");";

    request.setAttribute("formHeadline", formHeadline);
    request.setAttribute("formInfoText", formInfoText);
    request.setAttribute("customJavascript", customJavascript);

    request.setAttribute("mappingId", mappingId);
    request.setAttribute("clientId", clientId);

    request.setAttribute("mappingName", mappingName);
    request.setAttribute("mappingUrl", mappingUrl);
    request.setAttribute("mappingPath", mappingPath);
    request.setAttribute("mappingPattern", mappingPattern);

    request.setAttribute("actionCREATED", (Actions.contains(mappingActions, Actions.CREATED) ? "checked" : ""));
    request.setAttribute("actionUPDATED", (Actions.contains(mappingActions, Actions.UPDATED) ? "checked" : ""));
    request.setAttribute("actionDELETED", (Actions.contains(mappingActions, Actions.DELTETED) ? "checked" : ""));

    if (request.getMethod().equalsIgnoreCase("post")) {
        mappingActions = 0;
        
        if (request.getParameter("delete") != null) {
            mappingId = Integer.parseInt(request.getParameter("mapping-id"));
            ClientMapping mapping = EntityManager.create(ClientMapping.class).load(mappingId);
            mapping.delete();
            response.sendRedirect("");
            return;
        }

        if (request.getParameterValues("mapping-actions").length > 0) {
            for (String actionValue : request.getParameterValues("mapping-actions")) {
                int value = Integer.parseInt(actionValue);
                mappingActions += value;
            }
        }

        if (request.getParameter("mapping-id") != null && !request.getParameter("mapping-id").isEmpty() && !request.getParameter("mapping-id").equals("0")) {
            int id = Integer.parseInt(request.getParameter("mapping-id"));
            ClientMapping updateClientMapping = EntityManager.create(ClientMapping.class).load(id);
            updateClientMapping.setName(request.getParameter("mapping-name"));
            updateClientMapping.setUrl(request.getParameter("mapping-url"));
            updateClientMapping.setPath(request.getParameter("mapping-path"));
            updateClientMapping.setPattern(request.getParameter("mapping-pattern"));
            updateClientMapping.setActions(mappingActions);
            updateClientMapping.setUpdated(new Date());
            updateClientMapping.setClientId(Integer.parseInt(request.getParameter("client-id")));

            updateClientMapping.update();
        } else {
            ClientMapping newClientMapping = EntityManager.create(ClientMapping.class);
            newClientMapping.setName(request.getParameter("mapping-name"));
            newClientMapping.setUrl(request.getParameter("mapping-url"));
            newClientMapping.setPath(request.getParameter("mapping-path"));
            newClientMapping.setPattern(request.getParameter("mapping-pattern"));
            newClientMapping.setClientId(Integer.parseInt(request.getParameter("client-id")));
            newClientMapping.setActions(mappingActions);
            newClientMapping.create();
        }

        response.sendRedirect("mapping");
    }
%>
<t:genericpage pageTitle="| Frontpage">
    <jsp:attribute name="htmlHead">
        <link rel="stylesheet" href="css/frontpage.css">
    </jsp:attribute>
  <jsp:attribute name="footerJSScripts">
        <script>

            function validateInputs() {

                var mappingName = $("#mapping-name").val().trim();
                var mappingPath = $("#mapping-path").val().trim();
                var mappingUrl = $("#mapping-url").val().trim();
                var mappingPattern = $("#mapping-pattern").val().trim();
                var clientId = $("#client-id").val().trim();

                if (mappingName.length < 1 || mappingPath.length < 1 || mappingUrl.length < 1 || mappingPattern.length < 1 || clientId.length < 1) {
                    $('#form-save').prop('disabled', true);
                } else {
                    $('#form-save').prop('disabled', false);
                }

            }



            $("#mapping-name").change(function () {
                validateInputs();
            });

            $("#mapping-path").change(function () {
                validateInputs();
            });
            $("#mapping-url").change(function () {
                validateInputs();
            });
            
            $("#mapping-pattern").change(function () {
                validateInputs();
            });
            
            $("#client-id").change(function () {
                validateInputs();
            });
            
            validateInputs();

            // Delete button active 
            ${customJavascript}
        </script>
    </jsp:attribute>        
    <jsp:body>
        <h4>Bulldog Settings: Mappings</h4>
        <div class="row">
            <div class="medium-8 columns">
                ${formInfoText}
                <h5>${formHeadline}</h5>
                <form method="post" action="settings/mapping">
                    <input type="hidden" name="mapping-id" id="mapping-id" value="${mappingId}">
                    <div class="row">
                        <div class="medium-12 columns">
                            <label>Mapping name <span class="requiredMark">*</span>
                                <input type="text" placeholder="Mapping name" name="mapping-name" id="mapping-name" maxlength="100" value="${mappingName}" required="required">
                            </label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="medium-12 columns">
                            <label>Mapping (Endpoint) url <span class="requiredMark">*</span>
                                <input type="text" placeholder="Mapping url" name="mapping-url" id="mapping-url" maxlength="100" value="${mappingUrl}" required="required">      
                            </label>
                        </div>
                    </div>

                    <div class="row">                            
                        <div class="medium-12 columns">
                            <label>Mapping (Hardrive) path <span class="requiredMark">*</span>
                                <input type="text" placeholder="Mapping path" name="mapping-path" id="mapping-path" maxlength="100" value="${mappingPath}" required="required">      
                            </label>
                        </div>
                    </div>

                    <div class="row">
                        <div class="medium-6 columns">
                            <label>ClientMapping <span class="requiredMark">*</span>
                                <bulldog:ClientInputList selected="${clientId}"/>
                            </label>
                        </div>
                        <div class="medium-6 columns">
                            <label>Path pattern <span class="requiredMark">*</span>
                                <input type="text" placeholder="Path pattern" name="mapping-pattern" id="mapping-pattern" maxlength="100" value="${mappingPattern}" required="required">      
                            </label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="medium-12 columns">
                            <div class="callout primary">
                                <label><strong>Mapping actions</strong></label>
                                <i>Update WebService (endpoint), when one of the following actions performed in path</i>
                                <p>&nbsp;</p>
                                <div class="row" style="background-color: #fff; padding: 10px; border: solid 1px #000;">
                                    <div class="medium-2 columns">
                                        <label>CREATED
                                            <input type="checkbox"  name="mapping-actions" id="mapping-actions" value="1" ${actionCREATED}>      
                                        </label>
                                    </div>
                                    <div class="medium-2 columns">
                                        <label>UPDATED
                                            <input type="checkbox"  name="mapping-actions" id="mapping-actions" value="2" ${actionUPDATED}>      
                                        </label>
                                    </div>
                                    <div class="medium-2 columns">
                                        <label>DELETED
                                            <input type="checkbox"  name="mapping-actions" id="mapping-actions" value="4" ${actionDELETED}>      
                                        </label>
                                    </div>
                                    <div class="medium-6 columns">                        
                                        &nbsp;
                                    </div>
                                </div>                                

                            </div>

                        </div>    
                    </div>    

                    <div class="row">
                        <div class="medium-12 columns">                        
                            <p>&nbsp;</p>
                            <p>&nbsp;</p>
                            <p>&nbsp;</p>
                        </div>
                    </div>

                    <div class="row">
                        <div class="medium-6 columns">
                            <input type="submit" class="button" id="form-save" value="Save">
                        </div>
                        <div class="medium-6 columns">
                            <input type="submit" name="delete" id="form-delete" class="float-right button alert" value="Delete">
                        </div>                        
                    </div>
                </form>
            </div>
            <div class="medium-4 columns left-colum-border">
                <bulldog:MappingList />
            </div>
        </div>
    </jsp:body>
</t:genericpage>
