package dk.tv2.bulldog.fontend.tags;

import dk.tv2.bulldog.backend.db.EntityManager;
import dk.tv2.bulldog.backend.db.entities.Client;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author hjep
 */
public class ClientlList extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException {
        List<Client> clients = EntityManager.create(Client.class).loadAll();
        JspWriter out = getJspContext().getOut();

        try {
            out.println("<h5>Clients</h5>");
            out.println("<ul class=\"no-bullet\">");
            for(Client channel : clients) {
                out.println("<li><a href=\"settings/clients/" + channel.getId() + "\"><i class=\"fi-torsos\"></i> " + channel.getName() + "</a></li>");
            }
            out.println("</ul>");
        } catch (Exception ex) {
            throw new JspException("Error in ClientlList tag", ex);
        }

    }
}
