package br.com.gosv.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.gosv.dao.StatusServicoDao;
import br.com.gosv.dao.StatusServicoDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.StatusServico;
import br.com.gosv.util.JPAUtil;

@ManagedBean(name="statusservicoMB")
@ViewScoped
public class StatusServicoMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StatusServicoDao statusServDao;
	private StatusServico statusServ;
	private StatusServico statusServEd;
	private List<StatusServico> statusServicos;
	
	public StatusServicoMB(){
		this.statusServDao = new StatusServicoDaoImp(JPAUtil.getEmf());
		this.statusServ = new StatusServico();
		this.statusServEd = new StatusServico();
		this.statusServicos = null;
		
	}
	public void save(StatusServico statusServico){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		try {
			this.statusServDao.create(statusServico);
			severity = FacesMessage.SEVERITY_INFO;
			this.statusServ = new StatusServico();
			this.statusServicos = this.statusServDao.findStatusServicoEntities();
		} catch (Exception ex) {
			message = statusServ.getObservacao() + " Erro - Valores incorretos " + ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new  FacesMessage(severity, message, null);
		context.addMessage(null, msg);
	}
	public void apagar(StatusServico statusServico){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		StatusServicoDao statusServDao = new StatusServicoDaoImp(JPAUtil.getEmf());
		try {
			statusServDao.destroy(statusServ.getCodigo());
			message = " Ordem de Serviço " +  statusServ.getObservacao() + " Removido ";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (NonexistentEntityException e) {
			message =   statusServ.getObservacao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}
	public void atualizar(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		StatusServicoDao statusServDao = new StatusServicoDaoImp(JPAUtil.getEmf());
		StatusServico statusServAtu;
		try {
			statusServAtu = statusServDao.findStatusServico(this.statusServEd.getCodigo());
			statusServAtu.setObservacao(this.statusServEd.getObservacao());
			statusServAtu.setOrdemServicoCodigo(this.statusServEd.getOrdemServicoCodigo());
			statusServAtu.setSituacao(this.statusServEd.getSituacao());
			statusServDao.edit(statusServAtu);
			message = " Ordem de Serviço " + statusServ.getObservacao()  + " Alterado";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = statusServ.getObservacao() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = statusServ.getObservacao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception e) {
			message = statusServ.getObservacao() +  "  - Exception ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );	
	}
	public void edit(StatusServico statusServico){
		this.statusServEd = new StatusServico();
		this.statusServEd.setCodigo(statusServ.getCodigo());
		this.statusServEd.setObservacao(statusServ.getObservacao());
		this.statusServEd.setOrdemServicoCodigo(statusServ.getOrdemServicoCodigo());
		this.statusServEd.setSituacao(statusServ.getSituacao());
	}
	public StatusServicoDao getStatusServDao() {
		return statusServDao;
	}
	public void setStatusServDao(StatusServicoDao statusServDao) {
		this.statusServDao = statusServDao;
	}
	public StatusServico getStatusServ() {
		return statusServ;
	}
	public void setStatusServ(StatusServico statusServ) {
		this.statusServ = statusServ;
	}
	public StatusServico getStatusServEd() {
		return statusServEd;
	}
	public void setStatusServEd(StatusServico statusServEd) {
		this.statusServEd = statusServEd;
	}
	public List<StatusServico> getStatusServicos() {
		this.statusServicos = statusServDao.findStatusServicoEntities();
		return statusServicos;
	}
	public void setStatusServicos(List<StatusServico> statusServicos) {
		this.statusServicos = statusServicos;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
