var email = "";
var password = "";
var rememberMe = false;

var vue = new Vue({
    el:"#login",
    data:{email, password, rememberMe},
    mounted:function () {

    },
    methods:{
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