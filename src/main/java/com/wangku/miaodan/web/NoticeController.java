package com.wangku.miaodan.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	@RequestMapping("/detail")
	public String detail() {
		return "/option/notice-detail";
	}

}
