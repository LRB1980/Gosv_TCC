package br.com.gosv.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.gosv.dao.ClienteDao;
import br.com.gosv.dao.ClienteDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Cliente;
import br.com.gosv.model.Endereco;
import br.com.gosv.util.JPAUtil;

@ManagedBean (name="clienteMB")
@SessionScoped
public class ClienteMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ClienteDao clienteDao;
	private Cliente cliente;
	private Cliente clienteEd;
	private List<Cliente> clientes;
	private Endereco endereco;
	
	//criar uma nova instancia do cliente para (managedBean)
	public ClienteMB(){
		this.clienteDao = new ClienteDaoImp(JPAUtil.getEmf());
		this.cliente = new Cliente();
		this.clienteEd = new Cliente();
		this.clientes = null;
		
	}
	public void save(Cliente cliente){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		
		try {
			Date dataAtual = new Date(System.currentTimeMillis());
			cliente.setDataCadastro(dataAtual);
			cliente.setEnderecoCodigo(endereco);
			this.clienteDao.create(cliente);
			severity = FacesMessage.SEVERITY_INFO;
			this.cliente = new Cliente();
			this.clientes = this.clienteDao.findClienteEntities();
		} catch (Exception e) {
			message = cliente.getNome() + "Erro - valores incorretos" + e.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );
	}
	
	//metodo apagar
		public String apagar (Cliente cliente){ //esta string para poder retornar a mesma pagina
			System.out.println("Delete Cliente" + this.clienteEd.getNome());
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage.Severity severity;
			String message;
			ClienteDao clienteDao = new ClienteDaoImp(JPAUtil.getEmf());
			try {
				clienteDao.destroy(cliente.getCodigo());
				message = "Cliente " + cliente.getCodigo() + " Foi removido com sucesso ";
				severity = FacesMessage.SEVERITY_INFO;
			//	return "setor?faces-redirect=true" + message;
			}catch (IllegalOrphanException ex) {
				message = this.clienteEd.getNome() + " Cliente não pode ser excluido devido seu relacionamento";
				severity = FacesMessage.SEVERITY_ERROR;
			}
			catch (NonexistentEntityException e) {
				message = this.clienteEd.getNome() + " Setor não exixte ";
				severity = FacesMessage.SEVERITY_ERROR;
			}
			FacesMessage msg = new FacesMessage(severity, message, null);
			context.addMessage(null, msg );
			return  null;
			//return "setor?faces-redirect=true" + message; //retorno de pagina para renderização da tabela
		}
		public void atualizar(){
			System.out.println("Delete Cliente" + this.clienteEd.getNome());
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage.Severity severity;
			String message;
			ClienteDao clienteDao = new ClienteDaoImp(JPAUtil.getEmf());
			Cliente clienteAtualiza;
			try {
				clienteAtualiza = clienteDao.findCliente(this.clienteEd.getCodigo());
				clienteAtualiza.setNome(this.clienteEd.getNome());
				clienteAtualiza.setCpf(this.clienteEd.getCpf());
				clienteAtualiza.setRg(this.clienteEd.getRg());
				clienteAtualiza.setDataNacimento(this.clienteEd.getDataNacimento());
				clienteAtualiza.setCnpj(this.clienteEd.getCnpj());
				clienteAtualiza.setIe(this.clienteEd.getIe());
				//clienteAtualiza.setDataCadastro(this.clienteEd.getDataCadastro());
				clienteAtualiza.setAtivo(this.clienteEd.getAtivo());
				clienteAtualiza.setDescricao(this.clienteEd.getDescricao());
				clienteAtualiza.setHomepage(this.clienteEd.getHomepage());
				clienteAtualiza.setRazaoSocial(this.clienteEd.getRazaoSocial());
				clienteAtualiza.setEnderecoCodigo(this.clienteEd.getEnderecoCodigo());
				clienteDao.edit(clienteAtualiza);
				message = "Cliente " + cliente.getNome() + " Foi Atualizado com sucesso ";
				severity = FacesMessage.SEVERITY_INFO;
			}catch (IllegalOrphanException e) {
				message = cliente.getNome() + " Cliente não pode ser excluido devido seu relacionamento";
				severity = FacesMessage.SEVERITY_ERROR;
			}catch (NonexistentEntityException e) {
				message = cliente.getNome() + " Setor não exixte ";
				severity = FacesMessage.SEVERITY_ERROR;
			}catch (Exception ex) {
				message = clienteEd.getNome() + "- Exception";
				severity = FacesMessage.SEVERITY_ERROR;
			}
			FacesMessage msg = new FacesMessage(severity, message, null);
			context.addMessage(null, msg );
			//return "setor?faces-redirect=true" + message; //retorno de pagina para renderização da tabela
		}	
		public void edit(Cliente cliente){
			System.out.println("Editando Cliente " + cliente.getNome());
			//this....meu.clienteEd = novo Cliente();
			this.clienteEd = new Cliente();
			this.clienteEd.setCodigo(cliente.getCodigo());
			this.clienteEd.setNome(cliente.getNome());
			this.clienteEd.setCpf(cliente.getCpf());
			this.clienteEd.setRg(cliente.getRg());
			this.clienteEd.setDataNacimento(cliente.getDataNacimento());
			this.clienteEd.setCnpj(cliente.getCnpj());
			this.clienteEd.setIe(cliente.getIe());
			this.clienteEd.setDataCadastro(cliente.getDataCadastro());
			this.clienteEd.setAtivo(cliente.getAtivo());
			this.clienteEd.setDescricao(cliente.getDescricao());
			this.clienteEd.setHomepage(cliente.getHomepage());
			this.clienteEd.setRazaoSocial(cliente.getRazaoSocial());
			this.clienteEd.setEnderecoCodigo(cliente.getEnderecoCodigo());
		}
	public ClienteDao getClienteDao() {
		return clienteDao;
	}

	public void setClienteDao(ClienteDao clienteDao) {
		this.clienteDao = clienteDao;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getClienteEd() {
		return clienteEd;
	}

	public void setClienteEd(Cliente clienteEd) {
		this.clienteEd = clienteEd;
	}

	public List<Cliente> getClientes() {
		this.clientes = this.clienteDao.findClienteEntities();
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
/*	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}*/
	
}

