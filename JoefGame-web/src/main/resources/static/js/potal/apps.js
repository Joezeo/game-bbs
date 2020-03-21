var pagination = {};
var apps = {};
var type = 1;
var page = 1;
var loaded = false;
var condition = "";
var user = {};
var loadedUser = false;

var vue = new Vue({
    el: "#apps",
    data: {pagination, apps, type, loaded, page, condition, user, loadedUser},
    mounted: function () {
        this.getMem();
        this.list(this.page, this.type);
        this.getUser();
    },
    methods: {
        getUser: function () {
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
        getMem:function(){
            var page = window.sessionStorage.getItem("page");
            var type = window.sessionStorage.getItem("type");
            if(page){
                this.page = page;
            }
            if(type){
                this.type = type;
            }
        },
        list: function (page, type) {
            window.sessionStorage.setItem("page", page);
            window.sessionStorage.setItem("type", type);
            this.page = page;
            var url = "/steam/list";
            var params = {
                page: page,
                size: 30, // 默认每页展示30款软件
                appType: type
            };
            axios.post(url, params).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {
                    var appsDto = jsonResult.data;
                    vue.pagination = appsDto.pagination;
                    vue.page = vue.pagination.page;
                    vue.apps = vue.pagination.datas;
                    vue.loaded = true;
                } else {
                    layer.msg(jsonResult.message);
                }
            })
        },
        reload:function () {
            window.sessionStorage.removeItem("page");
            window.sessionStorage.removeItem("type");
            window.location.reload();
        },
        // 截除数字的小数位
        fixedNum:function (num) {
            var numStr = "" + num;
            return numStr.split(".")[0];
        },
        // search相关函数，函数从文件conmmonSearch.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic
    }
});