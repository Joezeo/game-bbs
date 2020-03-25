var user = {};
var loadedUser = false;
var condition = "";

var subscribeType = "user"; // user-左侧信息条展示用户关注用户列表 steam-左侧信息条展示用户收藏steam应用列表
var pagination = {};
var loadedSubscribe = false;
var subsribeNum = 0;

// 控制左侧关注列表，选择样式class
var leftUserClass = "my-active";
var leftSteamClass = "";

// 中部选择样式
var middleUserClass = "my-active"; // 控制中部显示 '关注动态'
var middleSteamClass = ""; // 控制中部显示 'STEAM新闻速递'

// 用户动态信息
var userPosts = {};

// Steam新闻
var appNews = {};

var vue = new Vue({
    el: "#home",
    data: {
        user, loadedUser, condition, subscribeType, pagination, loadedSubscribe,
        leftUserClass, leftSteamClass, subsribeNum, middleSteamClass, middleUserClass,
        userPosts, appNews
    },
    mounted: function () {
        this.removeStorage();
        this.getUser();
        this.listSubscribe(1, this.subscribeType);
        this.listUserPosts();
    },
    methods: {
        // 移除apps页面存储的page、type信息
        removeStorage: function () {
            window.sessionStorage.removeItem("page");
            window.sessionStorage.removeItem("type");
        },
        getUser: function () {
            var url = "/getUser";
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {
                    var getedUser = jsonResult.data;
                    if (getedUser) {
                        vue.user = getedUser;
                        vue.loadedUser = true;
                        if (jsonResult.hasMessage) {
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

        // search相关函数，函数从文件commonSearch.js中引入
        searchUser: searchUser,
        searchSteam: searchSteam,
        searchTopic: searchTopic,
        listSubscribe: function (page, subscribeType) {
            var url = "/home/listSubscribe";
            var params = {page: page, condition: subscribeType};
            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if (jsonResult.success) {
                    vue.pagination = jsonResult.data;
                    vue.subsribeNum = jsonResult.data.datas.length;
                    vue.loadedSubscribe = true;
                } else {
                    layer.msg(jsonResult.message);
                }
            });
        },
        listUser: function () {
            if (this.leftSteamClass == "my-active") {
                this.leftSteamClass = "";
            }
            if (this.leftUserClass == "") {
                this.leftUserClass = "my-active";
            }
            this.subscribeType = "user";
            this.listSubscribe(1, this.subscribeType);
        },
        listSteam: function () {
            if (this.leftUserClass == "my-active") {
                this.leftUserClass = "";
            }
            if (this.leftSteamClass == "") {
                this.leftSteamClass = "my-active";
            }
            this.subscribeType = "steam";
            this.listSubscribe(1, this.subscribeType);
        },
        listUserPosts: function () {
            if (this.middleUserClass == "") {
                this.middleUserClass = "my-active";
            }
            if (this.middleSteamClass == "my-active") {
                this.middleSteamClass = "";
            }

            var url = "/home/getUserPosts";
            axios.post(url).then(function (result) {
                var jsonResult = result.data;
                if (jsonResult.success) {
                    vue.userPosts = jsonResult.data;
                } else {
                    layer.msg(jsonResult.message);
                }
            });
        },
        listSteamAppNews: function () {
            if (this.middleSteamClass == "") {
                this.middleSteamClass = "my-active";
            }
            if (this.middleUserClass == "my-active") {
                this.middleUserClass = "";
            }
            var url = "/home/getSteamNews";
            axios.post(url).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    vue.appNews = jsonResult.data;
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
        }
    }
});