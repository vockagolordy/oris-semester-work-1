<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${group.name} - Budget Buddy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<c:import url="/WEB-INF/views/layout/header.jsp"/>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h1>${group.name}</h1>
            <p class="text-muted mb-0">${group.description}</p>
        </div>
        <div class="d-flex gap-2">
            <c:if test="${isCreator}">
                <form method="post" action="${pageContext.request.contextPath}/groups" class="d-inline">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="groupId" value="${group.id}">
                    <button type="submit" class="btn btn-danger"
                            onclick="return confirm('Удалить группу?')">
                        Удалить группу
                    </button>
                </form>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/groups" class="d-inline">
                <input type="hidden" name="action" value="leave">
                <input type="hidden" name="groupId" value="${group.id}">
                <button type="submit" class="btn btn-outline-secondary"
                        onclick="return confirm('Покинуть группу?')">
                    Покинуть группу
                </button>
            </form>
        </div>
    </div>

    <c:if test="${not empty groupGoal}">
        <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="card-title mb-0">Цель группы</h5>
                <span class="badge bg-success">${groupGoal.progressPercentage}%</span>
            </div>
            <div class="card-body">
                <h6>${groupGoal.name}</h6>
                <div class="progress mb-2">
                    <div class="progress-bar bg-success" role="progressbar"
                         style="width: ${groupGoal.progressPercentage}%"
                         aria-valuenow="${groupGoal.progressPercentage}"
                         aria-valuemin="0"
                         aria-valuemax="100">
                    </div>
                </div>
                <div class="d-flex justify-content-between text-muted small">
                    <span>${groupGoal.currentAmount} ₽</span>
                    <span>${goal.targetAmount} ₽</span>
                </div>

                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/goals/contribute/${groupGoal.id}"
                       class="btn btn-sm btn-outline-success">Внести вклад</a>

                    <c:if test="${isCreator}">
                        <button type="button" class="btn btn-sm btn-outline-warning ms-2"
                                data-bs-toggle="modal" data-bs-target="#editGoalModal">
                            Редактировать цель
                        </button>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${isCreator && empty groupGoal}">
        <div class="card mb-4">
            <div class="card-header">
                <h5 class="card-title mb-0">Создать цель группы</h5>
            </div>
            <div class="card-body">
                <form method="post" action="${pageContext.request.contextPath}/goals">
                    <input type="hidden" name="action" value="createGroupGoal">
                    <input type="hidden" name="groupId" value="${group.id}">

                    <div class="mb-3">
                        <label for="goalName" class="form-label">Название цели</label>
                        <input type="text" class="form-control" id="goalName" name="name" required>
                    </div>

                    <div class="mb-3">
                        <label for="goalTargetAmount" class="form-label">Целевая сумма (₽)</label>
                        <input type="number" class="form-control" id="goalTargetAmount" name="targetAmount"
                               min="0.01" step="0.01" required>
                    </div>

                    <button type="submit" class="btn btn-primary">Создать цель</button>
                </form>
            </div>
        </div>
    </c:if>

    <div class="row">
        <div class="col-12 col-lg-8">
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0">Участники</h5>
                    <span class="badge bg-primary">${memberCount} участников</span>
                </div>
                <div class="card-body">
                    <div class="list-group">
                        <c:forEach items="${members}" var="member">
                            <div class="list-group-item d-flex justify-content-between align-items-center">
                                <div>
                                    <span>Участник #${member.userId}</span>
                                    <c:if test="${member.role == 'creator'}">
                                        <span class="badge bg-success ms-2">Создатель</span>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <c:if test="${isCreator}">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Добавить участника</h5>
                    </div>
                    <div class="card-body">
                        <form method="post" action="${pageContext.request.contextPath}/groups">
                            <input type="hidden" name="action" value="addMember">
                            <input type="hidden" name="groupId" value="${group.id}">
                            <div class="input-group">
                                <input type="email" class="form-control" name="memberEmail"
                                       placeholder="Email пользователя" required>
                                <button type="submit" class="btn btn-primary">Добавить</button>
                            </div>
                        </form>
                    </div>
                </div>
            </c:if>
        </div>

        <div class="col-12 col-lg-4">
            <div class="card">
                <div class="card-header">
                    <h5 class="card-title mb-0">Информация</h5>
                </div>
                <div class="card-body">
                    <p><strong>Участников:</strong> ${memberCount}</p>
                    <c:if test="${isCreator}">
                        <div class="alert alert-info">
                            Вы создатель группы
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<c:if test="${isCreator && not empty groupGoal}">
    <div class="modal fade" id="editGoalModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Редактировать цель</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/goals">
                        <input type="hidden" name="action" value="updateGroupGoal">
                        <input type="hidden" name="groupId" value="${group.id}">

                        <div class="mb-3">
                            <label for="editGoalName" class="form-label">Название цели</label>
                            <input type="text" class="form-control" id="editGoalName" name="name"
                                   value="${groupGoal.name}" required>
                        </div>

                        <div class="mb-3">
                            <label for="editGoalTargetAmount" class="form-label">Целевая сумма (₽)</label>
                            <input type="number" class="form-control" id="editGoalTargetAmount" name="targetAmount"
                                   value="${groupGoal.targetAmount}" min="0.01"
                                   step="0.01" required>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                            <button type="submit" class="btn btn-warning">Сохранить</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</c:if>

<c:import url="/WEB-INF/views/layout/footer.jsp"/>
<script src="${pageContext.request.contextPath}/resources/js/groups.js"></script>
</body>
</html>