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
    <title><s:text name="addindustry.title" /></title>
    <script type="text/javascript">
        function addIndustry() {
            $('#Message').html("");
            $.ajax({
                type: 'post',
                <% if (null ==request.getAttribute("industryproperty")){%>
                url: 'Industry!AddIndustry',
                <%} else {%>
                url: 'Industry!EditIndustry',
                <%}%>
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        $('#Message').html(json.errorMessage);
                    }
                    else {
                        $('.input').val("");
                        parent.layer.msg("<s:text name="global.dosuccess" />");
                        parent.refreshIndustryList(json.industryList);
                        parent.layer.close(parent.layer.getFrameIndex(window.name));
                    }
                }
            })
        }
    </script>
</head>
<body>
<div>
    <form class="form form-horizontal">
        <input id="uuid" name="uuid" type="hidden"
               value="${industryproperty.uuid}" >
        <table class="table table-border table-bordered table-bg table-hover table-sort">
            <tr class="text-c odd" role="row">
                <td><s:text name="addindustry.industry" /></td>
                <td>
                    <input id="industryName" name="industryName" type="text" placeholder="<s:text name="addindustry.industry" />"
                    value="${industryproperty.name}" class="input-text size-S">
                </td>
            </tr>
            <tr class="text-c odd" role="row">
                <td><s:text name="global.status" /></td>
                <td>
                    <input id="industryEnabled" name="industryEnabled" type="checkbox" class="check-box size-S"
                    ${industryproperty.status} >
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
                <input type="button" class="btn btn-success radius size-M" value="<s:text name="addindustry.submit" />" onclick="addIndustry()">
            </div>
        </div>
    </form>
</div>
</body>
</html>
