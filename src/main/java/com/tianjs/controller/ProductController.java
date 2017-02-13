package com.tianjs.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.tianjs.model.Product;
import com.tianjs.service.ProductService;

@Controller
public class ProductController {
	@Autowired
	private ProductService productService;
	
//	@Autowired
//	SenderService senderService;
//	
//	@Autowired
//	ReceiverService receiverService;

	@RequestMapping("/findproduct")
	public String index(Model model, HttpSession session, @ModelAttribute(value = "product") Product product,
			@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
		PageInfo<Product> pageInfo = productService.findAll(product, pageNum, pageSize);
		// 获得当前页
		model.addAttribute("pageNum", pageInfo.getPageNum());
		// 获得一页显示的条数
		model.addAttribute("pageSize", pageInfo.getPageSize());
		// 是否是第一页
		model.addAttribute("isFirstPage", pageInfo.isIsFirstPage());
		// 获得总页数
		model.addAttribute("totalPages", pageInfo.getPages());
		// 是否是最后一页
		model.addAttribute("isLastPage", pageInfo.isIsLastPage());
		model.addAttribute("products", pageInfo.getList());

		return "product";
	}
	
	@RequestMapping("/productmq")
	public String sendMsg(Model model){
//		senderService.sendMsg();
		return "product";
	}
	
}
