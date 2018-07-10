package com.yla.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 采购员实体
 * @author 
 *
 */
@Entity
@Table(name="t_warehousekeeper")
public class Warehousekeeper {

	@Id
	@GeneratedValue
	private Integer id; // 编号
	
	@Column(length=100)
	private String name; // 采购员名称
	
	@Column(length=50)
	private String number; // 联系电话
	
	@Column(length=300)
	private String address; // 联系地址
	
	@Column(length=800)
	private String remarks; // 备注

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	
	
	@Override
	public String toString() {
		return "[id=" + id + ", name=" + name  + ", number=" + number + ", address="
				+ address + ", remarks=" + remarks + "]";
	}

	
	
	
}
