package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Visita;

public interface VisitaDao extends Serializable {

	public abstract EntityManager getEntityManager();

	public abstract void create(Visita visita);

	public abstract void edit(Visita visita) throws IllegalOrphanException,
			NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws IllegalOrphanException,
			NonexistentEntityException;

	public abstract List<Visita> findVisitaEntities();

	public abstract List<Visita> findVisitaEntities(int maxResults,
			int firstResult);

	public abstract Visita findVisita(Integer id);

	public abstract int getVisitaCount();

}