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
    <title><s:text name="mccmanager.title" /></title>
    <script type="text/javascript">
        function addMCC(){
            var index = layer.open({
                type: 2,
                title: "添加MCC",area: ['310px', '220px'],
                fix: false,
                content: "./systemmanager/addmcc.jsp"
            });}
        function editMCC(val) {
            var index = layer.open({
                type: 2,
                title: "<s:text name="global.edit"/>",area: ['310px', '220px'],
                fix: true,
                maxmin: false,
                content: "MCC!FetchMCC?UUID=" + val
            });
        }
        function refreshMCCList(mccList) {
            $('#mccList').html(mccList);
            init();
        }
    </script>
</head>
<body>
<div align="center">
    <div class="panel panel-default" style="float: left;width: 44%">
        <div class="panel-header"><s:text name="mccmanager.paneltitle" /><span style="float:right;" ><a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="addMCC()"><s:text name="mccmanager.add" /></a></span></div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:80%; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th width="80"><s:text name="mccmanager.mcc" /></th>
                            <th width="80"><s:text name="global.status" /></th>
                        </tr>
                        </thead>
                        <tbody id="mccList">
                            <s:property value="mccList" escape="false" />
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
<script type="text/javascript" >
    $().ready(
            init()
    )
    function init(){
        $("tr").click(function() {
            editMCC($(this).attr("value"));
        })
    }
</script>
</body>
</html>
