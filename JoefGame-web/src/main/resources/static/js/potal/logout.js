$(function () {
    $("#logout").click(function () {
        var url = "/logout";

        axios.post(url).then(function (result) {
            var jsonResult = result.data;
            if(jsonResult.success){
                window.location.href = window.location.href;
            } else {
                layer.msg(jsonResult.message);
            }
        })
    });
});