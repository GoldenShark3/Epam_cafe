<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="app_tag" uri="/WEB-INF/taglib/pagination.tld" %>

<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setBundle basename="content"/>
<fmt:setLocale value="${locale}" scope="session"/>

<c:set var="index" value="0"/>
<html>
<head>
    <title><fmt:message key="title.orders"/></title>
    <c:import url="../../parts/header.jsp"/>
</head>
<body>
<c:import url="../../parts/navbar.jsp"/>

<c:if test="${not requestScope.containsKey('pagination_context') or param.page > requestScope.pagination_context.totalPages}">
    <p class="text-center justify-content-center display-1 mb-4"><fmt:message key="title.emptyOrders"/></p>
</c:if>

<c:if test="${requestScope.containsKey('pagination_context') and param.page le requestScope.pagination_context.totalPages}">
    <p class="display-2 text-center"><fmt:message key="title.orders"/></p>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th scope="col">№</th>
            <th scope="col"><fmt:message key="label.deliveryDate"/></th>
            <th scope="col"><fmt:message key="label.orderProducts"/></th>
            <th scope="col"><fmt:message key="label.cost"/></th>
            <th scope="col"><fmt:message key="label.status"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.pagination_context.objectList}" var="order">
            <tr>
                <th scope="row">
                    <div class="mt-1 text-center">${index + 1}</div>
                    <c:set var="index" value="${index + 1}"/>
                </th>
                <td>
                    <div class="mt-1">
                        <input class="form-control text-center" style="background: white" type="datetime-local"
                               disabled readonly value="${order.deliveryDate}">
                    </div>
                </td>
                <td>
                    <c:forEach items="${order.products.entrySet()}" var="product">
                        <div class="mt-1">
                            <p style="width:50%; float: left; font-size: 20px;">${product.getKey().name}</p>
                            <p class="text-end" style="width:50%; float: right; font-size: 20px;"> X${product.getValue()}</p>
                        </div>
                    </c:forEach>
                </td>
                <td>
                    <div class="mt-1">
                        <p class="text-success text-center" style="font-size: 25px">${order.cost}$</p>
                    </div>
                </td>
                <td>
                    <div class="mt-1">
                        <c:if test="${order.orderStatus.name() eq 'ACTIVE'}">
                            <p class="text-danger text-center" style="font-size: 25px"><fmt:message
                                    key="title.activeOrder"/></p>
                        </c:if>
                        <c:if test="${order.orderStatus.name() ne 'ACTIVE'}">
                            <p class="text-success text-center" style="font-size: 25px"><fmt:message
                                    key="title.notActiveOrder"/></p>
                        </c:if>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="d-flex justify-content-center m-4">
        <ul class="pagination">
            <app_tag:pagination pages="${requestScope.pagination_context.totalPages}"
                                page="${requestScope.pagination_context.page}"
                                url='/cafe?command=to_my_orders&page='/>
        </ul>
    </div>
</c:if>

<c:import url="../../parts/footer.jsp"/>
</body>
</html>
