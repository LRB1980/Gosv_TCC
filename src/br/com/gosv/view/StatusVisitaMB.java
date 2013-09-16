package br.com.gosv.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.gosv.dao.StatusVisitaDao;
import br.com.gosv.dao.StatusVisitaDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.OrdemServico;
import br.com.gosv.model.StatusVisita;
import br.com.gosv.util.JPAUtil;

@ManagedBean(name="statusVisitaMB")
@ViewScoped
public class StatusVisitaMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StatusVisitaDao statusVisitaDao;
	private StatusVisita statusVisita;
	private StatusVisita statusVisitaEd;
	private List<StatusVisita> statusVisitas;
	
	public StatusVisitaMB(){
		this.statusVisitaDao = new StatusVisitaDaoImp(JPAUtil.getEmf());
		this.statusVisita = new StatusVisita();
		this.statusVisitaEd = new StatusVisita();
		this.statusVisitas = null;
		
	}
	public void save(StatusVisita statusVisita){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		try {
			this.statusVisitaDao.create(statusVisita);
			severity = FacesMessage.SEVERITY_INFO;
			this.statusVisita = new StatusVisita();
			this.statusVisitas = this.statusVisitaDao.findStatusVisitaEntities();
		} catch (Exception ex) {
			message = statusVisita.getObservacao() + " Erro - Valores incorretos " + ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new  FacesMessage(severity, message, null);
		context.addMessage(null, msg);
	}
	public void apagar(StatusVisita statusVisita){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		StatusVisitaDao statusVisitaDao = new StatusVisitaDaoImp(JPAUtil.getEmf());
		try {
			statusVisitaDao.destroy(statusVisita.getCodigo());
			message = " Ordem de Serviço " +  statusVisita.getObservacao() + " Removido ";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (NonexistentEntityException e) {
			message =   statusVisita.getObservacao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}
	public void atualizar(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		StatusVisitaDao statusVisitaDao = new StatusVisitaDaoImp(JPAUtil.getEmf());
		StatusVisita statusVisitaAtu;
		try {
			statusVisitaAtu = statusVisitaDao.findStatusVisita(this.statusVisitaEd.getCodigo());
			statusVisitaAtu.setObservacao(this.statusVisitaEd.getObservacao());
			statusVisitaAtu.setVisitaCodigo(this.statusVisitaEd.getVisitaCodigo());
			statusVisitaAtu.setSituacao(this.statusVisitaEd.getSituacao());
			statusVisitaDao.edit(statusVisitaAtu);
			message = " Ordem de Serviço " + statusVisita.getObservacao()  + " Alterado";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = statusVisita.getObservacao() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = statusVisita.getObservacao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception e) {
			message = statusVisita.getObservacao() +  "  - Exception ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );	
	}
	public void edit(OrdemServico ordemServico){
		this.statusVisitaEd = new StatusVisita();
		this.statusVisitaEd.setCodigo(statusVisita.getCodigo());
		this.statusVisitaEd.setObservacao(statusVisita.getObservacao());
		this.statusVisitaEd.setVisitaCodigo(statusVisita.getVisitaCodigo());
		this.statusVisitaEd.setSituacao(statusVisita.getSituacao());
	}
	public StatusVisitaDao getStatusVisitaDao() {
		return statusVisitaDao;
	}
	public void setStatusVisitaDao(StatusVisitaDao statusVisitaDao) {
		this.statusVisitaDao = statusVisitaDao;
	}
	public StatusVisita getStatusVisita() {
		return statusVisita;
	}
	public void setStatusVisita(StatusVisita statusVisita) {
		this.statusVisita = statusVisita;
	}
	public StatusVisita getStatusVisitaEd() {
		return statusVisitaEd;
	}
	public void setStatusVisitaEd(StatusVisita statusVisitaEd) {
		this.statusVisitaEd = statusVisitaEd;
	}
	public List<StatusVisita> getStatusVisitas() {
		this.statusVisitas = this.statusVisitaDao.findStatusVisitaEntities();
		return statusVisitas;
	}
	public void setStatusVisitas(List<StatusVisita> statusVisitas) {
		this.statusVisitas = statusVisitas;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
