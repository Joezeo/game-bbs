var url = window.location.href.split("#")[0];
var appid = "0";
if (url.lastIndexOf("?") != -1) {
    appid = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));
} else {
    appid = url.substring(url.lastIndexOf("/") + 1);
}
if(isNaN(appid)){
    layer.msg("很抱歉参数异常，将导航至主页");
    window.location.href = "/";
}
var type = "0";
if (url.lastIndexOf("type=") != -1) {
    type = url.substr(url.lastIndexOf("=") + 1);
}
if(isNaN(type) || type < 0 || type > 7){
    layer.msg("很抱歉参数异常，将导航至主页");
    window.location.href = "/";
}

var user = {};
var loadedUser = false;
var typeStr = '';
var app = {};
var loaded = false;
var chartTitle = '历史价格';
var prices = {};
var condition = "";
var favorite = false; // 用户是否收藏该Steam 应用
var favoriteList = {};
var typeStrs = ["游戏","软件","DLC","试玩游戏","捆绑包","原声带","礼包"];

var vue = new Vue({
    el: "#app",
    data: {user, loadedUser, appid, type, typeStr, app, loaded, prices, chartTitle,
        condition, favorite,favoriteList},
    mounted: function () {
        this.getApp();
        this.getPrice();
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
                        vue.getFavorites(vue.loadedUser);
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
        resolvAppType: function () {
            this.typeStr = typeStrs[this.type-1];
        },
        getApp: function () {
            var url = "/steam/getApp";
            var params = {appid: appid, type: type};
            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if (jsonResult.success) {
                    vue.app = jsonResult.data;
                    vue.type = vue.app.type;
                    if (vue.type != 7 && vue.type != 5) {
                        vue.app.summary = vue.app.summary.replace("|", "<br>");
                    }
                    vue.loaded = true;
                    vue.resolvAppType();
                } else {
                    layer.msg(jsonResult.message);
                }
            })
        },
        getPrice: function () {
            var url = "/steam/getPrice";
            var params = {appid: appid, type: type};
            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if (jsonResult.success) {
                    var priceDto = jsonResult.data;
                    vue.prices = priceDto;
                    loadPriceChart(vue.prices);
                } else {
                    layer.msg(jsonResult.message);
                }
            })
        },
        // search相关函数，函数从文件commonSearch.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic,
        getFavorites:function(loadedUser){
            if(loadedUser){ // 用户登录了
                var url = "/steam/getFavorites";
                axios.post(url).then(function (result) {
                    var jsonResult = result.data;
                    if(jsonResult.success){
                        vue.favoriteList = jsonResult.data.favorites;
                        checkFavorites(vue.favoriteList, vue.appid);
                    } else {
                        layer.msg(jsonResult.message);
                    }
                })
            } else { // 用户没有登录
                this.favorite = false;
            }
        },
        favoriteApp:function (appid,type,loadedUser) {
            if(!loadedUser){
                window.location.href = "/login";
                return false;
            }
            var url = "/steam/favoriteApp";
            var params = {appid:appid, type:type};
            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    vue.favoriteList = jsonResult.data.favorites;
                    vue.favorite = true;
                } else {
                    layer.msg(jsonResult.message);
                }
            });
        },
        unFavoriteApp:function (appid,type,loadedUser) {
            if(!loadedUser){
                window.location.href = "/login";
                return false;
            }
            var url = "/steam/unFavoriteApp";
            var params = {appid:appid, type:type};
            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    vue.favoriteList = jsonResult.data.favorites;
                    vue.favorite = false;
                } else {
                    layer.msg(jsonResult.message);
                }
            });
        }
    }
});

function loadPriceChart(prices) {
    var time = prices.time;
    var price = prices.price;

    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('chart'));

    // 指定图表的配置项和数据
    var option = {
        xAxis: {
            type: 'category',
            data: time
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: price,
            type: 'line',
            smooth: true
        }]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

function checkFavorites(favorites, appid) {
    for(var i=0; i<favorites.length; i++){
        if(favorites[i].appid == appid){
            vue.favorite = true;
            return;
        }
    }
}


