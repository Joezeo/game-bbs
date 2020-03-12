var user = {};
var loadedUser = false;

var vue = new Vue({
    el:"#panel",
    data:{user, loadedUser},
    mounted:function () {
        this.getUser();
    },
    methods:{
        getUser:function () {
            var url = "/getUser";
            window.location.href=url;
        }
    }
});