package br.multitenant;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class AutenticacaoServlet extends ServletBase {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> u = Repositorio.validarAcesso(
                obterParametro(req, "organizacao"), 
                obterParametro(req, "nome_acesso"), 
                req.getParameter("credencial") == null ? "" : req.getParameter("credencial"));
        
        if (u == null) {
            falhar(req, resp, "Organização, usuário ou senha incorretos.");
            return;
        }
        int idOperador = ((Number) u.get("idOperador")).intValue();
        if (Repositorio.possuiConflito(Repositorio.obterOrganizacoesDoOperador(idOperador))) {
            falhar(req, resp, "Acesso negado: vínculo com organizações em conflito de interesses.");
            return;
        }
        HttpSession antiga = req.getSession(false);
        if (antiga != null) antiga.invalidate();
        
        HttpSession s = req.getSession(true);
        s.setAttribute("idOperadorSessao", idOperador);
        s.setAttribute("nomeAcessoSessao", u.get("nome_acesso"));
        s.setAttribute("perfilAdmin", "ADMIN".equals(u.get("perfil")));
        s.setAttribute("idOrganizacaoSessao", ((Number) u.get("idOrganizacao")).intValue());
        s.setAttribute("nomeOrganizacaoSessao", u.get("nome_fantasia"));
        resp.sendRedirect(req.getContextPath() + "/app/publicacoes");
    }

    private void falhar(HttpServletRequest req, HttpServletResponse resp, String msg) throws ServletException, IOException {
        req.setAttribute("erro", msg);
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }
}