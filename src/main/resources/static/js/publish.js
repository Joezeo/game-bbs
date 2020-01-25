$(function () {
    $("#publish-btn").click(doPublish);
});

// 进行数据非空校验
function doCheckValue(title, description, tag) {
    var falg = true;

    if (title === "") {
        $("#title-warning").show();
        falg = false;
    } else {
        $("#title-warning").hide();
    }

    if (description === "") {
        $("#des-warning").show();
        falg = false;
    } else {
        $("#des-warning").hide();
    }

    if (tag === "") {
        $("#tag-warning").show();
        falg = false;
    } else {
        $("#tag-warning").hide();
    }
    return falg;
}

function doPublish() {
    // 进行表格内容空校验
    var title = $("#title").val();
    var description = $("#description").val();
    var tag = $("#tag").val();
    var id = $("#id").val();

    var flag = doCheckValue(title, description, tag);
    if (!flag) { // flag为 false 说明有数据为空
        return false;
    }
    $.ajax({
        url: '/publish',
        dataType: 'json',
        data: {title: title, description: description, tag: tag, id: id},
        type: 'post',
        success: function (jsonResult) {
            debugger;
            if (jsonResult.success) {
                if (id != null && id != ""){
                    location.href = "/question/" + id;
                }
                else{
                    location.href = "/";
                }
            } else {
                alert(jsonResult.message);
            }
        }
    });
}