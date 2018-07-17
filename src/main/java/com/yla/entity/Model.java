package com.yla.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 货品规格实体
 * @author
 *
 */
@Entity
@Table(name="t_model")
public class Model {
	
	@Id
	@GeneratedValue
	private Integer id; // 编号
	
	@Column(length=255)
	private String name; // 商品规格名称

	
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

	@Override
	public String toString() {
		return "[id=" + id + ", name=" + name + "]";
	}
	
	
}
