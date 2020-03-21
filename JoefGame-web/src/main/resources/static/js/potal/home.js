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

var vue = new Vue({
    el:"#home",
    data:{user,loadedUser, condition, subscribeType, pagination, loadedSubscribe, leftUserClass, leftSteamClass, subsribeNum},
    mounted:function () {
        this.removeStorage();
        this.getUser();
        this.listSubscribe(1, this.subscribeType);
    },
    methods:{
        // 移除apps页面存储的page、type信息
        removeStorage:function(){
            window.sessionStorage.removeItem("page");
            window.sessionStorage.removeItem("type");
        },
        getUser:function () {
            var url = "/getUser";
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {
                    var getedUser = jsonResult.data;
                    if (getedUser) {
                        vue.user = getedUser;
                        vue.loadedUser = true;
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

        // search相关函数，函数从文件commonSearch.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic,
        listSubscribe:function (page, subscribeType) {
            var url = "/home/listSubscribe";
            var params = {page:page,condition:subscribeType};
            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    vue.pagination = jsonResult.data;
                    vue.subsribeNum = jsonResult.data.datas.length;
                    vue.loadedSubscribe = true;
                } else {
                    layer.msg(jsonResult.message);
                }
            });
        },
        listUser:function () {
            if(this.leftSteamClass == "my-active") {
                this.leftSteamClass = "";
            }
            if(this.leftUserClass == ""){
                this.leftUserClass = "my-active";
            }
            this.subscribeType = "user";
            this.listSubscribe(1, this.subscribeType);
        },
        listSteam:function () {
            if(this.leftUserClass == "my-active") {
                this.leftUserClass = "";
            }
            if(this.leftSteamClass == ""){
                this.leftSteamClass = "my-active";
            }
            this.subscribeType = "steam";
            this.listSubscribe(1, this.subscribeType);
        }
    }
});