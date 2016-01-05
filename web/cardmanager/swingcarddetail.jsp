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
        function  changestatus(){
            var swingid="";
            var swingidno="";
            $("input[type='checkbox']:checkbox").each(function(){
                if($(this).prop("checked")){
                    swingid +="'"+ $(this).val()+"',"
                }
                else {
                    swingidno +="'"+ $(this).val()+"',"
                }
            })
            $.ajax({
                type: 'post',
                url: 'SwingCard!enableDetail',
                data: {swingIdList:swingid,swingIdNOList:swingidno},
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.successMessage) {
                        layer.msg(json.successMessage);
                    }
                    else if (json.errorMessage)
                        layer.msg(json.errorMessage);
                }
            });
        }
        function clickswing(button, swingid) {
            var val = button.value;
            if (val == "N") {
                layer.confirm('确定启用？', {
                    btn: ['yes', 'no'] //按钮
                }, function () {
                    button.value = "Y";
                    button.setAttribute("class", "btn btn-success radius");
                    layer.msg('你选择了YES', {icon: 1});
                    $.ajax({
                        type: 'post',
                        url: 'SwingCard!editDetail',
                        data: {status:"enable" , swingId:swingid},
                        success: function (data) {
                            var json = eval("(" + data + ")");
                            if (json.successMessage) {
                                button.value = "Y";
                                button.setAttribute("class", "btn btn-success radius");
                                layer.msg(json.successMessage);
                            }
                            else if (json.errorMessage)
                                layer.msg(json.errorMessage);
                        }
                    });
                }, function () {
                });
            }
        }
    </script>
</head>
<body >
<div align="center">
    <div class="panel panel-default" >
        <div class="panel-header"><s:text name="swingcarddetail.paneltitle"/></div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:auto; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="swingcarddetail.thedate"/></th>
                            <th><s:text name="swingcarddetail.cardno"/></th>
                            <th><s:text name="swingcarddetail.cardmaster"/></th>
                            <th><s:text name="swingcarddetail.amount"/></th>
                            <th><s:text name="swingcarddetail.sdatetm"/></th>
                            <th><s:text name="swingcarddetail.machinename"/></th>
                            <th><s:text name="swingcarddetail.teller"/></th>
                            <th><s:text name="swingcarddetail.realsdatetm"/></th>
                            <th><s:text name="swingcarddetail.validstatus"/></th>
                            <th><s:text name="swingcarddetail.swingstatus"/></th>
                        </tr>
                        </thead>
                        <tbody id="swingCardDetail">
                        <s:property value="swingCardDetail" escape="false"/>
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
<script type="text/javascript">
    $().ready( function(){
           // $("input[type='checkbox']").on("click", function() {changestatus();});
        }
    )
</script>
</body>
</html>