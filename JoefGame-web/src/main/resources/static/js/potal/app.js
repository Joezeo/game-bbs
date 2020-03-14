var url = window.location.href.split("#")[0];
var appid = "0";
if (url.lastIndexOf("?") != -1) {
    appid = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));
} else {
    appid = url.substring(url.lastIndexOf("/") + 1);
}
if(isNaN(appid)){
    alert("很抱歉参数异常，将导航至主页");
    window.location.href = "/";
}
var type = "0";
if (url.lastIndexOf("type=") != -1) {
    type = url.substr(url.lastIndexOf("=") + 1);
}
if(isNaN(type) || type < 1 || type > 7){
    alert("很抱歉参数异常，将导航至主页");
    window.location.href = "/";
}

var typeStr = '';
var app = {};
var loaded = false;
var chartTitle = '历史价格';
var prices = {};
var condition = "";

var vue = new Vue({
    el: "#app",
    data: {appid, type, typeStr, app, loaded, prices, chartTitle, condition},
    mounted: function () {
        this.getApp();
        this.getPrice();
    },
    methods: {
        resolvAppType: function () {
            switch (Number.parseInt(this.type)) {
                case 1:
                    this.typeStr = '游戏';
                    break;
                case 2:
                    this.typeStr = '软件';
                    break;
                case 3:
                    this.typeStr = 'DLC';
                    break;
                case 4:
                    this.typeStr = '试玩游戏';
                    break;
                case 5:
                    this.typeStr = '捆绑包';
                    break;
                case 6:
                    this.typeStr = '原声带';
                    break;
                case 7:
                    this.typeStr = '礼包';
                    break;
                default:
                    this.typeStr = '';
                    break;
            }
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
                    alert(jsonResult.message);
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
                    alert(jsonResult.message);
                }
            })
        },
        // search相关函数，函数从文件commonSearch.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic
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



