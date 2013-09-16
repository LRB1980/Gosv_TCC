package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Cliente;

public interface ClienteDao extends Serializable {

	public abstract EntityManager getEntityManager();

	public abstract void create(Cliente cliente);

	public abstract void edit(Cliente cliente) throws IllegalOrphanException,
			NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws IllegalOrphanException,
			NonexistentEntityException;

	public abstract List<Cliente> findClienteEntities();

	public abstract List<Cliente> findClienteEntities(int maxResults,
			int firstResult);

	public abstract Cliente findCliente(Integer id);

	public abstract int getClienteCount();

}