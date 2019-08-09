$(".trValueList").html("");

$(function(){
	refreshTable();
	$(".input-group-addon").click(function(){
		refreshTable();
	});
	
	$('#searchHospitalName').bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	refreshTable();
        }
    });
	$('#searchCloudPath').bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	refreshTable();
        }
    });
	
	$('#reservation').daterangepicker({
        timePicker: false,
        timePickerIncrement: 30,
        format: 'YYYY/MM/DD'
      }, function(start, end, label) {
        console.log(start.toISOString(), end.toISOString(), label);
    });
	
});


function refreshTable(){
	//查询：医院、云端路径、时间、状态
	var searchHospitalName = $("#searchHospitalName").val();
	var searchCloudPath = $("#searchCloudPath").val();
	var reservation = $.trim($("#reservation").val());
	var pageIndex = $("#pageIndex").val();
	if (pageIndex == null){
		pageIndex = 1;
	}
	var condition = "";
	if(searchHospitalName != null && searchHospitalName != "" ){
		condition = searchHospitalName;
	}
	
	var json = {
			searchHospitalName : condition,
			searchCloudPath : searchCloudPath,
			reservation : reservation,
			pageIndex : pageIndex
        };
	$.ajax({
         url: "upload/list",
         dataType: "json",
         data:json,
         type: "POST",
         success: function (data) {
    	     var uploadHtml = "";
        	 var uploadList = data.data.items;
             if (uploadList != null && uploadList.length >= 1){
            	 for (var index in uploadList){
            		 var uploadrecord = uploadList[index];
            		 var uploadTime = new Date(uploadrecord.createTime).format("yyyy-MM-dd hh:mm");
            		 var cloudfilestatus = uploadrecord.cloudFileType;
            		 var cloudfilestatusStr = "";
            		 if (cloudfilestatus == 0){
            			 cloudfilestatusStr = "未删除";
            		 }else if (cloudfilestatus == 1){
            			 cloudfilestatusStr = "已删除";
            		 }
            		 uploadHtml += "<tr><td>"+uploadrecord.hospitalName+"</td>"+"<td>"+uploadTime+"</td>" +
            		 		"<td>"+uploadrecord.downNum+"</td><td>"+uploadrecord.cloudpath+"</td><td>"+cloudfilestatusStr+"</td>";
            	 }
            	 $(".trValueList").html(uploadHtml);
            	 
            	 $("#pageIndex").val(data.data.pageIndex);
        	     $("#pageToal").val(data.data.totalPage);
        	     $("#pageEachNum").val(data.data.limit);
        	     //分页
             	 var page=$("#page");
             	 var options = {
                	     currentPage :  $("#pageIndex").val(),
                	     totalPages : $("#pageToal").val(),
                	     numberOfPages : 5,
                	     onPageClicked : function(event, originalEvent, type, page){
                 	    	$("#pageIndex").val(page);
             	    		$("#page").find("ul").addClass("pagination ").addClass("pagination-sm");
             	    		refreshTable();
                 	     }
                 }
             	 $("#page").bootstrapPaginator(options);
             	 $("#page").find("ul").addClass("pagination").addClass("pagination-sm");
             }else{
            	 $(".trValueList").html("<td colspan='5' align='center'>无上传信息</td>");
            	 $("#page").html("");
             }
         }
	});
}

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