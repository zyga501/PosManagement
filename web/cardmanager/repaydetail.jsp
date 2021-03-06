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
    <script type="text/javascript">
        function clickrepay(button, repayid,cardno,billuuid) {
            var val = button.value;
            if (val == "N") {

                layer.open({
                    type: 2,
                    title: "选择资产卡", area: ['310px', '250px'],
                    fix: false,
                    content: "<%=request.getContextPath()%>/cardmanager/repayChoose.jsp",
                    btn: ['确定', '关闭'],
                    yes: function (index) {
                        var res = window["layui-layer-iframe" + index].saveFunc();
                        layer.close(index);

                        layer.confirm('确定还款？', {
                            btn: ['是', '否'] //按钮
                        }, function () {
                            $.ajax({
                                type: 'post',
                                url: 'Repay!EditDetail',
                                data: {status: "enable", repayId: repayid, cardno: cardno, billUUID: billuuid},
                                success: function (data) {
                                    var json = eval("(" + data + ")");
                                    if (json.successMessage) {
                                        layer.msg(json.successMessage);
                                        dosearch();
                                    }
                                    else if (json.errorMessage)
                                        layer.msg(json.errorMessage);
                                }
                            });
                        }, function () {
                        });
                    }
                });
            }
        }

    </script>
</head>
<body style="overflow: hidden">
<div align="center">
    <div class="panel panel-default" >
        <div class="panel-header"><form id="searchform"><table id="searchtb" style="width: 80%"><tr>
            <td><s:text name="repaydetail.paneltitle"/></td>
            <td><input type="text" name="cardno" placeholder="<s:text name="swingcardsummary.cardno"/>" class="input-text radius size-s"></td>
            <td><input type="text" name="cardmaster" placeholder="<s:text name="swingcardsummary.cardmaster"/>" class="input-text radius size-s"></td>
            <td><select name="TRADESTATUS" placeholder="<s:text name="swingcardsummary.status"/>" onchange="dosearch()">
                <option value="all"><s:text name="global.alldata"/></option>
                <option value="finished"><s:text name="swingcardsummary.swingfinished"/></option>
                <option value="unfinished" selected><s:text name="swingcardsummary.swingunfinished"/></option>
            </select></td>
            <td><a href="javascript:void(0);" class="btn btn-primary  radius size-S " onclick="dosearch()">  <s:text name="global.search"/></a>
            </td></tr></table></form></div>
        <div id="navigatediv"></div>
        <div class="panel-body" id="parentIframe">
            <input type="hidden" id="cardNO" value="<s:property value="cardNO" escape="false"/>"/>
            <input type="hidden" id="billUUID" value="<s:property value="billUUID" escape="false"/>"/>
            <form>
                <div style="height:auto; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="repaydetail.cardno"/></th>
                            <th><s:text name="repaydetail.cardmaster"/></th>
                            <th><s:text name="repaydetail.amount"/></th>
                            <th><s:text name="repaysummary.charge"/></th>
                            <th><s:text name="repaydetail.sdatetm"/></th>
                            <th><s:text name="global.operation"/></th>
                        </tr>
                        </thead>
                        <tbody id="repayDetail">
                        <s:property value="repayDetail" escape="false"/>
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
<script type="text/javascript">
    $().ready(function() {
                <s:if test="#request.repayDetail==null">
                dosearch()
                </s:if>
            }
    );
    function dosearch() {
        $.ajax({
            type: 'post',
            url: 'Repay!FetchDetail',
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
                            url: 'Repay!FetchDetail?currpage='+obj.curr,
                            dataType:"json",
                            data:$("#searchform").serialize(),
                            success: function (data) {
                                var json = eval("(" + data + ")");
                                if (json.errorMessage != null) {
                                    layer.msg(json.errorMessage);
                                }
                                else {
                                    $('#repayDetail').html(json.repayDetail);
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