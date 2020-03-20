var vue = new Vue({
    el:"#spider",
    data:{},
    mounted:function () {

    },
    methods:{
        steamAuth:function(){
            var url = '/steam/auth';
            window.location.href = url;
        },
        spideProxyIP: function () {
            var url = 'spideProxyIP';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    layer.msg(jsonResult.message);
                }
            })
        },
        spideUrl: function () {
            var url = 'spideUrl';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    layer.msg(jsonResult.message);
                }
            })
        },
        spideApp: function () {
            var url = 'spideApp';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    layer.msg(jsonResult.message);
                }
            })
        },
        checkUrl: function () {
            var url = 'checkUrl';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    layer.msg(jsonResult.message);
                }
            })
        },
        checkApp: function () {
            var url = 'checkApp';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    layer.msg(jsonResult.message);
                }
            })
        },
        spideSpecialPrice: function () {
            var url = 'spideSpecialPrice';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    layer.msg(jsonResult.message);
                }
            })
        }
    }
});