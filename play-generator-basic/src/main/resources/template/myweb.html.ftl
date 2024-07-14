<!DOCTYPE html>
<html>
<head>
    <title>玩物志</title>
</head>
<body>
<h1>欢迎来到CodePlay</h1>
<ul>
    <#list menuItems as item>
        <li>
            <a href="${item.url}">
                ${item.label}
            </a>
        </li>
    </#list>
</ul>
<#-- 底部版本信息 -->
<footer>
    ${currentYear} 玩物志出品. All rights reserved
</footer>
</body>
</html>