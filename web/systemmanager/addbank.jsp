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
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <title><s:text name="addbank.title" /></title>
    <script type="text/javascript">
        function addBank() {
            $('#Message').html("");
            $.ajax({
                type: 'post',
                url: 'Bank!AddBank',
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        $('#Message').html(json.errorMessage);
                    }
                    else {
                        $('.input').val("");
                        $('#Message').html("<s:text name="addbank.addBankSuccess" />");
                        parent.refreshBankList(json.bankList);
                    }
                }
            })
        }
    </script>
</head>
<body scroll="no">
    <div>
        <form class="form form-horizontal">
            <table class="table table-border table-bordered table-bg table-hover table-sort">
                <tr class="text-c odd" role="row">
                    <td><s:text name="addbank.bankname" /></td>
                    <td>
                        <input id="bankName" name="bankName" type="text" placeholder="<s:text name="addbank.bankname" />"
                               value="${bankList.NAME}" class="input-text size-S">
                    </td>
                </tr>
                <tr class="text-c odd" role="row">
                    <td><s:text name="addbank.enabled" /></td>
                        <input id="bankEnabled" name="bankEnabled" type="checkbox" class="check-box size-S"
                        value="<s:property test="${bankList.status==true}">true" </s:property> >
                    </td>
                </tr>
            </table>
            <div class="row">
                <div class="formControls col-8 col-offset-3">
                    <div id="Message" style="color:#ff0000;font-size: 12px;height: 12px">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="formControls col-8 col-offset-3" align="center">
                    <input type="button" class="btn btn-success radius size-M" value="<s:text name="addbank.submit" />" onclick="addBank()">
                </div>
            </div>
        </form>
    </div>
</body>
</html>
