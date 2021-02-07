<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
            padding: 5px;
        }
        th {
            background-color: lightgray;
        }
        .calories_ok {
            color: green;
        }
        .calories_exceed {
            color: red;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
    </thead>
    <tbody>
        <%--@elvariable id="meals" type="java.util.List"--%>
        <%--@elvariable id="meal" type="ru.javawebinar.topjava.model.MealTo"--%>
        <c:forEach var = "meal" items="${meals}">
            <tr class = ${meal.excess ? "calories_exceed" : "calories_ok"}>
                <td>${meal.dateTime.toString().replace("T", " ")}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</body>
</html>