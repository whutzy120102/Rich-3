package house;

import java.awt.Color;

/**
 *  House对象 有  房屋等级属性 、地点 属性，以及父类的 ，房屋所具有的颜色属性，获取土地单价等一些方法。
 * @author Local_win8
 *
 */

public class House{
	private int houseRate;		// 房屋等级
	private String houseOwner;		// 房屋所有者名
	private int houseOwnerState;	// 房主状态
	private int location;
	private Color houseColor;
	
	public House(String owner, int houseRate, String district, int location) {
		super();
		this.houseOwner = owner;
		this.houseRate = houseRate;
//		super.district = district;
		this.location = location;
	}
	
	public House(){
		
	}
	
//	public House(String  owner, String fullLocation) {
	public House(String  owner, int location, int state, Color color) {
		this.houseRate = 0 ;
		this.houseOwner = owner;
		this.location = location;
		this.houseOwnerState = state;
		this.houseColor = color;
//		this.fullLocation = fullLocation;
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
		return getHouseMoney()/2;
	}

	// 房屋的等级
	public int getHouseRate() {
		return houseRate;
	}

	public void setHouseRate(int houseRate) {
		this.houseRate = houseRate;
	}

	public Color getHouseColor() {
		return houseColor;
	}

	public void setHouseColor(Color houseColor) {
		this.houseColor = houseColor;
	}

	public void setLocation(int location) {
		this.location = location;
	}
	
	
	
}
