var user = {};
var loadedUser = false;

var vue =new Vue({
    el:"#index",
    data:{
        user, loadedUser
    },
    mounted:function () {
        this.getUser();
    },
    methods:{
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
        },
        btnJumpTo:function (loadedUser) {
            if(loadedUser){
                window.location.href="/forum";
            } else {
                window.location.href="/login";
            }
        }
    }
});
