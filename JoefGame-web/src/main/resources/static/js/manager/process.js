var processes = {};
var pagination = {};
var loadedProcess = false;
var vue = new Vue({
    el: "#process",
    data: {processes, loadedProcess},
    mounted: function () {
        this.list(1);
    },
    methods: {
        list: function (page) {
            var url = "/manager/process/list";
            var params = {
                page: page,
                size: 5, // 默认每页展示5条流程
            };
            axios.post(url, params).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {
                    vue.pagination = jsonResult.data;
                    vue.processes = vue.pagination.datas;
                    vue.loadedProcess = true;
                } else {
                    alert(jsonResult.message);
                }
            })
        },
        selectFile: function () {
            $("#process-upload-file").click();
        },
        uploadProcess: function () {
            var options = {
                url: "/manager/process/uploadProcess",
                success: function (jsonResult) {
                    if (jsonResult.success) {
                        window.location.reload();
                    } else {
                        alert(jsonResult.message);
                    }
                }
            };

            $("#process-upload-form").ajaxSubmit(options); //异步提交
        },
        deleteProcess: function (id, name) {
            var flag = confirm("确定要删除流程：[" + name + "]么，此操作不可撤回！！");
            if (!flag) {
                return false;
            }
            var url = "/manager/process/delete";
            var params = {id: id};
            axios.post(url, params).then(function (result) {
                var jsonResult = result.data;
                if (jsonResult.success) {
                    window.location.reload();
                } else {
                    alert(jsonResult.message);
                }
            })
        },
        viewProcessPic: function (id) {
            $("#mymodal").modal('show');
            $("#process-pic-img").attr("src", "/manager/process/viewProcessPic?id="+id);
        }
    }
});