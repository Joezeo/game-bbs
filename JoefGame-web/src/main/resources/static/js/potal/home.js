var user = {};
var loadedUser = false;
var condition = "";

var subscribeType = "user"; // user-左侧信息条展示用户关注用户列表 steam-左侧信息条展示用户收藏steam应用列表
var pagination = {};
var loadedSubscribe = false;

var vue = new Vue({
    el:"#home",
    data:{user,loadedUser, condition, subscribeType, pagination, loadedSubscribe},
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
                    vue.loadedSubscribe = true;
                } else {
                    alert(jsonResult.message);
                }
            });
        },
        listUser:function () {
            this.subscribeType = "user";
            this.listSubscribe(1, this.subscribeType);
        },
        listSteam:function () {
            this.subscribeType = "steam";
            this.listSubscribe(1, this.subscribeType);
        }
    }
});