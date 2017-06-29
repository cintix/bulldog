package dk.tv2.bulldog.fontend.tags;


import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * @author hjep
 */
public class Base extends SimpleTagSupport {

    private String applicationRoot;

    @Override
    public void setJspContext(JspContext pc) {
        super.setJspContext(pc); //To change body of generated methods, choose Tools | Templates.
        PageContext context = (PageContext) getJspContext();
        applicationRoot = context.getServletContext().getContextPath();
    }

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        try { // active-elections
            out.println("<base href=\"" + applicationRoot + "/\" />");
        } catch (Exception ex) {
            throw new JspException("Error in ElectionList tag", ex);
        }
    }
}
