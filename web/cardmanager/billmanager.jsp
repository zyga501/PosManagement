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
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/js/laypage/skin/laypage.css" id="laypagecss">
    <title></title>
    <style type="text/css" >
        #searchtb tr td {
            padding-right: 1em;
        }
    </style>
    <script type="text/javascript">
        function addBill(){
            var index = layer.open({
                type: 2,
                title: "生成账单",area: ['310px', '500px'],
                fix: false,
                content: "./cardmanager/addbill.jsp"
            });}

        var billnov;
        function clickBill(button, billNO) {
            if (billnov==billNO) return;
            var val = button.value;
            if (val == "N") {
                layer.confirm('确定启用？', {
                    btn: ['是','否'] //按钮
                }, function () {
                    billnov = billNO;
                    $.ajax({
                        type: 'post',
                        url: 'Bill!editBill',
                        data: {status:"enable" , billNO:billNO},
                        success: function (data) {
                            billnov =null;
                            var json = eval("(" + data + ")");
                            if (json.successMessage) {
                                button.value = "Y";
                                button.setAttribute("class", "btn btn-success radius");
                                layer.msg(json.successMessage);
                            }
                            else if (json.errorMessage) {
                                layer.msg(json.errorMessage);
                            }
                        }
                    });
                }, function () {
                    billnov =null;
                });
            }
        }

        function setdopanel(){
            $(".billbtn").hover(function() {
                $("#dopanel").removeAttr("billuuid");
                $("#dopanel").attr("billuuid",$(this).attr("datav"));
                $("#dopanel").css({
                    "left" : parseInt($(this).offset().left) + "px",
                    "top" : parseInt($(this).offset().top) + "px"
                });
                $("#dopanel").show();
            }, function(){
                   // $("#dopanel").hide();
                });
        }

        function delbill(obj){
            var billuuid = $(obj).parent().attr("billuuid");
            if (billuuid == undefined) return ;
            $.ajax({
                type: 'post',
                url: 'Bill!DelBill',
                dataType:"json",
                data:{billuuid:billuuid},
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        layer.msg(json.errorMessage);
                    }
                    else {
                        layer.msg("账单已经删除");
                        dosearch();
                    }
                }
            })
        }

        function refreshBillList(billList) {
            $('#billList').html(billList);
            $("label[name=billamount]").on("click", function() {clickbtbill(this)});
            setdopanel();
        }

    </script>
</head>
<body>
<div align="center">
    <div class="panel panel-default"  >
        <div class="panel-header"><form id="searchform"><table id="searchtb" style="width: 80%"><tr>
            <td><input type="text" name="billdate" placeholder="<s:text name="billmanager.billdate"/>" class="input-text radius size-s"></td>
            <td><input type="text" name="cardno" placeholder="<s:text name="billmanager.cardno"/>" class="input-text radius size-s"></td>
            <td><input type="text" name="bankname" placeholder="<s:text name="cardmanager.bankname"/>" class="input-text radius size-s"></td>
            <% if (request.getSession().getAttribute("roleId").equals("e664d6f3-85f8-4bd6-bcb8-c4e053732b29")){ %>
            <td><input type="text" name="saleman" placeholder="<s:text name="billmanager.saleman"/>" class="input-text radius size-s"></td><%}%>
            <td><select name="billstatus" placeholder="<s:text name="global.status"/>" onchange="dosearch()">
                <option value="all"><s:text name="global.alldata"/></option>
                <option value="finished"><s:text name="global.ok"/></option>
                <option value="unfinished" selected><s:text name="global.no"/></option>
            </select></td>
            <td><a href="javascript:void(0);" class="btn btn-primary  radius size-S " onclick="dosearch()"><s:text name="global.search"/></a>
            </td>
        <td><a href="javascript:void(0);" class="btn btn-primary radius size-S " onclick="addBill()">
            <s:text name="billmanager.makebill" /></a></td></tr></table></form></div></div>
    <div id="navigatediv"></div>
        <div class="panel-body" id="parentIframe">
            <form>
                <div style="height:80%; overflow:auto;">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th><s:text name="billmanager.bankname" /></th>
                            <th><s:text name="billmanager.cardno" /></th>
                            <th><s:text name="billmanager.billdate" /></th>
                            <th><s:text name="billmanager.lastrepaymentdate" /></th>
                            <th><s:text name="billmanager.billamount" /></th>
                            <% if (request.getSession().getAttribute("roleId").equals("e664d6f3-85f8-4bd6-bcb8-c4e053732b29")){ %>
                            <th><s:text name="billmanager.saleman" /></th><%}%>
                            <th><s:text name="billmanager.expired" /></th>
                            <th><s:text name="global.status" /></th>
                        </tr>
                        </thead>
                        <tbody id="billList">
                        </tbody>
                    </table>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="dopanel" style="position: absolute;display: none;z-index: 5500;">
    <input class="btn btn-warning radius size-mini" onclick="delbill(this)" type="button" value="删除" title="删除账单"/><br>
    <input class="btn btn-warning radius size-mini" onclick="rebuild(this);" type="button" value="重生" title="重生账单"/></div>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/H-ui.admin.js"></script>
<script src="<%=request.getContextPath()%>/js/laypage/laypage.js"></script>
<script >
    function dosearch(){
        $.ajax({
            type: 'post',
            url: 'Bill!FetchBillList',
            dataType:"json",
            data:$("#searchform").serialize(),
            success: function (data) {
                var json = eval("(" + data + ")");
                if (json.errorMessage != null) {
                    layer.msg(json.errorMessage);
                }
                else {
                }
                laypage({
                    cont: 'navigatediv',
                    pages: json.pagecount,
                    skip: true,
                    skin: 'yahei',
                    jump: function (obj) {
                        $.ajax({
                            type: 'post',
                            url: 'Bill!FetchBillList?currpage='+obj.curr,
                            dataType:"json",
                            data:$("#searchform").serialize(),
                            success: function (data) {
                                var json = eval("(" + data + ")");
                                if (json.errorMessage != null) {
                                    layer.msg(json.errorMessage);
                                }
                                else {
                                    refreshBillList(json.billList);
                                }
                            }
                        })
                    }
                })
            }
        })
    }

$().ready( function(){
    $("label[name=billamount]").on("click", function() {clickbtbill(this)
    });
    laypage({
        cont: 'navigatediv',
        pages: ${pagecount},
        skip: true,
        skin: 'yahei',
        jump: function (obj) {
            $.ajax({
                type: 'post',
                url: 'Bill!FetchBillList?currpage='+obj.curr,
                dataType:"json",
                data:$("#searchform").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        layer.msg(json.errorMessage);
                    }
                    else { 
                        refreshBillList(json.billList);
                    }
                }
            })
        }
    })}
)

    function  clickbtbill(obj){

        var name = prompt("输入新的金额",obj.innerHTML);
        if (name==undefined) return ;
        $.ajax({
            type: 'post',
            url: 'Bill!modifyBill',
            data: {billamount:name , billNO:$(obj).attr("datav")},
            success: function (data) {
                var json = eval("(" + data + ")");
                if (json.successMessage) {
                    refreshBillList(json.billList);
                    layer.msg(json.successMessage, {icon: 1});
                }
                else if (json.errorMessage)
                    layer.msg(json.errorMessage, {icon:2});
            }
        });
    }


    function rebuild(obj){
        var pass = prompt("输入账单金额","");
            if (isNaN(pass)||(pass=="")) {
                alert('您输入的非法数字，请重新输入');
                return;
            };
            var billuuid = $(obj).parent().attr("billuuid");
           // if (billuuid == undefined) return ;
           $.ajax({
           type: 'post',
           url: 'Bill!RebuildBill',
           dataType:"json",
           data:{billuuid:billuuid,billamount:pass},
           success: function (data) {
           var json = eval("(" + data + ")");
           if (json.errorMessage != null) {
           layer.msg(json.errorMessage);
           }
           else {
           layer.msg("账单已经重新生成");
           dosearch();
           }
           }
           });
    }

</script>
</body>
</html>