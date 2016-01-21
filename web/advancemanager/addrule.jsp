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
    <title><s:text name="addasset.title" /></title>
    <script type="text/javascript">
        function addRule() {
            $.ajax({
                type: 'post',
                url: 'Rule!AddRule',
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        $('#Message').html(json.errorMessage);
                    }
                    else {
                        $('.input').val("");
                        $('#Message').html("<s:text name="global.addSuccess" />");
                        parent.refreshRuleList(json.ruleList);
                        parent.layer.close(parent.layer.getFrameIndex(window.name));
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
                    $("#bankUUID").html(json.bankList);
                    $("#bankUUID").prepend("<option value=''></option>");
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
                    $("#posServerUUID").html(json.posServerList);
                    $("#posServerUUID").prepend("<option value=''></option>");
                }
            });
        }
        function fetchSwingTimeList() {
            $.ajax({
                type: 'post',
                url: 'SwingTime!FetchSwingTimeList',
                data: "uiMode=SELECTLIST",
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#swingTimeUUID").html(json.swingTimeList);
                    $("#swingTimeUUID").prepend("<option value=''></option>");
                }
            });
        }
        function fetchIndustryList() {
            $.ajax({
                type: 'post',
                url: 'Industry!FetchIndustryList',
                data: "uiMode=SELECTLIST",
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#industryUUID").html(json.industryList);
                    $("#industryUUID").prepend("<option value=''></option>");
                }
            });
        }
        function fetchRateList() {
            $.ajax({
                type: 'post',
                url: 'Rate!FetchRateList',
                data: "uiMode=SELECTLIST",
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#rateUUID").html(json.rateList);
                    $("#rateUUID").prepend("<option value=''></option>");
                }
            });
        }
        function fetchMCCList() {
            $.ajax({
                type: 'post',
                url: 'MCC!FetchMCCList',
                data: "uiMode=SELECTLIST",
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#mccUUID").html(json.mccList);
                    $("#mccUUID").prepend("<option value=''></option>");
                }
            });
        }
        $(function(){
            fetchBankList();
            fetchPosServerList();
            fetchSwingTimeList();
            fetchIndustryList();
            fetchRateList();
            fetchMCCList();
        })
    </script>
</head>
<body scroll="no">
<div>
    <form class="form form-horizontal">
        <table class="table table-border table-bordered table-bg table-hover table-sort">
            <tr class="text-c odd" role="row">
                <td><s:text name="rule.bankname" /></td>
                <td>
                    <select id="bankUUID" name="bankUUID" style="width: 100%">
                    </select>
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="rule.posserver" /></td>
                <td>
                    <select id="posServerUUID" name="posServerUUID" style="width: 100%">
                    </select>
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="rule.swingtime" /></td>
                <td>
                    <select id="swingTimeUUID" name="swingTimeUUID" style="width: 100%">
                    </select>
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="rule.minswingmoney" /></td>
                <td>
                    <input id="minSwingMoney" name="minSwingMoney" type="text" placeholder="<s:text name="rule.minswingmoney" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="rule.maxswingmoney" /></td>
                <td>
                    <input id="maxSwingMoney" name="maxSwingMoney" type="text" placeholder="<s:text name="rule.maxswingmoney" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="rule.industryname" /></td>
                <td>
                    <select id="industryUUID" name="industryUUID" style="width: 100%">
                    </select>
                </td>
            </tr>
            <tr class="text-c odd" role="row" style="display: none">
                <td><s:text name="rule.industryfre" /></td>
                <td>
                    <input id="industryFre" name="industryFre" type="text" placeholder="<s:text name="rule.industryfre" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row" style="display: none">
                <td><s:text name="rule.industryinterval" /></td>
                <td>
                    <input id="industryInterval" name="industryInterval" type="text" placeholder="<s:text name="rule.industryinterval" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="rule.rate" /></td>
                <td>
                    <select id="rateUUID" name="rateUUID" style="width: 100%">
                    </select>
                </td>
            </tr>
            <tr class="text-c odd" role="row" style="display: none">
                <td><s:text name="rule.ratefre" /></td>
                <td>
                    <input id="rateFre" name="rateFre" type="text" placeholder="<s:text name="rule.ratefre" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row" style="display: none">
                <td><s:text name="rule.rateinterval" /></td>
                <td>
                    <input id="rateInterval" name="rateInterval" type="text" placeholder="<s:text name="rule.rateinterval" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="rule.mcc" /></td>
                <td>
                    <select id="mccUUID" name="mccUUID" style="width: 100%">
                    </select>
                </td>
            </tr>
            <tr class="text-c odd" role="row" style="display: none">
                <td><s:text name="rule.mccfre" /></td>
                <td>
                    <input id="mccFre" name="mccFre" type="text" placeholder="<s:text name="rule.mccfre" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row" style="display: none">
                <td><s:text name="rule.mccinterval" /></td>
                <td>
                    <input id="mccInterval" name="mccInterval" type="text" placeholder="<s:text name="rule.mccinterval" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row" style="display: none">
                <td><s:text name="rule.usefre" /></td>
                <td>
                    <input id="useFre" name="useFre" type="text" placeholder="<s:text name="rule.usefre" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row" style="display: none">
                <td><s:text name="rule.useinterval" /></td>
                <td>
                    <input id="useInterval" name="useInterval" type="text" placeholder="<s:text name="rule.useinterval" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="rule.ruleusefre" /></td>
                <td>
                    <input id="ruleUseFre" name="ruleUseFre" type="text" placeholder="<s:text name="rule.ruleusefre" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="rule.ruleuseinterval" /></td>
                <td>
                    <input id="ruleUseInterval" name="ruleUseInterval" type="text" placeholder="<s:text name="rule.ruleuseinterval" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="global.status" /></td>
                <td>
                    <input id="status" name="status" type="checkbox" class="check-box size-S">
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
                <input type="button" class="btn btn-success radius size-M" value="<s:text name="global.submit" />" onclick="addRule()">
            </div>
        </div>
    </form>
</div>
</body>
</html>