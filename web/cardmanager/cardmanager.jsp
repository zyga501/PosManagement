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
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/js/laypage//skin/laypage.css" id="laypagecss">
    <title><s:text name="bankmanager.title"/></title>
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
        function editcard() {
            var index = layer.open({
                type: 2,
                title: "<s:text name="global.edit"/>",
                fix: true,
                maxmin: false,
                content: "Card!FetchCard?" + $("form").serialize()
            });
            layer.full(index);
        }
        function refreshcardList(cardList) {
            $('#cardList').html(cardList);
        }
    </script>
</head>
<body >
<div align="center">
    <div class="panel panel-default">
        <div class="panel-header"><s:text name="cardmanager.paneltitle"/><span style="float:right;">
            <a href="javascript:void(0);" class="btn btn-warning  radius size-S " onclick="editcard()">
                <s:text name="global.edit"/></a></span> <span style="float:right;">
            <a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="
            dosearch();">
              2  <s:text name="global.add"/></a></span></div>
        <div id="navigatediv"></div>
        <div class="panel-body" id="parentIframe" >
            <form>
                <div >
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="global.sequence"/></th>
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
                        <s:property value="cardList" escape="false"/>
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
    function dosearch() {
        laypage({
            cont: 'navigatediv',
            pages: ${cardList.pages},
            skip: true,
            skin: 'yahei',
            jump: function (obj) {
                alert(obj.curr);
                refreshcardList(${cardList});
              //  document.getElementById('biuuu_city_list').innerHTML = thisDate(obj.curr);
            }
        })
    }
</script>
</body>
</html>