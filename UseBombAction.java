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

public class UseBombAction implements ActionListener {

	private BFrame basicFrame;
	static Player[] player;
	JTextField[] showPlayerInfo;
	
	
	public UseBombAction(BFrame frame) {
	this.basicFrame = frame;
	
	}
	public UseBombAction()
	{
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ButtonDiceAction btnDA =new ButtonDiceAction();
		int flag = btnDA.getFlag();
		
		if(flag==1 && player[0].getState()!=3)
		{
		    
		    List<JButton> labelList = basicFrame.getButtonList();
		    
		    if(player[0].getBomb()>0)
		    { 
		    if((!MainT.isNumeric((basicFrame.getBombLocation().getText().trim())))) 
		    {
				JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入数字");
			}
		    else if(Integer.parseInt(basicFrame.getBombLocation().getText().trim())>10)
		    {
		    	JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入10以内的数字");
		    }
		    
		    else
		    {
		    int loclab=Integer.parseInt(basicFrame.getBombLocation().getText().trim());//获得label中的String值并转化成int型
		    
		    JOptionPane.showMessageDialog(null,basicFrame.getBombLocation().getText().trim()+player[0].getPlayerName());
		    
		    int locply= player[0].getLocation();//获得当前玩家的地址值
		
		    Location loc3 = MainT.markLocation.get(locply+loclab);//获得要安置炸弹的位置的对象
			
		   
		    
		   
			if(loc3.getIsToolState()=="isBomb")//判断该位置的道具状态
			{
				JOptionPane.showMessageDialog(null, "此处有炸弹，不能添加炸弹！");
				
			}
			else if (loc3.getIsToolState()=="isRoadBlock")
			{
				JOptionPane.showMessageDialog(null, "此处有路障，不能添加炸弹！");
				
			} 
			else if (loc3.getIsToolState()=="isForbid")
			{
				JOptionPane.showMessageDialog(null, "此处属于公共场所，不能添加炸弹！");
				
			} 
			else 
			{
				loc3.setIsToolState("isBomb");
				int NumBomb = player[0].getBomb()-1;//获得玩家的炸弹数
				player[0].setBomb(NumBomb);//将玩家的炸弹数减一
				
			
				labelList.get(loc3.getLocation()).setText("<html>"+"<br>"+" B "+"<html>");//表示该位置有炸弹，并换行
			}
		}
			
			for(int i =0;i<70;i++) {
				System.out.println(i + ":" + MainT.markLocation.get(i).getIsHouseState() + "," +  MainT.markLocation.get(i).getIsToolState());
				
			}
		
		
	}
		    
		    else
		    {
		    	JOptionPane.showMessageDialog(null, player[0].getPlayerName()+"没有炸弹！");
		    }
		}
		
		
		
		if(flag==2 &&  player[flag-1].getState()!=3)
		{
			List<JButton> labelList = basicFrame.getButtonList();
			
			if(player[flag-1].getBomb()>0)
			{
			if((!MainT.isNumeric((basicFrame.getBombLocation().getText().trim())))) 
		    {
				JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入数字");
			}
			
			else if(Integer.parseInt(basicFrame.getBombLocation().getText().trim())>10)
		    {
		    	JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入10以内的数字");
		    }
			
			else
			{
		    int loclab=Integer.parseInt(basicFrame.getBombLocation().getText().trim());//获得label中的String值并转化成int型
		    
		    JOptionPane.showMessageDialog(null,basicFrame.getBombLocation().getText().trim()+player[flag-1].getPlayerName());
		    
		    int locply= player[flag-1].getLocation();//获得当前玩家的地址值
		
		    Location loc3 = MainT.markLocation.get(locply+loclab);//获得要安置炸弹的位置的对象
			
	
		    
		   
			if(loc3.getIsToolState()=="isBomb")//判断该位置的道具状态
			{
				JOptionPane.showMessageDialog(null, "此处有炸弹，不能添加炸弹！");
				
			}
			else if (loc3.getIsToolState()=="isRoadBlock")
			{
				JOptionPane.showMessageDialog(null, "此处有路障，不能添加炸弹！");
				
			} 
			else if (loc3.getIsToolState()=="isForbid")
			{
				JOptionPane.showMessageDialog(null, "此处属于公共场所，不能添加炸弹！");
				
			} 
			else 
			{
				loc3.setIsToolState("isBomb");
				int NumBomb = player[flag-1].getBomb()-1;//获得玩家的炸弹数
				player[flag-1].setBomb(NumBomb);//将玩家的炸弹数减一
				labelList.get(loc3.getLocation()).setText("<html>"+"<br>"+" B "+"<html>");//表示该位置有炸弹，并换行
			}
		}
		}
			
			 else
			    {
			    	JOptionPane.showMessageDialog(null, player[flag-1].getPlayerName()+"没有炸弹！");
			    }
		}
		
		
		if(player.length>=3) {
			if(flag==3) {
				if(player[flag-1].getState()!=3) {
					
					List<JButton> labelList = basicFrame.getButtonList();
					if(player[flag-1].getBomb()>0)
					{
					if((!MainT.isNumeric((basicFrame.getBombLocation().getText().trim())))) 
				    {
						JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入数字");
					}
					
					else if(Integer.parseInt(basicFrame.getBombLocation().getText().trim())>10)
				    {
				    	JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入10以内的数字");
				    }
					
					else
					{	
				    int loclab=Integer.parseInt(basicFrame.getBombLocation().getText().trim());//获得label中的String值并转化成int型
				    
				    JOptionPane.showMessageDialog(null,basicFrame.getBombLocation().getText().trim()+player[flag-1].getPlayerName());
				    
				    int locply= player[flag-1].getLocation();//获得当前玩家的地址值
				
				    Location loc3 = MainT.markLocation.get(locply+loclab);//获得要安置炸弹的位置的对象
					
				  
				    
				   
					if(loc3.getIsToolState()=="isBomb")//判断该位置的道具状态
					{
						JOptionPane.showMessageDialog(null, "此处有炸弹，不能添加炸弹！");
						
					}
					else if (loc3.getIsToolState()=="isRoadBlock")
					{
						JOptionPane.showMessageDialog(null, "此处有路障，不能添加炸弹！");
						
					} 
					else if (loc3.getIsToolState()=="isForbid")
					{
						JOptionPane.showMessageDialog(null, "此处属于公共场所，不能添加炸弹！");
						
					} 
					else 
					{
						loc3.setIsToolState("isBomb");
						int NumBomb = player[flag-1].getBomb()-1;//获得玩家的炸弹数
						player[flag-1].setBomb(NumBomb);//将玩家的炸弹数减一
						labelList.get(loc3.getLocation()).setText("<html>"+"<br>"+" B "+"<html>");//表示该位置有炸弹，并换行
					}
					
				}
					}	
					
					
					 else
					    {
					    	JOptionPane.showMessageDialog(null, player[flag-1].getPlayerName()+"没有炸弹！");
					    }
				}
			}
			
			
			
			if(flag==4) {
				if(player[flag-1].getState()!=3) {
					List<JButton> labelList = basicFrame.getButtonList();
					if(player[flag-1].getBomb()>0)
					{
					if((!MainT.isNumeric((basicFrame.getBombLocation().getText().trim())))) 
				    {
						JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入数字");
					}
					
					else if(Integer.parseInt(basicFrame.getBombLocation().getText().trim())>10)
				    {
				    	JOptionPane.showMessageDialog(basicFrame.getFrame(), "请输入10以内的数字");
				    }
					
					else
					{
				    int loclab=Integer.parseInt(basicFrame.getBombLocation().getText().trim());//获得label中的String值并转化成int型
				    
				    JOptionPane.showMessageDialog(null,basicFrame.getBombLocation().getText().trim()+player[flag-1].getPlayerName());
				    
				    int locply= player[flag-1].getLocation();//获得当前玩家的地址值
				
				    Location loc3 = MainT.markLocation.get(locply+loclab);//获得要安置炸弹的位置的对象
					
				   
				    
				   
					if(loc3.getIsToolState()=="isBomb")//判断该位置的道具状态
					{
						JOptionPane.showMessageDialog(null, "此处有炸弹，不能添加炸弹！");
						
					}
					else if (loc3.getIsToolState()=="isRoadBlock")
					{
						JOptionPane.showMessageDialog(null, "此处有路障，不能添加炸弹！");
						
					} 
					else if (loc3.getIsToolState()=="isForbid")
					{
						JOptionPane.showMessageDialog(null, "此处属于公共场所，不能添加炸弹！");
						
					} 
					else 
					{
						loc3.setIsToolState("isBomb");
						int NumBomb = player[flag-1].getBomb()-1;//获得玩家的炸弹数
						player[flag-1].setBomb(NumBomb);//将玩家的炸弹数减一
						labelList.get(loc3.getLocation()).setText("<html>"+"<br>"+" B "+"<html>");//表示该位置有炸弹，并换行
					}
					
					}
				}
					
					
					 else
					    {
					    	JOptionPane.showMessageDialog(null, player[flag-1].getPlayerName()+"没有炸弹！");
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


