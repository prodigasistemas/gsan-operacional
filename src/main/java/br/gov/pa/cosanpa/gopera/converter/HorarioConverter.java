package br.gov.pa.cosanpa.gopera.converter;

import static br.gov.model.util.Utilitarios.formataData;
import static br.gov.model.util.Utilitarios.converteData;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.model.util.FormatoData;

@FacesConverter("operacional.HorarioConverter")
public class HorarioConverter implements Converter{

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return converteData(value, FormatoData.HORA_MINUTO);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null){
            return formataData((Date) value, FormatoData.HORA_MINUTO);
        }else{
           return ""; 
        }
    }
}
