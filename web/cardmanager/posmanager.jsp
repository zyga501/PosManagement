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
        function addpos() {
            var index = layer.open({
                type: 2,
                title: "<s:text name="global.add"/>",
                fix: false,area: ['310px', '500px'],
                maxmin: false,
                content: "./cardmanager/addpos.jsp"
            });
        }
        function editpos(val) {
            if (val==undefined) return;//不知道为什么执行上面的addcard也触发这个事件
            var index = layer.open({
                type: 2,
                title: "<s:text name="global.edit"/>",
                fix:false,area: ['310px', '500px'],
                maxmin: false,
                content: "POS!FetchPOS?newid="+val+'&'+$("form").serialize()
            });
        }
        function refreshposList(posList) {
            $('#posList').html(posList);
            init();
        }
    </script>
</head>
<body >
<div align="center">
    <div class="panel panel-default"  >
        <div class="panel-header">
            <div class="pd-5" style="width: 80%"><form id="searchform"><table id="searchtb"><tr>
                <td><input type="text" name="posname" placeholder="<s:text name="posmanager.posname"/>" class="input-text radius size-s"></td>
                <td><input type="text" name="industryname" placeholder="<s:text name="posmanager.industryname"/>" class="input-text radius size-s"></td>
                <td><input type="text" name="rate" placeholder="<s:text name="posmanager.rate"/>" class="input-text radius size-s"></td>
                <td><input type="text" name="posserver" placeholder="<s:text name="posmanager.posserver"/>" class="input-text radius size-s"></td>
                <td><a href="javascript:void(0);" class="btn btn-primary  radius size-S " onclick="dosearch()">
                    <s:text name="global.search"/></a><span style="float:right;">
            <a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="addpos()">
                <s:text name="global.add"/></a></span></td></tr></table></form></div></div>
        <div id="navigatediv"></div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div >
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="posmanager.posname"/></th>
                            <th><s:text name="posmanager.industryname"/></th>
                            <th><s:text name="posmanager.rate"/></th>
                            <th><s:text name="posmanager.posserver"/></th>
                            <th><s:text name="posmanager.mcc"/></th>
                            <th><s:text name="posmanager.startdatetm"/></th>
                            <th><s:text name="posmanager.usecount"/></th>
                            <th><s:text name="posmanager.useamount"/></th>
                            <th><s:text name="global.status"/></th>
                        </tr>
                        </thead>
                        <tbody id="posList">
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
                url: 'POS!FetchPosList?currpage='+obj.curr,
                dataType:"json",
                data:$("#searchform").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        layer.msg(json.errorMessage);
                    }
                    else {
                        refreshposList(json.posList);
                    }
                }
            })
        }
    }) );

    function init(){
        $("tr").click(function() {
            editpos($(this).attr("value"));
        })
    }

    function dosearch() {
        $.ajax({
            type: 'post',
            url: 'POS!FetchPosList',
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
                            url: 'POS!FetchPosList?currpage='+obj.curr,
                            dataType:"json",
                            data:$("#searchform").serialize(),
                            success: function (data) {
                                var json = eval("(" + data + ")");
                                if (json.errorMessage != null) {
                                    layer.msg(json.errorMessage);
                                }
                                else {
                                    refreshposList(json.posList);
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