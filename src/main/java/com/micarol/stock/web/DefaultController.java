package com.micarol.stock.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {

	@RequestMapping(value="/index")
	public @ResponseBody String index() {
		return "hello stock_orign";
	}
}
