package com.jt.manager.service;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.service.RedisService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.common.vo.EasyUIResult;
import com.jt.manager.mapper.ItemDescMapper;
import com.jt.manager.mapper.ItemMapper;
import com.jt.manager.pojo.Item;
import com.jt.manager.pojo.ItemDesc;
@Service
public class ItemService extends BaseService<Item>{

	@Autowired
	private ItemMapper itemMapper;
	
	//注入商品描述的mapper
	@Autowired
	private ItemDescMapper itemDescMapper;
	
	//注入RabbitMq 和spring集成之后 spring中提供的
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	//添加自定义注解 注入属性
	@PropertyConfig
	private String REPOSITORY_PATH;
	@PropertyConfig
	private String IMAGE_BASE_URL;
	@Autowired
	private RedisService redisService;
	
	/**
	 * PO persistObject 持久类 数据库和service之间传递数据
	 * VO viewObject 视图类 controller和jsp之间传递数据 对象嵌套
	 * BO businessObject 业务类 当业务逻辑复杂时，controller和service之间传递数据
	 * 业务简单时，VO BO 都使用PO替代
	 * @return
	 */
	public EasyUIResult queryItemList(Integer page, Integer rows){
		/*
		 * 怎么将数据封装到EasyUIResult中?
		 * 在页面组件中，根据getTotal()方法获取记录总数 设置到分页条中
		 * 再根据getRows()获取当前页的值 然后自己遍历形成table
		 * 
		 * 设定分页参数 page当前页 rows每页记录的条数
		 */
		PageHelper.startPage(page, rows); //标识分页开始  准备拦截 下面的第一条执行查询的sql
		List<Item> itemList = itemMapper.queryItemList();
		
		PageInfo<Item> pageInfo = new PageInfo<Item>(itemList);
		//EasyUIResult提供了多个构造方法 以属性的形式写入
		
		//为线程安全 将数据封装到PageInfo对象中
		return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
	}
	
	//因为页面中需要status状态 200 201 400等…… 所以返回值需要是SysResult
	public void saveItem(Item item, String desc){
		//设置默认值
		item.setStatus(1);
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		itemMapper.insertSelective(item);
		
		//新增商品描述
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId()); //mybatis回调
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(itemDesc.getCreated());
		itemDescMapper.insertSelective(itemDesc);
	}
	
	//修改
	public void updateItem(Item item, String desc){
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		
		//修改商品信息时 把redis中的缓存信息删除
		String key = "ITEM_"+item.getId();
		//将缓存中的信息删除
//		redisService.del(key);	优化成rabbitmq 形式
		
		//换成rabbitMq机制
		String routingKey ="item_update";
		//mq是一个公用的 内容尽量小 这样网络传输快 消息传递快 消费也快！！不要放入太大的对象 （放入key）
		rabbitTemplate.convertAndSend(routingKey, key);
		
		//商品描述的修改
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(item.getUpdated());
		
		itemDescMapper.updateByPrimaryKey(itemDesc);
		
	}
	
	//删除
	public void deleteItem(String [] ids){
		itemMapper.deleteByIDS(ids);
		//删除商品描述
		itemDescMapper.deleteByIDS(ids);
	}

	public ItemDesc queryDescById(Long id) {
		return itemDescMapper.selectByPrimaryKey(id);
	}
	
}
