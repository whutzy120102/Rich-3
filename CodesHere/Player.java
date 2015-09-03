package player;

import house.House;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import others.MainT;

/**
 *  玩家类，有获取总资产方法
 * @author 周文俊
 *
 */

public class Player{
	private String playerName;
	private int cash;   // 现金
	private Map<Integer, House> houseMap;
	private int points;	// 玩家点数
	private int state;		// 玩家状态，1正常，2医院中，3监狱中，4财神附体中
	
//	private int totalTools;		// 玩家拥有的道具数,最多10个
	private int roadBlock;		// 路障道具数
	private int boomb;			// 炸弹数
	private int robot;			// 机器人数
	private int location;   // 玩家地址
	
	public Player(String playerName, int cash,int points) {
		this.playerName = playerName;
		this.cash = cash;
		this.houseMap = new HashMap<Integer, House>();
		this.points = points;
		this.state = 1;
		this.roadBlock = 0;
		this.boomb = 0;
		this.robot = 0;
		this.location = 0;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public long getCash() {
		return cash;
	}
	public void setCash(int cash) {
		this.cash = cash;
	}
	public long getTotalMoney() {
		long totalMoney = cash;
		for(Entry<Integer,House> entry : houseMap.entrySet()) {
			totalMoney += entry.getValue().getHouseMoney();
		}
		return totalMoney;
	}

	///////////////////////////////////////////////
	/*
	 * 玩家有添加房屋地块的方法
	 */
	public void addHouse() {
		House house = new House(playerName, location, state);
		houseMap.put(location, house);
	}
	
	/**
	 * 房屋升级
	 */
	public void updateHouse() {
		House house = houseMap.get(location);
		int rate = house.getHouseRate();
		house.setHouseRate(rate+1);
	}
	
	
	/**
	 *  前进, 根据用户骰子的数目，来决定前进的位置
	 * @return
	 */
	public void moveForward(int step) {
		location+=step;
		if(location>71){
			location -= 71; 
		}
	}
	
	public int getHouseNum() {
		return houseMap.size();
	}
	
	public int getLocation() {
		return location;
	}
	
	public long getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getTotalTools() {
		return this.robot+this.roadBlock+this.boomb;
	}

	public int getRoadBlock() {
		return roadBlock;
	}
	public void setRoadBlock(int roadBlock) {
		this.roadBlock = roadBlock;
	}
	public int getBoomb() {
		return boomb;
	}
	public void setBoomb(int boomb) {
		this.boomb = boomb;
	}
	public int getRobot() {
		return robot;
	}
	public void setRobot(int robot) {
		this.robot = robot;
	}
	
	
	/////////////////////////////////////
	// 获取参数列表
	public List<String> getPlayerData() {
		List<String> playerDataList = new ArrayList<String>();
		playerDataList.add("玩家姓名:" + getPlayerName());
		playerDataList.add("状态:" + getState());
		playerDataList.add("剩余:" + "天");
		playerDataList.add("总金额:" + getTotalMoney());
		playerDataList.add("现金:" + getCash());
		playerDataList.add("点数:" + getPoints());
		playerDataList.add("房屋数:" + getHouseNum());
		playerDataList.add("机器人:" + getRobot());
		playerDataList.add("路障:" + getRoadBlock());
		playerDataList.add("炸弹:" + getBoomb());
		
		return playerDataList;
	}
	@Override
	public String toString() {
		return "Player [playerName=" + playerName + ", cash=" + cash
				+ ", houseMap=" + houseMap + ", points=" + points + ", state="
				+ state + ", roadBlock=" + roadBlock + ", boomb=" + boomb
				+ ", robot=" + robot + ", location=" + location + "]";
	}
	
}
