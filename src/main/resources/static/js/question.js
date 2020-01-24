$(function () {
    $("#btn-comment-reply").click(doComment);
    // 点击回复评论功能
    $(".reply-comment").click(subComment);

    // 点击二级回复的取消 关闭二级评论框
    $(".sub-comment .back").click(closeSubcomment);

    // 点击二级回复的回复按钮，提交二级评论
    $(".sub-comment .reply").click(doSubcomment);

    // 如未登录点击链接自动登录
    $("#auto-login").click(autoLogin);
});

function autoLogin() {
    window.localStorage.setItem("closable", true);
    window.open("https://github.com/login/oauth/authorize?client_id=332735b1b85bfbb88779&scope=user&state=1");
    // 当完成github第三方验证后，关闭主页，取出localStorage中的closable，刷新当前页面
    window.setInterval(function () {
        if (!window.localStorage.getItem("closable"))
            window.location.reload();
    }, 200);
}

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
                window.location.reload();
            } else {
                if (jsonResult.code == 2004) { // 用户未进行登录操作
                    var flag = confirm("当前操作需用户登录后进行，点击确定自动登录");
                    if (flag) {
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

var commentId;

// 点击回复评论按钮
// 获取该评论的所有二级评论
function subComment() {
    let aId = $(this).data("id");
    if (aId) {
        commentId = aId;
    }
    var subcomment = $("#subcomment-" + commentId);
    var isShow = subcomment.hasClass("in");
    if (isShow) {
        subcomment.removeClass("in");
        $(this).removeClass("isShowSubComment");
    } else {
        // 显示二级评论前先关闭所有二级评论框
        $(".collapse").removeClass("in");
        subcomment.addClass("in");
        $(this).addClass("isShowSubComment");
        // 加载所有二级评论
        $.ajax({
            url: '/getSubcomment',
            data: JSON.stringify({'id': commentId}),
            contentType: 'application/json',
            dataType: 'json',
            type: 'post',
            success: function (jsonResult) {
                if (jsonResult.success) {
                    loadSubcomment(jsonResult.data);
                } else {

                }
            }
        })
    }

    return false;

}

function loadSubcomment(comments) {
    var content = "";
    for (var i = 0; i < comments.length; i++) {
        content += " <div class=\"media comment-div\" style='margin-top:20px; margin-left: 20px; margin-right: 20px'>\n" +
            "                                <div class=\"media-left\" style='margin-bottom: 15px'>\n" +
            "                                    <a href=\"#\">\n" +
            "                                        <img class=\"media-object head-img\" src='" + comments[i].user.avatarUrl + "' alt=\"用户头像\">\n" +
            "                                    </a>\n" +
            "                                </div>\n" +
            "                                <div class=\"media-body\" style='margin-bottom: 15px'>\n" +
            "                                    <h6 class=\"media-heading\"><a href=\"#\">" + comments[i].user.name + "</a></h6>\n" +
            "                                <div class=\"sub-comment-cotent\">" + comments[i].content + "</div>\n" +
            "                                </div>\n" +
            "                            </div>\n"
    }

    $("#data-load-div-" + commentId).html(content);
}

function closeSubcomment() {
    $(this).parent().parent().removeClass("in");
}

// 提交二级评论
function doSubcomment() {
    var content = $("#subcomment-content-" + commentId).val();
    if (!content) {
        alert("回复内容不可为空，请输入！");
        return false;
    }
    $.ajax({
        url: '/subComment',
        data: JSON.stringify({parentId: commentId, parentType: 1, content: content}),
        contentType: 'application/json',
        dataType: 'json',
        type: 'post',
        success: function (jsonResult) {
            if (jsonResult.success) {
                // 回复成功清空回复框的内容
                $("#subcomment-content-" + commentId).val("");
                // 重新加载二级评论
                var subcomment = $("#subcomment-" + commentId);
                subcomment.removeClass("in");
                subComment();
            } else {

            }
        }
    })
}