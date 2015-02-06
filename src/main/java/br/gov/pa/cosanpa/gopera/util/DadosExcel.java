package br.gov.pa.cosanpa.gopera.util;

import java.io.File;
import java.util.List;

import javax.faces.context.FacesContext;

public abstract class DadosExcel {
    
    private String diretorio;
    private String template;
    
    public abstract String nomeArquivo();
    
    public abstract String tituloRelatorio();
    
    public abstract String filtro();
    
    public abstract String[] cabecalho();
    
    public abstract String periodo();
    
    public abstract List<List<String>> dados(); 
    
    public DadosExcel() {
        diretorio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports");
        template  = "template-excel.xls"; 
    }

    public File getArquivoTemplate() {
        return new File(diretorio + "/" + template);
    }

}
