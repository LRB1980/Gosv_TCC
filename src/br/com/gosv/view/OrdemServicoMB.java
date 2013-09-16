package br.com.gosv.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.gosv.dao.OrdemServicoDao;
import br.com.gosv.dao.OrdemServicoDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.OrdemServico;
import br.com.gosv.util.JPAUtil;

@ManagedBean(name="ordemservicoMB")
@ViewScoped
public class OrdemServicoMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrdemServicoDao ordemDao;
	private OrdemServico ordem;
	private OrdemServico ordemEd;
	private List<OrdemServico> ordens;
	
	public OrdemServicoMB(){
		this.ordemDao = new OrdemServicoDaoImp(JPAUtil.getEmf());
		this.ordem = new OrdemServico();
		this.ordemEd = new OrdemServico();
		this.ordens = null;
	}
	public void save(OrdemServico ordemServico){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		try {
			this.ordemDao.create(ordemServico);
			severity = FacesMessage.SEVERITY_INFO;
			this.ordem = new OrdemServico();
			this.ordens = this.ordemDao.findOrdemServicoEntities();
		} catch (Exception ex) {
			message = ordem.getObservacao() + " Erro - Valores incorretos " + ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new  FacesMessage(severity, message, null);
		context.addMessage(null, msg);
	}
	public void apagar(OrdemServico ordemServico){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		OrdemServicoDao ordemDao = new OrdemServicoDaoImp(JPAUtil.getEmf());
		try {
			ordemDao.destroy(ordem.getCodigo());
			message = " Ordem de Serviço " + ordem.getObservacao() + " Removido ";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = ordem.getObservacao() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = ordem.getObservacao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}
	public void atualizar(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		OrdemServicoDao ordemDao = new OrdemServicoDaoImp(JPAUtil.getEmf());
		OrdemServico ordemAtu;
		try {
			ordemAtu = ordemDao.findOrdemServico(this.ordemEd.getCodigo());
			ordemAtu.setClienteCodigo(this.ordemEd.getClienteCodigo());
			ordemAtu.setObservacao(this.ordemEd.getObservacao());
			ordemAtu.setServicoCodigo(this.ordemEd.getServicoCodigo());
			ordemDao.edit(ordemAtu);
			message = " Ordem de Serviço " + ordem.getObservacao() + " Alterado";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = ordem.getObservacao() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = ordem.getObservacao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception e) {
			message = ordem.getObservacao() +  "  - Exception ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );	
	}
	public void edit(OrdemServico ordemServico){
		this.ordemEd = new OrdemServico();
		this.ordemEd.setCodigo (ordem.getCodigo());
		this.ordemEd.setClienteCodigo(ordem.getClienteCodigo());
		this.ordemEd.setObservacao(ordem.getObservacao());
		this.ordemEd.setServicoCodigo(ordem.getServicoCodigo());
	}
	public OrdemServicoDao getOrdemDao() {
		return ordemDao;
	}
	public void setOrdemDao(OrdemServicoDao ordemDao) {
		this.ordemDao = ordemDao;
	}
	public OrdemServico getOrdem() {
		return ordem;
	}
	public void setOrdem(OrdemServico ordem) {
		this.ordem = ordem;
	}
	public OrdemServico getOrdemEd() {
		return ordemEd;
	}
	public void setOrdemEd(OrdemServico ordemEd) {
		this.ordemEd = ordemEd;
	}
	public List<OrdemServico> getOrdens() {
		this.ordens = ordemDao.findOrdemServicoEntities();
		return ordens;
	}
	public void setOrdens(List<OrdemServico> ordens) {
		this.ordens = ordens;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
