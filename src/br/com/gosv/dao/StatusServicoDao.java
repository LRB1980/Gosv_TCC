package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.StatusServico;

public interface StatusServicoDao extends Serializable {

	public abstract EntityManager getEntityManager();

	public abstract void create(StatusServico statusServico);

	public abstract void edit(StatusServico statusServico)
			throws NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws NonexistentEntityException;

	public abstract List<StatusServico> findStatusServicoEntities();

	public abstract List<StatusServico> findStatusServicoEntities(
			int maxResults, int firstResult);

	public abstract StatusServico findStatusServico(Integer id);

	public abstract int getStatusServicoCount();

}