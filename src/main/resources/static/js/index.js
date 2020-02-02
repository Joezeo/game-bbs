var indexDTO = {
    user:{},
    pagination: {},
    condition: '',
    tab: '',
    loaded: false, // 页面是否异步加载完毕
    loadedUser: false
};
var vue = new Vue({
    el: "#index",
    data: indexDTO,
    mounted: function () { // 表示这个vue对象加载成功了
        this.closable();
        this.getUser();
        this.list(1);
    },
    methods: {
        getUser:function(){
            var url = "/getUser";
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if(jsonResult.success){
                    vue.user = jsonResult.data;
                    vue.loadedUser = true;
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
                tab = 'question';
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
        }
    }
});

