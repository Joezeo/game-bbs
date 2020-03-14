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
var pagination = {};
var results = {};
var loaded = false;
var apptypes = ["游戏","软件","DLC","试玩游戏","捆绑包","原声带","礼包"];

var vue = new Vue({
    el:"#search",
    data:{
        condition,type,param,pagination,results,loaded,apptypes
    },
    mounted:function(){
        this.search(1,this.type, this.param);
    },
    methods:{
        // search相关函数，函数从文件commonSearch.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic,
        search:function (page, type, param) {
            var url = "/search/"+type;
            var params = {condition: param, page: page};
            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    vue.pagination = jsonResult.data;
                    vue.results = vue.pagination.datas;
                    vue.loaded = true;
                } else {
                    alert(jsonResult.message);
                    window.location.href = "/"; //搜索失败，返回主页
                }
            })
        },
        // 截除数字的小数位
        fixedNum:function (num) {
            var numStr = "" + num;
            return numStr.split(".")[0];
        },
        // 处理时间戳
        timestampToTime: function (timestamp) {
            var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
            var Y = date.getFullYear() + '-';
            var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
            var D = date.getDate() + ' ';
            var h = date.getHours() + ':';
            var m = date.getMinutes() + ':';
            var s = date.getSeconds();

            return Y + M + D + h + m + s;
        }
    }
});