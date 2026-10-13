<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Acesso ao Sistema</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <div class="container">
        <h2>Login Multi-Tenant</h2>
        <c:if test="${not empty erro}"><p class="erro"><c:out value="${erro}"/></p></c:if>
        <form method="post" action="${pageContext.request.contextPath}/login">
            <input name="organizacao" placeholder="Nome da Organização" value="<c:out value='${param.organizacao}'/>" required>
            <input name="nome_acesso" placeholder="Usuário" value="<c:out value='${param.nome_acesso}'/>" required>
            <input name="credencial" type="password" placeholder="Senha" required>
            <button type="submit">Entrar no Sistema</button>
        </form>
        <a href="${pageContext.request.contextPath}/noticias">Acessar Portal Público de Notícias</a>
    </div>
</body>
</html>