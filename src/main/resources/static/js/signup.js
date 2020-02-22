var name = "";
var email = "";
var password = "";
var authCode="";

var vue = new Vue({
    el:"#signup",
    data:{name, email, password, authCode},
    mounted:function () {

    },
    methods:{
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