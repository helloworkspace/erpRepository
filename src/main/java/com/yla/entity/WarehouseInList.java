package com.yla.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 进仓单
 * @author
 *
 */
@Entity
@Table(name="t_warehouseInList")
public class WarehouseInList {

	@Id
	@GeneratedValue
	private Integer id; // 编号
	
	@Column(length=100)
	private String inNumber; // 入仓单号
	
	@ManyToOne
	@JoinColumn(name="departmentId")
	private Department department; // 领料部门
	
	@ManyToOne
	@JoinColumn(name="warehousekeeperId")
	private Warehousekeeper warehousekeeper; //仓管
	
	@Temporal(TemporalType.TIMESTAMP) 
	private Date inDate; // 入库日期
	
	private float amountPayable; // 应付金额
	
	private float amountPaid; // 实付金额
	
	private Integer state; // 交易状态 1 已付  2 未付
	
    private String auditState; //审核状态
	
	private String paymentMethod;//支付方式
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user; // 操作用户
	
	@Column(length=1000)
	private String remarks; // 备注
	
	@Transient
	private Date bInDate; // 起始时间 搜索用到
	
	@Transient
	private Date eInDate; // 结束时间 搜索用到
	
	@Transient
	private List<WarehouseInListGoods> warehouseInListGoodsList=null; // 退货单商品集合
	
	

	public Warehousekeeper getWarehousekeeper() {
		return warehousekeeper;
	}

	public void setWarehousekeeper(Warehousekeeper warehousekeeper) {
		this.warehousekeeper = warehousekeeper;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getInNumber() {
		return inNumber;
	}

	public void setInNumber(String inNumber) {
		this.inNumber = inNumber;
	}

	

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@JsonSerialize(using=CustomDateSerializer.class)
	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public float getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(float amountPayable) {
		this.amountPayable = amountPayable;
	}

	public float getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(float amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getbInDate() {
		return bInDate;
	}

	public void setbInDate(Date bInDate) {
		this.bInDate = bInDate;
	}

	public Date geteInDate() {
		return eInDate;
	}

	public void seteInDate(Date eInDate) {
		this.eInDate = eInDate;
	}
	
	

	public List<WarehouseInListGoods> getWarehouseInListGoodsList() {
		return warehouseInListGoodsList;
	}

	public void setWarehouseInListGoodsList(List<WarehouseInListGoods> warehouseInListGoodsList) {
		this.warehouseInListGoodsList = warehouseInListGoodsList;
	}

	@Override
	public String toString() {
		return "WarehouseInList [id=" + id + ", returnNumber=" + inNumber + ", department=" + department + ", returnDate="
				+ inDate + ", amountPayable=" + amountPayable + ", amountPaid=" + amountPaid + ", state=" + state
				+ ", user=" + user + ", remarks=" + remarks + ", bReturnDate=" + bInDate + ", eReturnDate="
				+ eInDate + "]";
	}

	
	
	
	
}
