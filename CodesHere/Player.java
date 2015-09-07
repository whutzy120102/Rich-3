package player;

import house.House;
import house.Location;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import others.MainT;

/**
 *  玩家类，有获取总资产方法
 * @author 周文俊
 *
 */


public class Player{
	private String playerName;
	
	private int cash;
	private Map<Integer, House> houseMap;
	private int points;	// 玩家点数
	private int state;		// 玩家状态，1正常，3医院中，2监狱中，4财神附体中,5出局状态
	private int pauseStateHospRest;		// 玩家进医院暂停状态的剩余天数(一次为3天)
	private int pauseStatePriRest;		// 玩家进监狱暂停状态剩余天数，初始化为0(一次性2天)
	private int roadBlock;		// 路障道具数
	private int bomb;			// 炸弹数
	private int robot;			// 机器人数
	private int location;
	private int godOfWealth;	// 财神
	
	private Color color;		// 每位用户拥有一个颜色
	
	
	
	public Player(String playerName, int cash,int points, Color color) {

		this.playerName = playerName;
		this.cash = cash;
		this.houseMap = new HashMap<Integer, House>();
		this.points = points;
		this.color = color;
		this.state = 1;
		this.pauseStateHospRest = 0;
		this.roadBlock = 3;
		this.bomb = 3;
		this.robot = 0;
		this.location = 0;
	}

	
	/**
	 * 获取玩家的总金额，所有的房屋出售价值，加上现金
	 * @return
	 */
	public int getTotalMoney() {
		
		int totalMoney = cash;
		for(Entry<Integer,House> entry : houseMap.entrySet()) {
			totalMoney += entry.getValue().getHouseMoney() * 2;
		}
		
		// 该方法执行可以刷新cash
//		int moneyFromHouse = 0;
//		for(Entry<Integer,House> entry : houseMap.entrySet()) {
//			moneyFromHouse += entry.getValue().getMoneyFromHouse();
//		}
//		this.cash += moneyFromHouse;
		
		return totalMoney;
	}
	
	
	///////////////////////////////////////////////
	/*
	 * 玩家有添加房屋地块的方法
	 */
	public String addHouse() {
		if(this.cash< Location.getLandMoney(this.location)) {
			JOptionPane.showMessageDialog(null, "<html>大兄弟 : " + this.playerName +"  您的现金不够啦，暂不能买房。<br>(小提示：下轮先卖房，再考虑卖房吧！)</html>");
			
			return "fail";
		} else {
			House house = new House(playerName, location, state);		//  state 为房主状态，房屋rate新建时为0
			MainT.allHouseMap.put(location, house);
			houseMap.put(location, house);
			MainT.markLocation.get(location).setIsHouseState("isHouse");	//  玩家买了该块地皮之后，就要设置地皮的标识为isHouse
			this.cash -= Location.getLandMoney(location);
			return "ok";
		}
	}
	
	/**
	 * 房屋升级
	 */
	public String updateHouse() {
		if(this.cash< Location.getLandMoney(this.location)) {
			JOptionPane.showMessageDialog(null, "<html>大兄弟 : " + this.playerName +"  您的现金不够啦，"
					+ "暂不能升级房屋。<br>(小提示：下轮先卖房，再考虑升级房吧！)</html>");
			return "fail";
		} else {
			House house = houseMap.get(location);
			int rate = house.getHouseRate();
			house.setHouseRate(rate+1);
			
			int landCost = house.getPerMoney();		//房屋升级费用
			this.cash -= landCost;
			return "ok";
		}
	}
	
	
	/**
	 *  售卖房屋
	 */
	// TODO 
//	public void updateHouse(int location) {
//		
//		House house = houseMap.get(location);
//		int rate = house.getHouseRate();
//		house.setHouseRate(rate+1);
//		
//		int landCost = house.getPerMoney();
//		this.cash -= landCost;
//	}
	
	/**
	 *  前进, 根据用户骰子的数目，来决定前进的位置
	 * @return
	 */
	public void moveForward(int step) {
		int oldLocation = this.location;
		// 检测 所要行走的路途中是否有路障
		int checkPos = checkPassway(oldLocation, step);
		if(checkPos != 999 && checkPos != 14) {	// 有路障,且终点不是炸弹
			step = checkPos-oldLocation;
			this.location+=step;											// "isPlain"空地,"isStartPlace"是起点,,"isGiftHouse"礼品屋
				
		} else if(checkPos == 999) {
			this.location+=step;
		} else {
			
		}
		
		if(this.location>=70){										// 	"isHouse"有房, "isToolHouse" 道具屋,"isMagicHouse"魔法屋,
			this.location %=70; 									// "empty","isPrison"有监狱,"isHospital"有医院
		}
				// 正常终点，包括路障终点
		
	}
	
	/**
	 * 	检测当前土地，是否为监狱，炸弹，等等
	 *  检测是否购买当前土地,返回购买的土地地点
	 */
	public int checkIfbuyLand(){
		Location destLocation = MainT.markLocation.get(this.location);
		String isHouseState = destLocation.getIsHouseState();
		
		if(isHouseState.equals("isPlain")) {	// "此处为空地，是否购买该房产？")
			
			int ret = JOptionPane.showConfirmDialog(null,
					this.playerName + " : 此处为空地，是否购买该房产？", "购买房产", 
					JOptionPane.YES_NO_OPTION);
			if(ret==0){		// 选择 是
					
			
				// 下方法购买房屋在执行后，房屋为0级，地点为当前的玩家地点，房主状态为1，玩家的房屋map自动增加当前的房屋
				String buyState = this.addHouse();
				if(buyState.equals("fail")) {		// 钱不够买
					return 999;
				} else {		// 购买成功
					
					// 返回此地点，方法调用处会将此地的边框置色
					return location;
				}
			} else {	// 选择 NO
				
			}
			

			// 不是空地，踩到监狱，则更改用户状态，以及名下所有房产的房主状态属性 
		} else if(isHouseState.equals("isPrison")) {
			this.state = 2;
			this.pauseStateHospRest = 2;
			// 将自己名下所有的房产的属性中的房主属性都置为isPrison:3
			for(Entry<Integer,House> entry : houseMap.entrySet()) {
				entry.getValue().setHouseOwnerState(state);
			}
			// TODO check up there whether it is correct
			
			
			// 当前土地为房屋
		} else if(isHouseState.equals("isHouse")){
			//  获取当前的house对象
			House house = MainT.allHouseMap.get(location);
			
			// 如果是自己的房子，不付过路钱，提示是否升级
			if(house.getHouseOwner().equals(this.playerName) && house.getHouseRate()<3) {
				
				// 提示是否升级
				// TODO
				if(JOptionPane.showConfirmDialog(null,
						this.playerName + " : 此处地产在您麾下，并且可以升级，是否升级？", "升级房产", 
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					String updateState = this.updateHouse();
					if(updateState.equals("fail")) {
						return 999;		// 升级钱不够
					} else {		// 升级成功
						return 888;
					}
				} else {		// 点击否
					return 999;		// 888代表升级房屋，999代表啥也不干
				}
				
				// 是别人的房子，付钱，(若对方玩家在监狱或者医院，则付款为0)
			} else {
				if(this.cash < house.getPassByCost()) {		// 付不起钱的情况
					JOptionPane.showMessageDialog(null, "<html>"   + this.playerName 
						+ "系统遗憾的通知您：<br>由于您没有及时的变卖房产为现金，您没有足够的现金来支付其他玩家的过路费,<br>"
						+ "(在退出贷款功能之前)<br>"
						+ "该局游戏，您被判定为输家出局"
						+ "</html>");
					this.state = 5;
//					this.destroy();		// 用户出局，销毁用户所有数据，包括房屋，房产，
					return 555;			// 555状态为出局状态
				} else {		// 付得起钱
					int passByCost = house.getPassByCost();
					this.cash -= passByCost;		// 当前玩家 付款
					house.setMoneyFromHouse(passByCost);
					return 556;			// 556 状态码为付过路费
				}
				
			}
			
			// 走进入医院，无动作
		} else if(isHouseState.equals("isHospital")) {
			
			// 矿区，得点数
		} else if(isHouseState.equals("isMine")) {
			this.points += getHereNowPoints(location);
			return 660;			// 666状态码表示进入矿区，得到点数
			
			// 进入礼品屋
		} else if(isHouseState.equals("isGiftHouse")) {
			
			// 玩家进入礼品屋，三样礼品中任选一样，
			// 奖金：2000元；点卡：200点；财神：5轮有效；
			Object[] options = { "现金2000", "200点卡", "财神" };
			int ret = JOptionPane.showOptionDialog(null, "<html>" + this.playerName+"  :好运连连，恭喜进入礼品屋:<br>"
					+ "现有三个礼品可供选择，分别是 ：奖金：2000元；点卡：200点；财神：5轮有效<br>"
					+ "请点击对应的按钮进行选择。(注意：选择财神时，下一轮即可开始使用。关闭窗口视为放弃奖励)", 
					"道具屋", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null,
					options, options[0]);
			if(ret==0) {	// 选择奖金
				this.cash += 2000;
				return 661;			// 661 状态码表示获得2000元现金
				
			} else if(ret==1) {		// 选择点卡
				this.points += 200;
				return 662;			// 662 状态码表示获得200点
			} else if(ret==2) {		// 选择财神
				this.godOfWealth++;
				return 663;			// 663 状态码表示获得财神
				
			} else {	// 关闭窗口视为放弃奖励
				
			}
			
			
			// 进入 道具屋
		} else if(isHouseState.equals("isToolHouse")) {
			
			// 每位玩家最多拥有10个道具
			// 路障：50点；机器娃娃：30点；炸弹：50点；
			
			Object[] options = { "路障", "机器娃娃", "炸弹" };
			int ret = JOptionPane.showOptionDialog(null, "<html>" + this.playerName +"  :恭喜你进入道具屋:<br>"
					+ "现有三个道具可供选择，但是都需要消耗你的道具点哦，<br>点击前，请确认你的点数够买响应的道具，否则，做买空处理(不消耗点数)。"
					+ "<br>路障：50点；机器娃娃：30点；炸弹：50点"
					+ "", 
					"道具屋", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null,
					options, options[0]);
			if(ret==0) {	// 选择路障
				// 既要有点数还要总道具数低于10
				if(this.points>=50) {
					if(this.getTotalTools()>=10) {			// 
						JOptionPane.showMessageDialog(null, ""+  this.playerName + "您的总道具数达到上限！不能再添加道具，建议先使用部分道具~", "道具上限提醒",  JOptionPane.WARNING_MESSAGE);
					} else {
						this.points -= 50;
						this.roadBlock++; 
					}
				}
			} else if(ret==1) { 	// 选择机器娃娃
				if(this.points>=30) {
					if(this.getTotalTools()>=10) {			// 
						JOptionPane.showMessageDialog(null, ""+  this.playerName + "您的总道具数达到上限！不能再添加道具，建议先使用部分道具~", "道具上限提醒",  JOptionPane.WARNING_MESSAGE);
					} else {	// 有购买资格
						this.points -= 30;
						this.robot++;
					}
				}
			} else if(ret==2) {		// 选择炸弹
				if(this.points>=50) {
					if(this.getTotalTools()>=10) {			// 
						JOptionPane.showMessageDialog(null, ""+  this.playerName + "您的总道具数达到上限！不能再添加道具，建议先使用部分道具~",
								"道具上限提醒",  JOptionPane.WARNING_MESSAGE);
					} else {
						this.points -= 50;
						this.bomb++;
					}
				}
			} else {
				
			}
			
			
			// 进入魔法屋
		} else if(isHouseState.equals("isMagicHouse")) {
			JOptionPane.showMessageDialog(null, ""+  this.playerName + "不好意思，魔法屋系列功能暂未开放，有待后续更新~",
					"魔法屋",  JOptionPane.WARNING_MESSAGE);
		} 
		
			
		return 999;			// 啥动作都没有
	}
	
	
	/**
	 *  检测所要行走的step路途中，是否有路障，返回路障的location
	 * @param location
	 * @param step
	 * @return
	 */
	public int checkPassway(int location, int step) {
		int roadBlockPos = 999;
		for(int key=location+1;key<= location+step;key++) {
			
			
			// 此处容易有死循环
			if(key>=64 && key <= 69) {
				continue;
			}

			if(MainT.markLocation.get(key%70).getIsToolState().equals("isRoadBlock")) {

				roadBlockPos = key%70;
				break;
			}
		}
		// 有炸弹啊，，返回医院地址
		if(MainT.markLocation.get((location+step)%70).getIsToolState().equals("isBomb")) {
			MainT.markLocation.get((location+step)%70).setIsToolState("isEmpty");
			this.state = 3;		// 被炸弹，进医院
			this.pauseStateHospRest = 3;	// 剩余医院天数为3
			// 炸弹，进医院，将所有的房屋房主状态变为3
			for(Entry<Integer,House> entry : this.houseMap.entrySet()) {
				entry.getValue().setHouseOwnerState(state);
			}
			this.location = 14;
			return 14;		// 直接返回医院地址
		}
		return roadBlockPos;
	}
	
	/**
	 *  根据当前所在的矿区的具体地点，获得对应的点数奖励
	 * @param loc
	 * @return
	 */
	public int getHereNowPoints(int loc) {
		if(loc==64) {
			return 20;
		} else if(loc==65 || loc == 68) {
			return 80;
		} else if(loc == 66) {
			return 100;
		} else if(loc == 67) {
			return 40;
		} else {
			return 60;
		}
	}
	
	
	/**
	 * 销毁所有用户数据
	 */
	public void destroy() {
		int key = 0;
		// 将辅助类中的所有该用户的房产都移除,辅助地点类的房屋属性更改
		for(Entry<Integer,House> entry : houseMap.entrySet()) {		
			key = entry.getKey();
			MainT.allHouseMap.remove(key);
			MainT.markLocation.get(key).setIsHouseState("isPlain");
		}
		// 玩家的houseMap清空
		this.houseMap.clear();
		this.bomb=0;
		this.roadBlock=0;
		this.robot=0;
		this.cash=0;
		this.points=0;
	}
	
	
	
	
	public Map<Integer, House> getHouseMap() {
		return houseMap;
	}
	
	public String getRestDay() {
		if(pauseStateHospRest==0 && pauseStatePriRest==0) {
			return "";
		} else {
			return pauseStateHospRest == 0 ? pauseStatePriRest+"" : pauseStateHospRest+"";
		}
			
	}

	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}
	
	
	public int getGodOfWealth() {
		return godOfWealth;
	}


	public void setGodOfWealth(int godOfWealth) {
		this.godOfWealth = godOfWealth;
	}


	public int getPauseStateHospRest() {
		return pauseStateHospRest;
	}
	public void setPauseStateHospRest(int pauseStateHospRest) {
		this.pauseStateHospRest = pauseStateHospRest;
	}
	public int getPauseStatePriRest() {
		return pauseStatePriRest;
	}
	public void setPauseStatePriRest(int pauseStatePriRest) {
		this.pauseStatePriRest = pauseStatePriRest;
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
		return this.robot+this.roadBlock+this.bomb;
	}

	public int getRoadBlock() {
		return roadBlock;
	}
	public void setRoadBlock(int roadBlock) {
		this.roadBlock = roadBlock;
	}
	public int getBomb() {
		return bomb;
	}
	public void setBomb(int bomb) {
		this.bomb = bomb;
	}
	public int getRobot() {
		return robot;
	}
	public void setRobot(int robot) {
		this.robot = robot;
	}
	
	public String getStateInStr(int state) {
		if(state == 1) {
			return "正常游戏";
		} else if(state == 2) {
			return "监狱中";
		} else if(state == 3) {
			return "医院中";
		} else if(state == 4) {
			return "财神附体";
		} else if(state == 5) {
			return "已出局";
		} else {
			return "";
		}
	}
	
	
	
	/////////////////////////////////////
	// 获取参数列表
	public List<String> getPlayerData() {
		List<String> playerDataList = new ArrayList<String>();
		playerDataList.add(getPlayerName());
		playerDataList.add("" + getStateInStr(this.state) + "---" + this.location + "号位");
		playerDataList.add(getRestDay() + "天");
		playerDataList.add("" + getTotalMoney());
		playerDataList.add("" + getCash());
		playerDataList.add("" + getPoints());
		playerDataList.add("" + getHouseNum());
		playerDataList.add("" + getRobot());
		playerDataList.add("" + getRoadBlock());
		playerDataList.add("" + getBomb());
		
		return playerDataList;
	}
	@Override
	public String toString() {
		return "Player [playerName=" + playerName + ", cash=" + cash
				+ ", houseMap=" + houseMap + ", points=" + points + ", state="
				+ state + ", roadBlock=" + roadBlock + ", bomb=" + bomb
				+ ", robot=" + robot + ", location=" + location + "]";
	}
	
	
}
