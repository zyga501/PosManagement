<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link href="css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
    <title></title>
    <script type="text/javascript" src="js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript">
    </script>
</head>
<body  style ="overflow:hidden">
<div >
<div class="panel panel-default"  style="margin: 100px auto;width: 44%">
    <div class="panel-header">${welcomeuser}</div>
    <div class="panel-body"><table class="table table-border table-bordered table-bg table-hover table-sort">
        ${generalinfo}
        </table>
    </div>
</div></div>
</body>
</html>