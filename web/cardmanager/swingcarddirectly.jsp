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
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/Validform_v5.3.2_min.js"></script>
    <title><s:text name="swingcarddirectly.title"/></title>
    <script type="text/javascript">
        $(function () {
            $("#vform").Validform({btnSubmit:"#submit_btn",
                beforeSubmit: function(curform) {swingDirectly();return false; }
            });
        })
        function swingDirectly() {
            $.ajax({
                type: 'post',
                url: 'SwingCard!SwingCardDirectly',
                dataType: "json",
                data: $("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        layer.msg(json.errorMessage,{icon: 2});
                    }
                    else {
                        $('.input').val("");
                        parent.layer.msg("<s:text name="global.dosuccess" />");
                        parent.layer.close(parent.layer.getFrameIndex(window.name));
                    }
                }
            })
        }

        function fetchPosList() {
            $.ajax({
                type: 'post',
                url: 'POS!FetchPosListEx',
                data: "uiMode=SELECTLIST",
                dataType: "json",
                success: function (data) {
                    var json = eval("(" + data + ")");
                    $("#posUUID").html(json.posList);
                }
            });
        }

        $(function () {
            fetchPosList();
        })
    </script>
</head>
<body scroll="no">
<div>
    <form class="form form-horizontal" id="vform">
        <input type="hidden" id="assetUUID" value="<s:property value="assetUUID" escape="false"/>"/>
        <table class="table table-border table-bordered table-bg table-hover table-sort">
            <tr class="text-c odd" role="row">
                <td><s:text name="swingcarddirectly.money"/></td>
                <td>
                    <input id="swingMoney" name="swingMoney" type="text" datatype="/^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/"
                           placeholder="<s:text name="swingcarddirectly.money" />" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c">
                <td><s:text name="swingcarddirectly.posname"/></td>
                <td>
                    <select id="posUUID" name="posUUID" style="width: 100%" datatype="*">
                    </select>
                </td>
            </tr>
            <tr class="text-c">
                <td><s:text name="cardmanager.cardno"/></td>
                <td>
                    <input id="cardno" name="cardno" type="text" class="input-text size-S" datatype="*">
                </td>
            </tr>
            <tr class="text-c">
                <td><s:text name="cardmanager.cardmaster"/></td>
                <td>
                    <input id="cardmaster" name="cardmaster" type="text" class="input-text size-S" datatype="*">
                </td>
            </tr>
            <tr class="text-c">
                <td><s:text name="cardmanager.bankname"/></td>
                <td>
                    <input id="bank" name="bank" type="text" class="input-text size-S" datatype="*">
                </td>
            </tr>
            <tr class="text-c">
                <td><s:text name="swinggengral.charge"/></td>
                <td>
                    <input id="charge" name="charge" type="text" class="input-text size-S" datatype="/^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/">
                </td>
            </tr>
            <tr class="text-c">
                <td><s:text name="swingcarddetail.sdatetm"/></td>
                <td>
                    <input id="realdate" name="realdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"  class="input-text size-S" datatype="*">
                </td>
            </tr>
        </table>
        <div class="row">
            <div class="formControls col-8 col-offset-3" align="center">
                <input type="button" id="submit_btn" class="btn btn-success radius size-M" value="<s:text name="global.submit" />">
            </div>
        </div>
    </form>
</div>
</body>
</html>