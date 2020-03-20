var path = window.location.href;
var topicId = path.substr(path.lastIndexOf("/") + 1);

// 当前登录用户
var user = {};
var topic = {}; // topic.user此篇帖子的发起用户
var comments = {};
var subComments = {};
var commentContent = "";
var likeUsersId = {}; // 对当前帖子点赞的所有用户id

var loadedUser = false;
var loadedTopic = false;
var loadedComments = false;
var loadedSubComments = false;

var likeStatus = false;
var liked = "";

var condition = "";

axios.default.withCredentials = true;
var vue = new Vue({
    el: "#topic",
    data: {
        topicId, topic, comments, subComments, user, likeUsersId,
        loadedTopic, loadedComments, loadedSubComments, loadedUser,
        commentContent, likeStatus, liked, condition
    },
    mounted: function () {
        this.removeStorage();
        this.getUser();
        this.getTopic(this.topicId);
        this.getComments(this.topicId);
        this.subComment(0);
    },
    methods: {
        // 移除apps页面存储的page、type信息
        removeStorage:function(){
            window.sessionStorage.removeItem("page");
            window.sessionStorage.removeItem("type");
        },
        // 回复问题
        doComment: doComment,
        // 点击回复评论功能
        subComment: subComment,
        // 点击二级回复的取消 关闭二级评论框
        closeSubcomment: closeSubcomment,
        // 点击二级回复的回复按钮，提交二级评论
        doSubcomment: doSubcomment,
        // 如未登录点击链接自动登录
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
                    vue.likeUsersId = vue.topic.likeUsersId;
                    // Editor.md无法用vue设置，暂时使用jquery赋值
                    // $("#topic-textarea").val(vue.topic.description);
                    loadLikeStatus(vue.user, vue.likeUsersId);
                } else {
                    layer.msg(jsonResult.message);
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
                    for (var idx in vue.comments) {
                        loadCommentLikeStatus(vue.user, vue.comments[idx]);
                    }
                } else {
                    layer.msg(jsonResult.message);
                }
            })
        },
        splitTag: function (tag) {
            return tag.split(",");
        },
        // 点赞帖子
        likeTopic: function (topicId, userId, likeStatus) {
            // 如果还未登录
            if (!vue.loadedUser) {
                layer.msg("此操作需要登录，请登录后继续操作");
                return false;
            }
            if (likeStatus) { // 已点赞取消点赞
                var url = "/topic/unlike";
                var params = {id: topicId, userid: userId}; // id-帖子id userid-当前点赞人的id
                axios.post(url, params).then(function (result) {
                    var jsonResult = result.data;
                    if (jsonResult.success) {
                        // 由于点赞数不是太重要，这里就直接静态修改帖子的点赞数就行了
                        vue.topic.likeCount--;
                        vue.likeUsersId = jsonResult.data.likeUsersId;
                        vue.likeStatus = false;
                        loadLikeStatus(vue.user, vue.likeUsersId);
                    } else {
                        layer.msg(jsonResult.message);
                    }
                })
            } else {
                var url = "/topic/like";
                var params = {id: topicId, userid: userId}; // id-帖子id userid-当前点赞人的id
                axios.post(url, params).then(function (result) {
                    var jsonResult = result.data;
                    if (jsonResult.success) {
                        // 由于点赞数不是太重要，这里就直接静态修改帖子的点赞数就行了
                        vue.topic.likeCount++;
                        vue.likeUsersId = jsonResult.data.likeUsersId;
                        vue.likeStatus = true;
                        loadLikeStatus(vue.user, vue.likeUsersId);
                    } else {
                        layer.msg(jsonResult.message);
                    }
                })
            }
        },
        // 点赞评论
        likeComment: function (commentID, userID, idx) {
            // 如果还未登录
            if (!vue.loadedUser) {
                layer.msg("此操作需要登录，请登录后继续操作");
                return false;
            }
            if (vue.comments[idx].likeStatus) { // 取消点赞
                var url = "/comment/unlike";
                var params = {id: commentID, userid: userID}; // id-帖子id userid-当前点赞人的id
                axios.post(url, params).then(function (result) {
                    var jsonResult = result.data;
                    if (jsonResult.success) {
                        // 由于点赞数不是太重要，这里就直接静态修改帖子的点赞数就行了
                        vue.comments[idx].likeCount--;
                        vue.comments[idx].likeUsersId = jsonResult.data.likeUsersId;
                        vue.comments[idx].likeStatus = false;
                        loadCommentLikeStatus(vue.user, vue.comments[idx]);
                    } else {
                        layer.msg(jsonResult.message);
                    }
                })
            } else {
                var url = "/comment/like";
                var params = {id: commentID, userid: userID};
                axios.post(url, params).then(function (result) {
                    var jsonResult = result.data;
                    if (jsonResult.success) {
                        vue.comments[idx].likeUsersId = jsonResult.data;
                        vue.comments[idx].likeCount++;
                        vue.comments[idx].likeStatus = true;
                        loadCommentLikeStatus(vue.user, vue.comments[idx]);
                    } else {
                        layer.msg(jsonResult.message);
                    }
                });
            }
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
        },
        // search相关函数，函数从文件commonSearch.js中引入
        searchUser:searchUser,
        searchSteam:searchSteam,
        searchTopic:searchTopic
    }
});

function doComment() {
    var content = vue.commentContent.trim();
    if (content == "") {
        layer.msg("回复内容不可为空，请输入后再回复");
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
                window.location.href = "/login";
            } else {
                layer.msg(jsonResult.message);
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
            } else {
                layer.msg(jsonResult.message);
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
        layer.msg("回复内容不可为空，请输入！");
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
                window.location.href = "/login";
            } else {
                layer.msg(jsonResult.message);
            }
        }
    });
}

// 为当前用户加载此帖子的点赞状态
function loadLikeStatus(user, likeUsersId) {
    var userid = user.id;
    likeUsersId = JSON.parse(JSON.stringify(likeUsersId));
    for (var idx in likeUsersId) {
        if (likeUsersId[idx] == userid) {
            vue.likeStatus = true;
        }
    }
    if (vue.likeStatus) {
        vue.liked = "liked";
    } else {
        vue.liked = "";
    }
}

function loadCommentLikeStatus(user, comment) {
    var likeUsersId = comment.likeUsersId;

    for (var idx in likeUsersId) {
        if (likeUsersId[idx] == user.id) {
            comment.likeStatus = true;
        }
    }
    if (comment.likeStatus) {
        comment.likeClass = "liked";
    } else {
        comment.likeClass = "";
    }
}

