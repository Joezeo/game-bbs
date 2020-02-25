var name = "";
var email = "";
var password = "";
var authCode="";

var vue = new Vue({
    el:"#signup",
    data:{name, email, password, authCode},
    mounted:function () {
        this.removeStorage();
    },
    methods:{
        // 移除apps页面存储的page、type信息
        removeStorage:function(){
            window.sessionStorage.removeItem("page");
            window.sessionStorage.removeItem("type");
        },
        signup:function (name, email, password, authCode) {
            var url = "/signup";
            var params = {
                name:name,email:email,password:password,authCode:authCode
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