package com.intent.minminas472.utils;

import java.util.ResourceBundle;

public class Constants {

	private static ResourceBundle bundle = ResourceBundle.getBundle("logic");

	public static String getProperty(String name) {
		return bundle.getString(name);
	}

}
