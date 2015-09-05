package house;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Queue;

public  class Location {

	protected int location;		// 准确的地图地址
	
	private String isHouseState;		// "isPlain"空地,"isStartPlace"是起点,
									// 	"isHouse"有房, "isToolHouse" 道具屋,"isGiftHouse"礼品屋,"isMagicHouse"魔法屋,
									// "isPrison"有监狱,"isHospital"有医院,"isMine"矿区
	
	private String isToolState;			// "isRoadBlock"有路障,"isBomb"有炸弹,isForbid 不能放置tool，能放置tool的地点,只能是isEmpty状态
	
	
	private Queue<Color> colorQueue;    // 在面板上用来记录在当前location上的玩家的颜色，并在离开时将其以先进先出的规则，移除颜色
	

	public Location() {
		
	}
	
	
	public Location(int location,String state, String state1) {
		this.location = location;
		this.isHouseState = state;
		this.colorQueue = new ArrayDeque<>();
		this.isToolState = state1;
	}

	
//	private int districtLandPerMoney;	// 玩家所处地区的地块单价单价
//	当前地段的单位地价
//	public int getPerMoney() {
//		if(district.equals("t"))  {
//			return 200;
//		}else if(district.equals("r")){
//			return 500;
//		}else 
//			return 500;
//	}
	
	
	public static int getLandMoney(int location) {
	
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
	
	
	
	public char getDistrict() {
		if(location>0 && location<28 && location!=14) {
			return 't';
		} else if(location>=29 && location<=34) {
			return 'r';
		} else if(location>35 && location<63) {
			return 'b';
		} else{
//			throw new RuntimeException("这块地区为矿地或者其他特殊用地，没有房产价！");
			return 'N';
		}
	}
		

//	public void setDistrict(String district) {
//		this.district = district;
//	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	
	public String getIsHouseState() {
		return isHouseState;
	}

	public void setIsHouseState(String isHouseState) {
		this.isHouseState = isHouseState;
	}

	public String getIsToolState() {
		return isToolState;
	}

	public void setIsToolState(String isToolState) {
		this.isToolState = isToolState;
	}


	public Queue<Color> getColorQueue() {
		return colorQueue;
	}


	public void setColorQueue(Queue<Color> colorQueue) {
		this.colorQueue = colorQueue;
	}

	
}
 
