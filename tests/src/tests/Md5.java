package tests;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Md5 {
	private static String md5(String s) {
		MessageDigest m;
		String retorno = "";
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(), 0, s.length());
			retorno = new BigInteger(1, m.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	public static String geraCodigoToken(String nomeUsuario) {
		String md5 = null;
		
		if (nomeUsuario != null){
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd-HH");
			md5 = md5(nomeUsuario + formato.format(Calendar.getInstance().getTime()));
		}
		
		return md5;
	}
	
	public static void main(String[] args) {
		System.out.println(Md5.geraCodigoToken("961"));
	}
}
