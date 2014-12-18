package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.util.DadosExcel;
import br.gov.pa.cosanpa.gopera.util.GeradorExcel;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.RelatorioHorasRepositorio;
import br.gov.servicos.operacao.to.ConsultaHorasTO;
import br.gov.servicos.operacao.to.HorasRelatorioTO;

@ManagedBean
@ViewScoped
public class RelatorioHorasBean extends BaseMensagemBean{

	private Logger logger = Logger.getLogger(RelatorioHorasBean.class);
	
	@EJB
	private RelatorioHorasRepositorio relatorioHoras;
	
	private ConsultaHorasTO to = new ConsultaHorasTO();
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;

	public String exibir() {
		try {
			if (!to.intervaloValido()) {
				mostrarMensagemErro(bundle.getText("erro_intervalo_invalido"));
				return "";
			}
			
			List<HorasRelatorioTO> relatorio = relatorioHoras.consultaHoras(to);
			
			if (relatorio.size() == 0){
			    mostrarMensagemAviso(bundle.getText("erro_nao_existe_retorno_filtro"));
			}else{
			    DadosExcel excel = new DadosExcel() {
			        
			        public String tituloRelatorio() {
			            return bundle.getText("horas_sistema");
			        }
			        
			        public String nomeArquivo() {
			            return "Horas do Sistema";
			        }
			        
			        public List<String[]> dados() {
			            List<String[]> linhas = new ArrayList<String[]>();
			            for (HorasRelatorioTO item : relatorio) {
			                linhas.add(item.toArray());
			            }
			            return linhas;
			        }
			        
			        public String[] cabecalho() {
			            return new String[]{
			                    bundle.getText("gerencia_regional")
			                    , bundle.getText("unidade_negocio")
			                    , bundle.getText("municipio")
			                    , bundle.getText("localidade")
			                    , bundle.getText("tipo_unidade_operacional")
			                    , bundle.getText("codigo_uc")
			                    , bundle.getText("unidade_operacional")
			                    , bundle.getText("referencia")
			                    , bundle.getText("conjunto_motor_bomba")
			                    , bundle.getText("total_horas_mes")
			                    , bundle.getText("horas_trabalhadas")
			                    , bundle.getText("horas_paradas_energia")
			                    , bundle.getText("horas_paradas_manutencao")
			                    , bundle.getText("horas_paradas_controle")
			            };
			        }
			    };
			    
			    GeradorExcel gerador = new GeradorExcel(excel);
			    gerador.geraPlanilha();
			}
		} catch (Exception e) {
			logger.error(bundle.getText("erro_gerar_relatorio"), e);
			mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
		}

		return null;
	}

	public ConsultaHorasTO getTo() {
		return to;
	}

	public void setTo(ConsultaHorasTO to) {
		this.to = to;
	}
}
