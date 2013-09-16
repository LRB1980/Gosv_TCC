package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Setor;

public interface SetorDao extends Serializable {

	public abstract EntityManager getEntityManager();

	public abstract void create(Setor setor);

	public abstract void edit(Setor setor) throws IllegalOrphanException,
			NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws IllegalOrphanException,
			NonexistentEntityException;

	public abstract List<Setor> findSetorEntities();

	public abstract List<Setor> findSetorEntities(int maxResults,
			int firstResult);

	public abstract Setor findSetor(Integer id);

	public abstract int getSetorCount();

}