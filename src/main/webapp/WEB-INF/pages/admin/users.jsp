<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="app_tag" uri="/WEB-INF/taglib/pagination.tld" %>

<c:set var="page" value="/WEV-INF/pages/secured/users.jsp"/>
<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setBundle basename="content"/>
<fmt:setLocale value="${locale}" scope="session"/>

<html>
<head>
    <title><fmt:message key="title.users"/></title>
    <c:import url="../../parts/header.jsp"/>
</head>
<body>
<c:import url="../../parts/navbar.jsp"/>
<p class="display-2 text-center"><fmt:message key="title.users"/></p>
<table class="table table-bordered">
    <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col"><fmt:message key="label.firstName"/></th>
            <th scope="col"><fmt:message key="label.lastName"/></th>
            <th scope="col"><fmt:message key="label.phoneNumber"/></th>
            <th scope="col"><fmt:message key="label.loyaltyPoints"/></th>
            <th scope="col"></th>
            <th scope="col"></th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${requestScope.pagination_context.objectList}" var="user">
        <tr>
            <th scope="row"><div class="mt-1">${user.id}</div></th>
            <td><div class="mt-1">${user.firstName}</div></td>
            <td><div class="mt-1">${user.lastName}</div></td>
            <td><div class="mt-1">${user.phoneNumber}</div></td>
            <td>
                <p id="error_message" class="text-danger">${error_message}</p>
                <input name="points" class="form-control" type="text" id="points-${user.id}"
                       value="${user.loyaltyPoints}" style="max-width: 9rem;">
            </td>
            <td>
                <div class="form-check form-switch mt-1">
                    <label class="form-check-label" for="check-${user.id}"> <fmt:message key="label.isBlocked"/></label>
                    <input name="check" class="form-check-input" type="checkbox" id="check-${user.id}"
                           <c:if test="${user.getIsBlocked()}">aria-checked="true"</c:if>>
                </div>
            </td>
            <td>
                <button onclick="save(${user.id})" class="btn btn-dark"><fmt:message key="button.save"/></button>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="d-flex justify-content-center m-4">
    <ul class="pagination">
        <app_tag:pagination pages="${requestScope.pagination_context.totalPages}"
                            page="${requestScope.pagination_context.page}"
                            url='/cafe?command=to_users&page='/>
    </ul>
</div>
<script>
    async function save(id) {
        let data = new FormData();
        data.append('id', id);
        data.append('points', document.getElementById('points-' + id).value);
        data.append('check', document.getElementById('check-' + id).checked);
        data.append('command', 'update_user');

        jQuery.ajax({
            url: '<c:url value="/cafe"/>',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            method: 'POST',
            success: successSave
        });

        function successSave(data) {
            let eMessages = document.getElementById("error_message");
            eMessages.innerText = "";
            let parse = JSON.parse(data);

            let errorMessages = parse.error_message;
            if (errorMessages != null) {
                for (let i = 0; i < errorMessages.length; i++) {
                    eMessages.innerText += errorMessages[i] + '\n';
                }
            } else {
                alert('<fmt:message key="info.saveSuccess"/>');
            }
        }
    }
</script>
    <c:import url="../../parts/footer.jsp"/>
</body>
</html>
