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
    <title><s:text name="rulemanager.title" /></title>
    <script type="text/javascript">
        function addRule(){
            var index = layer.open({
                type: 2,
                title: "添加规则",area: ['310px', '500px'],
                fix: false,
                content: "./advancemanager/addrule.jsp"
            });}

        function refreshRuleList(ruleList) {
            $('#ruleList').html(ruleList);
        }

        function  clickRule(ruleUUID) {
            var index = layer.open({
                type: 2,
                title: "规则管理",
                fix: true,
                maxmin: false,
                content: "Repay!InitDetail?cardNO=" + cardNo + "&billUUID=" + billUUID
            });
            layer.full(index);
        }
    </script>
</head>
<body>
<div align="center">
    <div class="panel panel-default" style="float: left;width: 100%">
        <div class="panel-header">
            <s:text name="rulemanager.paneltitle" />
            <span style="float:right;" >
                <a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="addRule()">
                    <s:text name="global.add" />
                </a>
            </span>
        </div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:80%; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="rule.bankname" /></th>
                            <th><s:text name="rule.posserver" /></th>
                            <th><s:text name="rule.swingtime" /></th>
                            <th><s:text name="rule.minswingmoney" /></th>
                            <th><s:text name="rule.maxswingmoney" /></th>
                            <th><s:text name="rule.industryname" /></th>
                            <th><s:text name="rule.rate" /></th>
                            <th><s:text name="rule.mcc" /></th>
                            <th><s:text name="rule.ruleusefre" /></th>
                            <th><s:text name="rule.ruleuseinterval" /></th>
                            <th><s:text name="global.status" /></th>
                            <th><s:text name="global.operation"/></th>
                        </tr>
                        </thead>
                        <tbody id="ruleList">
                        <s:property value="ruleList" escape="false" />
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