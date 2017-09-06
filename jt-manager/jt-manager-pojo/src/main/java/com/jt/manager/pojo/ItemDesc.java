package com.jt.manager.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tb_item_desc")
public class ItemDesc extends BasePojo{

	@Id
	private Long itemId; //不可以自增 这个值将来就是商品的id
	private String itemDesc;
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String desc) {
		this.itemDesc = desc;
	}
	
}
