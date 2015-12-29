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
    <title><s:text name="addasset.title" /></title>
    <link href="<%=request.getContextPath()%>/css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/upload/ajaxupload.js"></script>
    <script type="text/javascript">
        function addCard() {
            <%
                if (null==request.getAttribute("newid")){
            %>
            $('#newid').val("");
            $('#Message').html("");
            $.ajax({
                type: 'post',
                url: 'Card!AddCard',
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        $('#Message').html(json.errorMessage);
                    }
                    else {
                        $('.input').val("");
                        $('#Message').html("<s:text name="addasset.addassetSuccess" />");
                        $('#newid').val(json.newid);
                        ajaxFileUpload();
                        parent.refreshcardList(json.cardList);
                    }
                }
            })
            <%
               }else {
            %>
            $('#Message').html("");
            $.ajax({
                type: 'post',
                url: 'Card!editCard',
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        $('#Message').html(json.errorMessage);
                    }
                    else {
                        $('.input').val("");
                        $('#Message').html("<s:text name="global.dosuccess" />");
                        ajaxFileUpload();
                        parent.refreshcardList(json.cardList);
                    }
                }
            })
            <% } %>
        }

        function ajaxFileUpload() {
            $.ajaxFileUpload
            (
                    {
                        url: 'Card!UpdateZsf',
                        secureuri: false,
                        fileElementId: ["filesfz1","filesfz2"],
                        data:{newid :$('#newid').val()},
                        success: function (data, status)
                        {
                            alert('OK!');
                            $("#upfile").css("display","none");
                            parent.layer.close(parent.layer.getFrameIndex(window.name));
                        },
                        error: function (data, status, e)
                        {
                            alert(e);
                        }
                    }
            )
            return false;
        }

        function fetchSalemanList() {
            $.ajax({
                type: 'post',
                url: 'Saleman!FetchSalemanList',
                data: "uiMode=SELECTLIST",
                dataType : "json",
                success: function(data) {
                    var json = eval("(" + data + ")");
                    $("#salesman").html(json.salemanList);
                    $("#salesman").val("<s:property value="cardmanager.salesmanuname"/>");
                }
            });
        }
        function fetchBankList() {
            $.ajax({
                type: 'post',
                url: 'Bank!FetchBankList',
                data: "uiMode=SELECTLIST",
                dataType: "json",
                success: function (data) {
                    var json = eval("(" + data + ")");
                    $("#bankName").html(json.bankList);
                    $("#bankName").val("<s:property value="cardmanager.bankuuid"/>");
                }
            });
        }

        $(function () {
            fetchBankList();
            fetchSalemanList();
        })
    </script>
</head>
<body scroll="no">
    <form class="form form-horizontal">
        <div style="height:auto; overflow:auto;">
            <table class="table table-border table-bordered table-bg table-hover table-sort">
                <tr class="text-c">
                    <td><s:text name="cardmanager.inserttime"/></td><td><input id=inserttime name=cardmanager.inserttime type="text" value="<s:property value="cardmanager.inserttime"/>" placeholder="<s:text name="cardmanager.inserttime"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cardserial"/></td><td><input id=cardserial name=cardmanager.cardserial type="text" value="<s:property value="cardmanager.cardserial"/>" placeholder="<s:text name="cardmanager.cardserial"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cardno"/></td><td><input id=cardno name=cardmanager.cardno type="text" value="<s:property value="cardmanager.cardno"/>" placeholder="<s:text name="cardmanager.cardno"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.bankname"/></td>
                    <td>
                        <select id="bankName" name="cardmanager.bankname" style="width: 100%">
                        </select>
                    </td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.creditamount"/></td><td><input id=creditamount name=cardmanager.creditamount type="text" value="<s:property value="cardmanager.creditamount"/>" placeholder="<s:text name="cardmanager.creditamount"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.tempamount"/></td><td><input id=tempamount name=cardmanager.tempamount type="text" value="<s:property value="cardmanager.tempamount"/>" placeholder="<s:text name="cardmanager.tempamount"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.templimitdate"/></td><td><input id=templimitdate name=cardmanager.templimitdate type="text" value="<s:property value="cardmanager.templimitdate"/>" placeholder="<s:text name="cardmanager.templimitdate"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.useamount"/></td><td><input id=useamount name=cardmanager.useamount type="text" value="<s:property value="cardmanager.useamount"/>" placeholder="<s:text name="cardmanager.useamount"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.billdate"/></td><td><input id=billdate name=cardmanager.billdate type="text" value="<s:property value="cardmanager.billdate"/>" placeholder="<s:text name="cardmanager.billdate"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.pin"/></td><td><input id=pin name=cardmanager.pin type="text" value="<s:property value="cardmanager.pin"/>" placeholder="<s:text name="cardmanager.pin"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.telpwd"/></td><td><input id=telpwd name=cardmanager.telpwd type="text" value="<s:property value="cardmanager.telpwd"/>" placeholder="<s:text name="cardmanager.telpwd"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.tradepwd"/></td><td><input id=tradepwd name=cardmanager.tradepwd type="text" value="<s:property value="cardmanager.tradepwd"/>" placeholder="<s:text name="cardmanager.tradepwd"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.enchashmentpwd"/></td><td><input id=enchashmentpwd name=cardmanager.enchashmentpwd type="text" value="<s:property value="cardmanager.enchashmentpwd"/>" placeholder="<s:text name="cardmanager.enchashmentpwd"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.billafterdate"/></td><td><input id=billafterdate name=cardmanager.billafterdate type="text" value="<s:property value="cardmanager.billafterdate"/>"  placeholder="<s:text name="cardmanager.billafterdate"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.lastrepaymentdate"/></td><td><input id=lastrepaymentdate name=cardmanager.lastrepaymentdate value="<s:property value="cardmanager.lastrepaymentdate"/>" type="text" placeholder="<s:text name="cardmanager.lastrepaymentdate"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.billemail"/></td><td><input id=billemail name=cardmanager.billemail type="text" value="<s:property value="cardmanager.billemail"/>" placeholder="<s:text name="cardmanager.billemail"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.status"/></td><td><input id=sfqy name=cardmanager.status type="text" value="<s:property value="cardmanager.status"/>" placeholder="<s:text name="cardmanager.status"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.commissioncharge"/></td><td><input id=commissioncharge name=cardmanager.commissioncharge type="text" value="<s:property value="cardmanager.commissioncharge"/>" placeholder="<s:text name="cardmanager.commissioncharge"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cardmaster"/></td><td><input id=cardmaster name=cardmanager.cardmaster type="text" value="<s:property value="cardmanager.cardmaster"/>" placeholder="<s:text name="cardmanager.cardmaster"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.identityno"/></td><td><input id=identityno name=cardmanager.identityno type="text" value="<s:property value="cardmanager.identityno"/>" placeholder="<s:text name="cardmanager.identityno"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.cmaddress"/></td><td><input id=cmaddress name=cardmanager.cmaddress type="text" value="<s:property value="cardmanager.cmaddress"/>" placeholder="<s:text name="cardmanager.cmaddress"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cmtel"/></td><td><input id=cmtel name=cardmanager.cmtel type="text" value="<s:property value="cardmanager.cmtel"/>" placeholder="<s:text name="cardmanager.cmtel"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cmseccontact"/></td><td><input id=cmseccontact name=cardmanager.cmseccontact type="text" value="<s:property value="cardmanager.cmseccontact"/>" placeholder="<s:text name="cardmanager.cmseccontact"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.salesman"/></td>
                    <td>
                        <select id="salesman" name="cardmanager.salesman" style="width: 100%">
                        </select>
                    </td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.memos"/></td>
                    <td colspan="7"><input id=memos name=cardmanager.memos type="text" value="<s:property value="cardmanager.memos"/>" placeholder="<s:text name="cardmanager.memos"/>" class="input-text size-S"></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="row">
            <div class="formControls col-8 col-offset-3">
                <div id="Message" style="color:#ff0000;font-size: 12px;">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="formControls" align="center">
                <input type="button" class="btn btn-success radius size-M" value="<s:text name="addasset.submit" />" onclick="addCard();">
            </div>
        </div>
        <input type="hidden" id="newid" name="newid" value="<s:property value="newid"/>">
    </form>
    <form >
        <div id="upfile">
            <div style="float: left;width: 44%">
                <label ><s:text name="cardmanager.identitypicfront"/></label><br>
                <div><input type="file" title="divimg1" class="btn btn-secondary radius"  value="选取图片" id="selector" name="filesfz1"  onchange="previewImage(this)" /></div>
                <div id="divimg1" >
                    <%
                        if (null!=request.getAttribute("newid")){
                    %>
                    <img id="imgheaddivimg1" width="300" height="200" src="../pics/<s:property value="cardmanager.cardno"/>_1.jpg" >
                    <%
                    }
                    %>
                </div>
            </div>
                <div  style="float: right;width: 44%">
                    <label ><s:text name="cardmanager.identitypicback"/></label>
                <div ><input type="file" title="divimg2" class="btn btn-secondary radius" value="选取图片" id="selector2"  name="filesfz2"  onchange="previewImage(this)" /></div>
                    <div id="divimg2" >
                        <%
                            if (null!=request.getAttribute("newid")){
                        %><img id="imgheaddivimg2" width="300" height="200" src="/pics/<s:property value="cardmanager.cardno"/>_2.jpg" alt="不存在">
                        <%
                            }
                        %>
                    </div>
            </div>
        </div>
    </form>
</div>
</body>
<script type="text/javascript">
function previewImage(file)
{
var MAXWIDTH  = 260;
var MAXHEIGHT = 180;
var div=document.getElementById(file.title);
if (file.files && file.files[0])
{
div.innerHTML ='<img id=imghead'+file.title+'>';
var img = document.getElementById('imghead'+file.title);
img.onload = function(){
var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
img.width  =  rect.width;
img.height =  rect.height;
//                 img.style.marginLeft = rect.left+'px';
img.style.marginTop = rect.top+'px';
}
var reader = new FileReader();
reader.onload = function(evt){img.src = evt.target.result;}
reader.readAsDataURL(file.files[0]);
}
else //兼容IE
{
var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
file.select();
var src = document.selection.createRange().text;
div.innerHTML = '<img id=imghead'+file.title+'>';
var img = document.getElementById('imghead'+file.title);
img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
}
}
function clacImgZoomParam( maxWidth, maxHeight, width, height ){
var param = {top:0, left:0, width:width, height:height};
if( width>maxWidth || height>maxHeight )
{
rateWidth = width / maxWidth;
rateHeight = height / maxHeight;

if( rateWidth > rateHeight )
{
param.width =  maxWidth;
param.height = Math.round(height / rateWidth);
}else
{
param.width = Math.round(width / rateHeight);
param.height = maxHeight;
}
}

param.left = Math.round((maxWidth - param.width) / 2);
param.top = Math.round((maxHeight - param.height) / 2);
return param;
}
</script>
</html>