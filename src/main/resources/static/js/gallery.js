$(function () {
    $("#img-select").click(loadFile);
    $("#file-input").change(uploadFile);
});

function loadFile() {
    $("#file-input").click();
}

function uploadFile() {
    var options = {
        url: "/gallery/upload",
        success: function (jsonResult) {
            if (jsonResult.success) {
                $("#pic-view").attr("src", jsonResult.data);
                $("#pic-src").val(jsonResult.data);
            } else {
                if (jsonResult.code == 2004) { // 未登录
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
                    alert(jsonResult.message);
                }
            }
        }
    }

    $("#pic-form").ajaxSubmit(options); //异步提交
}