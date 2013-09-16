package br.com.gosv.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.gosv.dao.SetorDao;
import br.com.gosv.dao.SetorDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Setor;
import br.com.gosv.util.JPAUtil;


@ManagedBean (name="setorMB")
@SessionScoped
public class SetorMB  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SetorDao setorDao;
	//novo setor
	private Setor setor;
	//Edição Setor	
	private Setor setorEd;
	//lista de setores
	private List<Setor> setores;
	
	//Criando um nova instancia de SetorMB (ManagedBean)
	public SetorMB(){
		this.setorDao = new SetorDaoImp(JPAUtil.getEmf());
		this.setor = new Setor();
		this.setorEd = new Setor();
		this.setores = null;
	}
	
	

	//metodo salvar
	public void save(Setor setor){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message;
		try {
			this.setorDao.create(setor);
			message = "Setor " + setor.getDescricao() + " Salvo Com sucesso";
			severity = FacesMessage.SEVERITY_INFO;
			this.setor = new Setor();
			this.setores = this.setorDao.findSetorEntities();
			//return "setor?faces-redirect=true"; //retorno de pagina para renderização da tabela
		} catch (Exception e) {
			message = setor.getDescricao() + "Erro - valores incorretos" + e.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );
		//return "setor?faces-redirect=true";
	}
	//metodo apagar
	public String apagar (Setor setor){
		System.out.println("Delete Setor" + this.setorEd.getDescricao());
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message;
		SetorDao setorDao = new SetorDaoImp(JPAUtil.getEmf());
		try {//(obs: deve ser feito com todos os atributos da tabela do banco)
			setorDao.destroy(setor.getCodigo());
			message = "Setor " + setor.getDescricao() + " Foi removido com sucesso ";
			severity = FacesMessage.SEVERITY_INFO;
			return "setor?faces-redirect=true" + message;
		}catch (IllegalOrphanException ex) {
			message = this.setorEd.getDescricao() + "Setor não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = this.setorEd.getDescricao() + "Setor não exixte";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );
		return "setor?faces-redirect=true" + message; //retorno de pagina para renderização da tabela
	}
	//metodo Atualizar 
	public void atualizar(){
		System.out.println("Atualizando Setor" + setor.getDescricao());
		FacesMessage.Severity severity;
		String message;
		SetorDao setorDao = new SetorDaoImp(JPAUtil.getEmf());
		Setor setorAtu;
		try {//(obs: deve ser feito com todos os atributos da tabela do banco)
			setorAtu = setorDao.findSetor(this.setorEd.getCodigo());
			setorAtu.setDescricao(this.setorEd.getDescricao());
			setorAtu.setObservacao(this.setorEd.getObservacao());
			setorAtu.setResponsavel(this.setorEd.getResponsavel());
			setorDao.edit(setorAtu);
			message = setorEd.getDescricao() + "- Alteração OK";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (IllegalOrphanException e) {
			message = setor.getDescricao() + "Setor com Funcionarios";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (NonexistentEntityException e) {
			message = setor.getDescricao() + "Setor não exixte";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception ex) {
			message = setor.getDescricao() + "- Exception";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	//	return "setor?faces-redirect=true"; //retorno de pagina para renderização da tabela
	}
	//metodo editar
	public void edit(Setor setor){//(obs: deve ser feito com todos os atributos da tabela do banco)
		System.out.println("Editando Setor" + setor.getDescricao());
		this.setorEd = new Setor();
		this.setorEd.setCodigo(setor.getCodigo());
		this.setorEd.setDescricao(setor.getDescricao());
		this.setorEd.setObservacao(setor.getObservacao());
		this.setorEd.setResponsavel(setor.getResponsavel());
		
	}
	
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Setor getSetorEd() {
		return setorEd;
	}

	public void setSetorEd(Setor setorEd) {
		this.setorEd = setorEd;
	}

	public List<Setor> getSetores() {
		this.setores = setorDao.findSetorEntities();
		return setores;
	}

	public void setSetores(List<Setor> setores) {
		this.setores = setores;
	}
	public SetorDao getSetorDao() {
		return setorDao;
	}

	public void setSetorDao(SetorDao setorDao) {
		this.setorDao = setorDao;
	}
	
}

