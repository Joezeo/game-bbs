var path = window.location.href;
var section = path.substr(path.lastIndexOf("/")+1);
var sectionName = "";
var pagination = {};
var loaded = false;

var vue = new Vue({
    el:"#profile",
    data:{
        section:section,
        sectionName,
        pagination,
        loaded
    },
    mounted:function () {
        switch (this.section) {
            case "topics":
                this.sectionName = "我的帖子";
                break;
            case "notify":
                this.sectionName = "最新回复";
                break;
        }
        this.getDatas(this.section,1);
    },
    methods:{
        readAllNotification:readAllNotification,
        getDatas:function (section, page) {
            var url = "";
            var params={
                page:page,
                size:10 // 默认显示为10
            };
            switch (section) {
                case "topics":
                    url = "/profile/getTopics";
                    break;
                case "notify":
                    url = "/profile/getNotify";
                    break;
            }

            axios.post(url, params).then(function (response) {
                var jsonResult = response.data;
                if(jsonResult.success){
                    vue.pagination = jsonResult.data;
                    vue.loaded = true;
                }
            });
        }
    }
});

function readAllNotification() {
    $.post({
        url: '/notification/allRead',
        dataType: 'json',
        contentType: 'application/json',
        success: function (jsonResult) {
            if(jsonResult.success){
                window.location.reload();
            } else {
                if (jsonResult.code == 2004){ // 未登录
                    var flag = confirm("当前操作需用户登录后进行，点击确定自动登录");
                    if (flag) {
                        window.open("https://github.com/login/oauth/authorize?client_id=332735b1b85bfbb88779&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                        window.setInterval(function () {
                            if (!window.localStorage.getItem("closable"))
                                window.location.reload();
                        }, 200);
                    }
                } else {
                    alert(jsonResult.message);
                }
            }
        }
    })
}