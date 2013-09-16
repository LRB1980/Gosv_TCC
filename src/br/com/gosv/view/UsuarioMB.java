package br.com.gosv.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.gosv.dao.UsuarioDao;
import br.com.gosv.dao.UsuarioDaoImp;
import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Usuario;
import br.com.gosv.util.JPAUtil;

@ManagedBean(name="usuarioMB")
@SessionScoped
public class UsuarioMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UsuarioDao usuarioDao;
	private Usuario usuario;
	private Usuario usuarioEd;
	private List<Usuario> usuarios;
	
	public UsuarioMB(){
		this.usuarioDao = new UsuarioDaoImp(JPAUtil.getEmf());
		this.usuario = new Usuario();
		this.usuario = new Usuario();
		this.usuarios = null;
	}
	public void save(Usuario usuario){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		try {
			this.usuarioDao.create(usuario);
			severity = FacesMessage.SEVERITY_INFO;
			this.usuario = new Usuario();
			this.usuarios = this.usuarioDao.findUsuarioEntities();
		} catch (Exception ex) {
			message = usuario.getNome() + " Erro - Valores incorretos " + ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new  FacesMessage(severity, message, null);
		context.addMessage(null, msg);
	}
	public void apagar(Usuario usuario){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		UsuarioDao usuarioDao = new UsuarioDaoImp(JPAUtil.getEmf());
		try {
			usuarioDao.destroy(usuario.getCodigo());
			message = " Ordem de Serviço " + usuario.getNome() + " Removido ";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (NonexistentEntityException e) {
			message = usuario.getNome() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );		
	}
	public void atualizar(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage.Severity severity;
		String message = null;
		UsuarioDao usuarioDao = new UsuarioDaoImp(JPAUtil.getEmf());
		Usuario usuarioAtu;
		try {
			usuarioAtu = usuarioDao.findUsuario(this.usuarioEd.getCodigo());
			usuarioAtu.setNome(this.usuarioEd.getNome());
			usuarioAtu.setSenha(this.usuarioEd.getSenha());
			usuarioAtu.setTipoUser(this.usuarioEd.getTipoUser());
			usuarioDao.edit(usuarioAtu);
			message = " Ordem de Serviço " + usuario.getNome() + " Alterado";
			severity = FacesMessage.SEVERITY_INFO;
		}catch (IllegalOrphanException ex) {
			message = usuario.getNome() + "  não pode ser excluido devido seu relacionamento";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		catch (NonexistentEntityException e) {
			message = usuario.getNome() +  "  não exixte ";
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception e) {
			message = usuario.getNome() +  "  - Exception ";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage msg = new FacesMessage(severity, message, null);
		context.addMessage(null, msg );	
	}
	public void edit(){
		this.usuarioEd = new Usuario();
		this.usuarioEd.setNome(usuario.getNome());
		this.usuarioEd.setSenha(usuario.getSenha());
		this.usuarioEd.setTipoUser(usuario.getTipoUser());
	}
	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}
	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Usuario getUsuarioEd() {
		return usuarioEd;
	}
	public void setUsuarioEd(Usuario usuarioEd) {
		this.usuarioEd = usuarioEd;
	}
	public List<Usuario> getUsuarios() {
		this.usuarios = this.usuarioDao.findUsuarioEntities();
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
