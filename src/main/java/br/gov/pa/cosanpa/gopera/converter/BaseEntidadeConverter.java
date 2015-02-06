package br.gov.pa.cosanpa.gopera.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.model.operacao.BaseEntidade;

@FacesConverter("baseEntidadeConverter")
public class BaseEntidadeConverter implements Converter{
	
    public Object getAsObject(FacesContext context,UIComponent component, String value) {
        if (value != null && !value.isEmpty()) {
            return (BaseEntidade) component.getAttributes().get(value);
        }
        return null;        
    }
    
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof BaseEntidade) {
            BaseEntidade entity= (BaseEntidade) value;
            if (entity != null && entity instanceof BaseEntidade && entity.getCodigo() != null) {
                component.getAttributes().put( entity.getCodigo().toString(), entity);
                return entity.getCodigo().toString();
            }
        }
        
        return "";      
    }
}
