// 用于用户消息队列中消息的获取
function getMessage(userid) {
    var url = "/message/doGet";
    var params = {userid:userid};
    axios.post(url, params).then(function (result) {
        var jsonResult = result.data;
        if(jsonResult.success){
            var app = jsonResult.data;
            // TODO:Layer右下角弹窗显示消息
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                offset: 'rb', // 右下角弹出
                shadeClose: true,
                skin: 'right-bottom-message',
                content: '    <div class="panel panel-default right-bottom-message">\n' +
                    '        <div class="panel-body">\n' +
                    '            <div>\n' +
                    '                <img src="'+app.avatarUrl+'">\n' +
                    '                您收藏的Steam应用 <a href="/steam/app/'+app.appid+'">'+app.name+'</a> 降价了\n' +
                    '            </div>\n' +
                    '            <br>\n' +
                    '            <hr>\n' +
                    '            <input type="button" class="pixel-btn" value="关闭">\n' +
                    '        </div>\n' +
                    '    </div>\n'
            });
        } else {
            layer.msg("服务器异常，获取消息失败");
        }
    });
}