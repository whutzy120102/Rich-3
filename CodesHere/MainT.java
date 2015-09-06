package others;

import house.House;
import house.Location;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
*  主要是辅助类，以及一些常量，用来作为前后台之间的沟通桥梁
*/


public class MainT {
	
	public static String[] Names = {"金贝贝(1)","阿土伯(2)","孙小美(3)","钱夫人(4)"};
	public static String defaultChar = "SHTGPM$";
	
		// 根据地址把所有房屋都放入map，供取出
	public static Map<Integer, House> allHouseMap = new HashMap<>();
	
	public static Map<Integer, Location> markLocation = new HashMap<>();
	
	public static Color[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.GRAY};
	

	
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if(isNum.matches()) {
			return true;
		}
		return false;
	}
	
	public static boolean isNumeric(String str1, String str2) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum1 = pattern.matcher(str1);
		Matcher isNum2 = pattern.matcher(str2);
		if(isNum1.matches() && isNum2.matches()) {
			return true;
		}
		return false;
	}
	
	
	public static int getCounts(String str, String c) {
		int count = 0;
		for(int i = 0; i<str.length();i++) {
			if(c.equals(str.charAt(i)))
				count++;
		}
		return count;
	}
	
//	public static void main(String[] args) {
//		System.out.println(isNumeric("agdb132"));
//	}
	
}
