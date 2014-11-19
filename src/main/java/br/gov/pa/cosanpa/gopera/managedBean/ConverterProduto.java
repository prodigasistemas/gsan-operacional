package br.gov.pa.cosanpa.gopera.managedBean;

import java.lang.reflect.Field;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.model.operacao.Produto;

@FacesConverter("converterProduto")
public class ConverterProduto implements Converter{
	
	@Override
    public Object getAsObject(FacesContext context,UIComponent component, String value) {
        if (value == null || value.equals("") || value.trim().equals(""))
            return null;

        try{
        	Produto produto = new Produto();
        	Integer valor = Integer.parseInt(value);
        	produto.getUnidadeMedidaProduto().setCodigo(valor);
        	return produto;
        }catch(Exception ex){
        	throw new ConverterException("Não foi possível aplicar conversão de item com valor ["+value+"] no componente ["+component.getId()+"]", ex);
        }
    }
	
    @Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null || value.equals(""))	return "";
		return getIdByReflection(value).toString();
	}
    
    private Integer getIdByReflection(Object bean){
    	try{
    		Field idField = bean.getClass().getDeclaredField("codigo");
    		idField.setAccessible(true);
    		return (Integer) idField.get(bean);
    	}catch(Exception ex){
    		throw new RuntimeException("Não foi possível obter a propriedade 'id' do item",ex);
    	}
    }

}

