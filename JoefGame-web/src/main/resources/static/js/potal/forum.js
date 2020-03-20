var forumDTO = {
    user: {},
    pagination: {},
    condition: "",
    tab: 'square',
    loaded: false, // 页面是否异步加载完毕
    loadedUser: false
};
var vue = new Vue({
        el: "#forum",
        data: forumDTO,
        mounted: function () { // 表示这个vue对象加载成功了
            this.removeStorage();
            this.getUser();
            this.list(1);
        },
        methods: {
            // 移除apps页面存储的page、type信息
            removeStorage:function(){
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
                        } else {
                            vue.loadedUser = false;
                        }
                    } else {
                        vue.loadedUser = false;
                    }
                })
            },
            list: function (page, tab) {
                var url = "/list";
                if (!tab) {
                    tab = 'square';
                }
                var params = {
                    page: page,
                    size: 10, // 默认每页展示10条帖子
                    tab: tab
                };
                axios.post(url, params).then(function (response) {
                    var jsonResult = response.data;
                    if (jsonResult.success) {
                        var indexdto = response.data.data;
                        vue.pagination = indexdto.pagination;
                        vue.tab = indexdto.tab;
                        vue.loaded = true;
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
            // search相关函数，函数从文件commonSearch.js中引入
            searchUser:searchUser,
            searchSteam:searchSteam,
            searchTopic:searchTopic
        }
    })
;


