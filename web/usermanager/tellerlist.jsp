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
    <title><s:text name="userlist.title" /></title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript">
        function fetchTellerList(){
            $.ajax({
                type: 'post',
                url: 'Teller!FetchUnAssignTeller',
                data: "",
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#tellerList").html(json.tellerList);
                }
            });
        }

        function clickTeller(id) {
            if(confirm("您确定选择此用户？！")){
                parent.refreshTellerList(id);
                parent.layer.close(parent.layer.getFrameIndex(window.name));
                return true;
            }
            return false;
        }

        function searchtxt(){
            $("table tbody tr").hide();
            var ov=$("#dwtxt").val();
            $("table tbody tr").each(
                    function (i) {
                        if (($(this).children("td").text()).indexOf(ov)>-1){
                            $(this).css("display","");
                        }
                    });
        }

        $(function(){
            fetchTellerList();
            $('#dwtxt').on('input propertychange', function() {
                searchtxt();
            });
        })
    </script>
</head>
<body>
<div class="mt" align="center">
    <div class="row cl ">
        <input class="input-text " placeholder="<s:text name="search.hint"/>" id=dwtxt  >
    </div>
    <form>
        <div class="panel-body" id="tellerList"></div>
    </form>
</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.admin.js"></script>
</body>
</html>