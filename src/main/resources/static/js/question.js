$(function () {
    $("#btn-comment-reply").click(doComment);
});

function doComment() {
    var content = $("#content").val();
    if (content == "") {
        alert("回复内容不可为空，请输入后再回复");
        return false;
    }

    var id = $("#quesitonId").val();
    var type = 0; // question

    $.ajax({
        url: '/comment',
        data: JSON.stringify({content: content, parentId: id, parentType: type}),
        dataType: 'json',
        contentType: 'application/json',
        type: 'post',
        success: function (jsonResult) {
            if (jsonResult.success) {
                // TODO:回复评论成功，重新加载评论区内容
            } else {
                if (jsonResult.code == 2004) { // 用户未进行登录操作
                    var flag = confirm("当前操作需用户登录后进行，点击确定自动登录");
                    if(flag){
                        window.open("https://github.com/login/oauth/authorize?client_id=332735b1b85bfbb88779&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(jsonResult.message);
                }
            }
        }
    })
}