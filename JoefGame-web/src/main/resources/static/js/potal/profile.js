var path = window.location.href;
var section = path.substr(path.lastIndexOf("/") + 1);
var sectionName = "";
var pagination = {};
var loaded = false;
var unreadCount = 0;
var user = {};
var loadedUser = false;
var condition = "";

var vue = new Vue({
    el: "#profile",
    data: {
        section,
        sectionName,
        pagination,
        loaded,
        unreadCount,
        user, loadedUser,condition
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
    },
    methods: {
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
                    alert(jsonResult.message);
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
                    alert(jsonResult.message);
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
        // search相关函数，函数从文件search.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic
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
    })
}