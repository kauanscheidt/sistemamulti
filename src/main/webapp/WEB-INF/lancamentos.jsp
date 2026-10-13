<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Contas da empresa</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <%@ include file="menu.jspf" %>
    <div class="container">
        <h2>Cadastrar conta</h2>
        <form method="post" action="${pageContext.request.contextPath}/app/lancamentos">
            <input name="descricao" placeholder="Descrição" maxlength="150" required>
            <input name="valor" placeholder="Valor" required>
            <button type="submit">Salvar</button>
        </form>
        <h2>Contas de <c:out value="${sessionScope.nomeOrganizacaoSessao}"/></h2>
        <table>
            <tr><th>Descrição</th><th>Valor</th><th>Data</th></tr>
            <c:forEach var="c" items="${itens}">
                <tr>
                    <td><c:out value="${c.descricao}"/></td>
                    <td><c:out value="${c.valor}"/></td>
                    <td><c:out value="${c.data}"/></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
</html>