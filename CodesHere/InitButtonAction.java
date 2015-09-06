package actionListener;

import house.Location;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import others.MainT;
import player.Player;
import playerMaker.PlayerMaker;
import basicFrame.BFrame;

/**
*  数据初始化类
*/


public class InitButtonAction implements ActionListener {
	int num = 2;
	BFrame basicFrame;
	int money = 10000;
	int point = 100;
	Player[] player;

	
	public InitButtonAction(BFrame Bframe) {
		this.basicFrame = Bframe;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		boolean two = basicFrame.getTwoPlayersRadio().isSelected();
		boolean three = basicFrame.getThreePlayersRadio().isSelected();
		boolean four = basicFrame.getFourPlayersRadio().isSelected();
		
		if(two) {
			num = 2;
		} else if(three) {
			num = 3;
		} else { 
			num = 4;
		}
		
		
		String initMoneyStr = basicFrame.getInitMoney().getText().trim();
		String initPointsStr = basicFrame.getInitPoints().getText().trim();
		
		boolean isNumeric = MainT.isNumeric(initMoneyStr,initPointsStr);
		

		if(!isNumeric) {	// 不全是是数字
			JOptionPane.showMessageDialog(null, "大兄弟 : 您的输入有误，要输入纯数字");
			
			
			
			basicFrame.getInitMoney().setText("           ");
			basicFrame.getInitPoints().setText("           ");}
			
			
		} else if(initMoneyStr.equals("") || initPointsStr.equals("")){
			
			int ret2 = JOptionPane.showConfirmDialog(null,
					"<html>大兄弟 : 您没有输入<br>点击是，重新输入;点击否，按默认值进行游戏</html>", "初始输入验证", 
					JOptionPane.YES_NO_OPTION);
			if(ret2==0) {	// 是
				basicFrame.getInitMoney().setText("           ");
				basicFrame.getInitPoints().setText("           ");
			}else if(ret2==-1) {	// 直接关闭
				basicFrame.getInitMoney().setText("           ");
				basicFrame.getInitPoints().setText("           ");
		
			}else if(ret2==1) {			// 否，按默认值
				
				this.doInitData();
			}
			
		} else {	// isNumeric = true;
			// 是数字
			money = Integer.parseInt(initMoneyStr);
			point = Integer.parseInt(initPointsStr);
			
			
			if(money<1000 || money>50000 || point<30 || point>500) {
				
				int ret = JOptionPane.showConfirmDialog(null,
						"<html>大兄弟 : 您的输入有误，超出规定范围，<br>是否重新输入，点击是，重新输入;点击否，按默认值进行游戏</html>", "初始输入验证", 
						JOptionPane.YES_NO_OPTION);
				if(ret==0) {	// 是
					basicFrame.getInitMoney().setText("           ");
					basicFrame.getInitPoints().setText("           ");
				}else if(ret==-1) {	// 直接关闭
					
					basicFrame.getInitMoney().setText("           ");
					basicFrame.getInitPoints().setText("           ");
					
					
				}else if(ret==1) {			// 否，按默认值
						if(money<1000 || money>50000) {
						money = 10000;
					}
					if(point<30 || point>500) {
						point = 100;
					}
					
					this.doInitData();
					
				}
				
			} else {		// 输入的是数字，且在范围内
				
				this.doInitData();
			}

		}	// end else 输入的是数字
		
		}
	
	
	public void doInitData() {

		player = new Player[num];
		for(int i = 0; i< num; i++) {
			player[i] = PlayerMaker.makePlayer(MainT.Names[i], money, point, MainT.colors[i]);
		}
		
		JTextField[] showPlayerInfo = basicFrame.getShowPlayersInfo();
		for(int i = 0; i<10; i++) {
			showPlayerInfo[i].setText(player[0].getPlayerData().get(i));
			showPlayerInfo[i+10].setText(player[1].getPlayerData().get(i));
		}
		if(num>=3) {
			for(int i = 0; i<10; i++) {
				showPlayerInfo[i+20].setText(player[2].getPlayerData().get(i));
			}
		}
		if(num==4) {
			for(int i = 0; i<10; i++) {
				showPlayerInfo[i+30].setText(player[3].getPlayerData().get(i));
			}
		}
		
		/**
		 *  将所有的Location都初始化
		 */
		// "isPlain"空地,"isStartPlace"是起点,
		// "isHouse"有房, "isToolHouse" 道具屋,"isGiftHouse"礼品屋,"isMagicHouse"魔法屋,
		// "isRoadBlock"有路障,"isBoomb"有炸弹,"isPrison"有监狱,"isHospital"有医院,"isMine"矿地
		
		MainT.markLocation.put(0, new Location(0, "isStartPlace", "isForbid"));
		
		for(int i =1;i<=62;i++) {
			if(i==14) {
				Location hospitalLocation = new Location(14, "isHospital", "isForbid");
				MainT.markLocation.put(14, hospitalLocation);
			} else if(i==28) {
				Location toolHouseLocation = new Location(28, "isToolHouse", "isForbid");
				MainT.markLocation.put(28, toolHouseLocation);
			} else if(i==35) {
				Location giftHouseLocation = new Location(35, "isGiftHouse", "isForbid");
				MainT.markLocation.put(35, giftHouseLocation);
			} else if(i==49) {
				Location prisonLocation = new Location(49, "isPrison", "isForbid");
				MainT.markLocation.put(49, prisonLocation);
			} else {
				MainT.markLocation.put(i, new Location(i, "isPlain", "isEmpty"));
			}
		}
		MainT.markLocation.put(63, new Location(63, "isMagicHouse", "isForbid"));
		for(int i =64;i<=69;i++) {
			MainT.markLocation.put(i, new Location(i, "isMine", "isForbid"));
		}
			
		ButtonDiceAction.player = player;
		UseBombAction.player = player;
		UseRoadBlockAction.player = player;
		
		
		
		basicFrame.getInitMoney().setText("           ");
		basicFrame.getInitPoints().setText("           ");

	}

	
	/**
	 *  获取初始化的玩家
	 * @return
	 */
	public Player[] getPlayers() {
		return player;
	}
	
	

	public int getNum() {
		return num;
	}


	public void setNum(int num) {
		this.num = num;
	}


	public int getMoney() {
		return money;
	}


	public void setMoney(int money) {
		this.money = money;
	}


	public int getPoint() {
		return point;
	}


	public void setPoint(int point) {
		this.point = point;
	}
	
	
}
