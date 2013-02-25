package com.adamki11s.bundle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import com.adamki11s.io.FileLocator;
import com.adamki11s.io.GeneralConfigData;
import com.adamki11s.payload.ExtractPayload;
import com.adamki11s.questx.QuestX;

public class LocaleBundle {

	static final Locale[] supportedLocales = { Locale.ENGLISH };
	
	
	private static int locale = 0;
	
	public LocaleBundle(String l){
		this.extract();
		if(l.equalsIgnoreCase("en")){
			locale = 0;
		} else {
			locale = 0;
		}
	}
	
	private void extract(){
		File[] locales = new File[]{new File(res)};
		
		for(File f : locales){
			if(f.exists()){
				continue;
			} else {
				InputStream stream = LocaleBundle.class.getResourceAsStream("/res/" + f.getName());
				
				OutputStream resStreamOut;
				int readBytes;
				byte[] buffer = new byte[4096];
				
				try {
					resStreamOut = new FileOutputStream(f);

					while ((readBytes = stream.read(buffer)) > 0) {
						resStreamOut.write(buffer, 0, readBytes);
					}
					resStreamOut.flush();
					resStreamOut.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}
	
	public final static String res = ("plugins" + File.separator + "QuestX" + File.separator + "Locale" + File.separator + "locale.txt");
	
	public static String getString(String key){
		ResourceBundle bundle = ResourceBundle.getBundle(res, supportedLocales[locale]);
		return bundle.getString(key);
	}

}
