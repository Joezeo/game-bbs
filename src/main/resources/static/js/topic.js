var path = window.location.href;
var topicId = path.substr(path.lastIndexOf("/") + 1);

// 当前登录用户
var user = {};
var topic = {}; // topic.user此篇帖子的发起用户
var comments = {};
var subComments = {};
var commentContent = "";
var likeUsersId={}; // 对当前帖子点赞的所有用户id

var loadedUser = false;
var loadedTopic = false;
var loadedComments = false;
var loadedSubComments = false;

axios.default.withCredentials = true;
var vue = new Vue({
    el: "#topic",
    data: {
        topicId, topic, comments, subComments, user, likeUsersId,
        loadedTopic, loadedComments, loadedSubComments, loadedUser,
        commentContent
    },
    mounted: function () {
        this.getUser();
        this.getTopic(this.topicId);
        this.getComments(this.topicId);
        this.subComment(0);
    },
    methods: {
        // 回复问题
        doComment: doComment,
        // 点击回复评论功能
        subComment: subComment,
        // 点击二级回复的取消 关闭二级评论框
        closeSubcomment: closeSubcomment,
        // 点击二级回复的回复按钮，提交二级评论
        doSubcomment: doSubcomment,
        // 如未登录点击链接自动登录
        autoLogin: autoLogin,
        getUser: function () {
            var url = "/getUser";
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.data != null) {
                    vue.user = jsonResult.data;
                    vue.loadedUser = true;
                }
            })
        },
        getTopic: function (topicId) {
            var url = "/topic/getTopic";
            var params = {id: topicId};
            axios.post(url, params).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {
                    vue.topic = jsonResult.data;
                    vue.loadedTopic = true;
                    vue.likeUsersid = vue.topic.likeUsersid;
                    // Editor.md无法用vue设置，暂时使用jquery赋值
                    // $("#topic-textarea").val(vue.topic.description);
                } else {
                    alert(jsonResult.message);
                }
            })
        },
        getComments: function (topicId) {
            var url = "/topic/getComments";
            var params = {id: topicId};
            axios.post(url, params).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {
                    vue.comments = jsonResult.data;
                    vue.loadedComments = true;
                } else {
                    alert(jsonResult.message);
                }
            })
        },
        splitTag: function (tag) {
            return tag.split(",");
        },
        // 点赞帖子
        likeTopic:function (topicId, userId) {
            var url = "/topic/like";
            var params = {id:topicId, userid:userId}; // id-帖子id userid-当前点赞人的id
            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if(jsonResult.success){
                    // 由于点赞数不是太重要，这里就直接静态修改帖子的点赞数就行了
                    vue.topic.likeCount++;
                    vue.likeUsersid = jsonResult.data.likeUsersid;
                } else {
                    alert(jsonResult.message);
                }
            })
        }
    }
});

function autoLogin() {
    window.localStorage.setItem("closable", true);
    window.open("https://github.com/login/oauth/authorize?client_id=332735b1b85bfbb88779&scope=user&state=1");
    // 当完成github第三方验证后，关闭主页，取出localStorage中的closable，刷新当前页面
    window.setInterval(function () {
        if (!window.localStorage.getItem("closable"))
            window.location.reload();
    }, 200);
}

function doComment() {
    var content = vue.commentContent.trim();
    if (content == "") {
        alert("回复内容不可为空，请输入后再回复");
        return false;
    }
    content = vue.commentContent;

    var type = 0; // topic
    var url = "/comment";
    var params = {content: content, parentId: topicId, parentType: type, topicid: vue.topicId};
    axios.post(url, params).then(function (response) {
        var jsonResult = response.data;
        if (jsonResult.success) {
            window.location.reload();
        } else {
            if (jsonResult.code == 2004) { // 用户未进行登录操作
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
    });
}

// 点击回复评论按钮
// 获取该评论的所有二级评论
function subComment(commentId) {
    // vue mounted中执行一次 避免第一次点击无效
    var subcomment = $("#subcomment-" + commentId);
    var isShow = subcomment.hasClass("in");
    if (isShow) { // 当前二级评论框已经显示
        subcomment.removeClass("in");
    } else {
        // 显示二级评论前先关闭所有二级评论框
        $(".collapse").removeClass("in");
        subcomment.addClass("in");

        var url = '/comment/getSubcomment';
        var params = {id: commentId};
        axios.post(url, params).then(function (response) {
            var jsonResult = response.data;
            if (jsonResult.success) {
                vue.subComments = jsonResult.data;
                vue.loadedSubComments = true;
            }
        });
    }
    return false;
}

function closeSubcomment(commentId) {
    $("#subcomment-" + commentId).removeClass("in");
}

// 提交二级评论
function doSubcomment(commentId) {
    var content = $("#subcomment-content-" + commentId).val();
    if (!content) {
        alert("回复内容不可为空，请输入！");
        return false;
    }
    var url = "/comment/subComment";
    var params = {parentId: commentId, parentType: 1, content: content, topicid: vue.topicId};
    axios.post(url, params).then(function (response) {
        var jsonResult = response.data;
        if (jsonResult.success) {
            // 回复成功清空回复框的内容
            $("#subcomment-content-" + commentId).val("");

            // 静态地使页面的评论回复数增加
            var count = $("#subcomment-count" + commentId).html();
            $("#subcomment-count" + commentId).html(parseInt(count) + 1);

            // 重新加载二级评论
            var subcomment = $("#subcomment-" + commentId);
            subcomment.removeClass("in");
            subComment(commentId);
        } else {
            if (jsonResult.code == 2004) { // 用户未进行登录操作
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
    });
}