<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
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
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/dateRangeUtil.js"></script>
    <style type="text/css">
        .Huifold .item{position:relative}
        .Huifold .item h4{margin:0;font-weight:normal;position:relative;border-top:1px solid #fff;font-size:15px;line-height:22px;
            padding:7px 10px;background-color:#eee;cursor:pointer;padding-right:30px}
        .Huifold .item h4 b{position:absolute;display:block;cursor:pointer;right:10px;top:7px;width:16px;height:16px;text-align:center;color:#666}
        .Huifold .item .info{display:none;padding:10px}
        .Huifold i{color: forestgreen;}
        fn {
            color: #ab1e1e;
        }
        swing {
            color: #ab1e1e;
        }
        repay {
            color: #00cc00;
        }
        .table td{padding:2px}
        u {
            color: #0000FF;
        }
    </style>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/layer/1.9.3/layer.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.admin.js"></script>
    <script type="text/javascript" >
        function bindexp(){
            $("#Huifold1 .item").append("<div class='info' id='info'>"+
                    "<table class='table table-border table-bordered table-bg table-hover table-sort'>"+
                    "<thead>"+
                    "<tr class='text-c' >"+
                    "<th style='width: 50px;font-size: 180%;font-weight: bolder;color: #00CC00'><repay>还</repay></th>"+
                    "<th><s:text name='repaydetail.amount'/></th>"+
                    "<th><s:text name='repaysummary.charge'/></th>"+
                    "<th><s:text name='repaydetail.sdatetm'/></th>"+
                    "<th><s:text name='global.operation'/></th>"+
                    "</tr>"+
                    "</thead>"+
                    "<tbody id='repaydetail'>"+
                    "</tbody>"+
                    "</table> "+
                    "<table class='table table-border table-bordered table-bg table-hover table-sort'>"+
                    "<thead>"+
                    "<tr class='text-c'>"+
                    "<th style='width: 50px;font-size: 180%;font-weight: bolder;color: #ab1e1e'><swing>刷</swing></th>"+
                    "<th><s:text name="swingcarddetail.amount"/></th>"+
                    "<th><s:text name="statistics.paymoney"/></th>"+
                    "<th ><s:text name="swingcarddetail.machinename"/></th>"+
                    "<th><s:text name="swingcarddetail.sdatetm"/></th>"+
                    "<th><s:text name="global.operation"/></th>"+
                    "</tr>"+
                    "</thead>"+
                    "<tbody id='swingdetail'>"+
                    "</tbody>"+
                    "</table> "+
                    "</div>")
            $.Huifold("#Huifold1 .item h4","#Huifold1 .item .info","fast",1,"click");
        };

        function expandbill(obj,v){
            //var _text = $(obj).children(".info").children("table tbody").html();
            //if (null==_text || _text==""||_text==undefined) 
            {
                $.ajax({
                    type: 'post',
                    url: 'Bill!FetchbillDetail',
                    dataType: "json",
                    data: {billuuid:v,startdate:_sdate,enddate:_edate,status:_status},
                    success: function (data) {
                        var json = eval("(" + data + ")");
                        if (json.errorMessage != null) {
                            layer.msg(json.errorMessage);
                        }
                        else if (json.removeitem != null){
                            $(obj).remove();
                        }
                        else{
                            _billuuid = v;
                            $(obj).find("#repaydetail").html(json.repaydetail);
                            $(obj).find("#swingdetail").html(json.swingdetail);
                        }
                    }
                });
            }
        }
    </script>
</head>
<body >
<div align="center">
    <td class="panel panel-default" ><div class="row cl panel-header ">
        <input type="button" class="size-MINI btn btn-warning radius" id="todaybut"
               onclick="$('#sdate').val( DateUtil.Format(dateRangeUtil.getCurrentDate()));$('#edate').val( DateUtil.Format(dateRangeUtil.getCurrentDate()));"
               value="今天">
        <input type="button" class="btn btn-link size-MINI" id="yestoday"
               onclick="$('#sdate').val( DateUtil.Format(dateRangeUtil.getPreviousDate()));$('#edate').val( DateUtil.Format(dateRangeUtil.getPreviousDate()));"
               value="昨天">
        <input type="button" class="btn btn-link size-MINI" id="thisweek"
               onclick="$('#sdate').val( DateUtil.Format(dateRangeUtil.getCurrentWeek()[0]).toString());$('#edate').val( DateUtil.Format(dateRangeUtil.getCurrentWeek()[1]).toString());"
               value="这周">
        <input type="button" class="btn btn-link size-MINI"
               id="onweek"
               onclick=" $('#sdate').val( DateUtil.Format(dateRangeUtil.getPreviousWeek()[0]).toString());$('#edate').val( DateUtil.Format(dateRangeUtil.getPreviousWeek()[1]).toString());"
               value="上周">
        <input type="button"
               class="btn btn-link size-MINI"
               id="thismonth"
               onclick="$('#sdate').val( DateUtil.Format(dateRangeUtil.getCurrentMonth()[0]).toString());$('#edate').val(  DateUtil.Format(dateRangeUtil.getCurrentMonth()[1]).toString());"
               value="本月">
        <input type="button"
               class="btn btn-link size-MINI"
               id="nextmonth"
               onclick="$('#sdate').val(  DateUtil.Format(dateRangeUtil.getPreviousMonth()[0]).toString());$('#edate').val( DateUtil.Format(dateRangeUtil.getPreviousMonth()[1]));"
               value="上月">
    </div>
        <td class="panel-header"><form id="searchform"><table id="searchtb" style="width: 80%"><tr> <td>
            <input type="text" onfocus="WdatePicker()" id="sdate"
                   name="sdate" class="input-text Wdate" placeholder="开始日期"
                   AutoComplete="off" style="width:100%;">
        </td>
            <td><input type="text" AutoComplete="off"
                                                   onfocus="WdatePicker()" id="edate" name="edate"
                                                   placeholder="结束日期" class="input-text Wdate" style="width:100%;">
            </td>
            <td><input type="text" name="cardno" placeholder="<s:text name="swingcardsummary.cardno"/>" class="input-text radius size-s"></td>
            <td><input type="text" name="cardmaster" placeholder="<s:text name="swingcardsummary.cardmaster"/>" class="input-text radius size-s"></td>
            <td><select name="status" id="status" placeholder="<s:text name="swingcardsummary.status"/>" class="input-text radius size-s">
                <option value="all"><s:text name="global.alldata"/></option>
                <option value="finished"><s:text name="swingcardsummary.swingfinished"/></option>
                <option value="unfinished" selected><s:text name="swingcardsummary.swingunfinished"/></option>
            </select></td>
            <td><a href="javascript:void(0);" class="btn btn-primary  radius size-S " onclick="dosearch();">  <s:text name="global.search"/></a>
            </td></tr></table></form></div></div>
    <div id="navigatediv"></div>
<ul id="Huifold1" class="Huifold">
</ul>
<script src="<%=request.getContextPath()%>/js/laypage/laypage.js"></script>
<script>
    var _sdate;
    var _edate;
    var _status;
    var _billuuid;
    $().ready(function(){
        <%
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date= format.format(new Date());
        %>
        $("#sdate").val("<%=date%>");
        $("#edate").val("<%=date%>");
        dosearch();
    });

    function dosearch() {
        $.ajax({
            type: 'post',
            url: 'Bill!FetchbillDetailList',
            dataType:"json",
            data:$("#searchform").serialize(),
            success: function (data) {
                _sdate = $("#sdate").val();
                _edate = $("#edate").val();
                _status = $("#status").val();
                var json = eval("(" + data + ")");
                if (json.errorMessage != null) {
                    layer.msg(json.errorMessage);
                }
                else {
                    $('#Huifold1').html(json.htmlstring);
                    bindexp();
                }
                laypage({
                    cont: 'navigatediv',
                    pages: json.pagecount,
                    skip: true,
                    skin: 'yahei',
                    jump: function (obj) {
                        $.ajax({
                            type: 'post',
                            url: 'Bill!FetchbillDetailList?currpage='+obj.curr,
                            dataType:"json",
                            data:$("#searchform").serialize(),
                            success: function (data) {
                                var json = eval("(" + data + ")");
                                if (json.errorMessage != null) {
                                    layer.msg(json.errorMessage);
                                }
                                else {
                                    $('#Huifold1').html(json.htmlstring);
                                    bindexp();
                                }
                            }
                        })
                    }
                })
            }
        })
    }

    function clickSwingDetail(obj, swingid) {
        var _obj = $(obj).parent().parent().parent().parent().parent().parent();
        var val = obj.value;
        if (val == "N") {
            layer.confirm('确定启用？', {
                btn: ['是','否'] //按钮
            }, function () {
                $.ajax({
                    type: 'post',
                    url: 'SwingCard!EditDetail',
                    data: {status:"enable" , swingId:swingid, billUUID:_billuuid},
                    success: function (data) {
                        var json = eval("(" + data + ")");
                        if (json.successMessage) {
                            layer.msg(json.successMessage);
                            expandbill(_obj,_billuuid);
                        }
                        else if (json.errorMessage)
                            layer.msg(json.errorMessage);
                    }
                });
            }, function () {
            });
        }
    }

    function clickRepayDetail(obj, repayid) {
        var newobj = $(obj).parent().parent().parent().parent().parent().parent();
        var val = obj.value;
        if (val == "N") {
            layer.open({
                type: 2,
                title: "选择资产卡",area: ['310px', '250px'],
                fix: false,
                content: "<%=request.getContextPath()%>/cardmanager/assetChoose.jsp",
                btn: ['确定','关闭'],
                yes:function(index){
                var res = window["layui-layer-iframe" + index].saveFunc();
                    layer.close(index);
                        layer.confirm('确定还款？', {
                            btn: ['是','否'] //按钮
                        }, function () {
                            $.ajax({
                                type: 'post',
                                url: 'Repay!EditDetail',
                                data: {status:"enable" , repayId:repayid, billUUID:_billuuid,assetuuid:res},
                                success: function (data) {
                                    var json = eval("(" + data + ")");
                                    if (json.successMessage) {
                                        layer.msg(json.successMessage);
                                        expandbill($(newobj),_billuuid);
                                    }
                                    else if (json.errorMessage)
                                        layer.msg(json.errorMessage);
                                }
                            });
                        });
                }
            });
        }
    }

</script>
</body>
</html>