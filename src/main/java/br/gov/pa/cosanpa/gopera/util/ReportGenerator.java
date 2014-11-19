package br.gov.pa.cosanpa.gopera.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

public abstract class ReportGenerator {
	
	public abstract Map<String, Object> getParametros();
	public abstract JRDataSource getReportDataSource();
	public abstract TipoExportacao getTipoExportacao();
	public abstract String getNomeArquivo();
	public abstract String getTituloRelatorio();
	
	public String getReportPath(){
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + getNomeArquivo() + ".jasper");
	}
	
	public  void geraRelatorio() throws Exception{
		HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		httpServletResponse.setCharacterEncoding("ISO-8859-1");
		
		JasperPrint jasperPrint;
		
		Map<String, Object> parametros = getParametros();
		
		if (parametros == null){
			parametros = new HashMap<String, Object>();
		}
		
		parametros.put("logoRelatorio", "logoRelatorio.jpg");
		parametros.put("tituloRelatorio", getTituloRelatorio());
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		TipoExportacao tipo = getTipoExportacao();
		
		switch (tipo) {
		case PDF:
			parametros.put("exibirExcel", false);
 	        jasperPrint = JasperFillManager.fillReport(getReportPath(), parametros, getReportDataSource());

			httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + getNomeArquivo() + ".pdf");
			httpServletResponse.setContentType("application/pdf");

			ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
		    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
		    break;
		    
		case EXCEL:
			parametros.put("exibirExcel", true);
			jasperPrint = JasperFillManager.fillReport(getReportPath(), parametros, getReportDataSource());
			
			httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + getNomeArquivo() + ".xls");
			httpServletResponse.setContentType("application/vnd.ms-excel");
			servletOutputStream = httpServletResponse.getOutputStream();
			gerarExcel(servletOutputStream, jasperPrint);
			
		    break;
		}
		
		FacesContext.getCurrentInstance().responseComplete();  
	}
	
	private void gerarExcel(ServletOutputStream servletOutputStream, JasperPrint jasperPrint) throws JRException, IOException {
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
		exporter.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
		exporter.setParameter(JExcelApiExporterParameter.IS_IGNORE_GRAPHICS,Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		exporter.exportReport();
		servletOutputStream.flush();
		servletOutputStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}
}