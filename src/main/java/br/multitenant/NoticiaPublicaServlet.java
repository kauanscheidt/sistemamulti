package br.multitenant;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/noticias")
public class NoticiaPublicaServlet extends ServletBase {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("itens", Repositorio.buscar(
                "SELECT org.nome_fantasia AS organizacao, p.titulo, p.conteudo, DATE_FORMAT(p.data_criacao, '%d/%m/%Y %H:%i') AS data "
                + "FROM publicacao p JOIN organizacao org ON org.codigo = p.organizacao_codigo ORDER BY p.codigo DESC"));
        req.getRequestDispatcher("/WEB-INF/publico.jsp").forward(req, resp);
    }
}