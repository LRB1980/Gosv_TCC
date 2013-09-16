package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Endereco;

public interface EnderecoDao extends Serializable{

	public abstract EntityManager getEntityManager();

	public abstract void create(Endereco endereco);

	public abstract void edit(Endereco endereco) throws IllegalOrphanException,
			NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws IllegalOrphanException,
			NonexistentEntityException;

	public abstract List<Endereco> findEnderecoEntities();

	public abstract List<Endereco> findEnderecoEntities(int maxResults,
			int firstResult);

	public abstract Endereco findEndereco(Integer id);

	public abstract int getEnderecoCount();

}