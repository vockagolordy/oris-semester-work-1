<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Мои цели - Budget Buddy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<c:import url="/WEB-INF/views/layout/header.jsp"/>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Мои цели</h1>
        <a href="${pageContext.request.contextPath}/goals/add" class="btn btn-primary">
            Добавить цель
        </a>
    </div>

    <div class="row">
        <div class="col-12 col-lg-6 mb-4">
            <div class="card h-100">
                <div class="card-header">
                    <h5 class="card-title mb-0">Личные цели</h5>
                </div>
                <div class="card-body">
                    <c:if test="${empty personalGoals}">
                        <p class="text-muted">У вас пока нет личных целей</p>
                    </c:if>
                    <c:forEach items="${personalGoals}" var="goal">
                        <div class="mb-4">
                            <div class="d-flex justify-content-between align-items-start mb-2">
                                <h6 class="mb-0">${goal.name}</h6>
                                <span class="badge bg-primary">${goal.progressPercentage}%</span>
                            </div>
                            <div class="progress mb-2">
                                <div class="progress-bar" role="progressbar"
                                     style="width: ${goal.progressPercentage}%"
                                     aria-valuenow="${goal.progressPercentage}"
                                     aria-valuemin="0"
                                     aria-valuemax="100">
                                </div>
                            </div>
                            <div class="d-flex justify-content-between text-muted small">
                                <span>${goal.currentAmount} ₽</span>
                                <span>${goal.targetAmount} ₽</span>
                            </div>
                            <div class="mt-2">
                                <a href="${pageContext.request.contextPath}/goals/contribute/${goal.id}"
                                   class="btn btn-sm btn-outline-primary">Внести вклад</a>
                            </div>
                        </div>
                        <c:if test="${not loop.last}"><hr></c:if>
                    </c:forEach>
                </div>
            </div>
        </div>

        <div class="col-12 col-lg-6 mb-4">
            <div class="card h-100">
                <div class="card-header">
                    <h5 class="card-title mb-0">Групповые цели</h5>
                </div>
                <div class="card-body">
                    <c:if test="${empty groupGoals}">
                        <p class="text-muted">У вас пока нет групповых целей</p>
                    </c:if>
                    <c:forEach items="${groupGoals}" var="goal">
                        <div class="mb-4">
                            <div class="d-flex justify-content-between align-items-start mb-2">
                                <h6 class="mb-0">${goal.name}</h6>
                                <span class="badge bg-success">${goal.progressPercentage}%</span>
                            </div>
                            <div class="progress mb-2">
                                <div class="progress-bar bg-success" role="progressbar"
                                     style="width: ${goal.progressPercentage}%"
                                     aria-valuenow="${goal.progressPercentage}"
                                     aria-valuemin="0"
                                     aria-valuemax="100">
                                </div>
                            </div>
                            <div class="d-flex justify-content-between text-muted small">
                                <span>${goal.currentAmount} ₽</span>
                                <span>${goal.targetAmount} ₽</span>
                            </div>
                            <div class="mt-2">
                                <a href="${pageContext.request.contextPath}/goals/contribute/${goal.id}"
                                   class="btn btn-sm btn-outline-success">Внести вклад</a>
                            </div>
                        </div>
                        <c:if test="${not loop.last}"><hr></c:if>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<c:import url="/WEB-INF/views/layout/footer.jsp"/>
<script src="${pageContext.request.contextPath}/resources/js/goals.js"></script>
</body>
</html>