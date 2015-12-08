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
    <title><s:text name="bankmanager.title" /></title>
    <script type="text/javascript">
        function addcard(){
            var index = layer.open({
                type: 2,
                title: "添加卡信息",
                fix: true,
                maxmin: false,
                content: "cardmanager/addcard.jsp"
            });layer.full(index);}
        function refreshcardList(cardList) {
            $('#cardsList').html(cardList);
        }
    </script>
</head>
<body style="overflow: hidden">
<div align="center">
    <div class="panel panel-default" style="float: left;width: auto">
        <div class="panel-header"><s:text name="cardmanager.paneltitle" /><span style="float:right;" >
            <a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="addcard()">
                <s:text name="ratesmanager.add" /></a></span></div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:100%; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th ><s:text name="cardmanager.inserttime"/></th>
                            <th ><s:text name="cardmanager.cardserial"/></th>
                            <th ><s:text name="cardmanager.cardno"/></th>
                            <th ><s:text name="cardmanager.bankname"/></th>
                            <th ><s:text name="cardmanager.creditamount"/></th>
                            <th ><s:text name="cardmanager.tempamount"/></th>
                            <th ><s:text name="cardmanager.templimitdate"/></th>
                            <th ><s:text name="cardmanager.useamount"/></th>
                            <th ><s:text name="cardmanager.billdate"/></th>
                            <th ><s:text name="cardmanager.pin"/></th>
                            <th ><s:text name="cardmanager.telpwd"/></th>
                            <th ><s:text name="cardmanager.tradepwd"/></th>
                            <th ><s:text name="cardmanager.enchashmentpwd"/></th>
                            <th ><s:text name="cardmanager.billafterdate"/></th>
                            <th ><s:text name="cardmanager.lastrepaymentdate"/></th>
                            <th ><s:text name="cardmanager.billemail"/></th>
                            <th ><s:text name="cardmanager.sfqy"/></th>
                            <th ><s:text name="cardmanager.commissioncharge"/></th>
                            <th ><s:text name="cardmanager.cardmaster"/></th>
                            <th ><s:text name="cardmanager.identityno"/></th>
                            <th ><s:text name="cardmanager.identitypicfront"/></th>
                            <th ><s:text name="cardmanager.identitypicback"/></th>
                            <th ><s:text name="cardmanager.cmaddress"/></th>
                            <th ><s:text name="cardmanager.cmtel"/></th>
                        </tr>
                        </thead>
                        <tbody id="cardList">
                            <s:property value="cardList" escape="false" />
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