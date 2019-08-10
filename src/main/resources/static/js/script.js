// 计算高度
$(document).ready(function() {
    $("#header").load("common/header.html");
    $("#left_menu").load("common/menu.html");

});

// 计算高度
$(document).ready(function() {
    $(".main").height($(window).height() - 72 +'px');
    $(".submenuList").height($(window).height() - 237 +'px');
    $(".table_bd").height($(window).height() - 229 +'px');
    $(".patient_images_wrap").height($(window).height() - 172 +'px');
    $(".progress_list").height($(window).height() - 123 +'px');
    
});

$(document).ready(function(){
	$(".menuList li h3").click(function(){
		$(".menuList").find(".submenuList").css("display","none");
		$(this).parent("li").find(".submenuList").css("display","block");
		//.toggleClass("active");
	})
})

/* 当鼠标移到表格上时，当前一行背景变色 */
$(document).ready(function(){
	$(".table tr td").mouseover(function(){
		$(this).parent().find("td").css("background-color","#d5f0f9");
	});
})
/* 当鼠标在表格上移动时，离开的那一行背景恢复 */
$(document).ready(function(){ 
	$(".table tr td").mouseout(function(){
		var bgc = $(this).parent().attr("bg");
		$(this).parent().find("td").css("background-color",bgc);
	});
})
// 表格隔行变色
$(document).ready(function(){ 
	var color="#f3f3f3"
	$(".table tr:odd td").css("background-color",color);  //改变偶数行背景色
	/* 把背景色保存到属性中 */
	$(".table tr:odd").attr("bg",color);
	$(".table tr:even").attr("bg","#fff");
})

function writeToken(token) {
    localStorage.setItem("token", token);
}
function getToken() {
	return localStorage.getItem("token");
}

var CommonAjax = function () {

    this.get = function(url, data, successCall, errorCall){
        commonCall(url, data,"GET", successCall, errorCall);
    }

	this.post = function(url, data, successCall, errorCall){
        commonCall(url, data,"POST", successCall, errorCall);
	}

	function commonCall(url, data,type, successCall, errorCall) {
    	//获取localstorage
		var token = getToken();

		//请求需要带上token
        $.ajax({
            url: url,
            headers: {
				token: token
            },
            dataType: "json",
            data: data,
            type: type,
            success: function (data) {
                successCall(data);
            },
            error: function (xhr,textStatus,errorThrown) {
                if (xhr.status == 401 || xhr.status == 405) {
                    //无权限，跳转到登录页面
                    location.href="/index.html";
                } else{
                    if (errorCall != null){
                        errorCall(xhr);
                    }
                }
            }
        });
    }
}

var commonAjax = new CommonAjax();



Date.prototype.format = function(format) {
    var o = {
        "M+" : this.getMonth() + 1, // month
        "d+" : this.getDate(), // day
        "h+" : this.getHours(), // hour
        "m+" : this.getMinutes(), // minute
        "s+" : this.getSeconds(), // second
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
        "S" : this.getMilliseconds()
        // millisecond
    }

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    }

    for ( var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}

