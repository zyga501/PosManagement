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
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/layer/1.9.3/layer.js"></script>
    <title></title>
    <script type="text/javascript">
        function addBill() {
            layer.confirm('确定生成？', {
                btn: ['是', '不'] //按钮
            }, function () {
                $.ajax({
                    type: 'post',
                    url: 'Bill!makeBill',
                    dataType: "json",
                    data: $("form").serialize(),
                    success: function (data) {
                        var json = eval("(" + data + ")");
                        if (json.errorMessage != null) {
                            $('#Message').html(json.errorMessage);
                        }
                        else {
                            parent.layer.msg("<s:text name="global.dosuccess" />");
                            parent.refreshBillList(json.billList);
                            parent.layer.close(parent.layer.getFrameIndex(window.name));
                        }
                    }
                })
            })
        }

        function searchmaster(){
            $.ajax({
                type: 'post',
                url: 'Card!FetchMaster',
                dataType: "json",
                data:{"cardno":$("#cardno").val()},
                success: function (data) {
                    var json = eval("(" + data + ")");
                    $('#cardMaster').val(json.cardMaster);
                    $('#bankName').val("");
                }
            })
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
                }
            });
        }

        $(function () {
            fetchBankList();
        })
    </script>
</head>
<body scroll="no">
<div>
    <form class="form form-horizontal">
        <table class="table table-border table-bordered table-bg table-hover table-sort">
            <tr class="text-c odd" role="row">
                <td><s:text name="billmanager.billdate"/></td>
                <td>
                    <input id="billDate" name="billDate" type="text"
                           placeholder="<s:text name="billmanager.billdate" />" onfocus="WdatePicker()"
                           class="input-text Wdate">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="billmanager.bankname"/></td>
                <td>
                    <select id="bankName" name="bankName" style="width: 100%">
                    </select></td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="billmanager.cardno"/></td>
                <td>
                    <div class="row cl">
                        <div class="formControls col-10">
                            <input id="cardno" name="cardno" type="text"
                                   placeholder="<s:text name="billmanager.cardno" />" class="input-text size-S">
                        </div>
                        <div class="formControls col-2"><input type="button" onclick="searchmaster()"
                                                               value="<s:text name="billmanager.search" />"
                                                               class="btn btn-primary radius size-S ">
                        </div>
                    </div>
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="assetmanager.assetMaster"/></td>
                <td>
                    <input id="cardMaster" type="text" readonly="readonly"
                           placeholder="<s:text name="assetmanager.assetMaster" />" class="input-text size-S">
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
            <div class="formControls" align="center">
                <input type="button" class="btn btn-success radius size-M"
                       value="<s:text name="billmanager.makebill" />" onclick="addBill()">
            </div>
        </div>
    </form>
    <div style="height:auto; overflow:auto;">
        <table class="table table-border table-bordered table-bg table-hover table-sort">
            <thead>
            <tr class="text-c">
                <th><s:text name="global.sequence" /></th>
                <th><s:text name="cardmanager.cardmaster" /></th>
                <th><s:text name="billmanager.cardno" /></th>
                <th><s:text name="billmanager.bankname" /></th>
            </tr>
            </thead>
            <tbody id="billList">
            </tbody>
        </table>
    </div>
</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
    $().ready(function(){
        $.ajax({
            type: 'post',
            url: 'Bill!FetchTodayBillList',
            dataType:"json",
            success: function (data) {
                var json = eval("(" + data + ")");
                if (json.errorMessage != null) {
                    layer.msg(json.errorMessage);
                }
                else {
                    $("#billList").html(json.billList);
                }
            }
        })
    })
</script>
</body>
</html>