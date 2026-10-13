<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastro de usuários</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <%@ include file="menu.jspf" %>
    <div class="container">
        <h2>Cadastrar usuário</h2>
        <c:if test="${not empty erro}"><p class="erro"><c:out value="${erro}"/></p></c:if>
        <c:if test="${not empty msg}"><p class="ok"><c:out value="${msg}"/></p></c:if>
        <form method="post" action="${pageContext.request.contextPath}/app/operadores">
            <input name="nome_acesso" placeholder="Usuário" maxlength="50" required>
            <input name="credencial" type="password" placeholder="Senha" required>
            <select name="perfil">
                <option value="COMUM">Comum</option>
                <option value="ADMIN">Administrador</option>
            </select>
            <fieldset>
                <legend>Empresas</legend>
                <c:forEach var="e" items="${organizacoes}">
                    <label><input type="checkbox" name="organizacoes" value="${e.codigo}"> <c:out value="${e.nome_fantasia}"/></label><br>
                </c:forEach>
            </fieldset>
            <button type="submit">Salvar</button>
        </form>
    </div>
</body>
</html>