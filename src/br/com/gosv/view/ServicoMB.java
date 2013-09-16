package br.com.gosv.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.gosv.dao.ServicoDao;
import br.com.gosv.dao.ServicoDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Servico;
import br.com.gosv.util.JPAUtil;

@ManagedBean(name="servicoMB")
@SessionScoped
public class ServicoMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServicoDao servicoDao;
	private Servico servico;
	private Servico servicoEd;
	private List<Servico> servicos;
	
	public ServicoMB(){
		this.servicoDao = new ServicoDaoImp(JPAUtil.getEmf());
		this.servico = new Servico();
		this.servicoEd = new Servico();
		this.servicos = null;
	}
	public void save(Servico servico){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		try {
			this.servicoDao.create(servico);
			message = "Servico " + servico.getDescricao() + " Salvo com sucesso";
			severity = FacesMessage.SEVERITY_INFO;
			this.servico = new Servico();
			this.servicos = this.servicoDao.findServicoEntities();
		} catch (Exception e) {
			message = servico.getDescricao() + " Erro - Valores incorretos " + e.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new  FacesMessage(severity, message, null);
		context.addMessage(null, msg);
	}
	public void apagar( Servico servico){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		ServicoDao servicoDao = new ServicoDaoImp(JPAUtil.getEmf());
		try {
			servicoDao.destroy(servico.getCodigo());
			message = " Serviço " + servico.getDescricao() + "Removido ";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = servico.getDescricao() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = servico.getDescricao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}

	public void atualizar(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		ServicoDao servicoDao = new ServicoDaoImp(JPAUtil.getEmf());
		Servico servicoAtu;
		try {
			servicoAtu = servicoDao.findServico(this.servicoEd.getCodigo());
			servicoAtu.setDescricao(this.servicoEd.getDescricao());
			servicoAtu.setSetorCodigo(this.servicoEd.getSetorCodigo());
			servicoAtu.setTipo(this.servicoEd.getTipo());
			servicoDao.edit(servicoAtu);
			message = " Serviço " + servico.getDescricao() + "Alterado";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = servico.getDescricao() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = servico.getDescricao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception e) {
			message = servico.getDescricao() + " Erro - Valores incorretos " + e.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}
	public void edit(Servico servico){
		this.servicoEd = new Servico();
		this.servicoEd.setCodigo(servico.getCodigo());
		this.servicoEd.setDescricao(servico.getDescricao());
		this.servicoEd.setSetorCodigo(servico.getSetorCodigo());
		this.servicoEd.setTipo(servico.getTipo());
	}
	public ServicoDao getServicoDao() {
		return servicoDao;
	}
	public void setServicoDao(ServicoDao servicoDao) {
		this.servicoDao = servicoDao;
	}
	public Servico getServico() {
		return servico;
	}
	public void setServico(Servico servico) {
		this.servico = servico;
	}
	public Servico getServicoEd() {
		return servicoEd;
	}
	public void setServicoEd(Servico servicoEd) {
		this.servicoEd = servicoEd;
	}
	public List<Servico> getServicos() {
		this.servicos = servicoDao.findServicoEntities();
		return servicos;
	}
	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
