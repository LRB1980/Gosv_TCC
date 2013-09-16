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
@Table(name = "visita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Visita.findAll", query = "SELECT v FROM Visita v")})
public class Visita implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "setor_codigo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Setor setorCodigo;
    @JoinColumn(name = "agendamento_codigo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Agendamento agendamentoCodigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "visitaCodigo")
    private List<StatusVisita> statusVisitaList;

    public Visita() {
    }

    public Visita(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Setor getSetorCodigo() {
        return setorCodigo;
    }

    public void setSetorCodigo(Setor setorCodigo) {
        this.setorCodigo = setorCodigo;
    }

    public Agendamento getAgendamentoCodigo() {
        return agendamentoCodigo;
    }

    public void setAgendamentoCodigo(Agendamento agendamentoCodigo) {
        this.agendamentoCodigo = agendamentoCodigo;
    }

    @XmlTransient
    public List<StatusVisita> getStatusVisitaList() {
        return statusVisitaList;
    }

    public void setStatusVisitaList(List<StatusVisita> statusVisitaList) {
        this.statusVisitaList = statusVisitaList;
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
        if (!(object instanceof Visita)) {
            return false;
        }
        Visita other = (Visita) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.gosv.model.Visita[ codigo=" + codigo + " ]";
    }
    
}
