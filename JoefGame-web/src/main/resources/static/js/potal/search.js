var url = window.location.href; // url正确格式：xxxxx/search/type?condition=xx
var start = url.lastIndexOf("/");
var end = url.lastIndexOf("?");
if(end == -1){ // 表明用户不是通过点击按钮进入的搜索页面，直接返回主页
    window.location.href = "/";
}

var type = url.substring(start+1, end);
if(type != "steam" && type != "user" && type!="topic"){ // 这也表明用户是自己输入的一个错误的url，返回主页
    window.location.href = "/";
}

start = url.lastIndexOf("condition=");
if(start == -1){ // 输入了错误的条件参数，返回主页
    window.location.href = "/";
}
var param = url.substring(start+10); // 用户输入的搜索参数
if(!param){ // 搜索参数为空，返回主页
    window.location.href = "/";
}

var condition=""; // 如果在搜索结果页面使用搜索功能的参数

/*
此些参数用于接收搜索结果
start
 */
var steamAppList = {};
var topicList = {};
var userList = {};

var steamAppLoaded = false;
var topicLoaded = false;
var userLoaded = false;
/* end */

var vue = new Vue({
    el:"#search",
    data:{
        condition,type,param,
        steamAppDTO: steamAppList,topicDTO: topicList,userDTO: userList,steamAppLoaded,topicLoaded,userLoaded
    },
    mounted:function(){
        this.search(this.type, this.param);
    },
    methods:{
        // search相关函数，函数从文件commonSearch.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic,
        search:function (type, param) {
            var url = "/search/"+type;
            var params = {condition: param};
            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    switch (type) {
                        case "steam":
                            vue.steamAppDTO = jsonResult.data;
                            vue.steamAppLoaded = true;
                            break;
                        case "topic":
                            vue.topicDTO = jsonResult.data;
                            vue.topicLoaded = true;
                            break;
                        case "user":
                            vue.userDTO = jsonResult.data;
                            vue.userLoaded = true;
                            break;
                    }
                } else {
                    alert(jsonResult.message);
                    window.location.href = "/"; //搜索失败，返回主页
                }
            })
        }
    }
});