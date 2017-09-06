package com.jt.manager.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 为了通用的mapper加上 JPA 描述java持久化类和数据库表之间的映射关系
 * JPA映射：4个注解
 * 1、类和表的映射
 * 2、属性和数据库表的字段映射
 * 3、标识表的主键
 * 4、标识主键自增
 * @author bigjsd
 * 
 * 通用mapper采用mybatis提供专门的注解  
 * （？）参数值从pojo获取 通过pojo传递封装好的       
 * （where）如果字段为null就不拼接
 */

//注解一：类和表直接映射 ， 通过反射 
@Table(name="tb_item_cat")
public class ItemCat extends BasePojo{
	/*
	 * 注解三：属性与数据库表中的主键字段的映射
	 * 注解四：主键自增 @GeneratedValue
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	//注解二：属性与数据库表中的字段的映射
	@Column(name="parent_id")
	private Long parentId;
	
	//属性名与字段名相同的 可以不加注解 
	private String name;
	private Integer status;
	
	@Column(name="sort_order")
	private Integer sortOrder;
	@Column(name="is_parent")
	private Boolean isParent;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	/*
	 * 给EasyUI添加一个方法 获取树节点的名称，不用增加属性 因为json解析时利用get方法
	 */
	public String getText(){
		return this.getName();
	}
	/*
	 * 给EasyUI添加一个方法 ，获取节点的状态 实现异步加载
	 * 异步加载树：当点击一个节点时 会将这个节点的id值传递到后台去 where parentId = id
	 * ！获取值（在controller中设置一个id来取到值，判断是否传递）
	 * ！第一次调用，没有onclick，如何调用？  zTree默认第一次调用加载根节点下的数据（parentId=0）
	 */
	public String getState(){
		return this.getIsParent()?"closed":"open";
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	
}
