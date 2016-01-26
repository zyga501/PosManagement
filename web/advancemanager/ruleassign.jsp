<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link href="<%=request.getContextPath()%>/css/H-ui.min.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/H-ui.admin.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/skin/default/skin.css" rel="stylesheet" type="text/css" id="skin"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <title><s:text name="addasset.title"/></title>
    <script type="text/javascript">

    </script>
</head>
<body scroll="no">
<div>
    <form class="form form-horizontal">
        <div class="panel panel-default">
            <div class="panel-header">规则分配</div>
            <div class="panel-body">
                <div class="panel panel-default" style="float:left;width: 500px;height: 600px">
                    <div class="panel-header">
                        <span>编号：</span><s:property value="ruleUUID" escape="false" />  &nbsp; &nbsp; <input type="checkbox"
                            id="chk1"><label  for="chk1">启用</label>
                    </div>
                    <div class="panel-body">
                        <div  style="float:left;width:33% ">
                            <span class="label label-default radius">内容</span><br>
                            <s:property value="ruleInfo" escape="false" />
                        </div>
                        <div  style="float:left;width:33% ">
                            <span class="label label-default radius">银行列表</span><br>
                            <select name="list" multiple="true" size="25" style="width: auto">
                                <s:property value="bankList" escape="false" />
                            </select>
                        </div>
                        <div  style="float:left;width:33% ">
                            <span class="label label-default radius">业务员列表</span><br>
                            <select name="list" multiple="true" size="25" style="width: auto">
                                <s:property value="salemanList" escape="false" />
                            </select>
                        </div>
                    </div>
                </div>
                    <div class="panel-body" style="float:left;width: 100px; height: 600px;alignment: center;" vertical="middle">
                        <table style="height: 100%">
                            <tr class="text-c odd">
                                <td ><input type="button" class="btn btn-default radius" value="->"/><br>
                                <input type="button" class="btn btn-default radius" value="<-"/></td>
                            </tr>
                            <tr class="text-c odd">
                                <td ><input type="button" class="btn btn-default radius" value="->"/><br>
                                <input type="button" class="btn btn-default radius" value="<-"/></td>
                            </tr>
                        </table>
                    </div>
                <div class="panel panel-default" style="float:left;width: 180px;height: 600px">
                    <div class="panel-header" style="height: 3%"><span>使用银行</span></div>
                    <div class="panel-body" style="height: 40%">面板内容1</div>
                    <div class="panel-header" style="height: 3%"><span>使用业务员</span></div>
                    <div class="panel-body" style="height: 30%">面板内容2</div>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>