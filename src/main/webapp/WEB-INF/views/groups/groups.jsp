<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Мои группы - Budget Buddy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<c:import url="/WEB-INF/views/layout/header.jsp"/>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Мои группы</h1>
        <a href="${pageContext.request.contextPath}/groups/add" class="btn btn-primary">
            Создать группу
        </a>
    </div>

    <c:if test="${empty groups}">
        <div class="alert alert-info">
            Вы пока не состоите в группах. Создайте первую группу для совместного управления финансами.
        </div>
    </c:if>

    <div class="row">
        <c:forEach items="${groups}" var="group">
            <div class="col-12 col-md-6 col-lg-4 mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <h5 class="card-title">${group.name}</h5>
                        <p class="card-text text-muted">${group.description}</p>
                        <div class="mt-auto">
                            <a href="${pageContext.request.contextPath}/groups/${group.id}"
                               class="btn btn-outline-primary btn-sm">
                                Подробнее
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<c:import url="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>