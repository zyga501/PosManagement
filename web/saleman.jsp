<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
    <script type="text/javascript">
        function listUsers(){
            var index = layer.open({
                type: 2,
                title: "用户列表",area: ['310px', '530px'],
                fix: false, //不固定
                maxmin: true,
                content: "User!ListUsers"
            });}
    </script>
</head>
<body>
<div class="mt-20">
    <div class="panel panel-default" style="float: left;width: 44%">
        <div class="panel-header">业务员<span style="float:right;" ><a href="javascript:void(0);" onclick="listUsers();">+添加</a></span></div>
        <div class="panel-body" id="parentIframe">业务员列表</div>
    </div>
    <div class="panel panel-default" style="float: right;width: 54%;">
        <div class="panel-header">业务属性</div>
        <div class="panel-body">属性内容</div>
    </div>
</div>
<script type="text/javascript" src="js/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="js/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="js/H-ui.js"></script>
<script type="text/javascript" src="js/H-ui.admin.js"></script>
</body>
</html>