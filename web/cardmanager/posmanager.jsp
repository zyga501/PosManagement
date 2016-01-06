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
    <title><s:text name="bankmanager.title"/></title>
    <script type="text/javascript">
        function addpos() {
            var index = layer.open({
                type: 2,
                title: "<s:text name="global.add"/>",
                fix: false,area: ['310px', '500px'],
                maxmin: false,
                content: "./cardmanager/addpos.jsp"
            });
        }
        function editpos() {
            var index = layer.open({
                type: 2,
                title: "<s:text name="global.edit"/>",
                fix:false,area: ['310px', '500px'],
                maxmin: false,
                content: "POS!FetchPOS?"+$("form").serialize()
            });
        }
        function refreshposList(posList) {
            $('#posList').html(posList);
        }
    </script>
</head>
<body style="overflow: hidden">
<div align="center">
    <div class="panel panel-default"  >
        <div class="panel-header"><s:text name="posmanager.paneltitle"/><span style="float:right;">
            <a href="javascript:void(0);" class="btn btn-warning  radius size-S " onclick="editpos()">
                <s:text name="global.edit"/></a></span> <span style="float:right;">
            <a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="addpos()">
                <s:text name="global.add"/></a></span></div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:100%; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="global.sequence"/></th>
                            <th><s:text name="posmanager.posname"/></th>
                            <th><s:text name="posmanager.industryname"/></th>
                            <th><s:text name="posmanager.rate"/></th>
                            <th><s:text name="posmanager.posserver"/></th>
                            <th><s:text name="posmanager.mcc"/></th>
                            <th><s:text name="global.status"/></th>
                        </tr>
                        </thead>
                        <tbody id="posList">
                        <s:property value="posList" escape="false"/>
                        </tbody>
                    </table>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.admin.js"></script>
</body>
</html>