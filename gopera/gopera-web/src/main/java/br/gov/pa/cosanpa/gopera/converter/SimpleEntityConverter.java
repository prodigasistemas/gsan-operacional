package br.gov.pa.cosanpa.gopera.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pa.cosanpa.gopera.model.BaseEntidade;

@FacesConverter("simpleEntityConverter")
public class SimpleEntityConverter implements Converter {  
    
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {  
        if (value != null) {  
            return this.getAttributesFrom(component).get(value);  
        }  
        return null;  
    }  
  
    public String getAsString(FacesContext ctx, UIComponent component, Object value) {  
  
        if (value != null  
                && !"".equals(value)) {  
  
        	BaseEntidade entity = (BaseEntidade) value;  
  
            // adiciona item como atributo do componente  
            this.addAttribute(component, entity);  
  
            Integer codigo = entity.getCodigo();  
            if (codigo != null) {  
                return String.valueOf(codigo);  
            }
            else codigo = 0;
        }  
  
        return "";  
    }  
  
    protected void addAttribute(UIComponent component, BaseEntidade o) {
    	String key = "";
    	if (o.getCodigo() != null) {
    		key = o.getCodigo().toString();
    	}
        this.getAttributesFrom(component).put(key, o);  
    }  
  
    protected Map<String, Object> getAttributesFrom(UIComponent component) {  
        return component.getAttributes();  
    }  
  
}  