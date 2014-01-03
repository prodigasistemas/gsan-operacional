package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import br.gov.pa.cosanpa.gopera.util.PerfilBeanEnum;

@Embeddable
@SessionScoped
@Named
public class UsuarioProxy implements BaseEntidade, Serializable {
	private static final long serialVersionUID = 5091282867953913037L;
	
	@Column(name="usur_id", nullable=false)
	private Integer codigo;
	
	@Transient
	private String nome;
	
	@Transient
	private PerfilBeanEnum perfil;

	@Transient
	private Boolean administrador;
	
	@Transient
	private String unidadeOrganizacional;
	
	@Transient
	private RegionalProxy regionalProxy = new RegionalProxy();
	
	@Transient
	private List<LocalidadeProxy> localidadeProxy = new ArrayList<LocalidadeProxy>();	

	@Transient
	private Integer referencia;

	@Transient
	private Integer diaPendencia;

	@Transient
	private Boolean logado;
	
	public UsuarioProxy() {
		this.diaPendencia = 0;
		this.administrador = false;
		this.logado = false;
	}
	
	public UsuarioProxy(Integer codigo, String nome) {
		super();
		this.codigo = codigo;
		this.nome = nome;
	}

	public Integer getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public String getNome() throws Exception {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public PerfilBeanEnum getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilBeanEnum perfil) {
		this.perfil = perfil;
	}

	public String getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setUnidadeOrganizacional(String unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}
	
	public RegionalProxy getRegionalProxy() {
		return regionalProxy;
	}

	public void setRegionalProxy(RegionalProxy regionalProxy) {
		this.regionalProxy = regionalProxy;
	}

	public List<LocalidadeProxy> getLocalidadeProxy() {
		return localidadeProxy;
	}

	public void setLocalidadeProxy(List<LocalidadeProxy> localidadeProxy) {
		this.localidadeProxy = localidadeProxy;
	}

	public Integer getReferencia() {
		return referencia;
	}

	public void setReferencia(Integer referencia) {
		this.referencia = referencia;
	}

	public Integer getDiaPendencia() {
		return diaPendencia;
	}

	public void setDiaPendencia(Integer diaPendencia) {
		this.diaPendencia = diaPendencia;
	}

	public Boolean getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}

	public Boolean getLogado() {
		return logado;
	}

	public void setLogado(Boolean logado) {
		this.logado = logado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((administrador == null) ? 0 : administrador.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((diaPendencia == null) ? 0 : diaPendencia.hashCode());
		result = prime * result
				+ ((localidadeProxy == null) ? 0 : localidadeProxy.hashCode());
		result = prime * result + ((logado == null) ? 0 : logado.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((perfil == null) ? 0 : perfil.hashCode());
		result = prime * result
				+ ((referencia == null) ? 0 : referencia.hashCode());
		result = prime * result
				+ ((regionalProxy == null) ? 0 : regionalProxy.hashCode());
		result = prime
				* result
				+ ((unidadeOrganizacional == null) ? 0 : unidadeOrganizacional
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioProxy other = (UsuarioProxy) obj;
		if (administrador == null) {
			if (other.administrador != null)
				return false;
		} else if (!administrador.equals(other.administrador))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (diaPendencia == null) {
			if (other.diaPendencia != null)
				return false;
		} else if (!diaPendencia.equals(other.diaPendencia))
			return false;
		if (localidadeProxy == null) {
			if (other.localidadeProxy != null)
				return false;
		} else if (!localidadeProxy.equals(other.localidadeProxy))
			return false;
		if (logado == null) {
			if (other.logado != null)
				return false;
		} else if (!logado.equals(other.logado))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (perfil != other.perfil)
			return false;
		if (referencia == null) {
			if (other.referencia != null)
				return false;
		} else if (!referencia.equals(other.referencia))
			return false;
		if (regionalProxy == null) {
			if (other.regionalProxy != null)
				return false;
		} else if (!regionalProxy.equals(other.regionalProxy))
			return false;
		if (unidadeOrganizacional == null) {
			if (other.unidadeOrganizacional != null)
				return false;
		} else if (!unidadeOrganizacional.equals(other.unidadeOrganizacional))
			return false;
		return true;
	}

}
