<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="app_tag" uri="/WEB-INF/taglib/pagination.tld" %>

<c:set var="page" value="/WEV-INF/pages/secured/cart.jsp"/>
<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setBundle basename="content"/>
<fmt:setLocale value="${locale}" scope="session"/>

<html>
<head>
    <title><fmt:message key="title.cart"/></title>
    <c:import url="../../parts/header.jsp"/>
</head>
<body>
<c:import url="../../parts/navbar.jsp"/>

<div class="container-fluid d-flex justify-content-center mt-4">
    <div class="container-fluid w-50">

        <c:if test="${requestScope.cart.size() < 1}">
            <p class="text-center justify-content-center display-1 mb-4"><fmt:message key="title.emptyCart"/></p>
        </c:if>

        <c:if test="${requestScope.cart.size() > 0}">
            <p class="text-center justify-content-center display-1 mb-4 "><fmt:message key="title.cart"/></p>
        </c:if>

        <c:forEach items="${requestScope.cart}" var="item">
            <div class="card mb-3" style="width: 560px; margin-left: 50px">
                <div class="row g-0">
                    <div class="col-md-4 justify-content-center">
                        <img src="<c:url value="/data/${item.getKey().imgFileName}"/>"
                             style="max-width: 12rem; max-height: 12rem;">
                    </div>
                    <div class="col-md-8">
                        <div class="card-body">
                            <h5 class="card-title text-center ">${item.getKey().name}</h5>
                            <p class="card-text text-center">${item.getKey().description}</p>
                            <p class="card-text text-center">${item.getKey().price}$</p>
                            <div class="d-flex justify-content-center">
                                <div class="row align-items-end">

                                    <div class="btn-group" role="group">
                                        <form name="deleteFromCart-${item.getKey().id}" method="post"
                                              action="<c:url value="/cafe"/>" class="needs-validation w-100" novalidate>
                                            <input type="hidden" name="id" value="${item.getKey().id}">
                                            <input type="hidden" name="command" value="delete_product_from_cart">
                                            <button type="submit" onclick="add(${item.getKey().id}, -1)"
                                                    class="btn btn-danger" style="width: 2rem;">-
                                            </button>
                                        </form>

                                        <button id="${item.getKey().id}" type="button" class="btn btn-outline-dark mb-4"
                                                style="min-width: 4rem; border-width: 0" disabled>${item.getValue()}
                                        </button>

                                        <form name="addToCart-${item.getKey().id}"
                                              method="post" action="<c:url value="/cafe"/>"
                                              class="needs-validation w-100" novalidate>
                                            <input type="hidden" name="id" value="${item.getKey().id}">
                                            <input type="hidden" name="command" value="add_to_cart">
                                            <button type="submit" onclick="add(${item.getKey().id}, 1)"
                                                    class="btn btn-success" style="width: 2rem;">+
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
        <c:if test="${requestScope.cart.size() > 0}">
            <a class="btn btn-dark mb-3" type="button" href="<c:url value="/cafe?command=to_create_order"/>"
               style="width: 560px; margin-left: 50px">
                <fmt:message key="button.createOrder"/>
            </a>
        </c:if>
    </div>
</div>
<script>
    <c:if test="${requestScope.cart.size() > 0}">
    async function add(id, number) {
        let value = document.getElementById(id).innerText;
        let currNumber = parseInt(value);
        let addNumber = parseInt(number);
        currNumber += addNumber;
        document.getElementById(id).innerText = '' + currNumber;
    }

    function onAjaxSuccess(data) {
        let parse = JSON.parse(data);
        let redirectCommand = parse.redirect_command;
        if (redirectCommand != null) {
            localStorage.clear();
            window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand;
        }
    }
    </c:if>
</script>

<c:import url="../../parts/footer.jsp"/>
</body>
</html>
