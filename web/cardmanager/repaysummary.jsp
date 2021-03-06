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
    <style type="text/css" >
        #searchtb tr td {
            padding-right: 1em;
        }
    </style>
    <script type="text/javascript">
        function clickDetail(cardNo, billUUID) {
            var index = layer.open({
                type: 2,
                title: "还卡明细",
                fix: true,
                maxmin: false,
                content: "Repay!InitDetail?cardno=" + cardNo + "&billUUID=" + billUUID
            });
            layer.full(index);
        }
    </script>
</head>
<body style="overflow: hidden">
<div align="center">
    <div class="panel panel-default" >
        <div class="panel-header"><form id="searchform"><table id="searchtb" style="width: 80%"><tr>
            <td><input type="text" name="thedate" placeholder="<s:text name="repaysummary.lastrepaymentdate"/>" class="input-text radius size-s"></td>
            <td><input type="text" name="cardno" placeholder="<s:text name="swingcardsummary.cardno"/>" class="input-text radius size-s"></td>
            <td><input type="text" name="bankname" placeholder="<s:text name="cardmanager.bankname"/>" class="input-text radius size-s"></td>
            <td><input type="text" name="cardmaster" placeholder="<s:text name="swingcardsummary.cardmaster"/>" class="input-text radius size-s"></td>
            <td><select name="SWINGSTATUS" placeholder="<s:text name="swingcardsummary.status"/>">
                <option value="all"><s:text name="global.alldata"/></option></option>
                <option value="finished"><s:text name="global.finished"/></option>
                <option value="unfinished"><s:text name="global.unfinished"/></option>
            </select></td>
            <td><a href="javascript:void(0);" class="btn btn-primary  radius size-S " onclick="dosearch()">  <s:text name="global.search"/></a>
            </td></tr></table></form></div></div>
    <div id="navigatediv"></div>
    <div class="panel-body" id="parentIframe">
        <form>
            <div style="height:100%; overflow:auto;">
                <table class="table table-border table-bordered table-bg table-hover table-sort">
                    <thead>
                    <tr class="text-c">
                        <th><s:text name="repaysummary.thedate"/></th>
                        <th><s:text name="repaysummary.cardno"/></th>
                        <th><s:text name="repaysummary.lastrepaymentdate"/></th>
                        <th><s:text name="repaysummary.cardmaster"/></th>
                        <th><s:text name="repaysummary.amount"/></th>
                        <th><s:text name="repaysummary.charge"/></th>
                        <th><s:text name="global.operation"/></th>
                    </tr>
                    </thead>
                    <tbody id="repaySummary">
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

<script src="<%=request.getContextPath()%>/js/laypage/laypage.js"></script>
<script>
    $().ready
    (laypage({
        cont: 'navigatediv',
        pages: ${pagecount},
        skip: true,
        skin: 'yahei',
        jump: function (obj) {
            $.ajax({
                type: 'post',
                url: 'Repay!FetchRepayList?currpage='+obj.curr,
                dataType:"json",
                data:$("#searchform").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        layer.msg(json.errorMessage);
                    }
                    else {
                        refreshrepaySummaryList(json.repaySummary);
                    }
                }
            })
        }
    }) );

    function refreshrepaySummaryList(repaySummary) {
        $('#repaySummary').html(repaySummary);
    }
    function dosearch() {
        $.ajax({
            type: 'post',
            url: 'Repay!FetchRepayList',
            dataType:"json",
            data:$("#searchform").serialize(),
            success: function (data) {
                var json = eval("(" + data + ")");
                if (json.errorMessage != null) {
                    layer.msg(json.errorMessage);
                }
                else {
                }
                laypage({
                    cont: 'navigatediv',
                    pages: json.pagecount,
                    skip: true,
                    skin: 'yahei',
                    jump: function (obj) {
                        $.ajax({
                            type: 'post',
                            url: 'Repay!FetchRepayList?currpage='+obj.curr,
                            dataType:"json",
                            data:$("#searchform").serialize(),
                            success: function (data) {
                                var json = eval("(" + data + ")");
                                if (json.errorMessage != null) {
                                    layer.msg(json.errorMessage);
                                }
                                else {
                                    refreshrepaySummaryList(json.repaySummary);
                                }
                            }
                        })
                    }
                })
            }
        })
    }
</script>
</body>
</html>