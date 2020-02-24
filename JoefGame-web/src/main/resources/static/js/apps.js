var pagination={};
var apps={};
var type=1;
var loaded = false;
var vue = new Vue({
    el:"#apps",
    data:{pagination, apps, type, loaded},
    mounted:function () {
        this.list(1, this.type);
    },
    methods:{
        list:function (page, type) {
            var url = "/steam/list";
            var params = {
                page: page,
                size: 20, // 默认每页展示10条帖子
                appType: type
            };
            axios.post(url, params).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {
                    var appsDto = jsonResult.data;
                    vue.pagination = appsDto.pagination;
                    vue.apps = vue.pagination.datas;
                    vue.loaded = true;
                } else {
                    alert(jsonResult.message);
                }
            })
        },
        test:function () {
            alert(vue.type);
        }
    }
});