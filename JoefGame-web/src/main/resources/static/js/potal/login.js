var email = "";
var password = "";
var rememberMe = false;
var condition = "";


axios.defaults.withCredentials = true;
var vue = new Vue({
    el:"#login",
    data:{email, password, rememberMe, condition},
    mounted:function () {
        this.removeStorage();
    },
    methods:{
        // 移除apps页面存储的page、type信息
        removeStorage:function(){
            window.sessionStorage.removeItem("page");
            window.sessionStorage.removeItem("type");
        },
        login:function (email, password, rememberMe) {
            var url = "/login";
            var params = {
                email:email,password:password, rememberMe:rememberMe
            };

            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    window.location.href = "/home";
                } else {
                    layer.msg(jsonResult.message);
                }
            });
        },
        // search相关函数，函数从文件commonSearch.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic
    }
});