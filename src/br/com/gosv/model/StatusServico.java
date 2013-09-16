/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gosv.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Leonardo
 */
@Entity
@Table(name = "status_servico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StatusServico.findAll", query = "SELECT s FROM StatusServico s")})
public class StatusServico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "situacao")
    private String situacao;
    @Column(name = "observacao")
    private String observacao;
    @JoinColumn(name = "ordem_servico_codigo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private OrdemServico ordemServicoCodigo;

    public StatusServico() {
    }

    public StatusServico(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public OrdemServico getOrdemServicoCodigo() {
        return ordemServicoCodigo;
    }

    public void setOrdemServicoCodigo(OrdemServico ordemServicoCodigo) {
        this.ordemServicoCodigo = ordemServicoCodigo;
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
        if (!(object instanceof StatusServico)) {
            return false;
        }
        StatusServico other = (StatusServico) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.gosv.model.StatusServico[ codigo=" + codigo + " ]";
    }
    
}
