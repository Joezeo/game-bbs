// 用于用户消息队列中消息的获取
function getMessage(userid) {
    var url = "/message/doGet";
    var params = {userid:userid};
    axios.post(url, params).then(function (result) {
        var jsonResult = result.data;
        if(jsonResult.success){
            // TODO:Layer右下角弹窗显示消息
        } else {
            layer.msg("服务器异常，获取消息失败");
        }
    });
}