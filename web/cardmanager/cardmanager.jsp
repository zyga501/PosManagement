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
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/js/laypage/skin/laypage.css" id="laypagecss">
    <title></title>
    <style type="text/css" >
        #searchtb tr td {
            padding-right: 1em;
        }
    </style>
    <script type="text/javascript">
        function addcard() {
            var index = layer.open({
                type: 2,
                title: "<s:text name="global.add"/>",
                fix: true,
                maxmin: false,
                content: "./cardmanager/addcard.jsp"
            });
            layer.full(index);
        }
        function editcard(val) {
            if (val==undefined) return;//不知道为什么执行上面的addcard也触发这个事件
            var index = layer.open({
                type: 2,
                title: "<s:text name="global.edit"/>",
                fix: true,
                maxmin: false,
                content: "Card!FetchCard?newid=" + val//$ ("form").serialize()
            });
            layer.full(index);
        }
        function refreshcardList(cardList) {
            $('#cardList').html(cardList);
            init();
        }
    </script>
</head>
<body >
<div align="center">
    <div class="panel panel-default">
        <div class="panel-header">
            <div class="pd-5" style="width: 80%"><form id="searchform"><table id="searchtb"><tr>
                <td><input type="text" name="cardno" placeholder="<s:text name="cardmanager.cardno"/>" class="input-text radius size-s"></td>
                <td><input type="text" name="bankname" placeholder="<s:text name="cardmanager.bankname"/>" class="input-text radius size-s"></td>
                <td><input type="text" name="cardmaster" placeholder="<s:text name="cardmanager.cardmaster"/>" class="input-text radius size-s"></td>
                <% if (request.getSession().getAttribute("roleId").equals("e664d6f3-85f8-4bd6-bcb8-c4e053732b29")){ %>
                <td><input type="text" name="salesman" placeholder="<s:text name="cardmanager.salesman"/>" class="input-text radius size-s"></td><%}%>
                <td><a href="javascript:void(0);" class="btn btn-primary  radius size-S " onclick="dosearch()">
                    <s:text name="global.search"/></a><span style="float:right;"> <a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="
            addcard();"><s:text name="global.add"/></a></span></td></tr></table></form></div></div>
        <div id="navigatediv"></div>
        <div class="panel-body" id="parentIframe" >
            <form>
                <div >
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="cardmanager.cardno"/></th>
                            <th><s:text name="cardmanager.creditamount"/></th>
                            <th><s:text name="cardmanager.billdate"/></th>
                            <th><s:text name="cardmanager.bankname"/></th>
                            <th><s:text name="cardmanager.cardmaster"/></th>
                            <th><s:text name="cardmanager.cmtel"/></th>
                            <% if (request.getSession().getAttribute("roleId").equals("e664d6f3-85f8-4bd6-bcb8-c4e053732b29")){ %>
                            <th><s:text name="cardmanager.salesman"/></th><%}%>
                        </tr>
                        </thead>
                        <tbody id="cardList">
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
    $().ready( function(){
        laypage({
            cont: 'navigatediv',
            pages: ${pagecount},
            skip: true,
            skin: 'yahei',
            jump: function (obj) {
                $.ajax({
                    type: 'post',
                    url: 'Card!FetchCardList?currpage='+obj.curr,
                    dataType:"json",
                    data:$("#searchform").serialize(),
                    success: function (data) {
                        var json = eval("(" + data + ")");
                        if (json.errorMessage != null) {
                            layer.msg(json.errorMessage);
                        }
                        else {
                            refreshcardList(json.cardList);
                        }
                    }
                })
            }
        })});
    function init(){
        $("tr").click(function() {
            editcard($(this).attr("value"));
        })
    }
    function dosearch() {
        $.ajax({
            type: 'post',
            url: 'Card!FetchCardList',
            dataType:"json",
            data:$("#searchform").serialize(),
            success: function (data) {
                var json = eval("(" + data + ")");
                if (json.errorMessage != null) {
                    layer.msg(json.errorMessage);
                }
                else {
                   // refreshcardList(json.cardList);
                }
                laypage({
                    cont: 'navigatediv',
                    pages: json.pagecount,
                    skip: true,
                    skin: 'yahei',
                    jump: function (obj) {
                        $.ajax({
                            type: 'post',
                            url: 'Card!FetchCardList?currpage='+obj.curr,
                            dataType:"json",
                            data:$("#searchform").serialize(),
                            success: function (data) {
                                var json = eval("(" + data + ")");
                                if (json.errorMessage != null) {
                                    layer.msg(json.errorMessage);
                                }
                                else {
                                    refreshcardList(json.cardList);
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