package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Usuario;

public interface UsuarioDao extends Serializable {

	public abstract EntityManager getEntityManager();

	public abstract void create(Usuario usuario);

	public abstract void edit(Usuario usuario)
			throws NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws NonexistentEntityException;

	public abstract List<Usuario> findUsuarioEntities();

	public abstract List<Usuario> findUsuarioEntities(int maxResults,
			int firstResult);

	public abstract Usuario findUsuario(Integer id);

	public abstract int getUsuarioCount();

}