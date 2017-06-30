<%@page import="dk.tv2.bulldog.backend.db.entities.Client"%>
<%@page import="dk.tv2.bulldog.backend.db.EntityManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="bulldog" uri="/WEB-INF/bulldog.tld"%>

<%
    int clientId = 0;
    String clientName = "";
    String formHeadline = "Create new client";
    String formInfoText = "";
    String clientDescription = "";

    if (request.getParameter("__client_id") != null) {
        clientId = Integer.parseInt(request.getParameter("__client_id"));
        Client client = EntityManager.create(Client.class).load(clientId);
        if (client != null) {
            clientName = client.getName();
            clientDescription = client.getDescription();
            formHeadline = "Edit client";
        } else {
            formInfoText = "<div class=\"callout alert\"><h5>Not Found</h5><p>The client with ID " + clientId + " was not found!</p></div>";
            clientId = 0;
        }
    }

    String customJavascript = "$('#form-delete').prop('disabled', " + ((clientId > 0) ? "false" : "true") + ");";

    request.setAttribute("formHeadline", formHeadline);
    request.setAttribute("formInfoText", formInfoText);
    request.setAttribute("customJavascript", customJavascript);
    request.setAttribute("clientId", clientId);
    request.setAttribute("clientName", clientName);
    request.setAttribute("clientDescription", clientDescription);

    if (request.getMethod().equalsIgnoreCase("post")) {

        if (request.getParameter("delete") != null) {
            clientId = Integer.parseInt(request.getParameter("client-id"));
            Client client = EntityManager.create(Client.class).load(clientId);            
            System.out.println("DELETING CLIENT......... " + client);
            client.delete();
            response.sendRedirect("");
            return;
        }

        if (request.getParameter("client-id") != null && !request.getParameter("client-id").isEmpty() && !request.getParameter("client-id").equals("0")) {
            int id = Integer.parseInt(request.getParameter("client-id"));
            Client updateClient = EntityManager.create(Client.class).load(id);
            updateClient.setName(request.getParameter("client-name"));
            updateClient.setDescription(request.getParameter("client-description"));
            updateClient.update();
        } else {
            Client newClient = EntityManager.create(Client.class);
            newClient.setName(request.getParameter("client-name"));
            newClient.setDescription(request.getParameter("client-description"));
            newClient.create();
        }

        response.sendRedirect("clients");
    }
%>
<t:genericpage pageTitle="| Frontpage">
    <jsp:attribute name="footerJSScripts">
        <script>
            function validateInputs() {
                var clientName = $("#client-name").val().trim();
                var clientDescription = $("#client-description").val().trim();

                if (clientName.length < 1 || clientDescription.length < 1) {
                    $('#form-save').prop('disabled', true);
                } else {
                    $('#form-save').prop('disabled', false);
                }
            }

            $("#client-name").change(function () {
                validateInputs();
            });

            $("#client-description").change(function () {
                validateInputs();
            });

            validateInputs();

            // Delete button active 
            ${customJavascript}
        </script>
    </jsp:attribute>
    <jsp:attribute name="htmlHead">
        <link rel="stylesheet" href="css/frontpage.css">
    </jsp:attribute>
    <jsp:body>
        <h4>Bulldog Settings: Clients</h4>
        <div class="row">
            <div class="medium-8 columns">
                ${formInfoText}
                <h5>${formHeadline}</h5>
                <form method="post" action="settings/clients">
                    <input type="hidden" name="client-id" id="client-id" value="${clientId}">
                    <div class="row">
                        <div class="medium-12 columns">
                            <label>Client name <span class="requiredMark">*</span>
                                <input type="text" placeholder="Client name" name="client-name" id="client-name" maxlength="100" value="${clientName}" required="required">
                            </label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="medium-12 columns">
                            <label>Client description <span class="requiredMark">*</span>
                                <input type="text" placeholder="Client description" name="client-description" id="client-description" maxlength="100" value="${clientDescription}" required="required">      
                            </label>
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
                <bulldog:ClientList />
            </div>
        </div>
    </jsp:body>
</t:genericpage>
