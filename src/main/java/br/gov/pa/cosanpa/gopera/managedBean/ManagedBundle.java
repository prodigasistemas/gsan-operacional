package br.gov.pa.cosanpa.gopera.managedBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.pa.cosanpa.gopera.util.WebBundle;

@ManagedBean
@SessionScoped
public class ManagedBundle {
    
    @EJB
    private WebBundle bundle;
    
    public String text(String key){
        return bundle.getText(key);
    }
}
