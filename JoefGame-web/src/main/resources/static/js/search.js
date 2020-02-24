$(function () {
    $("#search-icon").click(function () {
        $("#input-condition").show();
        return false;
    });

    $(document).on('click', function (e) {
        var e = e || window.event; //浏览器兼容性
        var elem = e.target || e.srcElement;
        while (elem) { //循环判断至跟节点，防止点击的是div子元素
            if (elem.id && elem.id == 'input-condition') {
                return;
            }//如果还有别的div不想点击，就加else if判断
            elem = elem.parentNode;
        }
        //这里写你想实现的效果
        $("#input-condition").hide();
    })
});
