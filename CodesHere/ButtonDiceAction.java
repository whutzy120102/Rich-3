package actionListener;

import house.House;
import house.Location;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import others.MainT;
import player.Player;
import test.RandomProduce;
import basicFrame.BFrame;

public class ButtonDiceAction implements ActionListener {
	private BFrame basicFrame;
	
	
	static int flag = 1;
	
	static Player[] player;
	JTextField[] showPlayerInfo;
	List<JButton> labelList; 
	JLabel showCashInfo;
	
	Color originalColor;
	Border originalBorder;
	
	public ButtonDiceAction(){
		
	}

	public ButtonDiceAction(BFrame frame) {
	this.basicFrame = frame;
	flag = 1;
	this.beforeAction();
}
	
	public void beforeAction() {
		
		showPlayerInfo = basicFrame.getShowPlayersInfo();
		originalColor = new Color(238, 238, 238);
		showPlayerInfo[0].setBackground(Color.PINK);
		showCashInfo = basicFrame.getShowCashInfo();
		labelList = basicFrame.getButtonList();
		originalBorder = basicFrame.getOriginalBorder();
	}
	
	/**
	 * 响应骰子按钮事件
	 */
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int steps = RandomProduce.getRandom(1, 6);
		
		basicFrame.showDice().setText("" + steps);

		// 轮到第一个玩家
		if(flag==1) {
			
			/**
			 *  以下为玩家轮空时的状态
			 */
					// 3时为医院   2时为监狱   5 为 出局状态，不可逆
			if(player[flag-1].getState() == 3 || player[flag-1].getState() == 2 || player[flag-1].getState() == 5) {	
				// 玩家出局，啥也不做
				if(player[flag-1].getState() == 5) {
					showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   已经出局                                  ");
				} else {
				
					// 玩家为轮空状态
					if(player[flag-1].getPauseStatePriRest() != 0) {		// 因为监狱轮空
						int prisonTemp = player[flag-1].getPauseStatePriRest();
						player[flag-1].setPauseStatePriRest(--prisonTemp);
						if(prisonTemp == 0) {			// 监狱轮空2轮后，状态恢复
							player[flag-1].setState(1);			// 切记此处，出院了，要将所有房屋的房主状态切回来
							for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
								entry.getValue().setHouseOwnerState(1);
							}
						}
						// 玩家因为进医院轮空
					} else {
						int hospitalTemp = player[flag-1].getPauseStateHospRest();
						player[flag-1].setPauseStateHospRest(--hospitalTemp);
						if(hospitalTemp == 0) {
							player[flag-1].setState(1);			// 切记此处，出院了，要将所有房屋的房主状态切回来
							for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
								entry.getValue().setHouseOwnerState(1);
							}
							
						}
					}
				}
				
			} else {
			
			showCashInfo.setText("公告: " + " 暂无新消息                                                                  ");
			
			// 走之前的字符串
			String beforeLeaveText = labelList.get(player[flag-1].getLocation()).getText();
			// 走之后要删除当前用户的字符串
			String afterLeaveText = beforeLeaveText.replaceFirst("/1", "");
			labelList.get(player[flag-1].getLocation()).setText(afterLeaveText);
			// 走之前移除当前色
			Queue<Color> colorQueue = MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue(); //.pop();
			if(colorQueue.size()>=2) {		// 不是默认色size=1，且不是房主色size=2，
				colorQueue.remove();
//			// 设置玩家进来之前的颜色为当前土地的颜色
				labelList.get(player[flag-1].getLocation()).setBackground(MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue().peek());
				
			} else {	// 就剩下一种颜色，且没人买的房屋当前颜色移除时，应该显示原色
				if(colorQueue.size()!=0) {
					colorQueue.remove();
				}
				labelList.get(player[flag-1].getLocation()).setBackground(originalColor);
			}
		} 
			
			// 当前正在掷骰子的玩家名显示变色
			// TODO 0904 18.04
			// 前进到应该的地点，考虑了路障因素
			int oldPlayerBombLoc = player[flag-1].getLocation();
			player[flag-1].moveForward(steps);
			int newPlayerBombLoc = player[flag-1].getLocation();
			if((oldPlayerBombLoc+ steps) != 14 && newPlayerBombLoc==14) {	// 说明踩到炸弹了，炸到医院，没有按loc+steps来走
				String oldText = labelList.get(oldPlayerBombLoc+ steps).getText();
//				System.out.println("oldText:" + oldText);
				oldText = oldText.replaceFirst("/B", "");
				labelList.get(oldPlayerBombLoc+ steps).setText(oldText);
				showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   不幸踩到炸弹  " 
						+ "  被送进医院，默哀。");
			} else if((oldPlayerBombLoc+ steps)%70 != newPlayerBombLoc) {
				String oldText = labelList.get(newPlayerBombLoc).getText();
				oldText = oldText.replaceFirst("/R", "");
				labelList.get(newPlayerBombLoc).setText(oldText);
				showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   被路障拦截，停在  " 
						+ newPlayerBombLoc +"  号位。");
			}

			labelList.get(player[flag-1].getLocation()).setBackground(Color.RED);
			MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue().add(Color.RED);

			// 当用户进入新地点，改变地点的字符
			String originalText = labelList.get(player[flag-1].getLocation()).getText();
			originalText = originalText.replaceAll("<html>", "");
			originalText = originalText.replaceAll("</html>", "");
			// "/"出现的次数，两次时要加<br>
			int showTimes = MainT.getCounts(originalText, "/");
			String newText = "";
			if(showTimes>=2) {
				newText = "<html>" + originalText + "<br>/1" + "</html>";
			} else {
				newText = "<html>" + originalText + "/1" + "</html>";
			}
			labelList.get(player[flag-1].getLocation()).setText(newText);		// 设置新的字符串
			
			
			int myLand = player[flag-1].checkIfbuyLand();
			if(myLand<100) {			// 购买了土地，就更改土地的边框，为当前的玩家颜色,且设置房屋等级标识
				labelList.get(myLand).setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				String originalText1 = labelList.get(player[flag-1].getLocation()).getText();
				originalText1 = originalText1.replaceAll("<html>", "<html>Z/");
				labelList.get(player[flag-1].getLocation()).setText(originalText1);
				
				showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   花费了  " 
						+ Location.getLandMoney(player[flag-1].getLocation()) + "  元购买了一块地。");
				
				
			} else if(myLand == 888) {	// 代号888代表升级了房屋
				String originalText1 = labelList.get(player[flag-1].getLocation()).getText();
				int rate = player[flag-1].getHouseMap().get(player[flag-1].getLocation()).getHouseRate();
				String indexStr = "";
				if(rate==1) {		// 判断当前的房屋等级是多少， Y X W
					indexStr = "<html>Y";
					originalText1 = originalText1.replaceAll("<html>Z", indexStr);
				} else if(rate==2) {		// 房屋为X： 2级
					indexStr = "<html>X";
					originalText1 = originalText1.replaceAll("<html>Y", indexStr);
				} else if(rate==3) {		// 房屋为最高级 W
					indexStr = "<html>W";
					originalText1 = originalText1.replaceAll("<html>X", indexStr);
				}
				labelList.get(player[flag-1].getLocation()).setText(originalText1);		// 设置某级房屋标识
				
				
				showCashInfo.setText("公告: 玩家     " + player[flag-1].getPlayerName() + "     花费了  " 
						+ Location.getLandMoney(player[flag-1].getLocation()) + "  元将房屋升级到了  " 
						+ rate + "  级！");
				
			} else if(myLand == 555) {		// 代号555代表玩家付不起 过路费，被出局
				showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   穷到付不起路费，" 
						+ "  被迫出局，终止游戏");
				// 清楚所有标识边框,及房屋文本
				for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
					
					 int loc1 = entry.getValue().getLocation();		// 得到每个房屋的location
					 int rate = entry.getValue().getHouseRate();
					 labelList.get(loc1).setBorder(originalBorder);
					 String oldText11 = labelList.get(loc1).getText();

					 String regex = "";
					 if(rate==0) {
						 regex = "Z/";
					 } else if(rate == 1) {
						 regex = "Y/";
					 } else if(rate==2) {
						 regex = "X/";
					 } else {
						 regex = "W/";
					 }
					 oldText11 = oldText11.replaceAll(regex, "");
					 labelList.get(loc1).setText(oldText11);
				}
				player[flag-1].destroy();		// 清除所有绑定以及辅助类的信息
				// 清除当前玩家所在地块的颜色，
				Queue<Color> hereColorQue = MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue();
				if(hereColorQue.size()>=2) {
					hereColorQue.remove();
					labelList.get(player[flag-1].getLocation()).setBackground(hereColorQue.peek());
				} else {
					hereColorQue.remove();
					labelList.get(player[flag-1].getLocation()).setBackground(originalColor);
				}
				// 清除文本
				String hereUse1Text = labelList.get(player[flag-1].getLocation()).getText();
				labelList.get(player[flag-1].getLocation()).setText(hereUse1Text.replaceAll("/1", ""));
				

			} else if(myLand ==556) {
				House othersHhouse = MainT.allHouseMap.get(player[flag-1].getLocation());
				showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   付给   "
						+ othersHhouse.getHouseOwner() +"   路费   "
						+ othersHhouse.getPassByCost() +"  元。");
			}

			
			// 重置玩家现金
			refreshCash(player);
			// 重绘玩家信息列表
			reshowPlayerInfo(player);
			
			// 当前玩家变暗，下一个玩家高亮
			showPlayerInfo[flag-1].setBackground(originalColor);

			}
		}
		
		
		// 轮到第二个玩家
		if(flag==2) {		
			
			/**
			 *  以下为玩家轮空时的状态
			 */
			
			if(player[flag-1].getState() == 3 || player[flag-1].getState() == 2 || player[flag-1].getState() == 5) {		// 玩家为轮空状态
				
				if(player[flag-1].getState() == 5) {
					showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   已经出局                                  ");
				} else {
					if(player[flag-1].getPauseStatePriRest() != 0) {		// 因为监狱轮空
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   还在监狱中                                  ");
						int prisonTemp = player[flag-1].getPauseStatePriRest();
						player[flag-1].setPauseStatePriRest(--prisonTemp);
						if(prisonTemp == 0) {			// 监狱轮空2轮后，状态恢复
							player[flag-1].setState(1);
							for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
								entry.getValue().setHouseOwnerState(1);
							}
						}
						// 玩家因为进医院轮空
					} else if(player[flag-1].getPauseStateHospRest() != 0){
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   还在医院中                                  ");
						int hospitalTemp = player[flag-1].getPauseStateHospRest();
						player[flag-1].setPauseStateHospRest(--hospitalTemp);
						if(hospitalTemp == 0) {
							player[flag-1].setState(1);
							for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
								entry.getValue().setHouseOwnerState(1);
							}
						}
					} 
				}
				
			} else {

			showCashInfo.setText("公告: " + " 暂无新消息                                                                  ");

			// 走之前的字符串
			String beforeLeaveText = labelList.get(player[flag-1].getLocation()).getText();
			// 走之后要删除当前用户的字符串
			String afterLeaveText = beforeLeaveText.replaceFirst("/2", "");
			labelList.get(player[flag-1].getLocation()).setText(afterLeaveText);
			
			
			
			Queue<Color> colorQueue = MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue(); //.pop();
			if(colorQueue.size()>=2) {		// 不是默认色size=1，且不是房主色size=2，
				colorQueue.remove();

//			// 设置玩家进来之前的颜色
				
				labelList.get(player[flag-1].getLocation()).setBackground(MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue().peek());
				
			} else {
				if(colorQueue.size()!=0) {
					colorQueue.remove();
				}
				labelList.get(player[flag-1].getLocation()).setBackground(originalColor);
			}
			
			// 当前正在掷骰子的玩家名显示变色

			// TODO 0904 18.04
			
			int oldPlayerBombLoc = player[flag-1].getLocation();
			player[flag-1].moveForward(steps);
			int newPlayerBombLoc = player[flag-1].getLocation();
			if((oldPlayerBombLoc+ steps) != 14 && newPlayerBombLoc==14) {	// 说明踩到炸弹了，炸到医院，没有按loc+steps来走
				String oldText = labelList.get(oldPlayerBombLoc+ steps).getText();
				oldText = oldText.replaceFirst("/B", "");
				labelList.get(oldPlayerBombLoc+ steps).setText(oldText);
				showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   不幸踩到炸弹  " 
						+ "  被送进医院，默哀。");
			} else if((oldPlayerBombLoc+ steps) != newPlayerBombLoc) {
				String oldText = labelList.get(newPlayerBombLoc).getText();
				oldText = oldText.replaceFirst("/R", "");
				labelList.get(newPlayerBombLoc).setText(oldText);
				showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   被路障拦截，停在  " 
						+ newPlayerBombLoc +"  号位。");
			} else {
				
			}
			
			
			labelList.get(player[flag-1].getLocation()).setBackground(Color.YELLOW);
			MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue().add(Color.YELLOW);
			
			// 设置土地的显示文本
			String originalText = labelList.get(player[flag-1].getLocation()).getText();
			originalText = originalText.replaceAll("<html>", "");
			originalText = originalText.replaceAll("</html>", "");
			// "/"出现的次数，两次时要加<br>
			int showTimes = MainT.getCounts(originalText, "/");
			String newText = "";
			if(showTimes>=2) {
				newText = "<html>" + originalText + "<br>/2" + "</html>";
			} else {
				newText = "<html>" + originalText + "/2" + "</html>";
			}
			labelList.get(player[flag-1].getLocation()).setText(newText);		// 设置新的字符串

			int myLand = player[flag-1].checkIfbuyLand();
			if(myLand<100) {			// 购买了土地，就更改土地的边框，为当前的玩家颜色
				labelList.get(myLand).setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
				String originalText1 = labelList.get(player[flag-1].getLocation()).getText();
				originalText1 = originalText1.replaceAll("<html>", "<html>Z/");
				labelList.get(player[flag-1].getLocation()).setText(originalText1);			// 设置0级空地
				
				showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   花费了  " 
						+ Location.getLandMoney(player[flag-1].getLocation()) + "  元购买了一块地。");
				
			} else if(myLand == 888) {	// 代号888代表升级了房屋
				String originalText1 = labelList.get(player[flag-1].getLocation()).getText();
				int rate = player[flag-1].getHouseMap().get(player[flag-1].getLocation()).getHouseRate();
				String indexStr = "";
				if(rate==1) {		// 判断当前的房屋等级是多少， Y X W
					indexStr = "<html>Y";
					originalText1 = originalText1.replaceAll("<html>Z", indexStr);
				} else if(rate==2) {		// 房屋为X： 2级
					indexStr = "<html>X";
					originalText1 = originalText1.replaceAll("<html>Y", indexStr);
				} else if(rate==3) {		// 房屋为最高级 W
					indexStr = "<html>W";
					originalText1 = originalText1.replaceAll("<html>X", indexStr);
				}
				labelList.get(player[flag-1].getLocation()).setText(originalText1);		// 设置某级房屋标识

				showCashInfo.setText("公告: 玩家     " + player[flag-1].getPlayerName() + "     花费了  " 
						+ Location.getLandMoney(player[flag-1].getLocation()) + "  元将房屋升级到了  " 
						+ rate + "  级！");
				
			} else if(myLand == 555) {		// 代号555代表玩家付不起 过路费，被出局
				showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   穷到付不起路费，" 
						+ "  被迫出局，终止游戏");
				
				
				// 清楚所有标识边框,及房屋文本
				for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
					
					 int loc1 = entry.getValue().getLocation();		// 得到每个房屋的location
					 int rate = entry.getValue().getHouseRate();
					 labelList.get(loc1).setBorder(originalBorder);
					 String oldText11 = labelList.get(loc1).getText();

					 String regex = "";
					 if(rate==0) {
						 regex = "Z/";
					 } else if(rate == 1) {
						 regex = "Y/";
					 } else if(rate==2) {
						 regex = "X/";
					 } else {
						 regex = "W/";
					 }
					 oldText11 = oldText11.replaceAll(regex, "");

					 labelList.get(loc1).setText(oldText11);
				}
				
				player[flag-1].destroy();		// 清除
				
				Queue<Color> hereColorQue = MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue();
				if(hereColorQue.size()>=2) {
					hereColorQue.remove();
					labelList.get(player[flag-1].getLocation()).setBackground(hereColorQue.peek());
				} else {
					hereColorQue.remove();
					labelList.get(player[flag-1].getLocation()).setBackground(originalColor);
				}
				// 清除文本
				String hereUse1Text = labelList.get(player[flag-1].getLocation()).getText();
				labelList.get(player[flag-1].getLocation()).setText(hereUse1Text.replaceAll("/2", ""));


			} else if(myLand ==556) {
				House othersHhouse = MainT.allHouseMap.get(player[flag-1].getLocation());
				showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   付给   "
						+ othersHhouse.getHouseOwner() +"   路费   "
						+ othersHhouse.getPassByCost() +"  元。");
				
			}
			
			refreshCash(player);
			reshowPlayerInfo(player);
			showPlayerInfo[10].setBackground(originalColor);
		}
		}
		
			// 轮到第三个玩家
			if(flag==3) {
				
				/**
				 *  以下为玩家轮空时的状态
				 */
				if(player[flag-1].getState() == 3 || player[flag-1].getState() == 2 || player[flag-1].getState() == 5) {		// 玩家为轮空状态
					//  玩家已经出局
					if(player[flag-1].getState() == 5) {
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   已经出局                                  ");
					} else {
						if(player[flag-1].getPauseStatePriRest() != 0) {		// 因为监狱轮空
							int prisonTemp = player[flag-1].getPauseStatePriRest();
							player[flag-1].setPauseStatePriRest(--prisonTemp);
							if(prisonTemp == 0) {			// 监狱轮空2轮后，状态恢复
								player[flag-1].setState(1);
								for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
									entry.getValue().setHouseOwnerState(1);
								}
							}
							// 玩家因为进医院轮空
						} else {
							int hospitalTemp = player[flag-1].getPauseStateHospRest();
							player[flag-1].setPauseStateHospRest(--hospitalTemp);
							if(hospitalTemp == 0) {
								player[flag-1].setState(1);
								for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
									entry.getValue().setHouseOwnerState(1);
								}
							}
						}
					}
					
					
				} else {
					
					showCashInfo.setText("公告: " + " 暂无新消息                                                                  ");
			
					// 走之前的字符串
					String beforeLeaveText = labelList.get(player[flag-1].getLocation()).getText();
					// 走之后要删除当前用户的字符串
					String afterLeaveText = beforeLeaveText.replaceFirst("/3", "");
					labelList.get(player[flag-1].getLocation()).setText(afterLeaveText);
		
					Queue<Color> colorQueue = MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue(); //.pop();
					if(colorQueue.size()>=2) {		// 不是默认色size=1，且不是房主色size=2，

//					// 设置玩家进来之前的颜色
						
							labelList.get(player[flag-1].getLocation()).setBackground(MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue().peek());
						
					}  else {
						if(colorQueue.size()!=0) {
							colorQueue.remove();
						}
						labelList.get(player[flag-1].getLocation()).setBackground(originalColor);
					}
					// 当前正在掷骰子的玩家名显示变色

					int oldPlayerBombLoc = player[flag-1].getLocation();
					player[flag-1].moveForward(steps);
					int newPlayerBombLoc = player[flag-1].getLocation();
					if((oldPlayerBombLoc+ steps) != 14 && newPlayerBombLoc==14) {	// 说明踩到炸弹了，炸到医院，没有按loc+steps来走
						String oldText = labelList.get(oldPlayerBombLoc+ steps).getText();
						oldText = oldText.replaceFirst("/B", "");
						labelList.get(oldPlayerBombLoc+ steps).setText(oldText);
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   不幸踩到炸弹  " 
								+ "  被送进医院，默哀。");
					} else if((oldPlayerBombLoc+ steps) != newPlayerBombLoc) {
						String oldText = labelList.get(newPlayerBombLoc).getText();
						oldText = oldText.replaceFirst("/R", "");
						labelList.get(newPlayerBombLoc).setText(oldText);
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   被路障拦截，停在  " 
								+ newPlayerBombLoc +"  号位。");
					} else {
						
					}

					
					labelList.get(player[flag-1].getLocation()).setBackground(Color.GREEN);
					MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue().add(Color.GREEN);
			
					String originalText = labelList.get(player[flag-1].getLocation()).getText();

					originalText = originalText.replaceAll("<html>", "");
					originalText = originalText.replaceAll("</html>", "");
					// "/"出现的次数，两次时要加<br>
					int showTimes = MainT.getCounts(originalText, "/");
					String newText = "";
					if(showTimes>=2) {
						newText = "<html>" + originalText + "<br>/3" + "</html>";
					} else {
						newText = "<html>" + originalText + "/3" + "</html>";
					}

					labelList.get(player[flag-1].getLocation()).setText(newText);		// 设置新的字符串
					
//					player[flag-1].checkIfbuyLand();
					int myLand = player[flag-1].checkIfbuyLand();
					if(myLand < 100) {			// 购买了土地，就更改土地的边框，为当前的玩家颜色
						labelList.get(myLand).setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
						String originalText1 = labelList.get(player[flag-1].getLocation()).getText();
						originalText1 = originalText1.replaceAll("<html>", "<html>Z/");
						labelList.get(player[flag-1].getLocation()).setText(originalText1);
						
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   花费了  " 
								+ Location.getLandMoney(player[flag-1].getLocation()) + "  元购买了一块地。");
						
						
					} else if(myLand == 888) {	// 代号888代表升级了房屋
						String originalText1 = labelList.get(player[flag-1].getLocation()).getText();
						int rate = player[flag-1].getHouseMap().get(player[flag-1].getLocation()).getHouseRate();
						String indexStr = "";
						if(rate==1) {		// 判断当前的房屋等级是多少， Y X W
							indexStr = "<html>Y";
							originalText1 = originalText1.replaceAll("<html>Z", indexStr);
						} else if(rate==2) {		// 房屋为X： 2级
							indexStr = "<html>X";
							originalText1 = originalText1.replaceAll("<html>Y", indexStr);
						} else if(rate==3) {		// 房屋为最高级 W
							indexStr = "<html>W";
							originalText1 = originalText1.replaceAll("<html>X", indexStr);
						}
						labelList.get(player[flag-1].getLocation()).setText(originalText1);		// 设置某级房屋标识

						showCashInfo.setText("公告: 玩家     " + player[flag-1].getPlayerName() + "     花费了  " 
								+ Location.getLandMoney(player[flag-1].getLocation()) + "  元将房屋升级到了  " 
								+ rate + "  级！");
						
					} else if(myLand == 555) {		// 代号555代表玩家付不起 过路费，被出局
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   穷到付不起路费，" 
								+ "  被迫出局，终止游戏");

						// 清楚所有标识边框,及房屋文本
						for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
							
							 int loc1 = entry.getValue().getLocation();		// 得到每个房屋的location
							 int rate = entry.getValue().getHouseRate();
							 labelList.get(loc1).setBorder(originalBorder);
							 String oldText11 = labelList.get(loc1).getText();
							 System.out.println("开始清除标签：" + oldText11);
							 String regex = "";
							 if(rate==0) {
								 regex = "Z/";
							 } else if(rate == 1) {
								 regex = "Y/";
							 } else if(rate==2) {
								 regex = "X/";
							 } else {
								 regex = "W/";
							 }
							 oldText11 = oldText11.replaceAll(regex, "");
							 System.out.println("清除之后的标签：" + oldText11);
							 labelList.get(loc1).setText(oldText11);
						}
						player[flag-1].destroy();
						Queue<Color> hereColorQue = MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue();
						if(hereColorQue.size()>=2) {
							hereColorQue.remove();
							labelList.get(player[flag-1].getLocation()).setBackground(hereColorQue.peek());
						} else {
							hereColorQue.remove();
							labelList.get(player[flag-1].getLocation()).setBackground(originalColor);
						}
						// 清除文本
						String hereUse1Text = labelList.get(player[flag-1].getLocation()).getText();
						labelList.get(player[flag-1].getLocation()).setText(hereUse1Text.replaceAll("/3", ""));

					} else if(myLand ==556) {
						House othersHhouse = MainT.allHouseMap.get(player[flag-1].getLocation());

						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   付给   "
								+ othersHhouse.getHouseOwner() +"   路费   "
								+ othersHhouse.getPassByCost() +"  元。");
						
					}
					
					refreshCash(player);
					reshowPlayerInfo(player);
					
					// 设置当前玩家恢复暗色，下一个玩家高亮
					showPlayerInfo[20].setBackground(originalColor);                                                            ");
				}
			}
			
			// 玩家4
			if(flag==4) {
				
				/**
				 *  以下为玩家轮空时的状态
				 */
				
				if(player[flag-1].getState() == 3 || player[flag-1].getState() == 2 || player[flag-1].getState() == 5) {		// 玩家为轮空状态
					
					// 玩家已经出局
					if(player[flag-1].getState() == 5) {
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   已经出局                                  ");
					} else {
						if(player[flag-1].getPauseStatePriRest() != 0) {		// 因为监狱轮空，在监狱，则医院的天肯定为0
							int prisonTemp = player[flag-1].getPauseStatePriRest();
							player[flag-1].setPauseStatePriRest(--prisonTemp);
							if(prisonTemp == 0) {			// 监狱轮空2轮后，状态恢复
								player[flag-1].setState(1);
								for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
									entry.getValue().setHouseOwnerState(1);
								}
							}
							// 玩家因为进医院轮空，因为一个玩家不可能同时进医院和监狱
						} else {
							int hospitalTemp = player[flag-1].getPauseStateHospRest();
							player[flag-1].setPauseStateHospRest(--hospitalTemp);
							if(hospitalTemp == 0) {
								player[flag-1].setState(1);
								for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
									entry.getValue().setHouseOwnerState(1);
								}
							}
						}
					}
					
				} else {
					
					showCashInfo.setText("公告: " + " 暂无新消息                                                                  ");
				
					// 走之前的字符串
					String beforeLeaveText = labelList.get(player[flag-1].getLocation()).getText();
					// 走之后要删除当前用户的字符串
					String afterLeaveText = beforeLeaveText.replaceFirst("/4", "");
					labelList.get(player[flag-1].getLocation()).setText(afterLeaveText);
					
					
					Queue<Color> colorQueue = MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue(); //.pop();
					if(colorQueue.size()>=2) {		// 不是默认色size=1，
						colorQueue.remove();

//					// 设置玩家进来之前的颜色
						
							labelList.get(player[flag-1].getLocation()).setBackground(MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue().peek());
						
					} else {
						if(colorQueue.size()!=0) {
							colorQueue.remove();
						}
						labelList.get(player[flag-1].getLocation()).setBackground(originalColor);
					}
					
					
					// 当前正在掷骰子的玩家名显示变色

					int oldPlayerBombLoc = player[flag-1].getLocation();
					player[flag-1].moveForward(steps);
					int newPlayerBombLoc = player[flag-1].getLocation();
					if((oldPlayerBombLoc+ steps) != 14 && newPlayerBombLoc==14) {	// 说明踩到炸弹了，炸到医院，没有按loc+steps来走
						String oldText = labelList.get(oldPlayerBombLoc+ steps).getText();
						oldText = oldText.replaceFirst("/B", "");
						labelList.get(oldPlayerBombLoc+ steps).setText(oldText);
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   不幸踩到炸弹  " 
								+ "  被送进医院，默哀。");
					}else if((oldPlayerBombLoc+ steps) != newPlayerBombLoc) {
						String oldText = labelList.get(newPlayerBombLoc).getText();
						oldText = oldText.replaceFirst("/R", "");
						labelList.get(newPlayerBombLoc).setText(oldText);
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   被路障拦截，停在  " 
								+ newPlayerBombLoc +"  号位。");
					} else {
					}

					labelList.get(player[flag-1].getLocation()).setBackground(Color.GRAY);
					MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue().add(Color.GRAY);

					// 设置地块的显示
					String originalText = labelList.get(player[flag-1].getLocation()).getText();

					originalText = originalText.replaceAll("<html>", "");
					originalText = originalText.replaceAll("</html>", "");
					// "/"出现的次数，两次时要加<br>
					int showTimes = MainT.getCounts(originalText, "/");
					String newText = "";
					if(showTimes>=2) {
						newText = "<html>" + originalText + "<br>/4" + "</html>";
					} else {
						newText = "<html>" + originalText + "/4" + "</html>";
					}

					labelList.get(player[flag-1].getLocation()).setText(newText);		// 设置新的字符串

					int myLand = player[flag-1].checkIfbuyLand();
					if(myLand<100) {			// 购买了土地，就更改土地的边框，为当前的玩家颜色
						labelList.get(myLand).setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
						String originalText1 = labelList.get(player[flag-1].getLocation()).getText();
						originalText1 = originalText1.replaceAll("<html>", "<html>Z/");
						labelList.get(player[flag-1].getLocation()).setText(originalText1);		// 设置0级空地标识
						
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   花费了  " 
								+ Location.getLandMoney(player[flag-1].getLocation()) + "  元购买了一块地。");
						
						
					} else if(myLand == 888) {	// 代号888代表升级了房屋
						String originalText1 = labelList.get(player[flag-1].getLocation()).getText();
						int rate = player[flag-1].getHouseMap().get(player[flag-1].getLocation()).getHouseRate();
						String indexStr = "";
						if(rate==1) {		// 判断当前的房屋等级是多少， Y X W
							indexStr = "<html>Y";
							originalText1 = originalText1.replaceAll("<html>Z", indexStr);
						} else if(rate==2) {		// 房屋为X： 2级
							indexStr = "<html>X";
							originalText1 = originalText1.replaceAll("<html>Y", indexStr);
						} else if(rate==3) {		// 房屋为最高级 W
							indexStr = "<html>W";
							originalText1 = originalText1.replaceAll("<html>X", indexStr);
						}

						labelList.get(player[flag-1].getLocation()).setText(originalText1);		// 设置某级房屋标识
						
						showCashInfo.setText("公告: 玩家     " + player[flag-1].getPlayerName() + "     花费了  " 
								+ Location.getLandMoney(player[flag-1].getLocation()) + "  元将房屋升级到了  " 
								+ rate + "  级！");
						
					} else if(myLand == 555) {		// 代号555代表玩家付不起 过路费，被出局
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   穷到付不起路费，" 
								+ "  被迫出局，终止游戏");
						
						
						// 清楚所有标识边框,及房屋文本
						for(Entry<Integer,House> entry : player[flag-1].getHouseMap().entrySet()) {
							
							 int loc1 = entry.getValue().getLocation();		// 得到每个房屋的location
							 int rate = entry.getValue().getHouseRate();
							 labelList.get(loc1).setBorder(originalBorder);
							 String oldText11 = labelList.get(loc1).getText();

							 String regex = "";
							 if(rate==0) {
								 regex = "Z/";
							 } else if(rate == 1) {
								 regex = "Y/";
							 } else if(rate==2) {
								 regex = "X/";
							 } else {
								 regex = "W/";
							 }
							 oldText11 = oldText11.replaceAll(regex, "");

							 labelList.get(loc1).setText(oldText11);
						}
						player[flag-1].destroy();
						
						Queue<Color> hereColorQue = MainT.markLocation.get(player[flag-1].getLocation()).getColorQueue();
						if(hereColorQue.size()>=2) {
							hereColorQue.remove();
							labelList.get(player[flag-1].getLocation()).setBackground(hereColorQue.peek());
						} else {
							hereColorQue.remove();
							labelList.get(player[flag-1].getLocation()).setBackground(originalColor);
						}
						// 清除文本
						String hereUse1Text = labelList.get(player[flag-1].getLocation()).getText();
						labelList.get(player[flag-1].getLocation()).setText(hereUse1Text.replaceAll("/4", ""));

					} else if(myLand ==556) {
						House othersHhouse = MainT.allHouseMap.get(player[flag-1].getLocation());
						showCashInfo.setText("公告: 玩家    " + player[flag-1].getPlayerName() + "   付给   "
								+ othersHhouse.getHouseOwner() +"   路费   "
								+ othersHhouse.getPassByCost() +"  元。");

					} else {
					}
					System.out.println("4: " + labelList.get(player[flag-1].getLocation()).getText());
					refreshCash(player);
					reshowPlayerInfo(player);
					showPlayerInfo[30].setBackground(originalColor);
				}
			}
		
			refreshCash(player);
			reshowPlayerInfo(player);

			//  当一个玩家结束操作后，恢复他的高亮显示
			if(flag==1) {
				showPlayerInfo[0].setBackground(originalColor);
			} else if(flag==2) {
				showPlayerInfo[10].setBackground(originalColor);
			} else if(flag==3) {
				showPlayerInfo[20].setBackground(originalColor);
			} else if(flag ==4 ) {
				showPlayerInfo[30].setBackground(originalColor);
			} else {
				
			}
				// 设置高亮
			getNextTextfield(flag).setBackground(Color.PINK);
	
		/**
		 * 根据实际玩家人数
		 * 制造2或者3或者4人轮转效果
		 */
		if(player.length==2) {
			flag = (flag%2==0) ? 1 : 2;
		} else if (player.length==3) {
			if(flag==3) {
				flag=1;
			} else {
				flag++;
			}
		} else {		// 有4个人
			if(flag==4) {
				flag=1;
			} else {
				flag++;
			}
		}
		
		/////////////////////////////////////////////////////////////////////////end
	}


	public void setPlayers(Player[] players) {
		this.player = players;
	}
	
	public void reshowPlayerInfo(Player[] players) {
		for(int i = 0; i<10; i++) {
			showPlayerInfo[i].setText(player[0].getPlayerData().get(i));
			showPlayerInfo[i+10].setText(player[1].getPlayerData().get(i));
		}
	
		if(players.length>=3) {
			for(int i = 0; i<10; i++) {
				showPlayerInfo[i+20].setText(player[2].getPlayerData().get(i));
			}
		}
		if(players.length==4) {
			for(int i = 0; i<10; i++) {
				showPlayerInfo[i+20].setText(player[2].getPlayerData().get(i));
				showPlayerInfo[i+30].setText(player[3].getPlayerData().get(i));
			}
		}
	}

	/**
	 *  将下一个玩家高亮
	 * @param houseMap
	 * @return
	 */

	public JTextField getNextTextfield(int index) {
		int num = player.length;		// 当前玩家数
		if(num==2) {
			if(index==1) {	
					return showPlayerInfo[10];
			} else {	// index ==2
					return showPlayerInfo[0];
			}
		} else if(num==3) {
			if(index==1) {
				return showPlayerInfo[10];
			} else if(index==2) {
				return showPlayerInfo[20];
			} else {
				return showPlayerInfo[0];
			}
		} else {
			if(index==1) {
				return showPlayerInfo[10];
			}else if(index==2) {
				return showPlayerInfo[20];
			}else if(index==3) {
				return showPlayerInfo[30];
			}else if(index==4) {
				return showPlayerInfo[0];
			}
		}
		return null;
	}
	
	
	public int getMoneyFromHouse(Map<Integer,House> houseMap) {
		int moneyFromHouse = 0;
		// 累和之后将其归零
		for(Entry<Integer,House> entry : houseMap.entrySet()) {
			moneyFromHouse += entry.getValue().getMoneyFromHouse();
			entry.getValue().setMoneyFromHouse(0);
		}
		return moneyFromHouse;
	}
	
	public void refreshCash(Player[] player) {
		for(int i =0;i<player.length;i++) {
			int moneyFromHouse = this.getMoneyFromHouse(player[i].getHouseMap());
			player[i].setCash(player[i].getCash() + moneyFromHouse);
		}
	}
	
	public int getFlag() {
		return flag;
	}

}
