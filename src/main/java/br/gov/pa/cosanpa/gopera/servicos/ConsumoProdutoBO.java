package br.gov.pa.cosanpa.gopera.servicos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.gov.model.operacao.EstacaoOperacional;
import br.gov.model.operacao.LancamentoPendente;
import br.gov.model.operacao.Parametro;
import br.gov.model.operacao.TipoUnidadeOperacional;
import br.gov.servicos.operacao.EstacaoOperacionalRepositorio;
import br.gov.servicos.operacao.ParametroRepositorio;

@Stateless
public class ConsumoProdutoBO {

    @EJB
    private ParametroRepositorio parametroRepositorio;
    
    @EJB
    private EstacaoOperacionalRepositorio estacaoRepositorio;
        
    public List<LancamentoPendente> obterConsumosPendentes() {
        Parametro dias = parametroRepositorio.obterPeloNome(Parametro.Nome.DIAS_PENDENCIA_LANCAMENTO);
        
        List<LancamentoPendente> pendentes = new ArrayList<LancamentoPendente>();
        
        if (dias != null && dias.getValor() != null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(dias.getValor()));
            
            List<EstacaoOperacional> estacoes = estacaoRepositorio.listarEstacoesComConsumoPendente(cal.getTime(), TipoUnidadeOperacional.EAT);
            estacoes.addAll(estacaoRepositorio.listarEstacoesComConsumoPendente(cal.getTime(), TipoUnidadeOperacional.ETA));
            
            for (EstacaoOperacional estacao : estacoes) {
                LancamentoPendente lancamento = new LancamentoPendente();
                if (estacao.getUcOperacional() != null && !estacao.getUcOperacional().isEmpty()){
                    lancamento.setRegional(estacao.getUcOperacional().get(0).getUC().getRegionalProxy().getNome());
                    lancamento.setUnidadeNegocio(estacao.getUcOperacional().get(0).getUC().getUnidadeNegocioProxy().getNome());
                    lancamento.setMunicipio(estacao.getUcOperacional().get(0).getUC().getMunicipioProxy().getNome());
                    lancamento.setLocalidade(estacao.getUcOperacional().get(0).getUC().getLocalidadeProxy().getNome());
                }
                lancamento.setUnidadeOperacional(estacao.getNome());
                pendentes.add(lancamento);
            }
        }
        
        return pendentes;
    }
}
