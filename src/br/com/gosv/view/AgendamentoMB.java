package br.com.gosv.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.gosv.dao.AgendamentoDao;
import br.com.gosv.dao.AgendamentoDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Agendamento;
import br.com.gosv.util.JPAUtil;

@ManagedBean (name="agendamentoMB")
@SessionScoped
public class AgendamentoMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AgendamentoDao agendaDao;
	private Agendamento agenda;
	private Agendamento agendaEd;
	private List<Agendamento> agendas;
	
	public AgendamentoMB (){
		this.agendaDao = new AgendamentoDaoImp(JPAUtil.getEmf());
		this.agenda = new Agendamento();
		this.agendaEd = new Agendamento();
		this.agendas = null;
	}
	public void save(Agendamento agendamento){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		try{
			this.agendaDao.create(agendamento);
			severity = FacesMessage.SEVERITY_INFO;
			this.agenda = new Agendamento();
			this.agendas = this.agendaDao.findAgendamentoEntities();
		}catch ( Exception ex){
			message = agenda.getObservacao() + " Erro - Valores incorretos " + ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new  FacesMessage(severity, message, null);
		context.addMessage(null, msg);
	}
	public void apagar(Agendamento agendamento){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		AgendamentoDao agendaDao = new AgendamentoDaoImp(JPAUtil.getEmf());
		try {
			agendaDao.destroy(agenda.getCodigo());
			message = " Agendamento " + agenda.getObservacao() + " - Removido ";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
				message = this.agenda.getObservacao() + "  não pode ser excluido devido seu relacionamento";
				severity = FacesMessage.SEVERITY_ERROR;
			}
			catch (NonexistentEntityException e) {
				message = this.agenda.getObservacao() +  "  não exixte ";
				severity = FacesMessage.SEVERITY_ERROR;
			}
			FacesMessage msg = new FacesMessage(severity, message, null);
			context.addMessage(null, msg );		
		}
	public void atulaizar(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		AgendamentoDao agendaDao = new AgendamentoDaoImp(JPAUtil.getEmf());
		Agendamento agendaAtu;
		try {
			agendaAtu = agendaDao.findAgendamento(this.agendaEd.getCodigo());
			agendaAtu.setDataFinal(this.agendaEd.getDataFinal());
			agendaAtu.setDataInicial(this.agendaEd.getDataInicial());
			agendaAtu.setObservacao(this.agendaEd.getObservacao());
			agendaAtu.setLocalizacaoCodigo(this.agendaEd.getLocalizacaoCodigo());
			agendaDao.edit(agendaAtu);
			message = this.agendaEd.getObservacao() + " - Alterado";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (IllegalOrphanException ex) {
			message = this.agenda.getObservacao() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = this.agenda.getObservacao() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception e) {
			message = this.agenda.getObservacao() +  "  - Exception ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}
	public void edit(Agendamento agendamento){
		this.agendaEd = new Agendamento();
		this.agendaEd.setCodigo(agenda.getCodigo());
		this.agendaEd.setDataFinal(agenda.getDataFinal());
		this.agendaEd.setDataInicial(agenda.getDataInicial());
		this.agendaEd.setLocalizacaoCodigo(agenda.getLocalizacaoCodigo());
		this.agendaEd.setObservacao(agenda.getObservacao());
	}
	public AgendamentoDao getAgendaDao() {
		return agendaDao;
	}
	public void setAgendaDao(AgendamentoDao agendaDao) {
		this.agendaDao = agendaDao;
	}
	public Agendamento getAgenda() {
		return agenda;
	}
	public void setAgenda(Agendamento agenda) {
		this.agenda = agenda;
	}
	public Agendamento getAgendaEd() {
		return agendaEd;
	}
	public void setAgendaEd(Agendamento agendaEd) {
		this.agendaEd = agendaEd;
	}
	public List<Agendamento> getAgendas() {
		this.agendas = agendaDao.findAgendamentoEntities();
		return agendas;
	}
	public void setAgendas(List<Agendamento> agendas) {
		this.agendas = agendas;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
