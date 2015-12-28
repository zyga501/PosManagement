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

        function editSwingCard() {
            var index = layer.open({
                type: 2,
                title: "<s:text name="global.edit"/>",
                fix:false,area: ['310px', '500px'],
                maxmin: false,
                content: "SwingCard!FetchSwingCard?"+$("form").serialize()
            });
        }
        function refreshswingCardList(swingCardList) {
            $('#swingCardList').html(swingCardList);
        }
    </script>
</head>
<body style="overflow: hidden">
<div align="center">
    <div class="panel panel-default" >
        <div class="panel-header"><s:text name="swingcard.paneltitle"/><span style="float:right;">
            <a href="javascript:void(0);" class="btn btn-warning  radius size-S " onclick="editpos()">
                <s:text name="global.edit"/></a></span></div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:100%; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th></th>
                            <th><s:text name="swingcard.thedate"/></th>
                            <th><s:text name="swingcard.cardno"/></th>
                            <th><s:text name="swingcard.cardmaster"/></th>
                            <th><s:text name="swingcard.amount"/></th>
                            <th><s:text name="swingcard.sdatetm"/></th>
                            <th><s:text name="swingcard.machineno"/></th>
                            <th><s:text name="swingcard.machinename"/></th>
                            <th><s:text name="swingcard.swingstatus"/></th>
                            <th><s:text name="swingcard.teller"/></th>
                            <th><s:text name="swingcard.realsdatetm"/></th>
                            <th><s:text name="swingcard.salesman"/></th>
                            <th><s:text name="swingcard.validstatus"/></th>
                        </tr>
                        </thead>
                        <tbody id="swingCardList">
                        <s:property value="swingCardList" escape="false"/>
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