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
    <title></title>
    <script type="text/javascript">
        function clickDetail(cardNo, repayYear, repayMonth) {
            var index = layer.open({
                type: 2,
                title: "还卡明细",
                fix: true,
                maxmin: false,
                content: "Repay!InitDetail?CardNO=" + cardNo + "&repayYear=" + repayYear + "&repayMonth=" + repayMonth
            });
            layer.full(index);
        }
    </script>
</head>
<body style="overflow: hidden">
<div align="center">
    <div class="panel panel-default" >
        <div class="panel-header"><s:text name="repaysummary.paneltitle" /></div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:100%; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="repaysummary.thedate"/></th>
                            <th><s:text name="repaysummary.cardno"/></th>
                            <th><s:text name="repaysummary.cardmaster"/></th>
                            <th><s:text name="repaysummary.amount"/></th>
                            <th><s:text name="repaysummary.operation"/></th>
                        </tr>
                        </thead>
                        <tbody id="repaySummary">
                            <s:property value="repaySummary" escape="false"/>
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