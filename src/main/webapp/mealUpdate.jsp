<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="meal" type="ru.javawebinar.topjava.model.Meal"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal Editor</title>
    <style>
        dd {
            display: inline-block;
            width: 100px;
        }

        dt {
            display: inline-block;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3><c:out value="${empty meal.id ? 'Add' : 'Edit'}" /> meal</h3>

<form method="post" action="meals">
    <input type="hidden" name="id" value="${meal.id}">
    <dl>
        <dd>DateTime:</dd>
        <dt><input type="datetime-local" value="${meal.dateTime}" name="dateTime"></dt>
    </dl>
    <dl>
        <dd>Description:</dd>
        <dt><input type="text" value="${meal.description}" name="description"></dt>
    </dl>
    <dl>
        <dd>Calories:</dd>
        <dt><input type="number" value="${meal.calories}" name="calories"></dt>
    </dl>
    <button type="submit">Save</button>
    <button onclick="history.back(); return false">Cancel</button>
</form>
</body>
</html>
