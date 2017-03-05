/**
 * Created by MacBook on 2016/11/14.
 */
$(function () {
    $("[data-toggle='tooltip']").tooltip();
    $('#vcode1').bind("click",changeCode);
    $('#vcode2').bind("click",changeCode);
    //失去焦点的时候校验
    $(".inputFaild").blur(function(){
        var inputName = $(this).attr('id');
        if(inputName === "RegisterUsername") {
            validateRegisterUsername(inputName);
        }else if(inputName === "RegisterUserpassword"){
            validateRegisterPassword(inputName);
        }else if(inputName === "RegisterUserpasswordagain"){
            validateRegisterUserpasswordagain(inputName);
        }else if(inputName === "RegisterUseremail"){
            validateRegisterUserEmail(inputName);
        }
    });
    //得到焦点的时候移除错误信息
    $(".inputFaild").focus(function(){
        var inputName = $(this).attr('id');
        var group = inputName + "Group";
        var errors = inputName + "Errors";
        var msg = inputName + "Msg";
        $('#'+group).removeClass('has-error');
        $('#'+errors).removeClass('glyphicon-remove');
        $('#'+msg).text(" ");

    });
});
/*
    注册用户名校验
    1.非空校验
    2.长度校验
    3.ajax校验
 */
function validateRegisterUsername(inputName){
    var text = $('#'+inputName).val();
    var group = inputName + "Group";
    var errors = inputName + "Errors";
    var msg = inputName + "Msg";
    if(text === "")
    {
        $('#'+group).addClass('has-error');
        $('#'+errors).addClass('glyphicon-remove');
        $('#'+msg).text("用户名不能为空");
        return ;
    }else if (text.length < 6 || text.length > 20){
        $('#'+group).addClass('has-error');
        $('#'+errors).addClass('glyphicon-remove');
        $('#'+msg).text("用户名长度在6-20位");
        return ;
    }else{

    }
    $('#'+group).addClass('has-success');
    $('#'+errors).addClass('glyphicon-ok');
    $('#'+msg).text(" ");
}
function validateRegisterPassword(inputName){
    var text = $('#'+inputName).val();
    var group = inputName + "Group";
    var errors = inputName + "Errors";
    var msg = inputName + "Msg";
    if(text === "")
    {
        $('#'+group).addClass('has-error');
        $('#'+errors).addClass('glyphicon-remove');
        $('#'+msg).text("密码不能为空");
        return ;
    }else if (text.length < 6 || text.length > 20) {
        $('#' + group).addClass('has-error');
        $('#' + errors).addClass('glyphicon-remove');
        $('#' + msg).text("密码长度在6-24位");
        return;
    }
    $('#'+group).addClass('has-success');
    $('#'+errors).addClass('glyphicon-ok');
    $('#'+msg).text(" ");
}

function validateRegisterUserpasswordagain(inputName){
    var text = $('#'+inputName).val();
    var password = $('#RegisterUserpassword').val();
    var group = inputName + "Group";
    var errors = inputName + "Errors";
    var msg = inputName + "Msg";
    if(text === "")
    {
        $('#'+group).addClass('has-error');
        $('#'+errors).addClass('glyphicon-remove');
        $('#'+msg).text("密码不能为空");
        return ;
    }else if (text != password) {
        $('#' + group).addClass('has-error');
        $('#' + errors).addClass('glyphicon-remove');
        $('#' + msg).text("两次密码输入不一致");
        return;
    }
    $('#'+group).addClass('has-success');
    $('#'+errors).addClass('glyphicon-ok');
    $('#'+msg).text(" ");
}
function validateRegisterUserEmail(inputName){
    var text = $('#'+inputName).val();
    var group = inputName + "Group";
    var errors = inputName + "Errors";
    var msg = inputName + "Msg";
    console.log();
    var re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
    if(text === "")
    {
        $('#'+group).addClass('has-error');
        $('#'+errors).addClass('glyphicon-remove');
        $('#'+msg).text("邮箱不能为空");
        return ;
    }else if(!re.test(text)){
        $('#'+group).addClass('has-error');
        $('#'+errors).addClass('glyphicon-remove');
        $('#'+msg).text("邮箱格式错误");
        return;
    }
    $('#'+group).addClass('has-success');
    $('#'+errors).addClass('glyphicon-ok');
    $('#'+msg).text(" ");
}
function changeCode() {
    var imgNodeid = $(this).attr('id');
    var imgNode = document.getElementById(imgNodeid);
    imgNode.src = "http://localhost:8080/shop/hello/verifyCode?t=" + Math.random();  // 防止浏览器缓存的问题     
}
