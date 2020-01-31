var indexDTO = {
    pagination: {},
    condition: '',
    tab: '',
    loaded:false // 页面是否异步加载完毕
};
var vue = new Vue({
    el: "#index",
    data: indexDTO,
    mounted: function () { // 表示这个vue对象加载成功了
        this.closable();
        this.list(1);
    },
    methods: {
        closable: function () {
            // 判断此index页面是否是由自动登录功能打开的页面，如是则需关闭
            var closable = window.localStorage.getItem("closable");
            if (closable) {
                window.localStorage.removeItem("closable");
                window.close();
            }
        },
        list: function (page,tab) {
            var url = "/list";
            var params = {
                page: page,
                tab: tab
            };
            axios.get(url,{params}).then(function (response) {
                var indexdto = response.data.data;
                vue.pagination = indexdto.pagination;
                vue.condition = indexdto.condition;
                vue.tab = indexdto.tab;
                vue.loaded = true;
            })
        }
    }
});

