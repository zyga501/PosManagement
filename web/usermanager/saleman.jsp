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
    <title></title>
    <link href="<%=request.getContextPath()%>/css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript">
        function registerUser(){
            var index = layer.open({
                type: 2,
                title: "<s:text name="register.reg" />",area: ['310px', '330px'],
                fix: false,
                maxmin: false,
                content: "./register.jsp?usertype=1"
            });}

        function clickSaleman(id) {
            $.ajax({
                type: 'post',
                url: 'Saleman!FetchInfo',
                data: "salemanID=" + id,
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#salemanInfo").html(json.salemanInfo);
                    $("#tellerList").html(json.tellerList);
                }
            });
        }

        function payMoney(id){
            var money = prompt("充值金额","100");
            if (money==null || money=="") return ;
            $.ajax({
                type: 'post',
                url: 'Saleman!Recharge',
                data: {salemanID:id , rechargemoney:money},
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.successMessage) {
                        clickSaleman(id);
                        layer.msg(json.successMessage, {icon: 1});
                    }
                    else if (json.errorMessage)
                        layer.msg(json.errorMessage, {icon:2});
                }
            });
        }

        function refreshUserList(userList){
            $("#salemanList").html(userList);
        }

        function refreshTellerList(tellerID) {
            $.ajax({
                type: 'post',
                url: 'Saleman!AddTeller',
                data: "salemanID=" + $("#salemanID").val() + "&tellerID="+tellerID,
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#tellerList").html(json.tellerList);
                }
            });
        }

        function fetchTellerList(){
            if ("undefined"==typeof($("#salemanID").val())) {alert("<s:text name="saleman.hasuidalert"/>"); return;}
            var index = layer.open({
                type: 2,
                title: "<s:text name="teller.listtitle" />",area: ['310px', '380px'],
                fix: false,
                maxmin: false,
                content: "./usermanager/tellerlist.jsp"
            });}

        function updateSalemanInfo(){
            $.ajax({
                type: 'post',
                url: 'Saleman!UpdateInfo',
                data: $("form").serialize(),
                success: function(data) {
                    var json = eval("(" + data + ")");
                    if (json.ErrorMessage!=null)
                        layer.msg(json.ErrorMessage,{icon:2});
                    else
                    layer.msg("<s:text name="global.dosuccess"/>",{icon:1});
                }
            });
        }
    </script>
</head>
<body>
<div align="center">
    <div class="panel panel-default" style="float: left;width: 44%">
        <div class="panel-header"><s:text name="saleman.listtitle" /><span style="float:right;" >
            <a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="registerUser();"><s:text name="saleman.addbutton" /></a></span></div>
        <div class="panel-body" id="salemanList" >
            <s:property value="salemanList" escape="false" />
        </div>
    </div>
    <div class="panel panel-default" style="float: right;width: 54%;">
            <div class="panel-header"><s:text name="saleman.salemanProperty" />
                <span style="float:right">
                    <input class="btn btn-primary radius size-S " type="button" value="<s:text name="saleman.savebutton" />" onclick="updateSalemanInfo()">
                </span>
            </div>
            <div class="panel-body" id="salemanInfo"></div>
            <div class="panel-header"><s:text name="saleman.listtellertitle" />
                <span style="float:right">
                    <input class="btn btn-primary radius size-S " type="button" value="<s:text name="saleman.addbutton" />" onclick="fetchTellerList()">
                </span>
            </div>
            <div class="panel-body" id="tellerList"></div>
    </div>
</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.admin.js"></script>
</body>
</html>