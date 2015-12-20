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
    <link href="../css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="../css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="../css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="../skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
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
    </script>
</head>
<body>
<div align="center">
    <div class="panel panel-default" style="float: left;width: 100%">
        <div class="panel-header"><s:text name="rulemanager.paneltitle" /><span style="float:right;" ><a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="addRule()"><s:text name="rulemanager.add" /></a></span></div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:80%; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="rulemanager.ruleno" /></th>
                            <th><s:text name="rulemanager.bankname" /></th>
                            <th><s:text name="rulemanager.posserver" /></th>
                            <th><s:text name="rulemanager.minswingnum" /></th>
                            <th><s:text name="rulemanager.maxswingnum" /></th>
                            <th><s:text name="rulemanager.swingtime" /></th>
                            <th><s:text name="rulemanager.minswingmoney" /></th>
                            <th><s:text name="rulemanager.maxswingmoney" /></th>
                            <th><s:text name="rulemanager.swingpercent" /></th>
                            <th><s:text name="rulemanager.industryname" /></th>
                            <th><s:text name="rulemanager.industryfre" /></th>
                            <th><s:text name="rulemanager.industryinterval" /></th>
                            <th><s:text name="rulemanager.rate" /></th>
                            <th><s:text name="rulemanager.ratefre" /></th>
                            <th><s:text name="rulemanager.rateinterval" /></th>
                            <th><s:text name="rulemanager.mcc" /></th>
                            <th><s:text name="rulemanager.mccfre" /></th>
                            <th><s:text name="rulemanager.mccinterval" /></th>
                            <th><s:text name="rulemanager.usefre" /></th>
                            <th><s:text name="rulemanager.useinterval" /></th>
                            <th><s:text name="rulemanager.ruleusefre" /></th>
                            <th><s:text name="rulemanager.ruleuseinterval" /></th>
                            <th><s:text name="rulemanager.status" /></th>
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
<script type="text/javascript" src="../js/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="../js/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="../js/H-ui.js"></script>
<script type="text/javascript" src="../js/H-ui.admin.js"></script>
</body>
</html>