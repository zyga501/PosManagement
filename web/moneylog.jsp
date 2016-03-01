<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
    <meta charset="utf-8">
    <link href="css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
    <script type="text/javascript" src="<%= request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript">
                function GetRequest() {
                    var url = location.search; //获取url中"?"符后的字串
                    var theRequest ;
                    if (url.indexOf("?") != -1) {
                        var str = url.substr(1);
                        strs = str.split("&");
                        theRequest = decodeURI(strs[0].split("=")[1]);
                    }
                    return theRequest;
                }
        var rt = GetRequest();

        $().ready(function(){
            $.ajax({
                type: 'post',
                url: 'Saleman!FetchFeeinfo',
                data: "salemanID=" + rt,
                success: function (data) {
                    var json = eval("(" + data + ")");
                    $("#info").html(json.infostr);
                }
            });
            });
    </script>
</head>
<body>
<div id="info"></div>
</body>
</html>