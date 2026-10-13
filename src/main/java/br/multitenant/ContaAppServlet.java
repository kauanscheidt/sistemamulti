package br.multitenant;

import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/app/lancamentos")
public class ContaAppServlet extends ServletBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("itens", Repositorio.buscar(
                "SELECT historico AS descricao, valor, DATE_FORMAT(data_criacao, '%d/%m/%Y %H:%i') AS data FROM lancamento WHERE organizacao_codigo = ? ORDER BY codigo DESC",
                idOrganizacao(req)));
        req.getRequestDispatcher("/WEB-INF/lancamentos.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String descricao = obterParametro(req, "descricao");
        try {
            BigDecimal valor = new BigDecimal(obterParametro(req, "valor").replace(",", "."));
            if (!descricao.isEmpty()) {
                Repositorio.atualizar("INSERT INTO lancamento (organizacao_codigo, historico, valor) VALUES (?, ?, ?)", idOrganizacao(req), descricao, valor);
            }
        } catch (NumberFormatException e) {
        }
        resp.sendRedirect(req.getContextPath() + "/app/lancamentos");
    }
}