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
            var repayid="";
            var repayidno="";
            $("input[type='checkbox']:checkbox").each(function(){
                if($(this).prop("checked")){
                    repayid +="'"+ $(this).val()+"',"
                }
                else {
                    repayidno +="'"+ $(this).val()+"',"
                }
            })
            $.ajax({
                type: 'post',
                url: 'Repay!enableDetail',
                data: {repayIdList:repayid,repayIdNOList:repayidno},
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
        function clickrepay(button, repayid) {
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
                        url: 'Repay!editDetail',
                        data: {status:"enable" , repayId:repayid},
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
<body style="overflow: hidden">
<div align="center">
    <div class="panel panel-default" >
        <div class="panel-header"><s:text name="repaydetail.paneltitle"/><span style="float:right;">
            <a href="javascript:void(0);" class="btn btn-warning  radius size-S " onclick="changestatus()">
                <s:text name="global.edit"/></a></span></div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:auto; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="repaydetail.thedate"/></th>
                            <th><s:text name="repaydetail.cardno"/></th>
                            <th><s:text name="repaydetail.cardmaster"/></th>
                            <th><s:text name="repaydetail.amount"/></th>
                            <th><s:text name="repaydetail.sdatetm"/></th>
                            <th><s:text name="repaydetail.teller"/></th>
                            <th><s:text name="repaydetail.realsdatetm"/></th>
                            <th><s:text name="repaydetail.validstatus"/></th>
                            <th><s:text name="repaydetail.repaystatus"/></th>
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
<script type="text/javascript">
    $().ready( function(){
           // $("input[type='checkbox']").on("click", function() {changestatus();});
        }
    )
</script>
</body>
</html>