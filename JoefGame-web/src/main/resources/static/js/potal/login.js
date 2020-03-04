var email = "";
var password = "";
var rememberMe = false;

axios.defaults.withCredentials = true;
var vue = new Vue({
    el:"#login",
    data:{email, password, rememberMe},
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
                    alert(jsonResult.message);
                }
            });
        }

    }
});