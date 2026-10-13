<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Notícias da empresa</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <%@ include file="menu.jspf" %>
    <div class="container">
        <h2>Cadastrar notícia</h2>
        <form method="post" action="${pageContext.request.contextPath}/app/publicacoes">
            <input name="titulo" placeholder="Título" maxlength="150" required>
            <textarea name="texto" placeholder="Texto" rows="4" required></textarea>
            <button type="submit">Salvar</button>
        </form>
        <h2>Notícias de <c:out value="${sessionScope.nomeOrganizacaoSessao}"/></h2>
        <c:forEach var="n" items="${itens}">
            <article>
                <h3><c:out value="${n.titulo}"/></h3>
                <small><c:out value="${n.data}"/></small>
                <p><c:out value="${n.conteudo}"/></p>
            </article>
        </c:forEach>
    </div>
</body>
</html>