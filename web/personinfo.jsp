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
    <title>个人信息</title>
    <script type="text/javascript" src="js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript">
        function modifyPassword(){
            $.ajax({
                type: 'post',
                url: 'User!ModifyPassword',
                data: $("form").serialize(),
                success: function(data) {
                    alert((data));
                }
            });}
    </script>
</head>
<body>
<s:form>
    <table class="table table-border table-bordered" border="1">
        <tr>
            <td><span>账户显示</span></td>
            <td><input type="text" class="input-text radius" readonly="readonly" value="<s:property value="personInfo[0].UNICK"/>"/></td>
            <td rowspan="5"><div class="panel panel-default">
                <div class="panel-header">登录信息</div>
                <div class="panel-body">本次登录<br>IP：<s:property value="personInfo[0].LASTLOCATION"/> 时间：<s:property value="personInfo[0].LASTTIME"/><br>
                    上次登录<br><s:property value="#session.userLastLoginInfo"/>
                </div>
            </div></td>
        </tr>
        <tr>
            <td><span>账号</span></td>
            <td><input type="text" class="input-text radius" name="userName" readonly="readonly" value="<s:property value="personInfo[0].UNAME"/>"/></td>
        </tr>
        <tr>
            <td><span>原密码</span></td>
            <td><input type="text" class="input-text radius" name="userPwd"/></td>
        </tr>
        <tr>
            <td><span>新密码</span></td>
            <td><input type="text" class="input-text radius" name="userNewPwd" /></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: center"><input type="button" class="btn btn-primary"  onclick="modifyPassword()" value=" 提  交 "/></td>
        </tr>
    </table>
</s:form>
</body>
</html>