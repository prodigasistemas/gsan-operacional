package br.gov.pa.cosanpa.gopera.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import static br.gov.model.util.Utilitarios.converteAnoMesParaMesAno;
import static br.gov.model.util.Utilitarios.converteMesAnoParaAnoMes;

@FacesConverter("operacional.MesReferenciaConverter")
public class MesReferenciaConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return converteMesAnoParaAnoMes(value);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null)
            return converteAnoMesParaMesAno(Integer.valueOf(String.valueOf(value)));
        else
            return "";
    }

}
