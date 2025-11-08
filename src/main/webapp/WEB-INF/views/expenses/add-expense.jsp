<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Добавить операцию - Budget Buddy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<c:import url="/WEB-INF/views/layout/header.jsp"/>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-12 col-md-8 col-lg-6">
            <div class="card">
                <div class="card-header">
                    <h4 class="card-title mb-0">Добавить операцию</h4>
                </div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/expenses">
                        <input type="hidden" name="action" value="create">

                        <div class="mb-3">
                            <label for="type" class="form-label">Тип операции</label>
                            <select class="form-select" id="type" name="type" required>
                                <option value="INCOME">Доход</option>
                                <option value="EXPENSE">Расход</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="category" class="form-label">Категория</label>
                            <input type="text" class="form-control" id="category" name="category" required>
                        </div>

                        <div class="mb-3">
                            <label for="amount" class="form-label">Сумма (₽)</label>
                            <input type="number" class="form-control" id="amount" name="amount"
                                   min="0.01" step="0.01" required>
                        </div>

                        <div class="mb-3">
                            <label for="description" class="form-label">Описание</label>
                            <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                        </div>

                        <div class="d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/expenses" class="btn btn-secondary">Отмена</a>
                            <button type="submit" class="btn btn-primary">Добавить</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<c:import url="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>