var path = window.location.href;
var section = path.substr(path.lastIndexOf("/") + 1);
var sectionName = "";
var pagination = {};
var loaded = false;
var unreadCount = 0;
var user = {};
var loadedUser = false;
var condition = "";

// 在修改用户头像时需要的avatarUrl
var avatarUrl = "";
var avatarFile = null;

var vue = new Vue({
    el: "#profile",
    data: {
        section,
        sectionName,
        pagination,
        loaded,
        unreadCount,
        user, loadedUser,condition, avatarUrl,avatarFile
    },
    mounted: function () {
        this.removeStorage();
        switch (this.section) {
            case "personal":
                this.sectionName = "个人资料";
                break;
            case "topics":
                this.sectionName = "我的帖子";
                break;
            case "notify":
                this.sectionName = "最新回复";
                break;
        }
        this.getDatas(this.section, 1);
        this.getUnreadCount();
        this.getUser();
    },
    methods: {
        getUser:function () {
            var url = "/getUser";
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {
                    var getedUser = jsonResult.data;
                    if (getedUser) {
                        vue.user = getedUser;
                        vue.loadedUser = true;
                        vue.avatarUrl = vue.user.avatarUrl;
                        if(jsonResult.hasMessage){
                            // 消息队列中存在未读消息
                            getMessage(vue.user.id); // 函数在文件message.js中定义
                        }
                    } else {
                        vue.loadedUser = false;
                    }
                } else {
                    vue.loadedUser = false;
                }
            })
        },
        // 移除apps页面存储的page、type信息
        removeStorage:function(){
            window.sessionStorage.removeItem("page");
            window.sessionStorage.removeItem("type");
        },
        readAllNotification: readAllNotification,
        getDatas: function (section, page) {
            var url = "";
            var params = {
                page: page,
                size: 10 // 默认显示为10
            };
            switch (section) {
                case "personal":
                    url = "/profile/getPersonal";
                    break;
                case "topics":
                    url = "/profile/getTopics";
                    break;
                case "notify":
                    url = "/profile/getNotify";
                    break;
            }

            axios.post(url, params).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {
                    if(vue.section == "personal"){
                        vue.user = jsonResult.data;
                        vue.loadedUser = true;
                    } else {
                        vue.pagination = jsonResult.data;
                        vue.loaded = true;
                    }
                } else {
                    layer.msg(jsonResult.message);
                }
            });
        },
        getUnreadCount: function () {
            var url = "/getUnreadCount";
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {
                    vue.unreadCount = jsonResult.data;
                } else {
                    // 用户未登录，无法获取unreadCount
                    vue.unreadCount = 0;
                }
            });
        },
        readNotify: function (notifyid) {
            var url = "/notification/" + notifyid;
            axios.post(url).then(function (result) {
                var jsonResult = result.data;
                if (jsonResult.success) {
                    window.location.href = "/topic/" + jsonResult.data;
                } else {
                    layer.msg(jsonResult.message);
                }
            })
        },
        // 处理时间戳
        timestampToTime: function (timestamp) {
            var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
            var Y = date.getFullYear() + '-';
            var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
            var D = date.getDate() + ' ';
            var h = date.getHours() + ':';
            var m = date.getMinutes() + ':';
            var s = date.getSeconds();

            return Y + M + D + h + m + s;
        },
        // search相关函数，函数从文件conmmonSearch.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic,
        /*上传头像相关*/
        clickUploadBtn:function () { // 点击个人中心->更换头像按钮
            $("#avatar-uploader").modal('show');
        },
        openAvatarSelection:function () {
            $("#avatar-input-btn").click();
        },
        avatarReview:function () { // 用户选择的头像回显示
            var f = document.getElementById('avatar-input-btn').files[0];

            // 从文件对象获取url地址
            var url = null;
            if (window.createObjectURL != undefined) { // basic
                url = window.createObjectURL(f);
            } else if (window.URL != undefined) { // mozilla(firefox)
                url = window.URL.createObjectURL(f);
            } else if (window.webkitURL != undefined) { // webkit or chrome
                url = window.webkitURL.createObjectURL(f);
            }
            this.avatarUrl = url;
        },
        uploadNewAvatar:function () { // 上传新的头像
            if(this.avatarFile == null){
                layer.msg("请选择头像上传");
                return;
            }
            $("#avatar-uploader").modal('hide'); // 关闭模态框
            var options = {
                url: "/profile/uploadAvatar",
                success: function (jsonResult) {
                    if (jsonResult.success) {
                        layer.msg("更改头像成功");
                    } else {
                        alert("上传失败");
                        layer.msg("更改头像失败");
                        // 当前端向后台传文件时，content-type=multipart/form-data; boundary=----WebKitFormBoundaryVYh9jgAuwmuEaJ5b
                        // 后台在解析异常时，只对content-type=application/json形式的请求才返回json结果
                        // 故此处不能使用 layer.msg(jsonResult.message);
                    }
                }
            };

            $("#user-avatar-form").ajaxSubmit(options); //异步提交表单
            return false;
        },
        randomNewAvatar:function () {
            $("#avatar-uploader").modal('hide'); // 关闭模态框
            let url = '/profile/randomAvatar';
            axios.post(url, {headers:{'content-type':'application/json;charset=utf-8'}}).then(function (result) {
                let jsonResult = result.data;
                if(jsonResult.success){
                    layer.msg("生成随机头像成功");
                } else {
                    layer.msg(jsonResult.message);
                }
            });
        }
        /*上传头像 end*/
    }
});

function readAllNotification() {
    $.post({
        url: '/notification/allRead',
        dataType: 'json',
        contentType: 'application/json',
        success: function (jsonResult) {
            if (jsonResult.success) {
                window.location.reload();
            } else {
                if (jsonResult.code == 2004) { // 未登录
                    window.location.href = "/login";
                } else {
                    layer.msg(jsonResult.message);
                }
            }
        }
    })
}