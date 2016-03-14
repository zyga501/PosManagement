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
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript">
        (function($){
            $.getUrlParam = function(name)
            {
                var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
                var r = window.location.search.substr(1).match(reg);
                if (r!=null) return unescape(r[2]); return null;
            }
        })(jQuery);
        function fetchAssetList() {
            $.ajax({
                type: 'post',
                url: 'Asset!FetchAssetList',
                data: "uiMode=SELECTLIST",
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#assetuuid").html(json.assetList);
                    $("#assetuuid").val("<s:property value="posManager.assetuuid"/>");
                }
            });
        }

        function saveFunc(){
            var rt = $("#assetuuid").val();
            return rt;
        }

        function checkasset(){
            $.ajax({
                type: 'post',
                url: 'Asset!assetmoney',
                data: {assetuuid:$("#assetuuid")},
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage!=null){
                        layer.msg(json.errorMessage);
                    }
                }
            });
        }

        function initrepayinfo(){
            var _repayid=$.getUrlParam('repayid');
            $.ajax({
                type: 'post',
                url: 'Repay!FetchRepayInfo',
                data: {repayid:_repayid},
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    if (json.cardno!=null){
                        $("#cardno").val(json.cardno);
                        $("#cardmaster").val(json.cardmaster);
                        $("#bankname").val(json.bankname);
                        $("#trademoney").val(json.trademoney);
                        $("#thedate").val(json.thedate);
                    }
                }
            });
        }
        $(function () {
            fetchAssetList();
            initrepayinfo();
        })
    </script>
</head>
<body scroll="no">
    <form class="form form-horizontal">
        <table class="table table-border table-bordered table-bg table-hover table-sort">
            <tr class="text-c odd" role="row">
                <td><s:text name="cardmanager.cardmaster"/></td>
                <td><input id=cardmaster type="text" readonly="readonly"  class="input-text"></td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="cardmanager.bankname"/></td>
                <td><input id=bankname type="text" readonly="readonly" class="input-text"></td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="cardmanager.cardno"/></td>
                <td><input id=cardno type="text" readonly="readonly" class="input-text"></td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="repaydetail.amount"/></td>
                <td><input id=trademoney type="text"  class="input-text"></td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="repaydetail.sdatetm"/></td>
                <td><input id=thedate type="text" readonly="readonly" class="input-text"></td>
            </tr>
            <tr class="text-c">
                <td><s:text name="posmanager.assetcard"/></td>
                <td>
                    <select id="assetuuid" name="assetuuid" style="width: 100%">
                    </select>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>