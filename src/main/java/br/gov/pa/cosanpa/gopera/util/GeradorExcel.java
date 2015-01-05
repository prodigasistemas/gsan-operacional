package br.gov.pa.cosanpa.gopera.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class GeradorExcel {
    
    private DadosExcel excel;
    
    private WritableCellFormat label;
    
    private WritableCellFormat negrito;
    
    public GeradorExcel(DadosExcel excel) {
        this.excel = excel;
        
        prepare();
    }
    
    protected void prepare() {
        try {
            WritableFont tahoma10 = new WritableFont(WritableFont.TAHOMA, 10);
            WritableFont tahoma11 = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD);
            
            label = new WritableCellFormat(tahoma10);
            label.setWrap(false);
            label.setVerticalAlignment(VerticalAlignment.CENTRE);
            label.setAlignment(Alignment.CENTRE);
            label.setBorder(Border.ALL, BorderLineStyle.THIN);
            
            negrito = new WritableCellFormat(tahoma11);
            negrito.setAlignment(Alignment.LEFT);
        } catch (Exception e) {
            
        }
    }
    
    

    public void geraPlanilha() throws Exception {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=\""  + excel.nomeArquivo() + ".xls"+  "\"");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("ISO-8859-1");
        OutputStream output = response.getOutputStream();

        
        WorkbookSettings settings = new WorkbookSettings();
        settings.setLocale(new Locale("pt", "BR"));
        settings.setEncoding("ISO-8859-1");
        
        File file = new File(Calendar.getInstance().getTimeInMillis() + ".xls");
        
        Workbook workbook = Workbook.getWorkbook(excel.getArquivoTemplate(), settings);
        WritableWorkbook copy = Workbook.createWorkbook(file, workbook);
        
        WritableSheet sheet = copy.getSheet(0);
        escreveConteudo(sheet);
        copy.write();
        copy.close();
        
        FileInputStream fis = new FileInputStream(file);
        int data = -1;
        while ((data = fis.read()) != -1){
            output.write(data);
        }
        output.flush();
        output.close();
        fis.close();
        
        FacesContext.getCurrentInstance().responseComplete();        
        file.delete();
    }

    private void escreveConteudo(WritableSheet sheet) throws Exception {
        adicionaTitulo(sheet);
        
        String[] cabecalho = excel.cabecalho();
        
        List<String[]> dados = excel.dados();
        
        for (int i = 0; i < cabecalho.length; i++){
            this.addLabel(sheet, i, 2, cabecalho[i]);
        }
        
        for (int linha = 0; linha < dados.size(); linha++){
            String[] registro = dados.get(linha);
            for (int i = 0; i < registro.length; i++){
                this.addLabel(sheet, i, linha + 3, registro[i]);
            }
        }
    }

    private void adicionaTitulo(WritableSheet sheet) throws Exception {
        sheet.addCell(new Label(1, 0, excel.tituloRelatorio(), negrito));
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        sheet.addCell(new Label(3, 0, "Data: " + format.format(new Date()), negrito));
    }

    private void addLabel(WritableSheet planilha, int coluna, int linha, String s) throws Exception {
        if (s == null)
            s = "";
        planilha.addCell(new Label(coluna, linha, s, label));
    }
}
