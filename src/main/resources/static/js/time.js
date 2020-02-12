$(function(){
    getTime();
    window.setInterval(getTime, 1000);
});

function getTime(){
    var date = new Date();
    $("#time").html(date.toLocaleString());
}
