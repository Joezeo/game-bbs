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
        this.resolvAppType();
        this.getApp();
    },
    methods: {
        resolvAppType: function () {
            switch (type) {
                case 1:
                    vue.typeStr = '游戏';
                    break;
                case 2:
                    vue.typeStr = '软件';
                    break;
                case 3:
                    vue.typeStr = 'DLC';
                    break;
                case 4:
                    vue.typeStr = '试玩游戏';
                    break;
                case 5:
                    vue.typeStr = '捆绑包';
                    break;
                case 6:
                    vue.typeStr = '原声带';
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
