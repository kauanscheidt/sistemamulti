package br.multitenant;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter("/*")
public class FiltroSeguranca implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) req;
        HttpServletResponse rs = (HttpServletResponse) res;
        rq.setCharacterEncoding("UTF-8");
        String caminho = rq.getServletPath();
        
        if (caminho.startsWith("/app/")) {
            HttpSession s = rq.getSession(false);
            if (s == null || s.getAttribute("idOperadorSessao") == null) {
                rs.sendRedirect(rq.getContextPath() + "/login");
                return;
            }
            if (caminho.equals("/app/operadores") && !Boolean.TRUE.equals(s.getAttribute("perfilAdmin"))) {
                rs.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        chain.doFilter(req, res);
    }
}