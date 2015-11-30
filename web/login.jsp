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
    <title><s:text name="login.title" /></title>
    <script type="text/javascript" src="js\jquery\1.9.1\jquery.js"></script>
    <script type="text/javascript" >
      function refreshVerifyCode()
      {
        var src = $("#verifyCodeImage").attr("src");
        if (src.indexOf("?") <= 0)
          $("#verifyCodeImage").attr("src"," Auth!GenerateVerifyCode?");
        else
          $("#verifyCodeImage").attr("src"," Auth!GenerateVerifyCode");
      }
    </script>
  </head>
  <body scroll="no">
  <div class="loginWraper">
    <div id = "loginForm" class="lg" style="top:250px">
      <table  align="center" cellspacing="0"  border="0">
        <tr>
          <td align="center" ><img src="images/indexLogo.png" width="123" height="123"></td>
        </tr>
        <tr>
          <td align="center" width="350"  align="left"><h1><s:text name="login.title" /></h1></td>
        </tr>
      </table>
      <form class="form form-horizontal" action="User!Login" method="post">
        <div class="row cl">
          <div class="formControls col-8 col-offset-3">
            <input id="userName" name="userName" type="text" placeholder="<s:text name="login.userName" />" class="input-text size-L">
          </div>
        </div>
        <div class="row cl">
          <div class="formControls col-8 col-offset-3">
            <input id="userPwd" name="userPwd" type="password" placeholder="<s:text name="login.userPwd" />" class="input-text size-L">
          </div>
        </div>
        <div class="row cl">
          <div class="formControls col-8 col-offset-3">
            <input id="verifyCode" name="verifyCode" class="input-text size-L" type="text" placeholder="<s:text name="login.verifyCode" />" style="width:150px;">
            <img id="verifyCodeImage" src="Auth!GenerateVerifyCode" alt="<s:text name="login.verifyCode" />" onclick="refreshVerifyCode()" >
          </div>
        </div>
        <div class="row">
          <div class="formControls col-8 col-offset-3">
            <div style="color:#ff0000;font-size: 12px;">
              <s:property value="loginErrorMessage"/>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="formControls col-8 col-offset-3">
            <input name="" type="submit" class="btn btn-success radius size-L" value="<s:text name="login.submit" />">
          </div>
        </div>
      </form>
    </div>
  </div>
  <script type="text/javascript" src="js/jquery/1.9.1/jquery.min.js"></script>
  </body>
</html>
