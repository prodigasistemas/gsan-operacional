package br.gov.pa.cosanpa.gopera.managedBean;

import static br.gov.model.util.Utilitarios.converteAnoMesParaMesAno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.util.DadosExcel;
import br.gov.pa.cosanpa.gopera.util.GeradorExcel;
import br.gov.servicos.operacao.RelatorioHorasRepositorio;
import br.gov.servicos.operacao.to.FiltroOperacionalTO;
import br.gov.servicos.operacao.to.HorasRelatorioTO;

@ManagedBean
@ViewScoped
public class RelatorioHorasBean extends BaseMensagemBean{

	private Logger logger = Logger.getLogger(RelatorioHorasBean.class);
	
	@EJB
	private RelatorioHorasRepositorio relatorioHoras;
	
	private FiltroOperacionalTO to = new FiltroOperacionalTO();
	
	public void exibir() {
		try {
			if (!to.intervaloValido()) {
				mostrarMensagemErro(bundle.getText("erro_intervalo_invalido"));
				return;
			}
			
			List<HorasRelatorioTO> relatorio = relatorioHoras.consultaHoras(to);
			final Integer qtdMaximaCmb = relatorioHoras.quantidadeMaximaCmb(to);
			
			if (relatorio.size() == 0){
			    mostrarMensagemAviso(bundle.getText("erro_nao_existe_retorno_filtro"));
			}else{
			    DadosExcel excel = new DadosExcel() {
                    public String periodo() {
                        return converteAnoMesParaMesAno(to.getReferenciaInicial()) + " a " + converteAnoMesParaMesAno(to.getReferenciaFinal());
                    }
			        
			        public String filtro(){
			            return to.filtroSelecionado();
			        }
			        
			        public String tituloRelatorio() {
			            return bundle.getText("horas_sistema");
			        }
			        
			        public String nomeArquivo() {
			            return "Horas do Sistema";
			        }
			        
			        public List<List<String>> dados() {
			            List<List<String>> linhas = new ArrayList<List<String>>();
			            for (HorasRelatorioTO item : relatorio) {
			                linhas.add(item.toArray());
			            }
			            return linhas;
			        }
			        
			        public String[] cabecalho() {
			            String[] cabecalho = new String[]{
			                    bundle.getText("gerencia_regional")
			                    , bundle.getText("unidade_negocio")
			                    , bundle.getText("municipio")
			                    , bundle.getText("localidade")
			                    , bundle.getText("tipo_unidade_operacional")
			                    , bundle.getText("codigo_uc")
			                    , bundle.getText("unidade_operacional")
			                    , bundle.getText("referencia")
			                    , bundle.getText("total_horas_mes")
			                    , bundle.getText("conjunto_motor_bomba")
			                    , bundle.getText("horas_paradas_energia")
			                    , bundle.getText("horas_paradas_manutencao")
			                    , bundle.getText("horas_paradas_controle")
			                    , bundle.getText("horas_trabalhadas")
			            };
			            
			            String[] comCmbs = Arrays.copyOf(cabecalho, cabecalho.length + qtdMaximaCmb);
			            
			            for(int i = cabecalho.length; i < cabecalho.length + qtdMaximaCmb; i++){
			                comCmbs[i] = (i - cabecalho.length + 1) + " CMB";
			            }
			            
			            return comCmbs;
			        }
			    };
			    
			    GeradorExcel gerador = new GeradorExcel(excel);
			    gerador.geraPlanilha();
			}
		} catch (Exception e) {
			logger.error(bundle.getText("erro_gerar_relatorio"), e);
			mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
		}
	}

	public FiltroOperacionalTO getTo() {
		return to;
	}

	public void setTo(FiltroOperacionalTO to) {
		this.to = to;
	}
}
