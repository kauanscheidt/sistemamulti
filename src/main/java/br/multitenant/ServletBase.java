package br.multitenant;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public abstract class ServletBase extends HttpServlet {

    protected String obterParametro(HttpServletRequest req, String nome) {
        String v = req.getParameter(nome);
        return v == null ? "" : v.trim();
    }

    protected int idOperador(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("idOperadorSessao");
    }

    protected int idOrganizacao(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("idOrganizacaoSessao");
    }
}