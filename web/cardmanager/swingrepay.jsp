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
    <link href="<%=request.getContextPath()%>/css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
    <title></title>
    <script type="text/javascript">
    </script>
</head>
<body   style="overflow: hidden;" scroll="no" >
    <div class="panel panel-default" style="float: left;width: 49%;height:100%">
    <iframe src="SwingCard!Init"  style="width: 100%;height:100%"></iframe>
    </div>
    <div class="panel panel-default" style="float: left;width: 49%;height:100%">
        <iframe src="Repay!Init"  style="width: 100%;height:100%"></iframe>
    </div>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.admin.js"></script>
<script type="text/javascript" >
    $().ready(
            init2()
    )
    function init(){
        $("#swinghtml").load("<%=request.getContextPath()%>/cardmanager/swingcardsummary.jsp");
        $("#repayhtml").load("<%=request.getContextPath()%>/cardmanager/repaysummary.jsp");
    }
</script>
</body>
</html>