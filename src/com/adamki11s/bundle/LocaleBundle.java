package com.adamki11s.bundle;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleBundle {

	static final Locale[] supportedLocales = { Locale.ENGLISH };
	
	
	private static Locale use = Locale.ENGLISH;
	//english as default
	
	public static void setLocale(Locale l){
		for(Locale local : supportedLocales){
			if(local == l){
				//valid locale selected
				use = l;
			}
		}
	}	
	
	public static String getString(String key){
		ResourceBundle bundle = ResourceBundle.getBundle("res.bundle", use);
		return bundle.getString(key);
	}

}
