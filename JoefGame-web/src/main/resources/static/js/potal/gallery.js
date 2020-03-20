$(function () {
    removeStorage();
    $("#img-select").click(loadFile);
    $("#file-input").change(uploadFile);
});

var condition = "";
var vue = new Vue({
    el:"#gallery",
    data:{condition},
    methods:{
        // search相关函数，函数从文件commonSearch.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic
    }
});

// 移除apps页面存储的page、type信息
function removeStorage(){
    window.sessionStorage.removeItem("page");
    window.sessionStorage.removeItem("type");
}

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
                    layer.msg("使用图床需要登录，请登录");
                    window.location.href = "/login";
                } else {
                    layer.msg(jsonResult.message);
                }
            }
        }
    }

    $("#pic-form").ajaxSubmit(options); //异步提交
}