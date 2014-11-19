package br.gov.pa.cosanpa.gopera.converter;

import java.lang.reflect.Field;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("selectOneConverter")
public class SelectOneConverter implements Converter{
	
    public Object getAsObject(FacesContext context,UIComponent component, String value) {
        if (value == null || value.equals("") || value.equals("...")) {
            return new Object();
        }
        try {
        	return  Integer.valueOf(value);
        }catch(Exception ex){
        	throw new ConverterException("Não foi possível aplicar conversão de item com valor ["+value+"] no componente ["+component.getId()+"]", ex);
        }
    }
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null || value.toString().equals("0") || value.toString().isEmpty()) {
			return "";
		} 
		return getIdByReflection(value) != null ? getIdByReflection(value).toString() : null;
	}
    
    private Integer getIdByReflection(Object bean){
    	try{
    		if (bean.getClass().equals(Integer.class)) {
    			return bean.toString().isEmpty()  ? null : (Integer) bean;	
    		}
    		
    		Field idField = bean.getClass().getDeclaredField("codigo");
    		if (idField != null) {
    			idField.setAccessible(true);
    			return (Integer) idField.get(bean);
    		}
    		return null;
    		
    	}catch(Exception ex){
    		throw new RuntimeException("Não foi possível obter a propriedade 'id' do item",ex);
    	}
    }

}

