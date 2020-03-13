$(function () {
    $("#search-icon").click(function () {
        $("#input-condition").show();
        return false;
    });

    $("#input-condition").click(function () {
        $("#search-panel").show();
        return false;
    });

    // 点击页面其他地方隐藏搜索框
    $(document).on('click', function (e) {
        var e = e || window.event; //浏览器兼容性
        var elem = e.target || e.srcElement;
        while (elem) { //循环判断至跟节点，防止点击的是div子元素
            if (elem.id && elem.id == 'input-condition') {
                return;
            }//如果还有别的div不想点击，就加else if判断
            else if (elem.id && elem.id == 'search-panel') {
                return;
            }
            elem = elem.parentNode;
        }
        //这里写你想实现的效果
        $("#input-condition").hide();
    });

    // 点击页面其他地方隐藏搜索面板
    $(document).on('click', function (e) {
        var e = e || window.event; //浏览器兼容性
        var elem = e.target || e.srcElement;
        while (elem) { //循环判断至跟节点，防止点击的是div子元素
            if (elem.id && elem.id == 'search-panel') {
                return;
            }//如果还有别的div不想点击，就加else if判断
            elem = elem.parentNode;
        }
        //这里写你想实现的效果
        $("#search-panel").hide();
    });
});

function searchUser() {
    if(!vue.condition){
        return false;
    }
    window.location.href="/search/user?condition=" + vue.condition;
}

function searchSteam() {
    if(!vue.condition){
        return false;
    }
    window.location.href="/search/steam?condition=" + vue.condition;
}

function searchTopic() {
    if(!vue.condition){
        return false;
    }
    window.location.href="/search/topic?condition=" + vue.condition;
}
