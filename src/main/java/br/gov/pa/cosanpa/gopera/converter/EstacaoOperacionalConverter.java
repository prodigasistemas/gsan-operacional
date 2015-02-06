package br.gov.pa.cosanpa.gopera.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.model.operacao.EstacaoOperacional;

@FacesConverter("estacaoOperacionalConverter")
public class EstacaoOperacionalConverter implements Converter{
	
    public Object getAsObject(FacesContext context,UIComponent component, String value) {
        if (value != null && !value.isEmpty()) {
            return (EstacaoOperacional) component.getAttributes().get(value);
        }
        return null;        
    }
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
	    if (value instanceof EstacaoOperacional) {
	        EstacaoOperacional entity= (EstacaoOperacional) value;
            if (entity != null && entity instanceof EstacaoOperacional && entity.getPk() != null) {
                component.getAttributes().put( entity.getPk().toString(), entity);
                return entity.getPk().toString();
            }
        }
	    
        return "";	    
	}
}