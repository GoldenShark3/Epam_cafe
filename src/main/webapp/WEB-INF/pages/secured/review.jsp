<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="app_tag" uri="/WEB-INF/taglib/pagination.tld" %>
<!DOCTYPE html>
<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="content"/>
<html>
<head>
    <title><fmt:message key="title.reviews"/></title>
    <c:import url="../../parts/header.jsp"/>
    <style>
        <%@include file="../../styles/review_style.css"%>
    </style>
</head>
<body>
<c:import url="../../parts/navbar.jsp"/>

<div class="container-fluid d-flex justify-content-center">
    <div class="card w-75">
        <form name="review_form" action="<c:url value="/cafe"/> " method="post"
              class="needs-validation" novalidate accept-charset="UTF-8">
            <div class="card-header p-0">
                <p class="display-5 text-center"><fmt:message key="title.reviews"/></p>
            </div>
            <p id="error_message" class="text-danger"></p>
            <p id="server_message" class="text-danger"></p>
            <input type="hidden" name="command" value="add_review">

            <div class="form-group rating-area" style="padding-left: 30px">
                <input type="radio" id="star-5" name="rating" value="5">
                <label for="star-5" title="<fmt:message key="info.fiveStars"/>"></label>
                <input type="radio" id="star-4" name="rating" value="4">
                <label for="star-4" title="<fmt:message key="info.fourStars"/>"></label>
                <input type="radio" id="star-3" name="rating" value="3">
                <label for="star-3" title="<fmt:message key="info.threeStars"/>"></label>
                <input type="radio" id="star-2" name="rating" value="2">
                <label for="star-2" title="<fmt:message key="info.twoStars"/>"></label>
                <input type="radio" id="star-1" name="rating" value="1">
                <label for="star-1" title="<fmt:message key="info.oneStar"/>"></label>
            </div>

            <div class="p-4 pb-3">
                <label for="review_area"></label>
                <textarea id="review_area" rows="3" name="review" class="form-control" maxlength="2048"
                          placeholder="<fmt:message key="placeholder.review"/>" required></textarea>
                <div class="invalid-feedback">
                    <fmt:message key="error.review"/>
                </div>
            </div>
            <div class="input-group justify-content-center">
                <button class="btn btn-dark" type="submit">
                    <fmt:message key="button.submit"/>
                </button>
            </div>
        </form>

        <div class="card-footer bg-transparent">
            <c:forEach items="${requestScope.pagination_context.objectList}" var="item">
                <div class="w-100">
                    <p style="width:50%; float: left; font-size: 30px">${item.user.firstName} ${item.user.lastName}</p>
                    <p class="text-end" style="width:50%; float: right; font-size: 30px; color: gold">${item.rate}&#9733</p>
                </div>
                <hr/>
                <p>${item.feedback}</p>
                <hr/>
            </c:forEach>

            <div class="d-flex justify-content-center">
                <ul class="pagination">
                    <app_tag:pagination pages="${requestScope.pagination_context.totalPages}"
                                        page="${requestScope.pagination_context.page}"
                                        url='/cafe?command=to_review&page='/>
                </ul>
            </div>
        </div>

    </div>
</div>

<script>
    function onAjaxSuccess(data) {
        let pMessages = document.getElementById("server_message");
        let eMessages = document.getElementById("error_message");
        pMessages.innerText = "";
        eMessages.innerText = "";

        let parse = JSON.parse(data);

        let serverMessages = parse.server_message;
        let errorMessages = parse.error_message;

        if (serverMessages != null) {
            pMessages.innerText += serverMessages + '\n';
        }

        if (errorMessages != null) {
            for (let i = 0; i < errorMessages.length; i++) {
                eMessages.innerText += errorMessages[i] + '\n';
            }
        }

        let redirectCommand = parse.redirect_command;
        if (redirectCommand != null) {
            window.location.href = '<c:url value="/cafe"/>' + "?command="
                + redirectCommand + "&page=${param.page}";
        }
    }
</script>

<c:import url="../../parts/footer.jsp"/>
</body>
</html>
