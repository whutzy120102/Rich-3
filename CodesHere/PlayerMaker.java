package playerMaker;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import player.Player;

/**
 *  根据反射生成Player对象，可以根据输入指定的人数来生成指定数目的对象
 * @author zhouwenjun
 *
 */

public class PlayerMaker {
	public static Player makePlayer(String playerName, int money, int points, Color color){
		Player player = null;
		try {
			Class clazz =  Class.forName("player.Player");
			Constructor constructor = clazz.getConstructor(String.class, int.class, int.class, Color.class);
			player = (Player) constructor.newInstance(playerName, money, points, color);
			
		} catch (IException e) {
			
			throw new RuntimeException(e);
		}
		
		return player;
	}
	
}
