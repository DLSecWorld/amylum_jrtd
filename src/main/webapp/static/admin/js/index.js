var type = "user";
var size = 20;
var num = 0;

Date.prototype.format =function(format)
{
var o = {
"M+" : this.getMonth()+1, //month
"d+" : this.getDate(), //day
"h+" : this.getHours(), //hour
"m+" : this.getMinutes(), //minute
"s+" : this.getSeconds(), //second
"q+" : Math.floor((this.getMonth()+3)/3), //quarter
"S" : this.getMilliseconds() //millisecond
}
if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
(this.getFullYear()+"").substr(4- RegExp.$1.length));
for(var k in o)if(new RegExp("("+ k +")").test(format))
format = format.replace(RegExp.$1,
RegExp.$1.length==1? o[k] :
("00"+ o[k]).substr((""+ o[k]).length));
return format;
}

$(function() {
	loadElement(1);
	$(".nav_li").on("click", function() {
		if (!$(this).attr("class").indexOf("flag") >= 0) {
			$(this).addClass("flag");
			$(this).siblings().removeClass("flag");
			type = $(this).attr("data-type");
			loadElement(1);
		}
	});
	$("#submit").click(function() {
		loadElement(1);
	});
});


function initPaginate(pages, page) {
	$("#pageTradeListDiv").paginate({
		count 		: pages,
		start 		: page,
		display     : 10,
		border					: false,
		text_color  			: '#888',
		background_color    	: '#fff',	
		text_hover_color  		: '#FFF',
		background_hover_color	: '#f40',
		onChange    : function(page_index) {
			loadElement(page_index);
		}
	});
}

function loadElement(page) {
	num = (page - 1) * 20;
	if (type == "user") {
		loadUser(page);
	} else if (type == "product"){
		loadProduct(page);
	} else if (type == "recharge") {
		loadRecharge(page);
	} else if (type == "requit") {
		loadRequit(page);
	}
}

function loadUser(page) {
	var param = $("#form").serialize() + "&size=20&page=" + page ;
	$.post("/admin/user/list", param, function(data) {
		initPaginate(data.pages, page);
		var list = data.list;
		if(page == 1) {
			$("#tbody").empty();
			$("#thead").empty();
			$("#thead").append($('<tr class = "tbody_tr" >'
                    + '<th  align="center">序号</th>'
                    + '<th  align="center">姓名</th>'
                    + '<th  align="center">身份证号</th>'
                    + '<th  align="center">电话</th>'
                    + '<th  align="center">城市</th>'
                    + '<th  align="center">公司全称</th>'
                    + '<th  align="center">身份证正面照</th>'
                    + '<th  align="center">身份证背面照</th>'
                    + '<th  align="center">个人名片或工牌</th>'
                    + '<th  align="center">与公司logo合影</th>'
                    + '<th  align="center">精品券</th>'
                    + '<th  align="center">淘单券</th>'
                    + '<th  align="center">状态</th>'
                    + '<th  align="center">注册时间</th>'
                    + '<th  align="center">操作</th>'
                    + '</tr>'));
		}
		
		var str = "";
		if (list.length > 0) {
			for(var i= 0; i < list.length; ++i) {
				var imgIdentity =  "";
				var imgIdentityBack = "";
				var imgPicCard = "";
				var imgPicLogo = "";
				var statusStr = list[i].status == 0?"未认证":list[i].status == 1?"审核中":list[i].status == 2?"通过":"未通过";
				var optStr = '';
				if (list[i].picIdentity && list[i].picIdentity != "") {
					imgIdentity = '<img  src= "' + list[i].picIdentity + '">';
				}
				
				if (list[i].picIdentityBack && list[i].picIdentityBack != "") {
					imgIdentityBack = '<img  src= "' + list[i].picIdentityBack + '">';
				}
				
				if (list[i].picCard && list[i].picCard != "") {
					imgPicCard = '<img  src= "' + list[i].picCard + '">';
				}
				
				if (list[i].picLogo && list[i].picLogo != "") {
					imgPicLogo = '<img  src= "' + list[i].picLogo + '">';
				}
				
				if (list[i].status == 1) {
					optStr = '<button onclick="checkUserAuth(' + list[i].id + ',' + 2+ ')">通过</button>&nbsp;&nbsp;<button onclick="checkUserAuth(' + list[i].id + ',' + 3 + ')">驳回</button>';
				}
				var times = list[i].times;
				var timesStr = '<input type = "text" id = "times_' + list[i].id + '" size="6" value = "' + times + '" ><button onclick="submitTimes(' + list[i].id + ')">提交</button>';
				var tdTimes = list[i].tdTimes;
				var tdTimesStr = '<input type = "text" id = "tdTimes_' + list[i].id + '" size="6" value = "' + tdTimes + '" ><button onclick="submitTdTimes(' + list[i].id + ')">提交</button>';
				str += '<tr><td align="center">' + (++num) + '</td>'
				+ '<td align="center">' + list[i].name + '</td>'
				+ '<td align="center">' + list[i].identity + '</td>'
				+ '<td align="center">' + list[i].mobile + '</td>'
				+ '<td align="center">' + list[i].city + '</td>'
				+ '<td align="center">' + list[i].compName + '</td>'
				+ '<td align="center">' + imgIdentity + '</td>'
				+ '<td align="center">' + imgIdentityBack + '</td>'
				+ '<td align="center">' + imgPicCard + '</td>'
				+ '<td align="center">' + imgPicLogo + '</td>'
				+ '<td align="center">' + timesStr + '</td>'
				+ '<td align="center">' + tdTimesStr + '</td>'
				+ '<td align="center">' + statusStr + '</td>'
				+ '<td align="center">' + new Date(list[i].addTime).format('yyyy-MM-dd hh:mm:ss') + '</td>'
				+ '<td align="center" id= "user_' + list[i].id + '">' + optStr + '</td>'
				+ '</tr>';
			}
			$("#tbody").empty().append($(str));
			$(".ul_listz li:nth-child(2)").html($(".ul_listz li:nth-child(2)").html().replace("&nbsp;&nbsp;&nbsp;&nbsp;", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共" + data.count + "/" + data.sum + "条&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
		} else {
			$("#tbody").append($('<tr><td colspan="16" rowspan="6" align="center">暂无数据</td></tr>'));
		}
		
	});
}

function submitTimes(id, old_times) {
	var times = $("#times_" + id).val();
	$.post("/admin/updateusertimes", {"id":id, "times":times, "tdTimes":0, "isTd":"false"}, function(data) {
		if (data.code != 200) {
			$("#times_" + id).val(data.times);
			alert(data.msg);
		} else {
			alert("精品券修改成功");
		}
	});
	
}

function submitTdTimes(id, old_td_times) {
	var tdTimes = $("#tdTimes_" + id).val();
	$.post("/admin/updateusertimes", {"id":id, "times":0, "tdTimes":tdTimes, "isTd":"true"}, function(data) {
		if (data.code != 200) {
			$("#tdTimes_" + id).val(data.tdTimes);
			alert(data.msg);
		} else {
			alert("淘单券修改成功");
		}
	});	
}

function loadProduct(page) {
	var param = $("#form").serialize() + "&size=20&page=" + page ;
	$.post("/admin/product/list", param, function(data) {
		var list = data.list;
		initPaginate(data.pages, page);
		if(page == 1) {
			$("#thead").empty();
			$("#tbody").empty();
			$("#thead").append($('<tr class = "tbody_tr" >'
                    + '<th  align="center">序号</th>'
                    + '<th  align="center">姓名</th>'
                    + '<th  align="center">年纪</th>'
                    + '<th  align="center">电话</th>'
                    + '<th  align="center">身份证号/出生日期</th>'
                    + '<th  align="center">性别</th>'
                    + '<th  align="center">城市</th>'
                    + '<th  align="center">工作类型</th>'
                    + '<th  align="center">工作年限</th>'
                    + '<th  align="center">月收入</th>'
                    + '<th  align="center">工资发放形式</th>'
                    + '<th  align="center">车产</th>'
                    + '<th  align="center">房产</th>'
                    + '<th  align="center">信用卡</th>'
                    + '<th  align="center">公积金</th>'
                    + '<th  align="center">社保</th>'
                    + '<th  align="center">寿险</th>'
                    + '<th  align="center">微粒贷</th>'
                    + '<th  align="center">贷款金额</th>'
                    + '<th  align="center">申请日期</th>'
                    + '<th  align="center">数据来源</th>'
                    + '<th  align="center">数据状态</th>'
                    + '<th  align="center">操作</th>'
                    + '</tr>'));
		}
		var str = "";
		if (list.length > 0) {
			for (var i = 0; i < list.length; ++i) {				
				var sex = list[i].sex == 0? "男":"女";
				var vocation = list[i].vocation == 0?"上班族":list[i].vocation == 1?"个体户":"企业主";
				var workTime = list[i].workTime == 0?"半年以下":list[i].workTime == 1?"半年到一年":"一年以上";
				var monthIncome = list[i].monthIncome == 0?"":list[i].monthIncome == 1?"3千以下":list[i].monthIncome == 2?"3千到5千":list[i].monthIncome == 3?"5千到8千":"1万以上";
				var wagesType = list[i].wagesType == 0?"银行转账":"现金发放";
				var car = list[i].car == 0?"无车":"有车";
				var building = list[i].building == 0?"无房":"有房";
				var creditCard = list[i].creditCard == 0?"无":"有";
				var gjj = list[i].accumulationFund == 0?"无":"有";
				var sb = list[i].socialInsurance == 0?"无":"有";
				var sx = list[i].lifeInsurance == 0?"无":"有";
				var wld = list[i].weiLiDai == 0?"无":"有";
				var sum = list[i].sum + "万";
				var applyTime = new Date(list[i].applyTime).format('yyyy-MM-dd hh:mm:ss')
				var status = list[i].status == 0?"未抢":"已抢";
				var opt = (list[i].status == 0 || list[i].status == 1)? '<td align="center"><button onclick="updateProduct(' + list[i].id + ',' + 2 + ')">加精</button>&nbsp;&nbsp;&nbsp;<button onclick="updateProduct(' + list[i].id + ',' + 3 + ')">废弃</button></td>':"<td></td>";
				str += '<tr><td align="center">' + (++num) + '</td>'
					+ '<td align="center">' + list[i].name + '</td>'
					+ '<td align="center">' + list[i].age + '</td>'
					+ '<td align="center">' + list[i].mobile + '</td>'
					+ '<td align="center">' + list[i].identyNumber + '</td>'
					+ '<td align="center">' + sex  + '</td>'
					+ '<td align="center">' + list[i].city + '</td>'
					+ '<td align="center">' + vocation + '</td>'
					+ '<td align="center">' + workTime + '</td>'
					+ '<td align="center">' + monthIncome + '</td>'
					+ '<td align="center">' + wagesType + '</td>'
					+ '<td align="center">' + car + '</td>'
					+ '<td align="center">' + building + '</td>'
					+ '<td align="center">' + creditCard + '</td>'
					+ '<td align="center">' + gjj + '</td>'
					+ '<td align="center">' + sb + '</td>'
					+ '<td align="center">' + sx + '</td>'
					+ '<td align="center">' + wld + '</td>'
					+ '<td align="center">' + sum + '</td>'
					+ '<td align="center">' + applyTime + '</td>'
					+ '<td align="center">' + list[i].source + '</td>'
					+ '<td align="center">' + status + '</td>'
					+ opt
					+ '</tr>';
			}
			$("#tbody").empty().append($(str));				
			$(".ul_listz li:nth-child(2)").html($(".ul_listz li:nth-child(2)").html().replace("&nbsp;&nbsp;&nbsp;&nbsp;", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共" + data.count + "/" + data.sum + "条&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));		
		} else {
			$("#tbody").append($('<tr><td colspan="24" rowspan="6" align="center">暂无数据</td></tr>'));
		}
	});	
}

function loadRecharge(page) {
	var param = $("#form").serialize() + "&size=20&page=" + page ;
	$.post("/admin/recharge/list", param, function(data) {
		var list = data.list;
		initPaginate(data.pages, page);
		if (page == 1) {
			$("#tbody").empty();
			$("#thead").empty();
			$("#thead").append($('<tr class = "tbody_tr" >'
                    + '<th  align="center">序号</th>'
                    + '<th  align="center">姓名</th>'
                    + '<th  align="center">电话</th>'
                    + '<th  align="center">精品单购买数量</th>'
                    + '<th  align="center">淘单购买数量</th>'
                    + '<th  align="center">充值总金额</th>'
                    + '<th  align="center">充值状态</th>'
                    + '<th  align="center">充值时间</th>'
                    + '</tr>'));			
		}
		
		if (list.length > 0) { 
			var str = "";
			for (var i = 0; i < list.length; ++i) {
				var status = list[i].status == 2?"成功":"失败";
				str += '<tr><td align="center">' + (++num) + '</td>'
				+ '<td align="center">' + list[i].name + '</td>'
				+ '<td align="center">' + list[i].mobile + '</td>'
				+ '<td align="center">' + list[i].times + '</td>'
				+ '<td align="center">' + list[i].tdTimes + '</td>'
				+ '<td align="center">' + list[i].sum + '元</td>'
				+ '<td align="center">' + status + '</td>'
				+ '<td align="center">' + new Date(list[i].addTime).format('yyyy-MM-dd hh:mm:ss') + '</td>'
				+ '</tr>';
			}
			$("#tbody").empty().append($(str));
			$(".ul_listz li:nth-child(2)").html($(".ul_listz li:nth-child(2)").html().replace("&nbsp;&nbsp;&nbsp;&nbsp;", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共" + data.count + "/" + data.sum + "条&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
		} else {
			$("#tbody").append($('<tr><td colspan="8" rowspan="6" align="center">暂无数据</td></tr>'));
		}
	});	
}

function loadRequit(page) {
	var param = $("#form").serialize() + "&size=20&page=" + page ;
	$.post("/admin/requit/list", param, function(data) {
		var list = data.list;
		initPaginate(data.pages, page);
		if (page == 1) {
			$("#tbody").empty();
			$("#thead").empty();
			$("#thead").append($('<tr class = "tbody_tr" >'
                    + '<th  align="center">序号</th>'
                    + '<th  align="center">姓名</th>'
                    + '<th  align="center">电话</th>'
                    + '<th  align="center">贷款人姓名</th>'
                    + '<th  align="center">贷款人电话</th>'
                    + '<th  align="center">贷款总额</th>'
                    + '<th  align="center">申退原因</th>'
                    + '<th  align="center">申退状态</th>'
                    + '<th  align="center">驳回原因</th>'
                    + '<th  align="center">申退时间</th>'
                    + '<th  align="center">修改时间</th>'
                    + '<th  align="center">操作</th>'
                    + '</tr>'));			
		}
		
		if (list.length > 0) {
			var str = "";
			for (var i = 0; i < list.length; ++i) {
				var opt = list[i].status == 0?'<button  onclick="updateRequit(' + list[i].id + ',' + 1 + ')">通过</button>&nbsp;&nbsp;&nbsp;<button onclick="updateRequit(' + list[i].id + ',' + 2 + ')">驳回</button>':'';
				var status = list[i].status == 0? '<td align="center">未处理</td>':list[i].status == 1?'<td align="center">退单成功</td>':'<td align="center">已驳回</td>';
				str += '<tr><td align="center">' + (++num) + '</td>'
				+ '<td align="center">' + list[i].name + '</td>'
				+ '<td align="center">' + list[i].mobile + '</td>'
				+ '<td align="center">' + list[i].productName + '</td>'
				+ '<td align="center">' + list[i].productMobile + '</td>'
				+ '<td align="center">' + list[i].sum + '万元</td>'
				+ '<td align="center">' + list[i].requitReason + '</td>'
				+ status
				+ '<td align="center">' + list[i].reason + '</td>'
				+ '<td align="center">' + new Date(list[i].addTime).format('yyyy-MM-dd hh:mm:ss') + '</td>'
				+ '<td align="center">' + new Date(list[i].updateTime).format('yyyy-MM-dd hh:mm:ss') + '</td>'
				+ '<td align="center" id="requit_' + list[i].id + '">' + opt + '</td>'
				+ '</tr>';				
			}
			$("#tbody").empty().append($(str));
			$(".ul_listz li:nth-child(2)").html($(".ul_listz li:nth-child(2)").html().replace("&nbsp;&nbsp;&nbsp;&nbsp;", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共" + data.count + "/" + data.sum + "条&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));		
		} else {
			$("#tbody").append($('<tr><td colspan="12" rowspan="6" align="center">暂无数据</td></tr>'));
		}
	});	
}

function checkUserAuth(id, status) {
	$.post("/admin/checkAuth", {"id":id, "status":status}, function(data) {
		if (data.code == 200) {
			alert(data.msg);
			$("#user_" + id).text("");
		}
	});
}

function updateProduct(id, status) {
	$.post("/admin/updateProduct", {"id":id, "status":status}, function(data) {
		if (data.code == 200) {
			alert(data.msg);
		}
	});		
}

function updateRequit(id, status) {
	$.post("/admin/updateRequit", {"id":id, "status":status}, function(data) {
		if (data.code == 200) {
			alert(data.msg);
			$("#requit_" + id).text("");
		}
	});
}