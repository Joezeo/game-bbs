var user = {};
var loadedUser = false;
var vue = new Vue({
    el:"#home",
    data:{user,loadedUser},
    mounted:function () {
        this.removeStorage();
        this.getUser();
    },
    methods:{
        // 移除apps页面存储的page、type信息
        removeStorage:function(){
            window.sessionStorage.removeItem("page");
            window.sessionStorage.removeItem("type");
        },
        getUser:function () {
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
        }
    }
})