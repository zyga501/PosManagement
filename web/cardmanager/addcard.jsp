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
    <link href="../css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="../css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="../css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="../skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
    <script type="text/javascript" src="../js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="../js/upload/ajaxupload.js"></script>
    <script type="text/javascript">
        function addcard() {
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
                        $("#upfile").css("display","");
                        parent.refreshcardList(json.cardList);
                    }
                }
            })
        }
        function ajaxFileUpload() {
            $.ajaxFileUpload
            (
                    {
                        url: 'Card!UpdateZsf', //用于文件上传的服务器端请求地址
                        secureuri: false, //是否需要安全协议，一般设置为false
                        fileElementId: ["filesfz1","filesfz2"], //文件上传域的ID
                        data:{newid :$('#newid').val()},
                        success: function (data, status)  //服务器成功响应处理函数
                        {
                            alert('OK!');
                            $("#upfile").css("display","none");
                            parent.layer.close(parent.layer.getFrameIndex(window.name));
                        },
                        error: function (data, status, e)//服务器响应失败处理函数
                        {
                            alert(e);
                        }
                    }
            )
            return false;
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
                }
            });
        }


        $(function () {
            fetchBankList();
        })
    </script>
</head>
<body scroll="no">
<div>
    <form class="form form-horizontal">
        <div style="height:auto; overflow:auto;">
            <table class="table table-border table-bordered table-bg table-hover table-sort">
                <tr class="text-c">
                    <td><s:text name="cardmanager.inserttime"/></td><td><input id=inserttime name=cardinfo type="text" placeholder="<s:text name="cardmanager.inserttime"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cardserial"/></td><td><input id=cardserial name=cardinfo type="text" placeholder="<s:text name="cardmanager.cardserial"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cardno"/></td><td><input id=cardno name=cardinfo type="text" placeholder="<s:text name="cardmanager.cardno"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.bankname"/></td>
                    <td>
                        <select id="bankName" name="cardinfo" style="width: 100%">
                        </select>
                    </td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.creditamount"/></td><td><input id=creditamount name=cardinfo type="text" placeholder="<s:text name="cardmanager.creditamount"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.tempamount"/></td><td><input id=tempamount name=cardinfo type="text" placeholder="<s:text name="cardmanager.tempamount"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.templimitdate"/></td><td><input id=templimitdate name=cardinfo type="text" placeholder="<s:text name="cardmanager.templimitdate"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.useamount"/></td><td><input id=useamount name=cardinfo type="text" placeholder="<s:text name="cardmanager.useamount"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.billdate"/></td><td><input id=billdate name=cardinfo type="text" placeholder="<s:text name="cardmanager.billdate"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.pin"/></td><td><input id=pin name=cardinfo type="text" placeholder="<s:text name="cardmanager.pin"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.telpwd"/></td><td><input id=telpwd name=cardinfo type="text" placeholder="<s:text name="cardmanager.telpwd"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.tradepwd"/></td><td><input id=tradepwd name=cardinfo type="text" placeholder="<s:text name="cardmanager.tradepwd"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.enchashmentpwd"/></td><td><input id=enchashmentpwd name=cardinfo type="text" placeholder="<s:text name="cardmanager.enchashmentpwd"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.billafterdate"/></td><td><input id=billafterdate name=cardinfo type="text" placeholder="<s:text name="cardmanager.billafterdate"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.lastrepaymentdate"/></td><td><input id=lastrepaymentdate name=cardinfo type="text" placeholder="<s:text name="cardmanager.lastrepaymentdate"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.billemail"/></td><td><input id=billemail name=cardinfo type="text" placeholder="<s:text name="cardmanager.billemail"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.sfqy"/></td><td><input id=sfqy name=cardinfo type="text" placeholder="<s:text name="cardmanager.sfqy"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.commissioncharge"/></td><td><input id=commission-charge name=cardinfo type="text" placeholder="<s:text name="cardmanager.commissioncharge"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cardmaster"/></td><td><input id=cardmaster name=cardinfo type="text" placeholder="<s:text name="cardmanager.cardmaster"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.identityno"/></td><td><input id=identityno name=cardinfo type="text" placeholder="<s:text name="cardmanager.identityno"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.cmaddress"/></td><td><input id=cmaddress name=cardinfo type="text" placeholder="<s:text name="cardmanager.cmaddress"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cmtel"/></td><td><input id=cmtel name=cardinfo type="text" placeholder="<s:text name="cardmanager.cmtel"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cmseccontact"/></td><td><input id=cmseccontact name=cardinfo type="text" placeholder="<s:text name="cardmanager.cmseccontact"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.salesman"/></td><td><input id=salesman name=cardinfo type="text" placeholder="<s:text name="cardmanager.salesman"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.memos"/></td>
                    <td colspan="7"><input id=memos name=cardinfo type="text" placeholder="<s:text name="cardmanager.memos"/>" class="input-text size-S"></td>
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
                <input type="button" class="btn btn-success radius size-M" value="<s:text name="addasset.submit" />" onclick="addcard();">
            </div>
        </div>
    </form>
    <form >
        <input type="hidden" id="newid" value="989">
        <div id="upfile">
            <div   class="row cl" >
                <label class="form-label col-2"><s:text name="cardmanager.identitypicfront"/></label>
                <div class="formControls col-3"><input type="file" class="btn btn-secondary radius" value="选取图片" id="selector" name="filesfz1"  /></div>
                <label class="form-label col-2"><s:text name="cardmanager.identitypicback"/></label>
                <div class="formControls col-3"><input type="file" class="btn btn-secondary radius" value="选取图片" id="selector2"  name="filesfz2"   /></div>
            </div>
            <div class="formControls" align="center">
                <input type="button" class="btn btn-success radius size-M" value="<s:text name="addasset.submit" />" onclick="ajaxFileUpload();">
            </div>
        </div>
    </form>
</div>
</body>
</html>