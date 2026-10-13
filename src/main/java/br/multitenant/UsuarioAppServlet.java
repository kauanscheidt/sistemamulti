package br.multitenant;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/app/operadores")
public class UsuarioAppServlet extends ServletBase {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        exibir(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Set<Integer> administradas = Repositorio.obterOrganizacoesDoOperador(idOperador(req));
        String nomeAcesso = obterParametro(req, "nome_acesso");
        String senha = obterParametro(req, "credencial");
        String perfil = "ADMIN".equals(obterParametro(req, "perfil")) ? "ADMIN" : "COMUM";
        Set<Integer> escolhidas = new TreeSet<>();
        
        String[] marcadas = req.getParameterValues("organizacoes");
        if (marcadas != null) {
            for (String m : marcadas) {
                try { escolhidas.add(Integer.parseInt(m)); } catch (NumberFormatException e) {}
            }
        }
        
        if (nomeAcesso.isEmpty() || senha.isEmpty() || escolhidas.isEmpty()) {
            req.setAttribute("erro", "Preencha todos os campos e selecione ao menos uma organização.");
        } else if (!administradas.containsAll(escolhidas)) {
            req.setAttribute("erro", "Você só pode gerenciar operadores das organizações que administra.");
        } else if (Repositorio.possuiConflito(escolhidas)) {
            req.setAttribute("erro", "As organizações selecionadas possuem conflito de interesses.");
        } else if (Repositorio.nomeAcessoEmUso(nomeAcesso)) {
            req.setAttribute("erro", "Este nome de acesso já está em uso.");
        } else {
            Repositorio.registrarOperador(nomeAcesso, senha, perfil, escolhidas);
            req.setAttribute("msg", "Operador cadastrado com sucesso.");
        }
        exibir(req, resp);
    }

    private void exibir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("organizacoes", Repositorio.listarOrganizacoes(Repositorio.obterOrganizacoesDoOperador(idOperador(req))));
        req.getRequestDispatcher("/WEB-INF/operadores.jsp").forward(req, resp);
    }
}