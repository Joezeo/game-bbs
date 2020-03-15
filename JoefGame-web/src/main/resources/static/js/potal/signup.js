var name = "";
var email = "";
var password = "";
var authCode="";
var condition = "";

var tmpGithubUser; // 为空说明用户不是通过Github三方登录
var tmpSteamUser; // 为空说明用户不是通过Steam三方登录

var vue = new Vue({
    el:"#signup",
    data:{name, email, password, authCode, condition, tmpGithubUser, tmpSteamUser},
    mounted:function () {
        this.removeStorage();
        this.getTmpGithubUser();
        this.getTmpSteamUser();
    },
    methods:{
        // 移除apps页面存储的page、type信息
        removeStorage:function(){
            window.sessionStorage.removeItem("page");
            window.sessionStorage.removeItem("type");
        },
        signup:function (name, email, password, authCode, tmpGithubUser, tmpSteamUser) {
            var url = "/signup";
            var githubAccountId = null;
            var steamId = null;
            if(tmpGithubUser){
                githubAccountId = tmpGithubUser.id;
            }
            if(tmpSteamUser){
                steamId = tmpSteamUser.steamid;
            }

            var params = {
                name:name,email:email,password:password,authCode:authCode,githubAccountId:githubAccountId,steamId:steamId
            };

            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    window.location.href = "/home";
                } else {
                    alert(jsonResult.message);
                }
            });
        },
        getAuthcode:function (email) {
            var url = "/getAuthcode";
            var params = {email: email};
            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    alert("验证码已发送至你的邮箱!")
                } else {
                    alert(jsonResult.message)
                }
            })
        },
        // search相关函数，函数从文件commonSearch.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic,
        getTmpGithubUser:function () {
            var url = "/getTmpGithubUser";
            axios.post(url).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    vue.tmpGithubUser = jsonResult.data;
                    if(vue.tmpGithubUser != null){
                        vue.name = vue.tmpGithubUser.name;
                    }
                }
            })
        },
        getTmpSteamUser:function () {
            var url = "/getTmpSteamUser";
            axios.post(url).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    vue.tmpSteamUser = jsonResult.data;
                    if(vue.tmpSteamUser != null){
                        vue.name = vue.tmpGithubUser.name;
                    }
                }
            })
        }
    }
});