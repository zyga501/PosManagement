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
    <script type="text/javascript">

    </script>
</head>
<body scroll="no">
<div>
    <form class="form form-horizontal">
        <div class="panel panel-default" style="width: 814px">
            <div class="panel-header">规则管理</div>
            <div class="panel-body111">
                <div class="panel panel-default" style="float:left;width: 500px;height: 600px">
                    <div class="panel-header"><span>编号：G12312312    </span> &nbsp; &nbsp; <input type="checkbox"
                            id="chk1"><label  for="chk1">启用</label>
                        <input type="checkbox" id="chk2"><label for="chk2" style="color: red">停用</label></div>
                    <div class="panel-body">
                        <div  style="float:left;width:33% ">
                            <span class="label label-default radius">xxxxxx</span><br>
                            <select name="list" multiple="true"  size="25"  >
                                <option   value= "1 "   > 爱情片 </option>
                                <option   value= "2 "   > 动作片123456565 </option>
                                <option   value= "3 "   > 战争片 </option>
                                <option   value= "4 "   > 恐怖片 </option>
                                <option   value= "5 "   > 喜剧片 </option>
                                <option   value= "6 "   > 伦理片 </option>
                                <option   value= "7 "   > 电视剧 </option>
                                <option   value= "8 "   > 动画片 </option>
                                <option   value= "9 "   > 其它 </option>
                            </select>
                        </div>
                        <div  style="float:left;width:33% ">
                            <span class="label label-default radius">xxxxxx</span><br>
                            <select name="list" multiple="true"  size="25"  >
                                <option   value= "1 "   > 爱情片 </option>
                                <option   value= "2 "   > 动作片127676345 </option>
                                <option   value= "3 "   > 战争片 </option>
                                <option   value= "4 "   > 恐怖片 </option>
                                <option   value= "5 "   > 喜剧片 </option>
                                <option   value= "6 "   > 伦理片 </option>
                                <option   value= "7 "   > 电视剧 </option>
                                <option   value= "8 "   > 动画片 </option>
                                <option   value= "9 "   > 其它 </option>
                            </select>
                        </div>
                        <div  style="float:left;width:33% ">
                            <span class="label label-default radius">xxxxxx</span><br>
                            <select name="list" multiple="true"  size="25" width="100%" >
                                <option   value= "1 "   > 爱情片 </option>
                                <option   value= "2 "   > 动作片165652345 </option>
                                <option   value= "3 "   > 战争片 </option>
                                <option   value= "4 "   > 恐怖片 </option>
                                <option   value= "5 "   > 喜剧片 </option>
                                <option   value= "6 "   > 伦理片 </option>
                                <option   value= "7 "   > 电视剧 </option>
                                <option   value= "8 "   > 动画片 </option>
                                <option   value= "9 "   > 其它 </option>
                            </select>
                        </div>
                    </div>
                </div>
                    <div class="panel-body" style="float:left;width: 100px; height: 600px;alignment: center;" vertical="middle">
                        <table style="height: 100%">
                            <tr class="text-c odd">
                                <td ><input type="button" class="btn btn-default radius" value="->"/><br>
                                <input type="button" class="btn btn-default radius" value="<-"/></td>
                            </tr>
                            <tr class="text-c odd">
                                <td ><input type="button" class="btn btn-default radius" value="->"/><br>
                                <input type="button" class="btn btn-default radius" value="<-"/></td>
                            </tr>
                        </table>
                    </div>
                <div class="panel panel-default" style="float:left;width: 180px;height: 90%">
                    <div class="panel-header" style="height: 3%"><span>使用银行</span></div>
                    <div class="panel-body" style="height: 40%">面板内容1</div>
                    <div class="panel-header" style="height: 3%"><span>使用业务员</span></div>
                    <div class="panel-body" style="height: 30%">面板内容2</div>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>