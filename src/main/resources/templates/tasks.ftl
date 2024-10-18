<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hello</title>
    <link href="/css/main.css" rel="stylesheet">
</head>
<body>
<h2 class="hello-title">All task arrange by ${arrange}</h2>
    <table>
        <thead>
        <tr>
            <th>Title</th>
            <th>Description</th>
            <th>Score</th>
            <th>Status</th>
            <th>Create at</th>
            <th>Deadline</th>
        </tr>
        </thead>
        <tbody>
        <#list tasks as task>
            <tr>
                <td>${task.title}</td>
                <td>${task.description}</td>
                <td>${task.score}</td>
                <td>${task.status}</td>
                <td>${task.createdAt}</td>
                <td>${task.deadline}</td>
            </tr>
        </#list>
        </tbody>
    </table>
<script src="/js/main.js"></script>
</body>
</html>