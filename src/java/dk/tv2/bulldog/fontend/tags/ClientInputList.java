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
public class ClientInputList extends SimpleTagSupport {
    
    private int selected=0;

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
    
    
    @Override
    public void doTag() throws JspException {
        List<Client> clients = EntityManager.create(Client.class).loadAll();
        JspWriter out = getJspContext().getOut();

        try {
            out.println("<select name=\"client-id\" id=\"client-id\">");
            out.println("<option " + (0 == selected ? "selected" : "") + ">&nbsp;</option>");
            for(Client client : clients) {
                
                out.println("<option value=\""+ client.getId() + "\" " + (client.getId() == selected ? "selected" : "") + ">" + client.getName() + "</option>");
            }
            out.println("</select>");
        } catch (Exception ex) {
            throw new JspException("Error in ClientlInputList tag", ex);
        }

    }
}
