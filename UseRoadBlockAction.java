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
import basicFrame.BFrame;

public class UseRoadBlockAction implements ActionListener{

	private BFrame basicFrame;
	static Player[] player;
	JTextField[] showPlayerInfo;
	
	public UseRoadBlockAction(BFrame frame) {
		this.basicFrame = frame;
		
		}
		public UseRoadBlockAction()
		{
			
		}
	
		public void actionPerformed(ActionEvent e) {
		
			ButtonDiceAction btnDA =new ButtonDiceAction();
			int flag = btnDA.getFlag();
			
			if(flag==1 && player[flag-1].getState()!=3)
			{
			    
			    List<JButton> labelList = basicFrame.getButtonList();
			    
			    if(player[flag-1].getRoadBlock()>0)
			    {
			    
			    if(Integer.parseInt(basicFrame.getBlockLocation().getText().trim())>10
			    	||Integer.parseInt(basicFrame.getBlockLocation().getText().trim())<-10)
			    {
			    	JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入(-10,10)以内的数字");
			    }
			    
			    
			    else
			    {
			    int loclab=Integer.parseInt(basicFrame.getBlockLocation().getText().trim());//获得label中的String值并转化成int型
			    
			    JOptionPane.showMessageDialog(null,basicFrame.getBlockLocation().getText().trim()+player[flag-1].getPlayerName());
			    
			    int locply= player[flag-1].getLocation();//获得当前玩家的地址值
			    
			    int loclast=loclab+locply;
			    
			    if(loclast<0)
			    loclast+=70;
			    
			   
			    Location loc3 = MainT.markLocation.get(loclast);//获得要安置炸弹的位置的对象
				
				if(loc3.getIsToolState()=="isBomb")//判断该位置的道具状态
				{
					JOptionPane.showMessageDialog(null, "此处有炸弹，不能添加路障！");
					
				}
				else if (loc3.getIsToolState()=="isRoadBlock")
				{
					JOptionPane.showMessageDialog(null, "此处有路障，不能添加路障！");
					
				} 
				else if (loc3.getIsToolState()=="isForbid")
				{
					JOptionPane.showMessageDialog(null, "此处属于公共场所，不能添加路障！");
					
				} 
				else 
				{
					loc3.setIsToolState("isRoadBlock");
					int NumBolck = player[flag-1].getRoadBlock()-1;//获得玩家的路障数
					player[flag-1].setRoadBlock(NumBolck);//将玩家的路障数减一
					
				
                    String oldText = labelList.get(loc3.getLocation()).getText();
					
					labelList.get(loc3.getLocation()).setText(oldText+"/R");//表示该位置路障，并换行
				     
				    basicFrame.getBlockLocation().setText("          ");}
			}
			    }
				
			    
			    else
			    {
			    	JOptionPane.showMessageDialog(null, player[flag-1].getPlayerName()+"没有路障！");
			    }
		}
			
			
			if(flag==2 &&  player[flag-1].getState()!=3)
			{
				
				

			    List<JButton> labelList = basicFrame.getButtonList();
			    
			    if(player[flag-1].getRoadBlock()>0)
			    {
			    
			    if(Integer.parseInt(basicFrame.getBlockLocation().getText().trim())>10
			    	||Integer.parseInt(basicFrame.getBlockLocation().getText().trim())<-10)
			    {
			    	JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入(-10,10)以内的数字");
			    }
			    
			    
			    else
			    {
			    int loclab=Integer.parseInt(basicFrame.getBlockLocation().getText().trim());//获得label中的String值并转化成int型
			    
			    JOptionPane.showMessageDialog(null,basicFrame.getBlockLocation().getText().trim()+player[flag-1].getPlayerName());
			    
			    int locply= player[flag-1].getLocation();//获得当前玩家的地址值
			    
			    int loclast=loclab+locply;
			    if(loclast<0)
				    loclast+=70;
			   
			    Location loc3 = MainT.markLocation.get(loclast);//获得要安置炸弹的位置的对象
				
				if(loc3.getIsToolState()=="isBomb")//判断该位置的道具状态
				{
					JOptionPane.showMessageDialog(null, "此处有炸弹，不能添加路障！");
					
				}
				else if (loc3.getIsToolState()=="isRoadBlock")
				{
					JOptionPane.showMessageDialog(null, "此处有路障，不能添加路障！");
					
				} 
				else if (loc3.getIsToolState()=="isForbid")
				{
					JOptionPane.showMessageDialog(null, "此处属于公共场所，不能添加路障！");
					
				} 
				else 
				{
					loc3.setIsToolState("isRoadBlock");
					int NumBolck = player[flag-1].getRoadBlock()-1;//获得玩家的路障数
					player[flag-1].setRoadBlock(NumBolck);//将玩家的路障数减一
					
				
                    String oldText = labelList.get(loc3.getLocation()).getText();
					
					labelList.get(loc3.getLocation()).setText(oldText+"/R");//表示该位置路障，并换行
				     
				    basicFrame.getBlockLocation().setText("          ");}
			}
			    }
				
			    
			    else
			    {
			    	JOptionPane.showMessageDialog(null, player[flag-1].getPlayerName()+"没有路障！");
			    }
				
				
				
				
			}
			
			
			if(player.length>=3) {
				if(flag==3) {
					if(player[flag-1].getState()!=3) {
						
						List<JButton> labelList = basicFrame.getButtonList();
					    
					    if(player[flag-1].getRoadBlock()>0)
					    {
					    
					    if(Integer.parseInt(basicFrame.getBlockLocation().getText().trim())>10
					    	||Integer.parseInt(basicFrame.getBlockLocation().getText().trim())<-10)
					    {
					    	JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入(-10,10)以内的数字");
					    }
					    
					    
					    else
					    {
					    int loclab=Integer.parseInt(basicFrame.getBlockLocation().getText().trim());//获得label中的String值并转化成int型
					    
					    JOptionPane.showMessageDialog(null,basicFrame.getBlockLocation().getText().trim()+player[flag-1].getPlayerName());
					    
					    int locply= player[flag-1].getLocation();//获得当前玩家的地址值
					    
					    int loclast=loclab+locply;
					    
					    if(loclast<0)
						    loclast+=70;
					   
					    Location loc3 = MainT.markLocation.get(loclast);//获得要安置炸弹的位置的对象
						
						if(loc3.getIsToolState()=="isBomb")//判断该位置的道具状态
						{
							JOptionPane.showMessageDialog(null, "此处有炸弹，不能添加路障！");
							
						}
						else if (loc3.getIsToolState()=="isRoadBlock")
						{
							JOptionPane.showMessageDialog(null, "此处有路障，不能添加路障！");
							
						} 
						else if (loc3.getIsToolState()=="isForbid")
						{
							JOptionPane.showMessageDialog(null, "此处属于公共场所，不能添加路障！");
							
						} 
						else 
						{
							loc3.setIsToolState("isRoadBlock");
							int NumBolck = player[flag-1].getRoadBlock()-1;//获得玩家的路障数
							player[flag-1].setRoadBlock(NumBolck);//将玩家的路障数减一
							
						
							String oldText = labelList.get(loc3.getLocation()).getText();
							
							labelList.get(loc3.getLocation()).setText(oldText+"/R");//表示该位置路障，并换行
						     
						    basicFrame.getBlockLocation().setText("          ");}
					}
					    }
						
					    
					    else
					    {
					    	JOptionPane.showMessageDialog(null, player[flag-1].getPlayerName()+"没有路障！");
					    }
						
					}
				}
				
				
				
				if(flag==4) {
					if(player[flag-1].getState()!=3) {
						List<JButton> labelList = basicFrame.getButtonList();
					    
					    if(player[flag-1].getRoadBlock()>0)
					    {
					    
					    if(Integer.parseInt(basicFrame.getBlockLocation().getText().trim())>10
					    	||Integer.parseInt(basicFrame.getBlockLocation().getText().trim())<-10)
					    {
					    	JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入(-10,10)以内的数字");
					    }
					    
					    
					    else
					    {
					    int loclab=Integer.parseInt(basicFrame.getBlockLocation().getText().trim());//获得label中的String值并转化成int型
					    
					    JOptionPane.showMessageDialog(null,basicFrame.getBlockLocation().getText().trim()+player[flag-1].getPlayerName());
					    
					    int locply= player[flag-1].getLocation();//获得当前玩家的地址值
					    
					    int loclast=loclab+locply;
					    
					    if(loclast<0)
						    loclast+=70;
					   
					    Location loc3 = MainT.markLocation.get(loclast);//获得要安置炸弹的位置的对象
						
						if(loc3.getIsToolState()=="isBomb")//判断该位置的道具状态
						{
							JOptionPane.showMessageDialog(null, "此处有炸弹，不能添加路障！");
							
						}
						else if (loc3.getIsToolState()=="isRoadBlock")
						{
							JOptionPane.showMessageDialog(null, "此处有路障，不能添加路障！");
							
						} 
						else if (loc3.getIsToolState()=="isForbid")
						{
							JOptionPane.showMessageDialog(null, "此处属于公共场所，不能添加路障！");
							
						} 
						else 
						{
							loc3.setIsToolState("isRoadBlock");
							int NumBolck = player[flag-1].getRoadBlock()-1;//获得玩家的路障数
							player[flag-1].setRoadBlock(NumBolck);//将玩家的路障数减一
							
						
							String oldText = labelList.get(loc3.getLocation()).getText();
							
							labelList.get(loc3.getLocation()).setText(oldText+"/R");//表示该位置路障，并换行
						     
						    basicFrame.getBlockLocation().setText("          ");}
					}
					    }
						
					    
					    else
					    {
					    	JOptionPane.showMessageDialog(null, player[flag-1].getPlayerName()+"没有路障！");
					    }
						
					}
					}
				
					}
			
		
			/**
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
			} else {
				if(flag==4) {
					flag=1;
				} else {
					flag++;
				}
			}
			

			
			
			
			
			
			
			
		}
		
		
		
		
		
}
