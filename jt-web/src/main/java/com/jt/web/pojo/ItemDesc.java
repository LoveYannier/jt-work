package com.jt.web.pojo;

public class ItemDesc extends BasePojo{

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
