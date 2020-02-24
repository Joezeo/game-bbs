var indexDTO = {
    user: {},
    pagination: {},
    condition: '',
    tab: 'square',
    loaded: false, // 页面是否异步加载完毕
    loadedUser: false
};
var vue = new Vue({
        el: "#forum",
        data: indexDTO,
        mounted: function () { // 表示这个vue对象加载成功了
            this.closable();
            this.getUser();
            this.list(1);
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
                        } else {
                            vue.loadedUser = false;
                        }
                    } else {
                        vue.loadedUser = false;
                    }
                })
            },
            closable: function () {
                // 判断此index页面是否是由自动登录功能打开的页面，如是则需关闭
                var closable = window.localStorage.getItem("closable");
                if (closable) {
                    window.localStorage.removeItem("closable");
                    window.close();
                }
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
                        vue.condition = indexdto.condition;
                        vue.tab = indexdto.tab;
                        vue.loaded = true;
                    } else {
                        alert(jsonResult.message);
                    }
                })
            },
            spideProxyIP: function () {
                var url = 'spideProxyIP';
                axios.post(url).then(function (response) {
                    var jsonResult = response.data;
                    if (jsonResult.success) {

                    } else {
                        alert(jsonResult.message);
                    }
                })
            },
            spideUrl: function () {
                var url = 'spideUrl';
                axios.post(url).then(function (response) {
                    var jsonResult = response.data;
                    if (jsonResult.success) {

                    } else {
                        alert(jsonResult.message);
                    }
                })
            },
            spideApp: function () {
                var url = 'spideApp';
                axios.post(url).then(function (response) {
                    var jsonResult = response.data;
                    if (jsonResult.success) {

                    } else {
                        alert(jsonResult.message);
                    }
                })
            },
            checkUrl: function () {
                var url = 'checkUrl';
                axios.post(url).then(function (response) {
                    var jsonResult = response.data;
                    if (jsonResult.success) {

                    } else {
                        alert(jsonResult.message);
                    }
                })
            },
            checkApp: function () {
                var url = 'checkApp';
                axios.post(url).then(function (response) {
                    var jsonResult = response.data;
                    if (jsonResult.success) {

                    } else {
                        alert(jsonResult.message);
                    }
                })
            },
            spideSpecialPrice: function () {
                var url = 'spideSpecialPrice';
                axios.post(url).then(function (response) {
                    var jsonResult = response.data;
                    if (jsonResult.success) {

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
            }
        }
    })
;


