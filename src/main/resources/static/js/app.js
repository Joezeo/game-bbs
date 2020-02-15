var url = window.location.href;
var appid = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));
var type = url.substr(url.lastIndexOf("=") + 1);
var typeStr = '';
var app = {};
var loaded = false;

var vue = new Vue({
    el: "#app",
    data: {appid, type, typeStr, app, loaded},
    mounted: function () {
        this.getApp();
        this.resolvAppType();
    },
    methods: {
        resolvAppType: function () {
            switch (Number.parseInt(type)) {
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
                    vue.app.summary = vue.app.summary.replace("|", "<br>");
                    vue.loaded = true;
                } else {
                    alert(jsonResult.message);
                }
            })
        }
    }
});
