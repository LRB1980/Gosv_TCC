package br.com.gosv.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.gosv.dao.FuncionarioDao;
import br.com.gosv.dao.FuncionarioDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Funcionario;
import br.com.gosv.util.JPAUtil;

@ManagedBean (name="funcionarioMB")
@SessionScoped
public class FuncionarioMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FuncionarioDao funcionarioDao;
	private Funcionario funcionario;
	private Funcionario funcionarioEd;
	private List<Funcionario> funcionarios;
	
	public FuncionarioMB(){
		this.funcionarioDao = new FuncionarioDaoImp(JPAUtil.getEmf());
		this.funcionario = new Funcionario();
		this.funcionarioEd = new Funcionario();
		this.funcionarios = null;
	}
	
	public void save(Funcionario funcionario){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		try {
			this.funcionarioDao.create(funcionario);
			severity = FacesMessage.SEVERITY_INFO;
			this.funcionario = new Funcionario();
			this.funcionarios = this.funcionarioDao.findFuncionarioEntities();
		} catch (Exception e) {
			message = funcionario.getUsuario() + "Erro - Valores Incorretos " + e.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );
	}
	public void apagar(Funcionario funcionario){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		FuncionarioDao funcionarioDao = new FuncionarioDaoImp(JPAUtil.getEmf());
		try {
			funcionarioDao.destroy(funcionario.getCodigo());
			message = "Funcionario " + funcionario.getUsuario() + " Removido " ;
			severity = FacesMessage.SEVERITY_INFO;
		}catch (NonexistentEntityException e) {
			message = funcionario.getUsuario() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}
	public void atualizar(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		FuncionarioDao funcionarioDao = new FuncionarioDaoImp(JPAUtil.getEmf());
		Funcionario funcionarioAtu;
		try {
			funcionarioAtu = funcionarioDao.findFuncionario(this.funcionarioEd.getCodigo());
			funcionarioAtu.setDataRegistro(this.funcionarioEd.getDataRegistro());
			funcionarioAtu.setSenha(this.funcionarioEd.getSenha());
			funcionarioAtu.setSetorCodigo(this.funcionarioEd.getSetorCodigo());
			funcionarioAtu.setUsuario(this.funcionarioEd.getUsuario());
			funcionarioDao.edit(funcionarioAtu);
			message = this.funcionarioEd.getUsuario() + " - Alterado ";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = funcionario.getUsuario() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = funcionario.getUsuario() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception e) {
			message = funcionario.getUsuario() +  " - Exceotion ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}
	public void edit(Funcionario funcionario){
		this.funcionarioEd = new Funcionario();
		this.funcionarioEd.setCodigo(funcionario.getCodigo());
		this.funcionarioEd.setDataRegistro(funcionario.getDataRegistro());
		this.funcionarioEd.setSenha(funcionario.getSenha());
		this.funcionarioEd.setUsuario(funcionario.getUsuario());
		this.funcionarioEd.setSetorCodigo(funcionario.getSetorCodigo());
	}

	public FuncionarioDao getFuncionarioDao() {
		return funcionarioDao;
	}

	public void setFuncionarioDao(FuncionarioDao funcionarioDao) {
		this.funcionarioDao = funcionarioDao;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Funcionario getFuncionarioEd() {
		return funcionarioEd;
	}

	public void setFuncionarioEd(Funcionario funcionarioEd) {
		this.funcionarioEd = funcionarioEd;
	}

	public List<Funcionario> getFuncionarios() {
		this.funcionarios = funcionarioDao.findFuncionarioEntities();
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
