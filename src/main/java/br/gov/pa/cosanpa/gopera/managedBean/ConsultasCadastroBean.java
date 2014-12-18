package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.model.exception.ErroConsultaSistemaExterno;
import br.gov.model.operacao.EstacaoOperacional;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.servicos.operacao.EstacaoOperacionalRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.to.ConsultaCadastroTO;

@ManagedBean
@SessionScoped
public class ConsultasCadastroBean {

    @EJB
    private ProxyOperacionalRepositorio proxy;

    @EJB
    private EstacaoOperacionalRepositorio estacaoOperacionalEJB;

    public List<RegionalProxy> getRegionais() {
        List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
        try {
            regionais = proxy.getListaRegional();
            regionais.add(0, new RegionalProxy(-1, "Selecione..."));
            return regionais;
        } catch (Exception e) {
            throw new ErroConsultaSistemaExterno(e);
        }
    }

    public List<UnidadeNegocioProxy> unidadesNegocio(ConsultaCadastroTO to) {
        List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
        if (to.getCodigoRegional() != null) {
            try {
                unidadesNegocio = proxy.getListaUnidadeNegocio(to.getCodigoRegional());
            } catch (Exception e) {
                throw new ErroConsultaSistemaExterno(e);
            }
        }
        unidadesNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
        return unidadesNegocio;
    }

    public List<MunicipioProxy> municipios(ConsultaCadastroTO to) {
        List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
        if (to.getCodigoUnidadeNegocio() != null) {
            try {
                municipios = proxy.getListaMunicipio(to.getCodigoRegional(), to.getCodigoUnidadeNegocio());
            } catch (Exception e) {
                throw new ErroConsultaSistemaExterno(e);
            }
        }
        municipios.add(0, new MunicipioProxy(-1, "Selecione..."));
        return municipios;
    }
    
    public List<LocalidadeProxy> localidades(ConsultaCadastroTO to) {
        List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
        if (to.getCodigoMunicipio() != null) {
            try {
                localidades = proxy.getListaLocalidade(to.getCodigoRegional(), to.getCodigoUnidadeNegocio(), to.getCodigoMunicipio());
            } catch (Exception e) {
                throw new ErroConsultaSistemaExterno(e);
            }
        }
        localidades.add(0, new LocalidadeProxy(-1, "Selecione..."));
        return localidades;
    }

    public List<EstacaoOperacional> estacoesOperacionais(ConsultaCadastroTO to) {
        List<EstacaoOperacional> estacoes = new ArrayList<EstacaoOperacional>();
        if (to.getCodigoLocalidade() != null && to.getTipoUnidadeOperacional() != null) {
            try {
                estacoes = estacaoOperacionalEJB.estacoes(to.getCodigoRegional()
                        , to.getCodigoUnidadeNegocio()
                        , to.getCodigoMunicipio()
                        , to.getCodigoLocalidade()
                        , to.getTipoUnidadeOperacional());
            } catch (Exception e) {
                throw new ErroConsultaSistemaExterno(e);
            }
        }
        estacoes.add(0, new EstacaoOperacional(-1, -1, "Selecione..."));
        return estacoes;
    }    
}
