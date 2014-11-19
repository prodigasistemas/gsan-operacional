package br.gov.pa.cosanpa.gopera.util;

import java.util.ResourceBundle;

import javax.ejb.Singleton;

@Singleton
public class WebBundle {
	private ResourceBundle bundle = null;
	
	public String getText(String key){
		if (bundle == null){
			bundle = ResourceBundle.getBundle("messages");
		}
		
		return bundle.getString(key);
	}
}
