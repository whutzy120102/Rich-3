package house;

import java.awt.Color;

/**
 *  House对象 有  房屋等级属性，继承了地点类的    地区与地点 属性，以及父类的 获取土地单价方法。
 * @author Local_win8
 *
 */

public class House{
	private int houseRate;		// 房屋等级 0.1.2.3，在地图的面板上，用Z,Y,X,W分别对应表示
	private String houseOwner;		// 房屋所有者名
	private int houseOwnerState;	// 房主状态
	private int location;
	
	private int moneyFromHouse = 0;
	
	
	public House(String owner, int houseRate, String district, int location) {
		super();
		this.houseOwner = owner;
		this.houseRate = houseRate;
		this.location = location;
	}
	
	public House(){
		
	}
	
	public House(String  owner, int location, int state) {
		this.houseRate = 0 ;
		this.houseOwner = owner;
		this.location = location;
		this.houseOwnerState = state;
		this.moneyFromHouse = 0;
	}
	
	
	public int getLocation(){
		return location;
	}
	
	///////////////////////////////////////////////////
	// 根据当前地址，获取地区
	public char getDistrict() {
		if(location>0 && location<29) {
			return 't';
		} else if(location>29 && location<36) {
			return 'r';
		} else if(location> 36 && location<66) {
			return 'b';
		} else {
			throw new RuntimeException("这块地区为矿地或者其他特殊用地，没有房产价！");
		}
	}
	// 根据当前地址，获取 当前 地价
	public int getPerMoney() {
		if(location>0 && location<28) {
			return 200;
		} else if(location>=29 && location<=34) {
			return 500;
		} else if(location>=36 && location<63) {
			return 300;
		} else {
			throw new RuntimeException("这块地区为矿地或者其他特殊用地，没有房产价！");
		}
	}
	
	///////////////////////////////////////////////////
	
	public int getHouseOwnerState() {
		return houseOwnerState;
	}

	public void setHouseOwnerState(int houseOwnerState) {
		this.houseOwnerState = houseOwnerState;
	}

	public String getHouseOwner() {
		return houseOwner;
	}

	public void setHouseOwner(String houseOwner) {
		this.houseOwner = houseOwner;
	}

	// 获取当前房屋的价格
	public int getHouseMoney(){
		return getPerMoney() * (houseRate+1);
	}
	
	// 当前房屋的应收取的过路费
	public int getPassByCost() {
		if(houseOwnerState==1) {	// 正常状态
			return getHouseMoney()/2;
		}
		return 0;		// 监狱或医院状态
	}
	
	// 
	public void setMoneyFromHouse(int money) {
		this.moneyFromHouse = money;
	}
	// 房屋收取的过路费
	public int getMoneyFromHouse() {
		return moneyFromHouse;
	}
	
	// 房屋的等级
	public int getHouseRate() {
		return houseRate;
	}

	public void setHouseRate(int houseRate) {
		this.houseRate = houseRate;
	}

	public void setLocation(int location) {
		this.location = location;
	}
	
}
