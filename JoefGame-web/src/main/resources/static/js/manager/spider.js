var vue = new Vue({
    el:"#spider",
    data:{},
    mounted:function () {

    },
    methods:{
        spideProxyIP: function () {
            var url = 'spideProxyIP';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    alert(jsonResult.message);
                }
            })
        },
        spideUrl: function () {
            var url = 'spideUrl';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    alert(jsonResult.message);
                }
            })
        },
        spideApp: function () {
            var url = 'spideApp';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    alert(jsonResult.message);
                }
            })
        },
        checkUrl: function () {
            var url = 'checkUrl';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    alert(jsonResult.message);
                }
            })
        },
        checkApp: function () {
            var url = 'checkApp';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    alert(jsonResult.message);
                }
            })
        },
        spideSpecialPrice: function () {
            var url = 'spideSpecialPrice';
            axios.post(url).then(function (response) {
                var jsonResult = response.data;
                if (jsonResult.success) {

                } else {
                    alert(jsonResult.message);
                }
            })
        }
    }
});