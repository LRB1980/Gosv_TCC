package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Servico;

public interface ServicoDao extends Serializable {

	public abstract EntityManager getEntityManager();

	public abstract void create(Servico servico);

	public abstract void edit(Servico servico) throws IllegalOrphanException,
			NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws IllegalOrphanException,
			NonexistentEntityException;

	public abstract List<Servico> findServicoEntities();

	public abstract List<Servico> findServicoEntities(int maxResults,
			int firstResult);

	public abstract Servico findServico(Integer id);

	public abstract int getServicoCount();

}