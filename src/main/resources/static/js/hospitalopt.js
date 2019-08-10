$(".trValueList").html("");

$(function(){
	refreshTable();
	$(".glyphicon-search").click(function(){
		refreshTable();
	});
	
	$('#searchHospitalName').bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	refreshTable();
        }
    });
	
	$(".btn-add-record").click(function(){
       	$("#myModalLabel").text('添加医院信息');
       	$("#hospitalId").val("");
       	$("#hospitalName").val("");
        $('#Goto').modal('show');
        $(".btn-edit-info").unbind("click");
        $(".btn-edit-info").click(function(){
         var name = $("#hospitalName").val();
         if ($.trim(name) ==  ""){
        	 alert("医院名称不能为空");
        	 return false;
         } 
         $(".btn-edit-info").unbind("click");
       	 var json = {
       		 name : name,
       		 sentSwitch : getRadioVal($("input[name=sentRadio]")),
          	 recvSwitch:  getRadioVal($("input[name=recvRadio]"))
            };
       	 $.ajax({
	             url: "/hospital",
	             dataType: "json",
	             data: json,
	             type: "POST",
	             success: function (data) {
	            	 $('#Goto').modal('hide');
	            	 if (data.status == 1){
	            		 alert("添加成功");
	            	 }
	            	 refreshTable();
	             }
           	});
   	 });
	});
});


function initButton(){
	//修改
	$("a.btn-success").each(function(){
		$(this).unbind("click");
		$(this).click(function(){
			var hospitalId = $(this).attr("value");
			$.ajax({
	             url: "/hospital/"+hospitalId,
	             dataType: "json",
	             type: "GET",
	             success: function (data) {
	            	 var hospital = data.data;
	            	 $("#myModalLabel").text('修改医院信息');
	            	 $("#hospitalId").val(hospital.id);
	            	 $("#hospitalName").val(hospital.name);
	            	 var recvFlag = hospital.recvFlag;
	            	 var sentFlag = hospital.sentFlag;
	            	 
	            	 if (recvFlag != null && recvFlag == 1){
	            		 $("input[name=recvRadio]").each(function(){
	            			 $(this).removeAttr("checked");
	            			 if ($(this).attr("value") == 1){
	            				 $(this).prop("checked","checked");
	            		 	 }
	            		 });
	            	 }else{
	            		 $("input[name=recvRadio]").each(function(){
	            			 if ($(this).attr("value") == 0){
	            				 $(this).prop("checked","checked");
	            		 	 }
	            		 });
	            		 
	            	 }
	            	 if (recvFlag != null && sentFlag == 1){
	            		 $("input[name=sentRadio]").each(function(){
	            			 $(this).removeAttr("checked");
	            			 if ($(this).attr("value") == 1){
	            				 $(this).prop("checked","checked");
	            		 	 }
	            		 });
	            	 }else{
	            		 $("input[name=sentRadio]").each(function(){
	            			 $(this).removeAttr("checked");
	            			 if ($(this).attr("value") == 0){
	            				 $(this).prop("checked","checked");
	            		 	 }
	            		 });
	            	 }
	                 $('#Goto').modal('show');
	                 
	                 $(".btn-edit-info").unbind("click");
	                 
	                 $(".btn-edit-info").click(function(){
	                	 var json = {
	                		 name:$("#hospitalName").val(),
	                		 sentSwitch : getRadioVal($("input[name=sentRadio]")),
	                   		 recvSwitch:  getRadioVal($("input[name=recvRadio]"))
	                     };
	                	 $.ajax({
				             url: "hospital/"+hospitalId,
				             dataType: "json",
				             data: json,
				             type: "POST",
				             success: function (data) {
				            	 $('#Goto').modal('hide');
				            	 if (data.status == 1){
				            		 alert("修改成功");
				            	 }
				            	 refreshTable();
				             }
	                	 });
                	 });
	             }
			});
		})
	});
	//删除
	$("a.btn-danger").each(function(){
		$(this).unbind("click");
		$(this).click(function(){
			if (confirm("确认删除？")){
				var hospitalId = $(this).attr("value");
				$.ajax({
		             url: "/hospital/"+hospitalId,
		             dataType: "json",
		             type: "DELETE",
		             success: function (data) {
		                 if (data.status == 1){
		                	 
		                	 var hospitalHtml = "";
		                	 var hospitalList = data.data;
		                	 
		                	 for (var index in hospitalList){
		                		 var hospital = hospitalList[index];
		                		 var createDate = new Date(hospital.createTime).format("yyyy-MM-dd");
		                		 hospitalHtml += "<tr><td>"+hospital.name+"</td>"+"<td>"+hospital.ultrasoundmodel+"</td><td>"+createDate+"</td>";
		                		 hospitalHtml += "<td><div class='oper'>";
		                		 hospitalHtml += "<a href='javascript:void(0)' class='btn btn-info btn-xs' value="+hospital.id+"><span class='glyphicon glyphicon-plus'></span></a>";
		                		 hospitalHtml += "<a href='javascript:void(0)' class='btn btn-success btn-xs' value="+hospital.id+"><span class='glyphicon glyphicon-pencil'></span></a>";
		                		 hospitalHtml += "<a href='javascript:void(0)' class='btn btn-danger btn-xs' value="+hospital.id+"><span class='glyphicon glyphicon-remove'></span></a></a></div></td></tr>";
		                	 }
		                	 $(".trValueList").html(hospitalHtml);
		                	 initButton();
		                 }else{
		                	 $(".trValueList").html("<td>无医院信息</td>");
		                 }
		                 refreshTable();
		             }
				});
			}
		});
	});
	
	//查看医院关系
	$("a.btn-info-sent").each(function(){
		$(this).unbind("click");
		$(this).click(function(){
			var hospitalId = $(this).attr("value");
			$.ajax({
	             url: "/hospital/relation/list/"+hospitalId,
	             dataType: "json",
	             type: "GET",
	             success: function (data) {
	            	 $("div.localSentTo").html("");
	            	
	            	 var allhospitalList = data.data;
	            	 $("#myModalLabel").text('修改医院信息');
	            	 
	            	 var localSentToHtml = "";
	            	 
	            	 var hosptialList = allhospitalList.hosptialList;
	            	 
	            	 for (var index in hosptialList){
	            		 var hospital = hosptialList[index];
	            		 localSentToHtml += "<label class='checkbox-inline'><input type='checkbox' class='localsenttochk'";
	            		 
	            		 if (hospital.type == 1 || hospital.type == 3){
	            			 localSentToHtml += "checked='checked'";
	            		 }
	            		 localSentToHtml += "value='"+hospital.id+"'>"+hospital.name+"</label>";
	            		 if ((index+1) % 4 == 0){
	            			 localSentToHtml += "<br/>";
	            		 }
	            	 }
	            	 
	            	 $("div.localSentTo").html(localSentToHtml);
	                 $('#hospitalSentInfo').modal('show');
	                 
	                 $(".btn-addrelationship-sent-info").unbind();
	                 $(".btn-addrelationship-sent-info").click(function(){
	                	 var category = $("#catetoryList").val();
	                	 
		 				var localsenttoList = new Array();
		 				$("input.localsenttochk").each(function(){
		 					if ($(this).prop("checked") == true){
		 						localsenttoList.push($(this).val());
		 					}
		 				});
		 				
		 				var json = {
		 						localsenttoList : localsenttoList.toString(),
		 						localhospitalId : hospitalId
		 				};
	                	 $.ajax({
				             url: "/hospitalrelationship/recv",
				             dataType: "json",
				             data: json,
				             type: "POST",
				             success: function (data) {
				            	 $('#hospitalSentInfo').modal('hide');
				            	 if (data.status == 1){
				            		 alert("修改成功");
				            	 }
				            	 //$.teninedialog({
                                 //    title:'系统提示',
                                 //    content:data.msg,
                                 //    showCloseButton:false,
                                 //    dialogShown:function(modal){
                                 //       setTimeout(function(){
                                 //           $(this).closeDialog(modal);
                                 //       },2000);
                                 //    }
                                 //});
				            	 refreshTable();
				             }
	                	 });
	                 });
	                 
	             }
			});
		});
	});

	//查看医院关系
$("a.btn-info-recv").each(function(){
	$(this).unbind("click");
	$(this).click(function(){
		var hospitalId = $(this).attr("value");
		$.ajax({
             url: "/hospital/relation/list/"+hospitalId,
             dataType: "json",
             type: "GET",
             success: function (data) {
            	 $(".sentToLocal").html("");
            	 
            	 var allhospitalList = data.data;
            	 $("#myModalLabel").text('修改医院信息');
            	 
            	 var sentToLocalHtml = "";
            	 
            	 var hosptialList = allhospitalList.hosptialList;
            	 
            	 for (var index in hosptialList){
            		 var hospital = hosptialList[index];
            		 sentToLocalHtml += "<label class='checkbox-inline'><input type='checkbox' class='senttolocalchk'";
            		 if (hospital.type == 0 || hospital.type == 3){
            			 sentToLocalHtml += "checked='checked'";
            		 }
            		 sentToLocalHtml += "value='"+hospital.id+"'>"+hospital.name+"</label>";
            		 
            		 if ((index+1) % 4 == 0){
            			 sentToLocalHtml +="<br/>";
            		 }
            	 }
            	 
            	 $(".sentToLocal").html(sentToLocalHtml);
                 $('#hospitalRecvInfo').modal('show');

                 $(".btn-addrelationship-recv-info").unbind();
                 $(".btn-addrelationship-recv-info").click(function(){
                	var category = $("#catetoryList").val();
                	
	 				var localSentToList = new Array();
	 				$("input.senttolocalchk").each(function(){
	 					if ($(this).prop("checked") == true){
	 						localSentToList.push($(this).val());
	 					}
	 				});
	 				var json = {
	 						senttolocalList : localSentToList.toString(),
	 						localhospitalId : hospitalId
	 				};
                	 $.ajax({
			             url: "hospitalrelationship/sent",
			             dataType: "json",
			             data: json,
			             type: "POST",
			             success: function (data) {
			            	 $('#hospitalRecvInfo').modal('hide');
			            	 if (data.status == 1){
			            		 alert("修改成功");
			            	 }
			            	 //$.teninedialog({
                             //    title:'系统提示',
                             //    content:data.msg,
                             //    showCloseButton:false,
                             //    dialogShown:function(modal){
                             //       setTimeout(function(){
                             //           $(this).closeDialog(modal);
                             //       },2000);
                             //    }
                             //});
				            	 refreshTable();
				             }
	                	 });
	                 });
	             }
			});
		});
	});

	//查看医院关系
	$("a.btn-key").each(function(){
		$(this).unbind("click");
		$(this).click(function(){
			$(".keyDiv").html( $(this).attr("value"));
			$('#keymodalInfo').modal('show');
		});
	});

}
	
	
function refreshTable(){
	var searchHospitalName = $("#searchHospitalName").val();
	
	searchHospitalName = $.trim(searchHospitalName);
	
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
			pageIndex : pageIndex
        };
	$.ajax({
         url: "/hospital/list",
         dataType: "json",
         data:json,
         type: "POST",
         success: function (data) {
             if (data.status == 1){
            	 var hospitalHtml = "";
            	 var hospitalList = data.data.items;
            	 for (var index in hospitalList){
            		 var hospital = hospitalList[index];
            		 var createDate = new Date(hospital.createTime).format("yyyy-MM-dd");
            		 var updateDate = new Date(hospital.updateTime).format("yyyy-MM-dd");
            		 hospitalHtml += "<tr><td>"+hospital.name+"</td>"+"<td>"+createDate+"</td><td>"+updateDate+"</td>";
            		 hospitalHtml += "<td><div class='oper'>";
            		 hospitalHtml += "<a href='javascript:void(0)' class='btn btn-info-sent btn-xs' value="+hospital.id+"><span class='glyphicon glyphicon-arrow-up'></span></a>";
            		 hospitalHtml += "<a href='javascript:void(0)' class='btn btn-info-recv btn-xs' value="+hospital.id+"><span class='glyphicon glyphicon-arrow-down'></span></a>";
            		 hospitalHtml += "<a href='javascript:void(0)' class='btn btn-success btn-xs' value="+hospital.id+"><span class='glyphicon glyphicon-pencil'></span></a>";
            		 hospitalHtml += "<a href='javascript:void(0)' class='btn btn-danger btn-xs' value="+hospital.id+"><span class='glyphicon glyphicon-remove'></span></a>" +
            		 		"<a href='javascript:void(0)' class='btn btn-key btn-xs' value="+hospital.id+"><span class='glyphicon glyphicon-key'></span></a></div></td></tr>";
            	 }
            	 $(".trValueList").html(hospitalHtml);
            	 
            	 $("#pageIndex").val(data.data.pageIndex);
        	     $("#pageToal").val(data.data.totalPage);
        	     $("#pageEachNum").val(data.data.limit);
             }else{
            	 $(".trValueList").html("<td colspan='4' align='center'>无医院信息</td>");
             }
             initButton();
             
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
         }
	});
}

function submitForm(obj){
	
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

function getRadioVal(objStr){
	var radioValue = null;
	objStr.each(function(){
		if ($(this).prop("checked") == true){
			radioValue = $(this).val();
		}
	});
	return radioValue;
}