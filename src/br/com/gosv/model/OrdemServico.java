/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gosv.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Leonardo
 */
@Entity
@Table(name = "ordem_servico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrdemServico.findAll", query = "SELECT o FROM OrdemServico o")})
public class OrdemServico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "observacao")
    private String observacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordemServicoCodigo")
    private List<StatusServico> statusServicoList;
    @JoinColumn(name = "servico_codigo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Servico servicoCodigo;
    @JoinColumn(name = "cliente_codigo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Cliente clienteCodigo;

    public OrdemServico() {
    }

    public OrdemServico(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @XmlTransient
    public List<StatusServico> getStatusServicoList() {
        return statusServicoList;
    }

    public void setStatusServicoList(List<StatusServico> statusServicoList) {
        this.statusServicoList = statusServicoList;
    }

    public Servico getServicoCodigo() {
        return servicoCodigo;
    }

    public void setServicoCodigo(Servico servicoCodigo) {
        this.servicoCodigo = servicoCodigo;
    }

    public Cliente getClienteCodigo() {
        return clienteCodigo;
    }

    public void setClienteCodigo(Cliente clienteCodigo) {
        this.clienteCodigo = clienteCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdemServico)) {
            return false;
        }
        OrdemServico other = (OrdemServico) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.gosv.model.OrdemServico[ codigo=" + codigo + " ]";
    }
    
}
