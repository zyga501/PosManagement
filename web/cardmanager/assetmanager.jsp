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
    <title><s:text name="assetmanager.title" /></title>
    <script type="text/javascript">
        function addAsset(){
            layer.open({
                type: 2,
                title: "添加资产",area: ['310px', '500px'],
                fix: false,
                content: "./cardmanager/addasset.jsp"
            });
        }
        function refreshAssetList(assetList) {
            $('#assetList').html(assetList);
        }
        function  hedgeAsset(uuid) {
            layer.open({
                type: 2,
                title: "对冲资产",area: ['310px', '170px'],
                fix: false,
                content: "Asset!HedgeAsset?assetUUID=" + uuid
            });
        }
    </script>
</head>
<body>
<div align="center">
    <div class="panel panel-default"  >
        <div class="panel-header">
            <s:text name="assetmanager.paneltitle" />
            <span style="float:right;" >
                <a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="addAsset()">
                    <s:text name="assetmanager.add" />
                </a>
            </span>
        </div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:80%; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="assetmanager.assetMaster" /></th>
                            <th><s:text name="assetmanager.bankName" /></th>
                            <th><s:text name="assetmanager.cardCode" /></th>
                            <th><s:text name="assetmanager.balance" /></th>
                            <th><s:text name="assetmanager.signpwd" /></th>
                            <th><s:text name="assetmanager.cashpwd" /></th>
                            <th><s:text name="assetmanager.transferpwd" /></th>
                            <th><s:text name="assetmanager.atmcashpwd" /></th>
                            <th><s:text name="global.operation"/></th>
                        </tr>
                        </thead>
                        <tbody id="assetList">
                            <s:property value="assetList" escape="false" />
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
</body>
</html>