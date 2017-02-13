package com.tianjs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tianjs.mapper.ProductMapper;
import com.tianjs.model.Product;

@Service
@Transactional
public class ProductService {
	/**
     * 缓存的key
     */
//    public static final String PRODUCT_ALL_KEY   = "\"product_all\"";
    /**
     * value属性表示使用哪个缓存策略，缓存策略在ehcache.xml
     */
//    public static final String DEMO_CACHE_NAME = "tianjs_cache";
    
	@Autowired
	private ProductMapper productMapper;

	@Transactional(readOnly = true)
//	@Cacheable(value = DEMO_CACHE_NAME,key = PRODUCT_ALL_KEY)
	public PageInfo<Product> findAll(Product product, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<Product> products = productMapper.findPageBy(product);
		return new PageInfo<>(products);
	}

	@Transactional(rollbackFor = {Exception.class}) // 可多个异常 进行事务管理
//	@CacheEvict(value = DEMO_CACHE_NAME,key = PRODUCT_ALL_KEY)
	public void insert(Product product) {
		productMapper.insert(product);
	}
}
