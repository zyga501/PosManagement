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
    <title><s:text name="addasset.title" /></title>
    <link href="<%=request.getContextPath()%>/css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/upload/ajaxupload.js"></script>
    <script type="text/javascript">
        function addPOS() {
            <%
                if (null==request.getAttribute("newid")){
            %>
            $('#newid').val("");
            $('#Message').html("");
            $.ajax({
                type: 'post',
                url: 'POS!addPos',
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
                        $('#newid').val(json.newid);
                        parent.refreshposList(json.posList);
                    }
                }
            })
            <%
               }else {
            %>
            $('#Message').html("");
            $.ajax({
                type: 'post',
                url: 'POS!editPos',
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        $('#Message').html(json.errorMessage);
                    }
                    else {
                        $('.input').val("");
                        $('#Message').html("<s:text name="global.dosuccess" />");
                        parent.refreshposList(json.posList);
                    }
                }
            })
            <% } %>
        }

        function fetchBankList() {
            $.ajax({
                type: 'post',
                url: 'Bank!FetchBankList',
                data: "uiMode=SELECTLIST",
                dataType: "json",
                success: function (data) {
                    var json = eval("(" + data + ")");
                    $("#bankName").html(json.bankList);
                    $("#bankName").val("<s:property value="posManager.bankuuid"/>");
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
                    $("#industryname").html(json.industryList);
                    $("#industryname").val("<s:property value="posManager.industryuuid"/>");
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
                    $("#rate").html(json.rateList);
                    $("#rate").val("<s:property value="posManager.rateuuid"/>");
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
                    $("#mcc").html(json.mccList);
                    $("#mcc").val("<s:property value="posManager.mccuuid"/>");
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
                    $("#posserver").html(json.posServerList);
                    $("#posserver").val("<s:property value="posManager.posserveruuid"/>");
                }
            });
        }
        function fetchSalemanList() {
            $.ajax({
                type: 'post',
                url: 'Saleman!FetchSalemanList',
                data: "uiMode=SELECTLIST",
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#salesman").html(json.salemanList);
                }
            });
        }
        $(function () {
            fetchBankList();
            fetchIndustryList();
            fetchRateList();
            fetchMCCList();
            fetchSalemanList();
            fetchPosServerList();
        })
    </script>
</head>
<body scroll="no">
    <form class="form form-horizontal">
        <div style="height:auto; overflow:auto;">
            <table class="table table-border table-bordered table-bg table-hover table-sort">
                <tr class="text-c">
                    <td><s:text name="posmanager.posname"/></td><td><input name="posname"  type="text"  value="<s:property value="posManager.posname"/>" placeholder="<s:text name="posmanager.posname" />" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.industryname"/></td>
                    <td>
                        <select id="industryname" name="industryname" style="width: 100%">
                        </select>
                    </td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.rate"/></td>
                    <td>
                    <select id="rate" name="rate" style="width: 100%">
                    </select>
                    </td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.corporation"/></td><td><input name="corporation" type="text"  value="<s:property value="posManager.corporation"/>" placeholder="<s:text name="posmanager.corporation" />" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.topqk"/></td><td><input name="topqk" type="text"  value="<s:property value="posManager.topqk"/>" placeholder="<s:text name="posmanager.topqk" />" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.mcc"/></td>
                    <td>
                    <select id="mcc" name="mcc" style="width: 100%">
                    </select>
                    </td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.posserver"/></td><td>
                    <select id="posserver" name="posserver" style="width: 100%">
                    </select>
                </td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.recipientbank"/></td><td><input name="recipientbank" type="text"  value="<s:property value="posManager.recipientbank"/>" placeholder="<s:text name="posmanager.recipientbank" />" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.recipientaccount"/></td><td><input name="recipientaccount" type="text"  value="<s:property value="posManager.recipientaccount"/>" placeholder="<s:text name="posmanager.recipientaccount" />" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.startdatetm"/></td><td><input name="startdatetm" type="text"  value="<s:property value="posManager.startdatetm"/>" placeholder="<s:text name="posmanager.startdatetm" />" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.usecount"/></td><td><input name="usecount" type="text"  value="<s:property value="posManager.usecount"/>" placeholder="<s:text name="posmanager.usecount" />" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.useamount"/></td><td><input name="useamount" type="text"  value="<s:property value="posManager.useamount"/>" placeholder="<s:text name="posmanager.useamount" />" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="global.status"/></td><td><input name="status" type="checkbox"  <s:if  test="posManager.status =='enable'">checked</s:if><s:else></s:else> > </td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="posmanager.salesman"/></td><td>
                    <select id="salesman" name="salesman" style="width: 100%">
                    </select>
                </tr>
            </table>
        </div>
        <div class="row">
            <div class="formControls col-8 col-offset-3">
                <div id="Message" style="color:#ff0000;font-size: 12px;">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="formControls" align="center">
                <input type="button" class="btn btn-success radius size-M" value="<s:text name="addasset.submit" />" onclick="addPOS();">
            </div>
        </div>
        <input type="hidden" id="newid" name="newid" value="<s:property value="posManager.uuid"/>">
    </form>
</div>
</body>
</html>