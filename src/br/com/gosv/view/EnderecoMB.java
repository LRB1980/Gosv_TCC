package br.com.gosv.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;




import br.com.gosv.dao.EnderecoDao;
import br.com.gosv.dao.EnderecoDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Endereco;
import br.com.gosv.util.JPAUtil;

@ManagedBean (name="enderecoMB")
@SessionScoped
public class EnderecoMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EnderecoDao enderecoDao; //instancia do DAO
	private Endereco endereco; //instancia do model para novo endereco
	private Endereco enderecoEd;//instancia do model para editar
	private List<Endereco> enderecos; //instancia de uma lista de endereços
	
	public EnderecoMB(){
		this.enderecoDao = new EnderecoDaoImp(JPAUtil.getEmf());
		this.endereco = new Endereco();
		this.enderecoEd = new Endereco();
		this.enderecos = null;
	}
	public void save(Endereco endereco){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		try {
			this.enderecoDao.create(endereco);
			severity = FacesMessage.SEVERITY_ERROR;
			this.endereco = new Endereco();
			this.enderecos = this.enderecoDao.findEnderecoEntities();
		} catch (Exception e) {
			message = endereco.getLogradouro() + "Erro - Verificar informações ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg);
	}
	public void apagar(Endereco endereco){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message;
		EnderecoDao enderecoDao = new EnderecoDaoImp(JPAUtil.getEmf());
		try {//(obs: deve ser feito com todos os atributos da tabela do banco)
			enderecoDao.destroy(endereco.getCodigo());
			message = "Endereço " + endereco.getCodigo() + " Foi removido com sucesso ";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = this.enderecoEd.getLogradouro() + " Endereço não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = this.enderecoEd.getLogradouro() +  " Endereço não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}
	public void atualizar(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message;
		EnderecoDao enderecoDao = new EnderecoDaoImp(JPAUtil.getEmf());
		Endereco enderecoAtu;
		try{
			enderecoAtu = enderecoDao.findEndereco(this.enderecoEd.getCodigo());
			enderecoAtu.setBairro(this.enderecoEd.getBairro());
			enderecoAtu.setCel(this.enderecoEd.getCel());
			enderecoAtu.setCep(this.enderecoEd.getCep());
			enderecoAtu.setComplemento(this.enderecoEd.getComplemento());
			enderecoAtu.setDdd(this.enderecoEd.getDdd());
			enderecoAtu.setEmail(this.enderecoEd.getEmail());
			enderecoAtu.setLogradouro(this.enderecoEd.getLogradouro());
			enderecoAtu.setNumero(this.enderecoEd.getNumero());
			enderecoAtu.setTel(this.enderecoEd.getTel());
			enderecoAtu.setCidade(this.enderecoEd.getCidade());
			enderecoAtu.setEstado(this.enderecoEd.getEstado());
			enderecoAtu.setCidade(this.enderecoEd.getCidade());
			enderecoAtu.setEstado(this.enderecoEd.getEstado());
			enderecoDao.edit(enderecoAtu);
			message = this.enderecoEd.getLogradouro() +"- Alteração OK";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (IllegalOrphanException e) {
			message = this.enderecoEd.getLogradouro() + " Endereço não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (NonexistentEntityException e) {
			message = this.enderecoEd.getLogradouro() +  " Endereço não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception ex) {
			message = this.enderecoEd.getLogradouro() + "- Exception";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg);
	}
	public void edit(Endereco endereco){
		this.enderecoEd = new Endereco();
		this.enderecoEd.setCodigo(endereco.getCodigo());
		this.enderecoEd.setBairro(endereco.getBairro());
		this.enderecoEd.setCel(endereco.getCel());
		this.enderecoEd.setCep(endereco.getCep());
		this.enderecoEd.setComplemento(endereco.getComplemento());
		this.enderecoEd.setDdd(endereco.getDdd());
		this.enderecoEd.setEmail(endereco.getEmail());
		this.enderecoEd.setLogradouro(endereco.getLogradouro());
		this.enderecoEd.setNumero(endereco.getNumero());
		this.enderecoEd.setTel(endereco.getTel());
		this.enderecoEd.setCidade(endereco.getCidade() );
		this.enderecoEd.setEstado(endereco.getEstado());
	}

	
	public EnderecoDao getEnderecoDao() {
		return enderecoDao;
	}
	public void setEnderecoDao(EnderecoDao enderecoDao) {
		this.enderecoDao = enderecoDao;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public Endereco getEnderecoEd() {
		return enderecoEd;
	}
	public void setEnderecoEd(Endereco enderecoEd) {
		this.enderecoEd = enderecoEd;
	}
	public List<Endereco> getEnderecos() {
		this.enderecos = this.enderecoDao.findEnderecoEntities();
		return enderecos;
	}
	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}
	
	
	
}
