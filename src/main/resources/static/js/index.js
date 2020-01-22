$(function () {
    // 判断此index页面是否是由自动登录功能打开的页面，如是则需关闭
    debugger;
    var closable = window.localStorage.getItem("closable");
    if(closable){
        window.localStorage.removeItem("closable");
        window.close();
    }
});

