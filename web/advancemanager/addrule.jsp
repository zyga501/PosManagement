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
    <script type="text/javascript" src="../js/jquery/1.9.1/jquery.min.js"></script>
    <title><s:text name="addasset.title" /></title>
    <script type="text/javascript">
        function addAsset() {
            $.ajax({
                type: 'post',
                url: 'Rule!addRule',
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        $('#Message').html(json.errorMessage);
                    }
                    else {
                        $('.input').val("");
                        $('#Message').html("<s:text name="addasset.addassetSuccess" />");
                        parent.refreshAssetList(json.assetList);
                    }
                }
            })
        }

        function fetchBankList() {
            $.ajax({
                type: 'post',
                url: 'Bank!FetchBankList',
                data: "uiMode=SELECTLIST",
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#bankName").html(json.bankList);
                }
            });
        }

        function fetchPosServerList() {
            $.ajax({
                type: 'post',
                url: 'PosServer!FetchPosServerList',
                data: "uiMode=SELECTLIST",
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#posServer").html(json.posServerList);
                }
            });
        }

        $(function(){
            fetchBankList();
            fetchPosServerList();
        })
    </script>
</head>
<body scroll="no">
<div>
    <form class="form form-horizontal">
        <table class="table table-border table-bordered table-bg table-hover table-sort">
            <tr class="text-c odd" role="row">
                <td><s:text name="addrule.bankname" /></td>
                <td>
                    <select id="bankName" name="bankName">
                    </select>
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="addrule.posserver" /></td>
                <td>
                    <select id="posServer" name="posServer">
                    </select>
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="addrule.minswingnum" /></td>
                <td>
                    <input id="minSwingNum" name="minSwingNum" type="text" placeholder="<s:text name="addrule.minswingnum" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="addrule.maxswingnum" /></td>
                <td>
                    <input id="cardCode" name="cardCode" type="text" placeholder="<s:text name="addrule.maxswingnum" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="addrule.posserver" /></td>
                <td>
                    <select id="swingTime" name="posServer">
                    </select>
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="addrule.signpwd" /></td>
                <td>
                    <input id="signPwd" name="signPwd" type="text" placeholder="<s:text name="addrule.signpwd" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="addrule.cashpwd" /></td>
                <td>
                    <input id="cashPwd" name="cashPwd" type="text" placeholder="<s:text name="addrule.cashpwd" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="addrule.transferpwd" /></td>
                <td>
                    <input id="transferPwd" name="transferPwd" type="text" placeholder="<s:text name="addrule.transferpwd" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="addrule.atmcashpwd" /></td>
                <td>
                    <input id="atmCashPwd" name="atmCashPwd" type="text" placeholder="<s:text name="addrule.atmcashpwd" />" class="input-text size-S">
                </td>
            </tr>
        </table>
        <div class="row">
            <div class="formControls col-8 col-offset-3">
                <div id="Message" style="color:#ff0000;font-size: 12px;height: 12px">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="formControls col-8 col-offset-3" align="center">
                <input type="button" class="btn btn-success radius size-M" value="<s:text name="addrule.submit" />" onclick="addRule()">
            </div>
        </div>
    </form>
</div>
</body>
</html>