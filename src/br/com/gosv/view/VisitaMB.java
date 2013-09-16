package br.com.gosv.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.gosv.dao.VisitaDao;
import br.com.gosv.dao.VisitaDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Visita;
import br.com.gosv.util.JPAUtil;

@ManagedBean(name="visitaMB")
@ViewScoped
public class VisitaMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VisitaDao visitaDao;
	private Visita visita;
	private Visita visitaEd;
	private List<Visita> visitas;
	
	public VisitaMB(){
		this.visitaDao = new VisitaDaoImp(JPAUtil.getEmf());
		this.visita = new Visita();
		this.visitaEd = new Visita();
		this.visitas = null;
	}
	public void save(Visita visita){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		try {
			this.visitaDao.create(visita);
			severity = FacesMessage.SEVERITY_INFO;
			this.visita = new Visita();
			this.visitas = this.visitaDao.findVisitaEntities();
		} catch (Exception ex) {
			message = visita.getDescricao() + " Erro - Valores incorretos " + ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new  FacesMessage(severity, message, null);
		context.addMessage(null, msg);
	}
	public void apagar(Visita visita){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		VisitaDao visitaDao = new VisitaDaoImp(JPAUtil.getEmf());
		try {
			visitaDao.destroy(visita.getCodigo());
			message = " Ordem de Serviço " + visita.getDescricao() + " Removido ";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = visita.getDescricao() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = visita.getDescricao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}
	public void atualizar(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		VisitaDao visitaDao = new VisitaDaoImp(JPAUtil.getEmf());
		Visita visitaAtu;
		try {
			visitaAtu = visitaDao.findVisita(this.visitaEd.getCodigo());
			visitaAtu.setAgendamentoCodigo(this.visitaEd.getAgendamentoCodigo());
			visitaAtu.setDescricao(this.visitaEd.getDescricao());
			visitaAtu.setSetorCodigo(this.visitaEd.getSetorCodigo());
			visitaAtu.setTipo(this.visitaEd.getTipo());
			visitaDao.edit(visitaAtu);
			message = " Ordem de Serviço " + visita.getDescricao() + " Alterado";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = visita.getDescricao() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = visita.getDescricao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception e) {
			message = visita.getDescricao() +  "  - Exception ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );	
	}
	public void edit(){
		this.visitaEd = new Visita();
		this.visitaEd.setCodigo(visita.getCodigo());
		this.visitaEd.setAgendamentoCodigo(visita.getAgendamentoCodigo());
		this.visitaEd.setDescricao(visita.getDescricao());
		this.visitaEd.setSetorCodigo(visita.getSetorCodigo());
		this.visitaEd.setTipo(visita.getTipo());
	}
	public VisitaDao getVisitaDao() {
		return visitaDao;
	}
	public void setVisitaDao(VisitaDao visitaDao) {
		this.visitaDao = visitaDao;
	}
	public Visita getVisita() {
		return visita;
	}
	public void setVisita(Visita visita) {
		this.visita = visita;
	}
	public Visita getVisitaEd() {
		return visitaEd;
	}
	public void setVisitaEd(Visita visitaEd) {
		this.visitaEd = visitaEd;
	}
	public List<Visita> getVisitas() {
		this.visitas = this.visitaDao.findVisitaEntities();
		return visitas;
	}
	public void setVisitas(List<Visita> visitas) {
		this.visitas = visitas;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
