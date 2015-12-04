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
        function registerinfo(){
            $.ajax({
                type: 'post',
                url: 'User!Register',
                data: $("form").serialize(),
                success: function(data) {
                    parent.callback();
                    parent.layer.close(parent.layer.getFrameIndex(window.name));
                }
            });}
    </script>
</head>
<body>
<s:form>
    <input type="hidden" name="userType" value="<%= request.getParameter("usertype")%>">
    <table class="table table-border table-bordered" border="1">
        <tr>
            <td><span><s:text name="register.nick" /></span></td>
            <td><input type="text" class="input-text radius" name="userNickName"/></td>
        </tr>
        <tr>
            <td><span><s:text name="register.name" /></span></td>
            <td><input type="text" class="input-text radius" name="userName"/></td>
        </tr>
        <tr>
            <td><span><s:text name="register.pwd" /></span></td>
            <td><input type="text" class="input-text radius" name="userPwd"/></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: center"><input type="button" class="btn btn-primary"  onclick="registerinfo()" value="<s:text name="register.submit" />"/></td>
        </tr>
    </table>
</s:form>
</body>
</html>