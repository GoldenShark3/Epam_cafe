<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="content"/>

<html>
<head>
    <title><fmt:message key="title.menu"/></title>
    <c:import url="../parts/header.jsp"/>
</head>
<body>
<c:import url="../parts/navbar.jsp"/>

<div class="d-flex justify-content-center mt-0">
    <p class="display-1">
        <fmt:message key="title.menu"/>
    </p>
</div>

<div class="row justify-content-center">
    <c:forEach items="${requestScope.product_types}" var="item">

        <div class="card col-6 m-5" style="width: 18rem;">
            <a class="link-dark text-decoration-none"
               href="<c:url value='/cafe?command=to_menu_item&type_id=${item.id}&page=1'/>">
                <img src="<c:url value='/data/${item.fileName}'/>" class="card-img-top" alt="${item.fileName}">
                <div class="card-body row align-content-end">
                    <p class="text-center">${item.name}</p>
                </div>
            </a>

            <c:if test="${isAdmin}">
                <button type="button" class="btn btn-dark mb-2" data-bs-toggle="modal" data-bs-target="#exampleModal"
                        onclick='prepareModal("${item.id}", "${item.name}")'>
                    <fmt:message key="button.edit"/>
                </button>
            </c:if>
        </div>
    </c:forEach>

    <c:if test="${isAdmin}">
        <div class="card col-6 m-5" style="width: 18rem;">
            <a class="link-dark text-decoration-none" href="<c:url value="/cafe?command=to_add_product_type"/>">
                <div class="d-flex justify-content-center">
                    <img src="<c:url value="../../img/add.svg"/>" class="card-img-top mt-1"
                         alt="add" style="width: 12rem; height: 12rem">
                </div>
                <div class="card-body text-center">
                    <p><fmt:message key="link.addType"/></p>
                </div>
            </a>
        </div>
    </c:if>
</div>

<c:if test="${isAdmin}">
    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="title.edit"/></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <form name="editTypeForm" action="<c:url value="/cafe"/>" method="post" class="needs-validation"
                      novalidate
                      enctype="multipart/form-data">
                    <div class="modal-body">

                        <p id="error_message" class="text-danger"></p>
                        <p id="server_message" class="text-danger"></p>
                        <input type="hidden" name="command" value="edit_product_type">
                        <input type="hidden" id="id" name="id" value="">

                        <div class="form-group mt-4">
                            <label for='type_name' class="form-label"><fmt:message key="label.productTypeName"/></label>
                            <input id="type_name" class="form-control" type="text" name="product_name"
                                   placeholder="<fmt:message key="placeholder.productTypeName"/>"
                                   pattern="^[A-Za-zа-яА-Я\s'-]{4,20}?$"
                                   required>
                            <div class="invalid-feedback">
                                <fmt:message key="error.productTypeName"/>
                            </div>
                        </div>
                        <div class="form-group mt-4">
                            <label for="img" class="form-label"><fmt:message key="label.imgFile"/></label>
                            <input id="img" class="form-control" type="file" name="img_file"
                                   accept="image/x-png,image/jpeg"
                                   required>
                        </div>
                    </div>
                </form>

                <div class="modal-footer">

                    <form name="deleteType" action="<c:url value="/cafe"/>" method="post" class="needs-validation"
                          novalidate>
                        <input type="hidden" hidden name="command" value="delete_product_type" required>
                        <input id="delete_id" type="text" hidden name="id" value="" required>
                        <button type="submit" class="btn btn-danger">
                            <fmt:message key="button.delete"/>
                        </button>
                    </form>

                    <button onclick="sendForm()" class="btn btn-dark">
                        <fmt:message key="button.save"/>
                    </button>
                </div>
            </div>
        </div>
    </div>
</c:if>

<script>
    function prepareModal(id, name) {
        document.getElementById('type_name').value = name;
        document.getElementById('id').value = id;
        document.getElementById('delete_id').value = id;
    }

    function sendForm() {
        const htmlFormElement = document.forms.namedItem('editTypeForm');
        if (htmlFormElement.checkValidity()) {
            sendRequest(htmlFormElement.name);
        }
        htmlFormElement.classList.add('was-validated');
    }

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
            window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand
        }
    }
</script>

<c:import url="../parts/footer.jsp"/>
</body>
</html>
