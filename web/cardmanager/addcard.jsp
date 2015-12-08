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
    <link href="../css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="../css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="../css/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="../skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
    <script type="text/javascript" src="../js/jquery/1.9.1/jquery.min.js"></script>
    <title><s:text name="addassets.title" /></title>
    <script type="text/javascript">
        function addcard() {
            $('#Message').html("");
            $.ajax({
                type: 'post',
                url: 'Cards!AddCards',
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                        $('#Message').html(json.errorMessage);
                    }
                    else {
                        $('.input').val("");
                        $('#Message').html("<s:text name="addassets.addAssetsSuccess" />");
                        parent.refreshcardList(json.cardsList);
                    }
                }
            })
        }
    </script>
</head>
<body scroll="no">
<div>
    <form class="form form-horizontal">
        <div style="height:auto; overflow:auto;">
            <table class="table table-border table-bordered table-bg table-hover table-sort">
                <tbody>
                <tr class="text-c">
                    <td><s:text name="cardmanager.paneltitle"/></td><td><input id=paneltitle name=cardinfo[1] type="text" placeholder="<s:text name="cardmanager.paneltitle"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.inserttime"/></td><td><input id=inserttime name=cardinfo[2] type="text" placeholder="<s:text name="cardmanager.inserttime"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cardserial"/></td><td><input id=cardserial name=cardinfo[3] type="text" placeholder="<s:text name="cardmanager.cardserial"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cardno"/></td><td><input id=cardno name=cardinfo[4] type="text" placeholder="<s:text name="cardmanager.cardno"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.bankname"/></td><td><input id=bankname name=cardinfo[5] type="text" placeholder="<s:text name="cardmanager.bankname"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.creditamount"/></td><td><input id=creditamount name=cardinfo[6] type="text" placeholder="<s:text name="cardmanager.creditamount"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.tempamount"/></td><td><input id=tempamount name=cardinfo[7] type="text" placeholder="<s:text name="cardmanager.tempamount"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.templimitdate"/></td><td><input id=templimitdate name=cardinfo[8] type="text" placeholder="<s:text name="cardmanager.templimitdate"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.useamount"/></td><td><input id=useamount name=cardinfo[9] type="text" placeholder="<s:text name="cardmanager.useamount"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.billdate"/></td><td><input id=billdate name=cardinfo[10] type="text" placeholder="<s:text name="cardmanager.billdate"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.pin"/></td><td><input id=pin name=cardinfo[11] type="text" placeholder="<s:text name="cardmanager.pin"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.telpwd"/></td><td><input id=telpwd name=cardinfo[12] type="text" placeholder="<s:text name="cardmanager.telpwd"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.tradepwd"/></td><td><input id=tradepwd name=cardinfo[13] type="text" placeholder="<s:text name="cardmanager.tradepwd"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.enchashmentpwd"/></td><td><input id=enchashmentpwd name=cardinfo[14] type="text" placeholder="<s:text name="cardmanager.enchashmentpwd"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.billafterdate"/></td><td><input id=billafterdate name=cardinfo[15] type="text" placeholder="<s:text name="cardmanager.billafterdate"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.lastrepaymentdate"/></td><td><input id=lastrepaymentdate name=cardinfo[16] type="text" placeholder="<s:text name="cardmanager.lastrepaymentdate"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.billemail"/></td><td><input id=billemail name=cardinfo[17] type="text" placeholder="<s:text name="cardmanager.billemail"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.sfqy"/></td><td><input id=sfqy name=cardinfo[18] type="text" placeholder="<s:text name="cardmanager.sfqy"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.commissioncharge"/></td><td><input id=commission-charge name=cardinfo[19] type="text" placeholder="<s:text name="cardmanager.commissioncharge"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cardmaster"/></td><td><input id=cardmaster name=cardinfo[20] type="text" placeholder="<s:text name="cardmanager.cardmaster"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.identityno"/></td><td><input id=identityno name=cardinfo[21] type="text" placeholder="<s:text name="cardmanager.identityno"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.identitypicfront"/></td><td><input id=identitypicfront name=cardinfo[22] type="text" placeholder="<s:text name="cardmanager.identitypicfront"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.identitypicback"/></td><td><input id=identitypicback name=cardinfo[23] type="text" placeholder="<s:text name="cardmanager.identitypicback"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cmaddress"/></td><td><input id=cmaddress name=cardinfo[24] type="text" placeholder="<s:text name="cardmanager.cmaddress"/>" class="input-text size-S"></td>
                </tr>
                <tr class="text-c">
                    <td><s:text name="cardmanager.cmtel"/></td><td><input id=cmtel name=cardinfo[25] type="text" placeholder="<s:text name="cardmanager.cmtel"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.cmseccontact"/></td><td><input id=cmseccontact name=cardinfo[26] type="text" placeholder="<s:text name="cardmanager.cmseccontact"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.salesman"/></td><td><input id=salesman name=cardinfo[27] type="text" placeholder="<s:text name="cardmanager.salesman"/>" class="input-text size-S"></td>
                    <td><s:text name="cardmanager.memos"/></td><td><input id=memos name=cardinfo[28] type="text" placeholder="<s:text name="cardmanager.memos"/>" class="input-text size-S"></td>
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
                <input type="button" class="btn btn-success radius size-M" value="<s:text name="addassets.submit" />" onclick="addCards()">
            </div>
        </div>
    </form>
</div>
</body>
</html>
