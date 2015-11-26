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
    <link href="css/H-ui.login.css" rel="stylesheet" type="text/css" />
    <link href="css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="css/index.css" rel="stylesheet" type="text/css" />
    <title>卡务管理系统</title>
  </head>
  <body scroll="no">
  <div class="loginWraper">
    <div id = "loginForm" class="lg" style="top:250px">
      <table  align="center" cellspacing="0"  border="0">
        <tr>
          <td align="center" ><img src="images/indexLogo.png" width="123" height="123"></td>
        </tr>
        <tr>
          <td align="center" width="350"  align="left"><h1>卡务管理系统</h1></td>
        </tr>
      </table>
      <form class="form form-horizontal" action="User!Login" method="post">
        <div class="row cl">
          <div class="formControls col-8 col-offset-3">
            <input id="userName" name="userName" type="text" placeholder="账号" class="input-text size-L">
          </div>
        </div>
        <div class="row cl">
          <div class="formControls col-8 col-offset-3">
            <input id="userPwd" name="userPwd" type="password" placeholder="密码" class="input-text size-L">
          </div>
        </div>
        <div class="row cl">
          <div class="formControls col-8 col-offset-3">
            <input id="verifyCode" name="verifyCode" class="input-text size-L" type="text" placeholder="验证码" style="width:150px;">
          </div>
        </div>
        <div class="row">
          <div class="formControls col-8 col-offset-3">
            <input name="" type="submit" class="btn btn-success radius size-L" value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;">
            <input name="" type="reset" class="btn btn-default radius size-L" value="&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;">
          </div>
        </div>
      </form>
    </div>
  </div>
  <script type="text/javascript" src="js/jquery/1.9.1/jquery.min.js"></script>
  </body>
</html>
