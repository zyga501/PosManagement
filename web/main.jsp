<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link href="css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="css/main.css" rel="stylesheet" type="text/css" id="skin"/>
    <title>卡务管理系统</title>
</head>
<body onselectstart="return false ">
<header class="Hui-header cl" style="background-color: #0e90d2"> <a class="Hui-logo l" href="./index.jsp">卡务管理系统平台</a>
    <ul class="Hui-userbar">
        <li>Hello,</li>
        <li class="dropDown dropDown_hover">
            <a href="#" class="dropDown_A"><%=request.getSession().getAttribute("userName")%> <i class="Hui-iconfont">&#xe6d5;</i></a>
        <ul class="dropDown-menu radius box-shadow">
            <li><a href="javascript:void(0);">个人信息</a></li>
            <li><a href="javascript:window.location.href='validLogin';window.close();window.location.href='about:blank';">退出</a></li>
        </ul>
        </li>
    </ul>
    <div class="formControls col-8 col-offset-3">
        <%=request.getSession().getAttribute("userLastLoginInfo")%>
    </div>
    <a aria-hidden="false" class="Hui-nav-toggle" href="#"></a>
</header>
<aside class="Hui-aside">
    <input runat="server" id="divScrollValue" type="hidden" value="" />
    <div class="menu_dropdown bk_2">
        <%=request.getSession().getAttribute("userMenu")%>
    </div>
</aside>
<div class="dislpayArrow"><a class="pngfix" href="javascript:void(0);" onClick="displaynavbar(this)"></a></div>
<section class="Hui-article-box">
    <div id="Hui-tabNav" class="Hui-tabNav">
        <div class="Hui-tabNav-wp">
            <ul id="min_title_list" class="acrossTab cl">
                <li class="active">
                    <span title="我的桌面" data-href="welcome.html">我的桌面</span>
                    <em></em>
                </li>
            </ul>
        </div>
        <div class="Hui-tabNav-more btn-group"><a id="js-tabNav-prev" class="btn radius btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d4;</i></a><a id="js-tabNav-next" class="btn radius btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d7;</i></a></div>
    </div>
    <div id="iframe_box" class="Hui-article">
        <div class="show_iframe">
            <div style="display:none" class="loading"></div>
            <iframe scrolling="yes" frameborder="0" src="desktop.html"></iframe>
        </div>
    </div>
</section>
<script type="text/javascript" src="js/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="js/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="js/H-ui.js"></script>
<script type="text/javascript" src="js/H-ui.admin.js"></script>
<script type="text/javascript">
    /*资讯-添加*/
    function article_add(title,url){
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*图片-添加*/
    function picture_add(title,url){
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*产品-添加*/
    function product_add(title,url){
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*用户-添加*/
    function member_add(title,url,w,h){
        layer_show(title,url,w,h);
    }
</script>
</body>
</html>
