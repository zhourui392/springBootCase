//local-upload页面JS
$(document).ready(function() {
	//加载诊室
	var clinialRoomIdInput = "<input type='hidden' value='' id='clinialRoomId'/>";
	var recvHospitalIdInput = "<input type='hidden' value='' id='recvHospitalIdInput'/>";
	var sentHospitalIdInput = "<input type='hidden' value='' id='sentHospitalIdInput'/>";
	$("div.search_wrap").append(clinialRoomIdInput);
	$("div.search_wrap").append(recvHospitalIdInput);
	$("div.search_wrap").append(sentHospitalIdInput);
	var detialPatientId = "<input type='hidden' value='' id='detialPatientId'/>";
	$("div").append(detialPatientId);

	$(".upndownload_wrap").hide();
	
	$(".images_list").css("width","100%");
	
	$(".uploadBtn").click(function(){
		if ($(".uploadBtn").hasClass("selected")){
			$(".upndownload_wrap").hide();
			$(".uploadBtn").removeClass("selected");
		}else{
			$(".upndownload_wrap").show();
			$(".uploadBtn").addClass("selected");
		}
	});
	//1、获取诊室
	$.ajax({  
            url:'clinialroomlist',  
            type:"GET",  
            async:true,  
            dataType:"JSON",  
            error:function(){alert("服务加载出错");},  
            success:function(data){
            	var clinialroomlist = data.data;
            	var clinialhtml = "";
            	for (var index in clinialroomlist){
            		var clinialroom = clinialroomlist[index];
            		if (index == 0){
            			$("#clinialRoomId").val(clinialroom.id);
            			clinialhtml += "<li class='clinialli selected' val="+clinialroom.id+"><span>"+clinialroom.name+"</span></li>"
            		}else{
            			clinialhtml += "<li class='clinialli' val="+clinialroom.id+"><span>"+clinialroom.name+"</span></li>"
            		}
            	}
            	$(".menuItem1 .submenuList").html(clinialhtml);
            	querylocalPatientInfo();
            	//绑定点击事件
            	$(".clinialli").each(function(){
            		$(this).unbind();
        			$(this).click(function(){
        				$("#clinialRoomId").val($(this).attr("val"));
        				querylocalPatientInfo();
        				$(".clinialli").each(function(){
        					$(this).removeClass("selected");
        				});
        				$(this).addClass("selected");
        			});
        		});
            }  
     });
	
	//2、获取接收医院
	$.ajax({  
        url:'hospital/list/recv',  
        type:"GET",  
        async:true,  
        dataType:"JSON",  
        error:function(){alert("服务加载出错");},  
        success:function(data){
        	var hospitalList = data.data;
        	var hospitalHtml = "";
        	for (var index in hospitalList){
        		var hospital = hospitalList[index];
        		if (index == 0){
        			$("#recvHospitalIdInput").val(hospital.id);
        			hospitalHtml += "<li class='hospitalliRecv selected' val="+hospital.id+"><span>"+hospital.name+"</span><span class='fr'>"+hospital.finishCount+"/"+hospital.totalCount+"</span></li>"
        		}else{
        			hospitalHtml += "<li class='hospitalliRecv selected' val="+hospital.id+"><span>"+hospital.name+"</span><span class='fr'>"+hospital.finishCount+"/"+hospital.totalCount+"</span></li>"
        		}
        	}
        	$(".menuItem2 .submenuList").html(hospitalHtml);
        	//绑定点击事件
        	$(".hospitalliRecv").each(function(){
    			$(this).click(function(){
    				$("#recvHospitalIdInput").val($(this).attr("val"));
    				querydownPatientInfo();
    				$(".hospitalliRecv").each(function(){
    					$(this).removeClass("selected");
    				});
    				$(this).addClass("selected");
    			});
    		});
        }  
	});
	//3、获取发送数据
	$.ajax({  
        url:'hospital/list/send',  
        type:"GET",  
        async:true,  
        dataType:"JSON",  
        error:function(){alert("服务加载出错");},  
        success:function(data){
        	var hospitalList = data.data;
        	var hospitalHtml = "";
        	for (var index in hospitalList){
        		var hospital = hospitalList[index];
        		if (index == 0){
        			$("#sentHospitalIdInput").val(hospital.id);
        			hospitalHtml += "<li class='hospitalli selected' val="+hospital.id+"><span>"+hospital.name+"</span></li>"
        		}else{
        			hospitalHtml += "<li class='hospitalli' val="+hospital.id+"><span>"+hospital.name+"</span></li>"
        		}
        	}
        	$(".menuItem3 .submenuList").html(hospitalHtml);
        	//绑定点击事件
        	$(".hospitalli").each(function(){
    			$(this).click(function(){
    				$("#recvHospitalIdInput").val($(this).attr("val"));
    				queryuploadPatientInfo();
    				$(".hospitalli").each(function(){
    					$(this).removeClass("selected");
    				});
    				$(this).addClass("selected");
    			});
    		});
        }  
	});
	
	$("#selectAll").click(function(){
		if ( $(this).prop("checked") == true){
			$("div.img_con").each(function(){
				$(this).addClass("selected");
				$(this).attr("style","border: solid 2px #0098d6;");
			});
		}else{
			$("div.img_con").each(function(){
				$(this).removeClass("selected");
				$(this).attr("style","");
	    	});
		}
	});
	
	$(".menuItem1").click(function(){
		querylocalPatientInfo();
		$(".operBtn_wrap .btn-success").show();
		//搜素按钮
		$(".searchBtn").unbind();
		$(".searchBtn").click(function(){
			querylocalPatientInfo();
		});
		
		//删除
		$(".btn-danger").click(function(){
			var imgIds = getSelectedImages();
			deletelocalPatientImages(imgIds);
		});
	});
	
	//接收
	$(".menuItem2").click(function(){
		querydownPatientInfo();
		$(".operBtn_wrap .btn-success").hide();
		//搜素按钮
		$(".searchBtn").click(function(){
			$(".searchBtn").unbind();
			querydownPatientInfo();
		});
		
		//删除
		$(".btn-danger").click(function(){
			var imgIds = getSelectedImages();
			deleteDownloadPatientImages();
		});
	});
	
	//发送
	$(".menuItem3").click(function(){
		queryuploadPatientInfo();
		$(".operBtn_wrap .btn-success").hide();
		$(".searchBtn").click(function(){
			$(".searchBtn").unbind();
			queryuploadPatientInfo();
		});
		
		//删除
		$(".btn-danger").click(function(){
			var imgIds = getSelectedImages();
			deleteUploadPatientImages();
		});
	});
	
	//发送按钮
	$("button.btn-success").click(function(){
		var imgIds = getSelectedImages();
		//发送请求
		$.ajax({  
	        url:'localimage/sentrecord',  
	        type:"POST",  
	        async:true,  
	        dataType:"JSON",  
	        data:{'patientId':$("#detialPatientId").val(),'imageIds':imgIds},
	        error:function(){alert("服务加载出错");},  
	        success:function(data){
	        	alert(data.msg);
	        	queryPatientDetialAndImages($("#detialPatientId").val());
	        }  
		});
	});
	
	$("button.btn-edit-info").click(function(){
		var patientId = $("#detialPatientId").val();
		//发送请求
		$.ajax({  
	        url:'localpatient/advice',  
	        type:"PUT",  
	        async:true,  
	        dataType:"JSON",  
	        data:{'patientId':$("#detialPatientId").val(),'patientdecribe':$(".patientdecribe").val(),'patientadvice':$(".patientadvice").val()},
	        error:function(){alert("服务加载出错");},  
	        success:function(data){
	        	alert(data.msg);
	        	if (data.status == 1){
	        		$("div.modal").hide();
	        	}
	        	queryPatientDetialAndImages($("#detialPatientId").val());
	        }  
		});
	});
	
	$('#reservation').daterangepicker({
        timePicker: true,
        timePickerIncrement: 30,
        format: 'YYYY/MM/DD'
      }, function(start, end, label) {
        console.log(start.toISOString(), end.toISOString(), label);
      });
	
	initPage();
});

//查询本地病人
function querylocalPatientInfo(){
	var patientUID = $.trim($("#patientUID").val());
	var patientName = $.trim($("#patientName").val());
	var patientExamTIme = $.trim($("#patientExamTIme").val());
	//1、根据诊室获取病人数据
	$.ajax({  
            url:'localpatient/list',
            type:"POST",  
            async:true, 
            dataType:"JSON",  
            data:{'clinialRoomId':$("#clinialRoomId").val(),'patientName':patientName,'patientUID':patientUID,'exDateStart':patientExamTIme,'examDateEnd':patientExamTIme},
            error:function(){alert("服务加载出错");},  
            success:function(data){
            	$("div.table_hd .table").html("<tr><th width='60'>ID</th><th width='120'>时间</th><th width='70'>姓名</th><th width='40'>性别</th><th width='40'>年龄</th></tr>");
            	var patientlist = data.data;
            	var patienthtml = "";
            	var firstPaientId = "";
            	for (var index in patientlist){
            		var patient = patientlist[index];
            		if (index == 0){
            			firstPaientId = patient.id;
            		}
            		var examDate = new Date(patient.examDate).format("yyyy-MM-dd");
            		var sex = processSex(patient.sex) ;
            		patienthtml += "<tr id="+patient.id+"><td width='60'>"+patient.patientUID+"</td><td width='120'>"+examDate+"</td><td width='70'>"+patient.name+"</td><td width='40'>"+sex+"</td><td width='40'>"+patient.age+"</td></tr>";
            	}
            	$("div.table_bd .table").html(patienthtml);
            	$("div.table_bd .table").addClass("localpatientDiv");
            	if (firstPaientId != ""){
            		queryPatientDetialAndImages(firstPaientId);
            	}
            	$("table.localpatientDiv tr").each(function(){
            		$(this).click(function(){
            			queryPatientDetialAndImages($(this).prop("id"));
            		});
            	});
            	initPage();
            }  
     });
}

function queryPatientDetialAndImages(patientId){
	//加载病人信息，以及影像。
	$.ajax({  
        url:'localpatient/'+patientId,  
        type:"GET",  
        async:true,  
        dataType:"JSON", 
        error:function(){alert("服务加载出错");},  
        success:function(data){
        	//成功
        	if (data.status = 1){
        		var patient = data.data.patient;
        		$(".patientid").html(patient.patientUID);
        		var hospital = data.data.hospital;
            	$(".paitenthospital").html(hospital.name);
            	$(".patientname").html(patient.name);
            	var examDate = new Date(patient.examDate).format("yyyy-MM-dd hh:mm");
        		
            	$(".patientexamtime").html(examDate);
            	var sex = processSex(patient.sex) ;
            	$(".patientsex").html(sex);
            	$(".patientdecribe").html(patient.descript);
            	$(".patientage").html(patient.age);
            	$(".patientadvice").html(patient.consultation);
            	
            	$("#detialPatientId").val(patient.id);
            	//加载影像
            	var localImageList = data.data.localImageList;
            	var localImageHtml = "";
            	for (var index in localImageList){
            		var localImage = localImageList[index];
            		var thumbPath = localImage.thumbPath;
            		thumbPath = decToHex(thumbPath);
            		localImageHtml += "<li><div class='img_con' val='"+localImage.id+"'><img alt='' src=show/images/"+thumbPath+"></div></li>";
            	}
            	$("ul.images_list").html(localImageHtml);
            	
            	$("div.img_con").each(function(){
            		$(this).click(function(){
            			if($(this).hasClass("selected")){
            				$(this).removeClass("selected");
            				$(this).attr("style","");
            			}else{
            				$(this).addClass("selected");
            				$(this).attr("style","border: solid 2px #0098d6;");
            			}
            			
            		});
            	});
        	}
        }  
	});
}

function deletelocalPatientImages(imageIds){
	//加载病人信息，以及影像。
	alert(imageIds);
	$.ajax({  
        url:'localimage/'+$("#detialPatientId").val()+'/'+imageIds,  
        type:"DELETE",  
        async:true,  
        dataType:"JSON", 
        error:function(){alert("服务加载出错");},  
        success:function(data){
        	alert(data);
        }  
	});
}

//查询上传记录
function queryuploadPatientInfo(){
	var patientUID = $.trim($("#patientUID").val());
	var patientName = $.trim($("#patientName").val());
	var patientExamTIme = $.trim($("#patientExamTIme").val());
	//1、根据诊室获取员工数据
	$.ajax({  
            url:'sentReocrd/list/$("#sentHospitalIdInput").val()',  
            type:"POST",  
            async:true,  
            dataType:"JSON",  
            data:{'patientName':patientName,'patientUID':patientUID,'exDateStart':patientExamTIme,'examDateEnd':patientExamTIme},
            error:function(){alert("服务加载出错");},  
            success:function(data){
            	$("div.table_hd .table").html("<tr><th width='60'>ID</th><th width='120'>时间</th><th width='70'>姓名</th><th width='40'>性别</th><th width='40'>年龄</th></tr>");
            	var sentRecordlist = data.data;
            	var patienthtml = "";
            	var firstSentReocrdId = "";
            	for (var index in sentRecordlist){
            		var sentRecord = sentRecordlist[index];
            		if (index == 0){
            			firstSentReocrdId = sentRecord.id;
            		}
            		var patient = sentRecord.localPatientValue;
            		var examDate = new Date(patient.examDate).format("yyyy-MM-dd");
            		var sex = processSex(patient.sex) ;
            		patienthtml += "<tr id="+sentRecord.id+"><td width='60'>"+patient.patientUID+"</td><td width='120'>"+examDate+"</td><td width='70'>"+patient.name+"</td><td width='40'>"+sex+"</td><td width='40'>"+patient.age+"</td></tr>";
            	}
            	
            	if(firstSentReocrdId != ""){
            		queryUploadPatientDetialAndImages(firstSentReocrdId);
            	}
            	$("div.table_bd .table").html(patienthtml);
            	//绑定点击事件
            	$("div.table_bd .table tr").each(function(){
        			$(this).click(function(){
        				var id = $(this).prop("id");
        				//加载病人信息，以及影像。
        				if (id != ""){
                    		queryUploadPatientDetialAndImages(id);
                    	}
        			});
        		});
            	initPage();
            }  
     });
}

function queryUploadPatientDetialAndImages(sentRecordId){
	//通过sentRecord刷新病人信息详细页面
	$.ajax({  
        url:'sentReocrd/'+sentRecordId,  
        type:"GET",  
        async:true,  
        dataType:"JSON", 
        error:function(){alert("服务加载出错");},  
        success:function(data){
        	//成功
        	if (data.status = 1){
        		var patient = data.data.patient;
        		$(".patientid").html(patient.patientUID);
        		var hospital = data.data.hospital;
            	$(".paitenthospital").html(hospital.name);
            	$(".patientname").html(patient.name);
            	var examDate = new Date(patient.examDate).format("yyyy-MM-dd hh:mm");
        		
            	$(".patientexamtime").html(examDate);
            	var sex = processSex(patient.sex) ;
            	$(".patientsex").html(sex);
            	$(".patientdecribe").html(patient.descript);
            	$(".patientage").html(patient.age);
            	$(".patientadvice").html(patient.consultation);
            	
            	$("#detialPatientId").val(patient.id);
            	//加载影像
            	var localImageList = data.data.localImageList;
            	var localImageHtml = "";
            	for (var index in localImageList){
            		var localImage = localImageList[index];
            		var thumbPath = localImage.thumbPath;
            		thumbPath = decToHex(thumbPath);
            		localImageHtml += "<li><div class='img_con' val='"+localImage.id+"'><img alt='' src=show/images/"+thumbPath+"></div></li>";
            	}
            	$("ul.images_list").html(localImageHtml);
            	
            	$("div.img_con").each(function(){
            		$(this).click(function(){
            			if($(this).hasClass("selected")){
            				$(this).removeClass("selected");
            				$(this).attr("style","");
            			}else{
            				$(this).addClass("selected");
            				$(this).attr("style","border: solid 2px #0098d6;");
            			}
            		});
            	});
        	}
        }  
	});
}

function deleteUploadPatientImages(imageIds){
	$.ajax({  
        url:'recvImage/'+$("#detialPatientId").val()+'/'+imageIds,  
        type:"DELETE",  
        async:true,  
        dataType:"JSON", 
        error:function(){alert("服务加载出错");},  
        success:function(data){
        	alert(data);
        }  
	});
}

//查询下载记录
function querydownPatientInfo(){
	var patientUID = $.trim($("#patientUID").val());
	var patientName = $.trim($("#patientName").val());
	var patientExamTIme = $.trim($("#patientExamTIme").val());
	//1、根据诊室获取员工数据
	$.ajax({  
            url:'recvpatient/list',  
            type:"POST",  
            async:true,  
            dataType:"JSON",  
            data:{'hospitalId':$("#recvHospitalIdInput").val(),'patientName':patientName,'patientUID':patientUID,'exDateStart':patientExamTIme,'examDateEnd':patientExamTIme},
            error:function(){alert("服务加载出错");},  
            success:function(data){
				$("div.table_hd .table").html("<tr><th width='20'></th><th width='60'>ID</th><th width='120'>时间</th><th width='70'>姓名</th><th width='40'>性别</th><th width='40'>年龄</th></tr>");
            	var patientlist = data.data;
            	var patienthtml = "";
            	var firstPatientId = "";
            	for (var index in patientlist){
            		var patient = patientlist[index];
            		if (index == 0){
            			firstPatientId = patient.id;
            		}
            		var examDate = new Date(patient.examDate).format("yyyy-MM-dd");
            		var sex = processSex(patient.sex) ;
            		patienthtml += "<tr id="+patient.id+">"
            		if (patient.status == 2){
            			patienthtml += "<td width='20'><span class='msg_icon read'></span></td>";
            		}else{
            			patienthtml += "<td width='20'><span class='msg_icon unread'></span></td>"
            		}
            		patienthtml += "<td width='60'>"+patient.patientUID+"</td><td width='120'>"+examDate+"</td><td width='70'>"+patient.name+"</td><td width='40'>"+sex+"</td><td width='40'>"+patient.age+"</td></tr>";
            	}
            	$("div.table_bd .table").html(patienthtml);
            	queryDownloadPatientDetialAndImages(firstPatientId);
            	//绑定点击事件
            	$("div.table_bd .table tr").each(function(){
        			$(this).click(function(){
        				var id = $(this).prop("id");
        				queryDownloadPatientDetialAndImages(id);
        			});
        		});
            	initPage();
            }  
     });
}

function queryDownloadPatientDetialAndImages(patientId){
	//加载病人信息，以及影像。
	$.ajax({  
        url:'recvpatient/'+patientId,  
        type:"GET",  
        async:true,  
        dataType:"JSON", 
        error:function(){alert("服务加载出错");},  
        success:function(data){
        	//成功
        	if (data.status == 1){
        		var patient = data.data.patient;
        		$(".patientid").html(patient.patientUID);
            	$(".paitenthospital").html(patient.hospitalName);
            	$(".patientname").html(patient.name);
            	var examDate = new Date(patient.examDate).format("yyyy-MM-dd hh:mm");
        		
            	$(".patientexamtime").html(examDate);
            	var sex = processSex(patient.sex) ;
            	$(".patientsex").html(sex);
            	$(".patientdecribe").html(patient.descript);
            	$(".patientage").html(patient.age);
            	$(".patientadvice").html(patient.consultation);
            	
            	//info_line 
            	//patientid、paitenthospital、patientname、patientexamtime
            	//patientsex、patientdecribe、patientage、patientadvice
            	var patientlist = data.data;
            	
            	$("#detialPatientId").val(patient.id);
            	//加载影像
            	var recvImageList = data.data.recvImageList;
            	var localImageHtml = "";
            	for (var index in recvImageList){
            		var recvImage = recvImageList[index];
            		var thumbPath = recvImage.thumbPath;
            		thumbPath = decToHex(thumbPath);
            		localImageHtml += "<li><div class='img_con' val='"+recvImage.id+"'><img alt='' src=show/images/"+thumbPath+"></div></li>";
            	}
            	$("ul.images_list").html(localImageHtml);
            	
            	$("div.img_con").each(function(){
            		$(this).click(function(){
            			if($(this).hasClass("selected")){
            				$(this).removeClass("selected");
            				$(this).attr("style","");
            			}else{
            				$(this).addClass("selected");
            				$(this).attr("style","border: solid 2px #0098d6;");
            			}
            			
            		});
            	});
        	}else{
        		alert(data.msg);
        	}
        }  
	});
}

function deleteDownloadPatientImages(imageIds){
	$.ajax({  
        url:'recvImage/'+$("#detialPatientId").val()+'/'+imageIds,  
        type:"DELETE",  
        async:true,  
        dataType:"JSON", 
        error:function(){alert("服务加载出错");},  
        success:function(data){
        	alert(data);
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


function getSelectedImages(){
	var imgIds = "";
	$("div.img_con").each(function(){
		if ($(this).hasClass("selected")){
			imgIds = imgIds+","+$(this).attr("val");
		}
	});
	imgIds = imgIds.substring(1, imgIds.length);
	return imgIds;
}

function initPage(){
	var color="#f3f3f3"
	$(".table tr:odd td").css("background-color",color);  //改变偶数行背景色
	/* 把背景色保存到属性中 */
	$(".table tr:odd").attr("bg",color);
	$(".table tr:even").attr("bg","#fff");
	$(".menuList li h3").click(function(){
		$(".menuList").find(".submenuList").css("display","none");
		$(this).parent("li").find(".submenuList").css("display","block");
		//.toggleClass("active");
	});

	/* 当鼠标移到表格上时，当前一行背景变色 */
	$(".table tr td").mouseover(function(){
		$(this).parent().find("td").css("background-color","#d5f0f9");
	});
	/* 当鼠标在表格上移动时，离开的那一行背景恢复 */
	$(".table tr td").mouseout(function(){
		var bgc = $(this).parent().attr("bg");
		$(this).parent().find("td").css("background-color",bgc);
	});
	
    $(".main").height($(window).height() - 72 +'px');
    $(".submenuList").height($(window).height() - 237 +'px');
    $(".table_bd").height($(window).height() - 229 +'px');
    $(".patient_images_wrap").height($(window).height() - 172 +'px');
    $(".progress_list").height($(window).height() - 123 +'px');
}

function processSex(sexInt){
	var sex;
	if (sexInt == 0){
		sex = "男";
	}else if (sexInt == 0){
		sex = "女";
	}else{
		sex = "未知";
	}
	return sex;
}

function queryPatientDataNull(){
	$(".patientid").html("");
	$(".paitenthospital").html("");
	$(".patientname").html("");
	$(".patientexamtime").html("");
	$(".patientsex").html("");
	$(".patientdecribe").html("");
	$(".patientage").html("");
	$(".patientadvice").html("");
}

//js Unicode编码转换,/替换为&
var decToHex = function(str) {
    var res=[];
    for(var i=0;i < str.length;i++)
        res[i]=("00"+str.charCodeAt(i).toString(16)).slice(-4);
    return "&u"+res.join("&u");
}
var hexToDec = function(str) {
    str=str.replace(/\\/g,"%");
    return unescape(str);
}