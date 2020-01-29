$(function () {
    $("#publish-btn").click(doPublish);

    // 点击标签输入框弹出标签选择面板
    $("#tag").click(loadTagPanel);

    // 标签选择面板失去焦点时关闭
    $(".tag-panel").blur(closeTagPanel);

    // 点击标签时在输入框中输出
    $(".label-span").click(addTag);

    // 加载md编辑器
    var editor = editormd("question-editor", {
        width: "100%",
        height: 400,
        path: "/editor/lib/",
        watch: false,
        placeholder: '请输入详细的问题描述',
        imageUpload    : true,
        imageFormats   : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL : "/file/imgUpload"
    });
});

// 进行数据非空校验
function doCheckValue(title, description, tag) {
    var falg = true;

    if (title === "") {
        $("#title-warning").show();
        falg = false;
    } else {
        $("#title-warning").hide();
    }

    if (description === "") {
        $("#des-warning").show();
        falg = false;
    } else {
        $("#des-warning").hide();
    }

    if (tag === "") {
        $("#tag-warning").show();
        falg = false;
    } else {
        $("#tag-warning").hide();
    }
    return falg;
}

function doPublish() {
    // 进行表格内容空校验
    var title = $("#title").val();
    var description = $("#description").val();
    var tag = $("#tag").val();
    var id = $("#id").val();

    var flag = doCheckValue(title, description, tag);
    if (!flag) { // flag为 false 说明有数据为空
        return false;
    }
    $.ajax({
        url: '/publish',
        dataType: 'json',
        data: {title: title, description: description, tag: tag, id: id},
        type: 'post',
        success: function (jsonResult) {
            if (jsonResult.success) {
                if (id != null && id != "") {
                    location.href = "/question/" + id;
                } else {
                    location.href = "/";
                }
            } else {
                if (jsonResult.code == 2004) { // 用户未进行登录操作
                    var flag = confirm("当前操作需用户登录后进行，点击确定自动登录");
                    if (flag) {
                        window.open("https://github.com/login/oauth/authorize?client_id=332735b1b85bfbb88779&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                        window.setInterval(function () {
                            if (!window.localStorage.getItem("closable"))
                                window.location.reload();
                        }, 200);
                    }
                } else {
                    if (jsonResult.code == 2006) { // 标签存在非法项目
                        var msg = "存在非法的标签：[";
                        for (var i = 0; i < jsonResult.data.length; i++) {
                            if (i != jsonResult.data.length - 1)
                                msg += jsonResult.data[i] + "，";
                            else
                                msg += jsonResult.data[i] + "]";
                        }
                        alert(msg);
                    } else {
                        alert(jsonResult.message);
                    }
                }
            }
        }
    });
}

function loadTagPanel() {
    $(".tag-panel").show();
}

function closeTagPanel() {
    $(".tag-panel").hide();
}

function addTag() {
    var tag = $(this).data("tag");

    var content = $("#tag").val();

    if (content) { // 如果不为空
        // 判断是否重复
        if (content.indexOf(tag) == -1) { // 不重复
            content = content + ',' + tag;
        } else {
            return false;
        }
    } else {
        content += tag;
    }

    $("#tag").val(content);
}