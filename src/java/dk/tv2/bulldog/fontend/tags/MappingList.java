package dk.tv2.bulldog.fontend.tags;

import dk.tv2.bulldog.backend.db.EntityManager;
import dk.tv2.bulldog.backend.db.entities.Client;
import dk.tv2.bulldog.backend.db.entities.ClientMapping;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author hjep
 */
public class MappingList extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException {
        List<Client> clients = EntityManager.create(Client.class).loadAll();
        List<ClientMapping> mappings = EntityManager.create(ClientMapping.class).loadAll();

        JspWriter out = getJspContext().getOut();

        try {
            out.println("<h5>Mappings</h5>");
            out.println("<ul class=\"accordion\" data-accordion data-multi-expand=\"true\" data-allow-all-closed=\"true\">");
            for (Client client : clients) {
                out.println("<li class=\"accordion-item\" data-accordion-item>");
                out.println("<a href=\"#\" class=\"accordion-title\"><i class=\"fi-torsos\"></i>  " + client.getName() + "</a>");
                out.println("<div class=\"accordion-content\" data-tab-content>");
                out.println("<ul class=\"no-bullet\" style=\"padding-left: 20px;\">");
                for (ClientMapping mapping : mappings) {
                    if (client.getId() == mapping.getClientId()) {
                        out.println("<li><a href=\"settings/mapping/" + mapping.getId() + "\"><i class=\"fi-link\"></i> " + mapping.getName() + "</a></li>");
                    }
                }
                out.println("</ul>");
                out.println("</ul>");
                out.println("</div>");
                out.println("</li>");
            }
            out.println("</ul>");
        } catch (Exception ex) {
            throw new JspException("Error in ClientlList tag", ex);
        }

    }
}
