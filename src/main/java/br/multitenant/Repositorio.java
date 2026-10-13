package br.multitenant;

import java.sql.*;
import java.util.*;

public class Repositorio {

    public static List<Map<String, Object>> buscar(String sql, Object... params) {
        try (Connection c = Conexao.obter(); PreparedStatement s = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) s.setObject(i + 1, params[i]);
            try (ResultSet r = s.executeQuery()) {
                ResultSetMetaData m = r.getMetaData();
                List<Map<String, Object>> linhas = new ArrayList<>();
                while (r.next()) {
                    Map<String, Object> linha = new LinkedHashMap<>();
                    for (int i = 1; i <= m.getColumnCount(); i++) linha.put(m.getColumnLabel(i), r.getObject(i));
                    linhas.add(linha);
                }
                return linhas;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void atualizar(String sql, Object... params) {
        try (Connection c = Conexao.obter(); PreparedStatement s = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) s.setObject(i + 1, params[i]);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Map<String, Object> validarAcesso(String organizacao, String nomeAcesso, String senha) {
        List<Map<String, Object>> r = buscar(
                "SELECT op.codigo AS idOperador, op.nome_acesso, op.perfil, org.codigo AS idOrganizacao, org.nome_fantasia "
                        + "FROM operador op "
                        + "JOIN operador_organizacao oo ON oo.operador_codigo = op.codigo "
                        + "JOIN organizacao org ON org.codigo = oo.organizacao_codigo "
                        + "WHERE org.nome_fantasia = ? AND op.nome_acesso = ? AND op.credencial = SHA2(?, 256)",
                organizacao, nomeAcesso, senha);
        return r.isEmpty() ? null : r.get(0);
    }

    public static Set<Integer> obterOrganizacoesDoOperador(int idOperador) {
        Set<Integer> ids = new TreeSet<>();
        for (Map<String, Object> m : buscar("SELECT organizacao_codigo FROM operador_organizacao WHERE operador_codigo = ?", idOperador)) {
            ids.add(((Number) m.get("organizacao_codigo")).intValue());
        }
        return ids;
    }

    public static List<Map<String, Object>> listarOrganizacoes(Set<Integer> ids) {
        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Map<String, Object> m : buscar("SELECT codigo, nome_fantasia FROM organizacao ORDER BY nome_fantasia")) {
            if (ids.contains(((Number) m.get("codigo")).intValue())) resultado.add(m);
        }
        return resultado;
    }

    public static boolean possuiConflito(Set<Integer> ids) {
        for (Map<String, Object> m : buscar("SELECT codigo, restricoes FROM organizacao")) {
            if (!ids.contains(((Number) m.get("codigo")).intValue())) continue;
            for (String c : String.valueOf(m.get("restricoes")).split(",")) {
                c = c.trim();
                if (!c.isEmpty() && ids.contains(Integer.parseInt(c))) return true;
            }
        }
        return false;
    }

    public static boolean nomeAcessoEmUso(String nomeAcesso) {
        return !buscar("SELECT codigo FROM operador WHERE nome_acesso = ?", nomeAcesso).isEmpty();
    }

    public static void registrarOperador(String nomeAcesso, String senha, String perfil, Set<Integer> organizacoes) {
        try (Connection c = Conexao.obter()) {
            c.setAutoCommit(false);
            try {
                int id;
                try (PreparedStatement s = c.prepareStatement(
                        "INSERT INTO operador (nome_acesso, credencial, perfil) VALUES (?, SHA2(?, 256), ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    s.setString(1, nomeAcesso);
                    s.setString(2, senha);
                    s.setString(3, perfil);
                    s.executeUpdate();
                    try (ResultSet k = s.getGeneratedKeys()) {
                        k.next();
                        id = k.getInt(1);
                    }
                }
                try (PreparedStatement s = c.prepareStatement("INSERT INTO operador_organizacao (operador_codigo, organizacao_codigo) VALUES (?, ?)")) {
                    for (int org : organizacoes) {
                        s.setInt(1, id);
                        s.setInt(2, org);
                        s.addBatch();
                    }
                    s.executeBatch();
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}