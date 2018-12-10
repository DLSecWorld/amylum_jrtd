(function($) {
	$.fn.paginate = function(options) {
		var opts = $.extend({}, $.fn.paginate.defaults, options);
		return this.each(function() {
			$this = $(this);
			var o = $.meta ? $.extend({}, opts, $this.data()) : opts;
			var selectedpage = o.start;
			$.fn.draw(o,$this,selectedpage);	
		});
	};
	
	$.fn.draw = function(o,obj,selectedpage){
		if(o.display > o.count)
			o.display = o.count;
		$this.empty();
		
		//dump_obj(o);
		//上一页li
		var _first = $(document.createElement('li')).addClass("up").html('<a href="javascript::"><img src="http://static.99114.com/static/js/common/plugins/custom/paginate/images/dj-l.gif" width="8" height="10" /></a>');
	//	if(o.rotate){
	//		if(o.images) var _rotleft	= $(document.createElement('span'));
	//		else var _rotleft	= $(document.createElement('span'));		
	//	}
		//上一页ul
		var _divwrapleft	= $(document.createElement('ul')).css("float","left");
		_divwrapleft.append(_first);
		
		//页码
		var _ulwrapdiv	= $(document.createElement('div')).attr({id:"pagelist"}).css("float","left");;
		var _ul			= $(document.createElement('ul'));
		var selobj;
		for(var i = 0; i < o.count; i++){
			var val = i+1;
			if(o.count<10){
				if(val == selectedpage){
					var _obj = $(document.createElement('li')).html('<span class="jPag-current">'+val+'</span>');
					selobj = _obj;
					_ul.append(_obj);
				}	
				else{
					var _obj = $(document.createElement('li')).html('<a>'+ val +'</a>');
					_ul.append(_obj);
				}	
			}else{
				if(o.start>4 && o.count-o.start>5){
					if(o.start-5<=i && o.start+4>=i){
						if(val == selectedpage){
							var _obj = $(document.createElement('li')).html('<span class="jPag-current">'+val+'</span>');
							selobj = _obj;
							_ul.append(_obj);
						}	
						else{
							var _obj = $(document.createElement('li')).html('<a>'+ val +'</a>');
							_ul.append(_obj);
						}				
					}
				}else if(i<10 || o.count-o.start<=5){
					if(o.count-5<=o.start && o.count-10<=i){
						if(val == selectedpage){
							var _obj = $(document.createElement('li')).html('<span class="jPag-current">'+val+'</span>');
							selobj = _obj;
							_ul.append(_obj);
						}	
						else{
							var _obj = $(document.createElement('li')).html('<a>'+ val +'</a>');
							_ul.append(_obj);
						}
					}else if(i<10 && o.count-5>o.start){
						if(val == selectedpage){
							var _obj = $(document.createElement('li')).html('<span class="jPag-current">'+val+'</span>');
							selobj = _obj;
							_ul.append(_obj);
						}	
						else{
							var _obj = $(document.createElement('li')).html('<a>'+ val +'</a>');
							_ul.append(_obj);
						}
					}
				}
			}
		}		
		_ulwrapdiv.append(_ul);
		
		//if(o.rotate){
		//	if(o.images) var _rotright	= $(document.createElement('span'));
		//	else var _rotright	= $(document.createElement('span'));
		//}
		//下页   跳转
		var _last = $(document.createElement('li')).addClass("up").html('<a href="javascript::"><img src="http://static.99114.com/static/js/common/plugins/custom/paginate/images/dj.gif" height="10" /></a>');
		var _pa = $(document.createElement('li')).html('共'+o.count+'页&nbsp;&nbsp;&nbsp;&nbsp;到第<input value='+o.start+' type="text"  name="textfield" id="textfield" class="input_li" />页');
		var _textfie = $(document.createElement('li')).html('<input  id="textfie" type="button" class="sub123" value="" />');
		
		var _divwrapright	= $(document.createElement('ul')).addClass("ul_listz");
		_divwrapright.append(_last).append(_pa).append(_textfie);
		
		//append all:
		$this.addClass('jPaginate').append(_divwrapleft).append(_ulwrapdiv).append(_divwrapright);
		//$this.addClass('jPaginate').append(_divwrapleft).append(_ulwrapdiv).append(_divwrapright);
			
		
		//$.fn.applystyle(o,$this,a_css,hover_css,_first,_ul,_ulwrapdiv,_divwrapright);
		//calculate width of the ones displayed:
		if(o.rotate){}
		//跳转
		$this.find(".sub123").eq(0).click(function(){
			var i = $(this).parent().parent().find("#textfield").eq(0).val();			
			reg = /^[0-9]*[1-9][0-9]*$/;
			if(!i.match(reg)){
				alert("页码应为大于等于1的整数");
				$(this).parent().parent().find("#textfield").eq(0).attr("value",o.start);
			}else if(i>=1 && i<=o.count ){
				o.onChange(i);		
			}else{
				alert("输入的页码不存在");
				$(this).parent().parent().find("#textfield").eq(0).attr("value",o.start);
			}
		});
		//first and last:
		//上页下页
		_first.click(function(e){
			if(o.start!=1)
				o.onChange(parseInt(o.start)-1);
		});
		_last.click(function(e){
			if(o.start!=o.count)
				o.onChange(parseInt(o.start)+1);
		});
		
		//click a page
		_ulwrapdiv.find('li').click(function(e){
			selobj.html('<a>'+selobj.find('.jPag-current').html()+'</a>'); 
			var currval = $(this).find('a').html();
			$(this).html('<span class="jPag-current">'+currval+'</span>');
			selobj = $(this);
			//$.fn.applystyle(o,$(this).parent().parent().parent(),a_css,hover_css,_first,_ul,_ulwrapdiv,_divwrapright);	
			//var left = (this.offsetLeft) / 2;
			//var left2 = _ulwrapdiv.scrollLeft() + left;
			//var tmp = left - (outsidewidth / 2);
			o.onChange(currval);	
		});
		
	}
	
	//$.fn.applystyle = function(o,obj,a_css,hover_css,_first,_ul,_ulwrapdiv,_divwrapright){}
})(jQuery);
