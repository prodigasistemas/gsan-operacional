package br.gov.pa.cosanpa.gopera.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadProperties {

	String propertieFile = "";
	
	public LoadProperties(String propertieFile) {
		this.propertieFile = propertieFile;
	}
	
	
	public void setPropertie(String propertie, String value){
		try {  
			File file = new File( propertieFile );
			if (!file.exists()) {
				file.createNewFile();
			}
			Properties defaultProps = new Properties();
			FileInputStream in = new FileInputStream(propertieFile);
			defaultProps.load(in);
			defaultProps.setProperty(propertie, ""+value);
			FileOutputStream out = new FileOutputStream(propertieFile);
			defaultProps.store(out, "------");
			out.close();
			in.close();
		} catch (FileNotFoundException ex) {  
			ex.printStackTrace();  
		} catch (IOException ex) {  
			ex.printStackTrace();  
		}  
	}

	public String getPropertie(String propertie){
		File file = new File( propertieFile );
		if (!file.exists()) {
			return null;
		}
		Properties props = new Properties();
		try {
			FileInputStream fileInputStream = new FileInputStream( file ); 
			props.load(fileInputStream);
			fileInputStream.close();
			props.getProperty( propertie );
			String value = props.getProperty( propertie );
			if (value == null || value.equalsIgnoreCase("null")) {
				return null;
			} else return value; 
		} catch (FileNotFoundException ex) {  
			ex.printStackTrace();  
		} catch (IOException ex) {  
			ex.printStackTrace();  
		}
		return null;
	}
	

}
