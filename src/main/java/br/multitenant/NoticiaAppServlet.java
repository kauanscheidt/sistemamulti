package br.multitenant;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/app/publicacoes")
public class NoticiaAppServlet extends ServletBase {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("itens", Repositorio.buscar(
                "SELECT titulo, conteudo, DATE_FORMAT(data_criacao, '%d/%m/%Y %H:%i') AS data FROM publicacao WHERE organizacao_codigo = ? ORDER BY codigo DESC",
                idOrganizacao(req)));
        req.getRequestDispatcher("/WEB-INF/publicacoes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String titulo = obterParametro(req, "titulo");
        String conteudo = obterParametro(req, "conteudo");
        if (!titulo.isEmpty() && !conteudo.isEmpty()) {
            Repositorio.atualizar("INSERT INTO publicacao (organizacao_codigo, titulo, conteudo) VALUES (?, ?, ?)", idOrganizacao(req), titulo, conteudo);
        }
        resp.sendRedirect(req.getContextPath() + "/app/publicacoes");
    }
}