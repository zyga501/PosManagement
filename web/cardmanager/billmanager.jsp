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
    <title></title>
    <script type="text/javascript">
        function addBill(){
            var index = layer.open({
                type: 2,
                title: "生成账单",area: ['310px', '500px'],
                fix: false,
                content: "./cardmanager/addbill.jsp"
            });}
        function refreshBillList(billList) {
            $('#billList').html(billList);
        }
    </script>
</head>
<body>
<div align="center">
    <div class="panel panel-default" style="float: left;width: auto">
        <div class="panel-header"><s:text name="billmanager.paneltitle" /><span style="float:right;" ><a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="addBill()"><s:text name="billmanager.makebill" /></a></span></div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:80%; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="billmanager.bankname" /></th>
                            <th><s:text name="billmanager.cardno" /></th>
                            <th><s:text name="billmanager.billdate" /></th>
                            <th><s:text name="billmanager.lastrepaymentdate" /></th>
                            <th><s:text name="billmanager.billamount" /></th>
                            <th><s:text name="billmanager.canuseamount" /></th>
                            <th><s:text name="billmanager.billhadpay" /></th>
                            <th><s:text name="billmanager.billnopay" /></th>
                            <th><s:text name="billmanager.lastpay" /></th>
                            <th><s:text name="billmanager.lastpaydatetm" /></th>
                            <th><s:text name="billmanager.lastteller" /></th>
                            <th><s:text name="billmanager.salesman" /></th>
                            <th><s:text name="billmanager.expired" /></th>
                            <th><s:text name="billmanager.status" /></th>
                        </tr>
                        </thead>
                        <tbody id="billList">
                            <s:property value="billList" escape="false" />
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