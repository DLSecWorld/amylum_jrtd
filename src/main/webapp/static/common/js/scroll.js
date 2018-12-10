//创建MeScroll对象,内部已默认开启下拉刷新,自动执行up.callback,重置列表数据;
			var temp_type = 1;
			var temp_status = "";
			var from = "";
			var mescroll = new MeScroll("mescroll", {
				up: {
					clearEmptyId: "temp", //1.下拉刷新时会自动先清空此列表,再加入数据; 2.无任何数据时会在此列表自动提示空
					callback: getListData, //上拉回调,此处可简写; 相当于 callback: function (page) { getListData(page); }
				}
			});
			
			/*联网加载列表数据  page = {num:1, size:10}; num:当前页 从1开始, size:每页数据条数 */
			function getListData(page){
				getListDataFromNet(page.num, page.size, function(data){
					mescroll.endSuccess(data.length);
					setListData(data);
				}, function(){
	                mescroll.endErr();
				});
			}		
			
			/*设置列表数据*/
			function setListData(data){
				from = temp_type == 1? "/home": temp_type == 2? "/td":"/mine";
				if (temp_status != "") {
					from += "&status" + temp_status;
				}
				
				var now = new Date().getTime();
				var listDom=document.getElementById("temp");
				var now = new Date().getTime();
				for (var i = 0; i < data.length; i++) {
					var str = "";
					var temp = "";
					var vocation = data[i].vocation == 0 ? "上班族" : data[i].vocation == 1 ? "个体户" : data[i].vocation == 2 ? "企业主" : "无业游民";
					var month_income = data[i].monthIncome == 1 ? "3000-5000" : data[i].monthIncome == 2 ? "5001-8000" : data[i].monthIncome == 3 ? "8000-一万" : data[i].monthIncome == 4 ? "一万以上" : "3000以下";
					var building = data[i].building == 0 ? "无房" : "有房";
					var car = data[i].car == 0 ? "无车" : "有车";
					if (temp_type != 3) {
						if (data[i].status == 0) {
							temp = '<div ' + class_data + ' data-v-ecaca2b0="" class="weui-cell__ft cando">可抢单</div>';
						} else {
							temp = '<div ' + class_data + ' data-v-ecaca2b0="" class="weui-cell__ft cannotdo">已抢单</div>';
						}
					}
					var condition = "";
					if (data[i].lifeInsurance == 1) {
						condition += '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info" style="padding: 1px 3px; border: 1px solid dodgerblue;color: dodgerblue; border-radius: 5px; margin-top:5px; display:inline-block;">有保单</span>';
					}
					if (data[i].socialInsurance == 1) {
						condition += '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info" style="padding: 1px 3px; border: 1px solid dodgerblue; color: dodgerblue; border-radius: 5px; margin-top:5px; display:inline-block;">有社保</span>';
					}
					if (data[i].wagesType == 0) {
						condition += '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info" style="padding: 1px 3px; border: 1px solid dodgerblue; color: dodgerblue; border-radius: 5px; margin-top:5px; display:inline-block;">有银行流水</span>';
					}
					if (data[i].accumulationFund == 1) {
						condition += '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info" style="padding: 1px 3px; border: 1px solid dodgerblue; color: dodgerblue; border-radius: 5px; margin-top:5px; display:inline-block;">有公积金</span>';
					}
					if (data[i].weiLiDai == 1) {
						condition += '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info" style="padding: 1px 3px; border: 1px solid dodgerblue; color: dodgerblue; border-radius: 5px; margin-top:5px; display:inline-block;">微粒贷</span>';
					}
					var img = data[i].sex == 0? "/static/img/meil.png":"/static/img/femail.png";
					str += '<a ' + class_data + ' data-v-ecaca2b0="" class="menu" href="/order/detail/' + data[i].id + '?from=' + from + '">'
							+ '<span ' + class_data + ' data-v-ecaca2b0="" href="javascript:;" class="weui-cell weui-cell_access">'
							+ '<div ' + class_data + ' data-v-ecaca2b0="" class="weui-cell__bd">'
							+ '<div ' + class_data + ' data-v-ecaca2b0="" class="mb5">'
							+ '<div ' + class_data + ' data-v-ecaca2b0="" class="seller-name">' + data[i].name + '</div><span data-v-2559bb2a="" data-v-ecaca2b0="" style="font-size:16px;color: orange; margin-left:10px;">' + data[i].sum  + '万元</span></div>'
							+ '<p ' + class_data + ' data-v-ecaca2b0="" style="display:flex;"><span style="flex:1;">' + condition + '</span>'
							+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-time" style="width:20%; display:flex;justify-content: center;">' + plus_time(now, data[i].applyTime) + '</span></p></div></span>'
							+ '<div ' + class_data + ' data-v-ecaca2b0="" class="weui-cell seller-bottom">'
							+ '<div ' + class_data + ' data-v-ecaca2b0="" class="weui-cell__bd">'
							+ '<p ' + class_data + ' data-v-ecaca2b0="" class="mb5">'
							+ '<i ' + class_data + ' data-v-ecaca2b0="" class="fa fa-map-marker fa-fw"></i>'
							+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info2">' + data[i].city + '</span>|'
							+ '<i ' + class_data + ' data-v-ecaca2b0="" class="fa fa-user fa-fw"></i>'
							+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info3">' + vocation + '</span></p>'
							+ '<p ' + class_data + ' data-v-ecaca2b0="" class="mb5">'
							+ '<i ' + class_data + ' data-v-ecaca2b0="" class="fa fa-cny fa-fw"></i>月收入'
							+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info3" >' + month_income + '</span></p>'
							+ '<p ' + class_data + ' data-v-ecaca2b0="">'
							+ '<i ' + class_data + ' data-v-ecaca2b0="" class="fa fa-database fa-fw"></i>'
							+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info2">' + building + '</span>|'
							+ '<i ' + class_data + ' data-v-ecaca2b0="" class="fa fa-car fa-fw"></i>'
							+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info3">' + car + '</span> </p> </div>'
							+ temp + '</div>' + '</a>';
					$("#temp").append($(str));
				}
			}
			/*联网加载列表数据*/
			function getListDataFromNet(pageNum, pageSize,successCallback, errorCallback) {
	        	var params = loadParams();
	        	type = params.type;
	        	params += "&page=" + pageNum;
	        	params += "&size=" + pageSize;
	        	$.post("/order/search", params, function(data) {
	        		successCallback(data.orders);                		
	        	});
			}
			
			function plus_time(nowTime, target) {
				var plus = (nowTime - target)/1000;
				var day = parseInt(plus / (24 * 60 * 60 ));
				var hour = parseInt(plus / (60 * 60));
				var minute = parseInt(plus / 60);
				if (day > 0) {
					return day + "天前";
				} else if (hour > 0) {
					return hour + "小时前";
				} else {
					return minute + "分钟前";
				}
			}			
			
			//禁止PC浏览器拖拽图片,避免与下拉刷新冲突;如果仅在移动端使用,可删除此代码
			document.ondragstart=function() {return false;}