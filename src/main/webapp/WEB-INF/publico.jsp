<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Portal de Notícias</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <nav>
        <span>Portal Público</span>
        <c:choose>
            <c:when test="${not empty sessionScope.idOperadorSessao}">
                <a href="${pageContext.request.contextPath}/app/publicacoes">Área Restrita</a>
                <a href="${pageContext.request.contextPath}/logout">Sair</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Login</a>
            </c:otherwise>
        </c:choose>
    </nav>
    <div class="container">
        <h2>Últimas Notícias</h2>
        <c:forEach var="n" items="${itens}">
            <article>
                <h3><c:out value="${n.titulo}"/></h3>
                <small>Empresa: <strong><c:out value="${n.organizacao}"/></strong> | Data: <c:out value="${n.data}"/></small>
                <p><c:out value="${n.conteudo}"/></p>
            </article>
        </c:forEach>
        <c:if test="${empty itens}"><p>Nenhuma notícia publicada até o momento.</p></c:if>
    </div>
</body>
</html>