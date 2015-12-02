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
    <title><s:text name="userlist.title" /></title>
    <script type="text/javascript">
        function checkuser(){
            alert("2");
            parent.$('#parentIframe').text("1212");
            parent.layer.close(index);}
    </script>
</head>
<body>
<div class="mt-20" align="center">
    <form>
        <table class="table table-border table-bordered table-bg table-hover table-sort">
            <thead>
            <tr class="text-c">
                <th width="25">选择</th>
                <th width="80">用户名字</th>
                <th width="80">用户账号</th>
            </tr>
            </thead>
            <tbody>
            <s:property value="userListHtml" escape="false"/>
            </tbody>
        </table>
    </form>
    <br>
    <span> <button id="transmit" class="btn btn-primary radius" onclick="checkuser();">  确 定  </button></span>
</div>
</body>
</html>