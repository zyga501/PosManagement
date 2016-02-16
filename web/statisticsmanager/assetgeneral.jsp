<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/dateRangeUtil.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script >
        function setval(v){
            $('#assetgeneralsummary').html(v);
        }
        function explist(){
            alert('开发中。。。稍等');
        }
    </script>
</head>
<body align="center" >
<div class="panel panel-default">
    <div class="panel-header " align="center">
        <div class="row cl panel-header ">
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
        <div pd-5 style="width: 80%;margin: 0 auto;">
            <form id="searchform" onsubmit="return false;">
                <div class="row cl ">
                    <div class="formControls col-3">
                        <input type="text" onfocus="WdatePicker()" id="sdate"
                               name="sdate" class="input-text Wdate" placeholder="开始日期"
                               AutoComplete="off" style="width:auto;">
                    </div>
                    <div class="formControls col-3"><input type="text" AutoComplete="off"
                                                           onfocus="WdatePicker()" id="edate" name="edate"
                                                           placeholder="结束日期" class="input-text Wdate" style="width:auto;">
                    </div>
                    <div class="formControls col-2">
                        <input name="saleman" placeholder="业务员" class="input-text">
                        <%--<span style="width:40%;">业务员</span><select name="salemanList" id="salemanid"--%>
                                                                                               <%--class="input-text" style="width:60%" placeholder="业务员"--%>
                                                                                               <%--AutoComplete="off"></select> --%>
                    </div>
                    <div class="formControls col-1"><input type="button" value="<s:text name="global.search" />" onclick="dosearch()" class="btn btn-primary radius size-S"></div>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="cl pd-5 bg-1 bk-gray mt-20">
	 <span style="float:left;">共<strong><span id="cnt"></span></strong> 条，还款 <strong><span id="repay">
     </span></strong> 还款收费 <strong><span id="repaycharge"></span></strong> 结算到账 <strong><span id="inbank"></span></strong> 刷卡扣费 <strong><span id="charge"></span></strong> 产生利润 <strong><span id="profit"></span></strong></span>
		<span style="float:right;">
		<a href="javascript:void(0);" title="<s:text name="global.exportdate" />"
           class="btn btn-danger radius size-MINI" onclick="explist();"> <i
                class="Hui-iconfont">&#xe644;</i><s:text name="global.exportdate" />
        </a>
	</span>
</div>
<div id="expdiv"
     style="display:none;position:fixed;z-index:901;top:30%;width:100%;height:400px;background-color:gray;  filter:alpha(opacity=70);opacity:0.7;vertical-align:middle; align:center;"></div>
<div class="mt"></div>
<div id="navigatediv"></div>
<div class="panel-body" id="parentIframe">
    <form>
        <div style="height:100%; overflow:auto;">
            <table class="table table-border table-bordered table-bg table-hover table-sort">
                <thead>
                <tr class="text-c">
                    <th><s:text name="repaydetail.thedate"/></th>
                    <th><s:text name="statistics.type"/></th>
                    <th><s:text name="swingcardsummary.amount"/></th>
                    <th><s:text name="statistics.balance"/></th>
                    <th><s:text name="statistics.general"/></th>
                </tr>
                </thead>
                <tbody id="assetgeneralsummary">
                </tbody>
            </table>
        </div>
    </form>
</div>
</div>
<script src="<%=request.getContextPath()%>/js/laypage/laypage.js"></script>
<script>
    function dosearch() {
        $.ajax({
            type: 'post',
            url: 'Statistics!FetchAssetGeneral',
            dataType:"json",
            data:$("#searchform").serialize(),
            success: function (data) {
                var json = eval("(" + data + ")");
                if (json.errorMessage != null) {
                    layer.msg(json.errorMessage);
                }
                else {
                    setval(json.assetGeneral);
                    $('#cnt').html(json.cnt);
                    $('#amount').html(json.amount);
                    $('#charge').html(json.charge);
                    $('#inbank').html(json.inbank);
                }
                laypage({
                    cont: 'navigatediv',
                    pages: json.pagecount,
                    skip: true,
                    skin: 'yahei',
                    jump: function (obj) {
                        $.ajax({
                            type: 'post',
                            url: 'Statistics!FetchAssetGeneral?currpage='+obj.curr,
                            dataType:"json",
                            data:$("#searchform").serialize(),
                            success: function (data) {
                                var json = eval("(" + data + ")");
                                if (json.errorMessage != null) {
                                    layer.msg(json.errorMessage);
                                }
                                else {
                                    setval(json.assetGeneral);
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