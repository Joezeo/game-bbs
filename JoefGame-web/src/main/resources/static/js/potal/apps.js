var pagination = {};
var apps = {};
var type = 1;
var page = 1;
var loaded = false;

var vue = new Vue({
    el: "#apps",
    data: {pagination, apps, type, loaded, page},
    mounted: function () {
        this.getMem();
        this.list(this.page, this.type);
    },
    methods: {
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
                size: 20, // 默认每页展示10条帖子
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
                    alert(jsonResult.message);
                }
            })
        },
        reload:function () {
            window.sessionStorage.removeItem("page");
            window.sessionStorage.removeItem("type");
            window.location.reload();
        }
    }
});