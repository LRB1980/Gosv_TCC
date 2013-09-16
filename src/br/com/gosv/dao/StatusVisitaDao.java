package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.StatusVisita;

public interface StatusVisitaDao extends Serializable{

	public abstract EntityManager getEntityManager();

	public abstract void create(StatusVisita statusVisita);

	public abstract void edit(StatusVisita statusVisita)
			throws NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws NonexistentEntityException;

	public abstract List<StatusVisita> findStatusVisitaEntities();

	public abstract List<StatusVisita> findStatusVisitaEntities(int maxResults,
			int firstResult);

	public abstract StatusVisita findStatusVisita(Integer id);

	public abstract int getStatusVisitaCount();

}