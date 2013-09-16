package br.com.gosv.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.gosv.dao.LocalizacaoDao;
import br.com.gosv.dao.LocalizacaoDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Localizacao;
import br.com.gosv.util.JPAUtil;

@ManagedBean(name="localizacaoMB")
@SessionScoped
public class LocalizacaoMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LocalizacaoDao localizaDao;
	private Localizacao localiza;
	private Localizacao localizaEd;
	private List<Localizacao>  localizacoes;
	
	public LocalizacaoMB(){
		this.localizaDao = new LocalizacaoDaoImp(JPAUtil.getEmf());
		this.localiza = new Localizacao();
		this.localizaEd = new Localizacao();
		this.localizacoes = null;
	}
	public void save(Localizacao localizacao){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		try {
			this.localizaDao.create(localizacao);
			severity = FacesMessage.SEVERITY_INFO;
			this.localiza = new Localizacao();
			this.localizacoes = this.localizaDao.findLocalizacaoEntities();
		} catch (Exception ex) {
			message = localiza.getDescricao() + " Erro - Valores incorretos " + ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new  FacesMessage(severity, message, null);
		context.addMessage(null, msg);
	}
	public void apagar(Localizacao localizacao){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		LocalizacaoDao localizaDao = new LocalizacaoDaoImp(JPAUtil.getEmf());
		try {
			localizaDao.destroy(localiza.getCodigo());
			message = " Localização " + localiza.getDescricao() + " - Removido ";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = this.localiza.getDescricao() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = this.localiza.getDescricao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}
	public void atualizar(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		LocalizacaoDao localizaDao = new LocalizacaoDaoImp(JPAUtil.getEmf());
		Localizacao localizaAtu;
		try {
			localizaAtu = localizaDao.findLocalizacao(this.localizaEd.getCodigo());
			localizaAtu.setDescricao(this.localizaEd.getDescricao());
			localizaAtu.setLatitude(this.localizaEd.getLatitude());
			localizaAtu.setLongitude(this.localizaEd.getLongitude());
			localizaAtu.setObservacao(this.localizaEd.getObservacao());
			localizaDao.edit(localizaAtu);
			message = " Localização " + localiza.getDescricao() + " - Alterado ";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = this.localiza.getDescricao() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = this.localiza.getDescricao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception e) {
			message = this.localiza.getDescricao() +  "  Exception ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );
	}
	public void edit(Localizacao localizacao){
		this.localizaEd = new Localizacao();
		this.localizaEd.setCodigo(localiza.getCodigo());
		this.localizaEd.setDescricao(localiza.getDescricao());
		this.localizaEd.setLatitude(localiza.getLatitude());
		this.localizaEd.setLongitude(localiza.getLongitude());
		this.localizaEd.setObservacao(localiza.getObservacao());
	}
	public LocalizacaoDao getLocalizaDao() {
		return localizaDao;
	}
	public void setLocalizaDao(LocalizacaoDao localizaDao) {
		this.localizaDao = localizaDao;
	}
	public Localizacao getLocaliza() {
		return localiza;
	}
	public void setLocaliza(Localizacao localiza) {
		this.localiza = localiza;
	}
	public Localizacao getLocalizaEd() {
		return localizaEd;
	}
	public void setLocalizaEd(Localizacao localizaEd) {
		this.localizaEd = localizaEd;
	}
	public List<Localizacao> getLocalizacoes() {
		this.localizacoes = localizaDao.findLocalizacaoEntities();
		return localizacoes;
	}
	public void setLocalizacoes(List<Localizacao> localizacoes) {
		this.localizacoes = localizacoes;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
