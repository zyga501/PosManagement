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
    <title><s:text name="addasset.title"/></title>
</head>
<body scroll="no">
<div>
    <form class="form form-horizontal">
        <div class="panel panel-default">
            <div class="panel-header">规则分配
                <div style="float: right">
                    <input type="button" class="btn btn-primary radius size-s" value="<s:text name="global.save"/>"  onclick="save()" />
                </div>
            </div>
            <div class="panel-body11">
                <div class="panel panel-default" style="float:left;width: 500px;">
                    <div class="panel-header">
                        <input type="hidden" name="ruleUUID" value="<s:property value="ruleUUID" />">
                        <span>编号：</span><s:property value="ruleUUID" escape="false" />  &nbsp; &nbsp;
                        <input type="checkbox" id="ruleEnabled" name="ruleEnabled" <s:property value="ruleEnabled" escape="false" />>
                        <label for="ruleEnabled">启用</label>
                    </div>
                    <div class="panel-body">
                        <div  style="float:left;width:33%;">
                            <span class="label label-default radius">内容</span><br>
                            <div  style="width:150px;border-color: #9c9c9c;border-width: 1px;border-style:solid;">
                                <s:property value="ruleInfo" escape="false" />
                            </div>
                        </div>
                        <div  style="float:left;width:33% ">
                            <span class="label label-default radius">银行列表</span><br>
                            <select  id="bankList" size="25" style="width:150px;" multiple="multiple" >
                                <s:property value="bankList" escape="false" />
                            </select>
                        </div>
                        <div  style="float:left;width:33% ">
                            <span class="label label-default radius">业务员列表</span><br>
                            <select  id="salemanList"  size="25"style="width:150px;" multiple="multiple" >
                                <s:property value="salemanList" escape="false" />
                            </select>
                        </div>
                    </div>
                </div>
                <div class="panel-body" style="float:left;width: 100px; height: 500px;alignment: center;" vertical="middle">
                    <table style="height: 100%">
                        <tr class="text-c odd">
                            <td ><input type="button" class="btn btn-default radius" value="->" onclick="bankin()"/><br>
                                <input type="button" class="btn btn-default radius" value="<-"  onclick="bankout()" /></td>
                        </tr>
                        <tr class="text-c odd">
                            <td ><input type="button" class="btn btn-default radius" value="->"  onclick="salemanin()"  /><br>
                                <input type="button" class="btn btn-default radius" value="<-" onclick="salemanout()"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel panel-default" style="float:left;width: 180px;">
                    <div class="panel-header"  ><span>使用银行</span></div>
                    <div class="panel-body"  >
                        <select  name="selectedbankList" id="selectedbankList"   size="11" style="width:155px;" >
                            ${selectedbankList}
                        </select>
                    </div>
                    <div class="panel-header"  ><span>使用业务员</span></div>
                    <div class="panel-body"  >
                        <select name="selectedsalemanList"  id="selectedsalemanList"  size="11" style="width:155px;" >
                            ${selectedsalemanList}</select>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    function  bankin(){
            var v = ""+$("#bankList").val();
            var t ="";
        $("#bankList").find("option:selected").each(function(){
            t = t + $(this).text() + ',';
        })
        var vs = new Array(); vs = v.split(",");
        var ts = new Array(); ts = t.split(",");
        for (i=0;i<vs.length;i++) {
            if ((vs[i] == 'undefined') || (vs[i] == null) || (vs[i] == "")) {
            }
            else {
                $("#selectedbankList").append("<option value=" + vs[i] + ">" + ts[i] + "</option>");
            }
        }
    }

    function  bankout(){
        var v= $("#selectedbankList").val();
        if ((v=='undefined') || (v==null)||(v=="")){}
        else{
            $("#selectedbankList option[value='"+v+"']").remove();
        }
    }

    function  salemanin(){
        var v = ""+$("#salemanList").val();
        var t ="";
        $("#salemanList").find("option:selected").each(function(){
            t = t + $(this).text() + ',';
        })
        var vs = new Array(); vs = v.split(",");
        var ts = new Array(); ts = t.split(",");
        for (i=0;i<vs.length;i++) {
            if ((vs[i] == 'undefined') || (vs[i] == null) || (vs[i] == "")) {
            }
            else {
                $("#selectedsalemanList").append("<option value=" + vs[i] + ">" + ts[i] + "</option>");
            }
        }
    }
    function  salemanout(){
        var v= $("#selectedsalemanList").val();
        if ((v=='undefined') || (v==null)||(v=="")){}
        else{
            $("#selectedsalemanList option[value='"+v+"']").remove();
        }
    }
    function  save(){
        var banklist =  $("#selectedbankList option").map(function(){return $(this).val();}).get().join(",");
        var salemanlist =  $("#selectedsalemanList option").map(function(){return $(this).val();}).get().join(",");
        $.ajax({
            type: 'post',
            url: 'Rule!AssignRule',
            dataType:"json",
            data:$("form").serialize()+'&bankList='+banklist+'&salemanList='+salemanlist,
            success: function (data) {
                parent.layer.msg("<s:text name="global.dosuccess" />");
                parent.layer.close(parent.layer.getFrameIndex(window.name));
            }
        })
    }
</script>
</body>
</html>