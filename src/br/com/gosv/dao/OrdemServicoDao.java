package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.OrdemServico;

public interface OrdemServicoDao extends Serializable{

	public abstract EntityManager getEntityManager();

	public abstract void create(OrdemServico ordemServico);

	public abstract void edit(OrdemServico ordemServico)
			throws IllegalOrphanException, NonexistentEntityException,
			Exception;

	public abstract void destroy(Integer id) throws IllegalOrphanException,
			NonexistentEntityException;

	public abstract List<OrdemServico> findOrdemServicoEntities();

	public abstract List<OrdemServico> findOrdemServicoEntities(int maxResults,
			int firstResult);

	public abstract OrdemServico findOrdemServico(Integer id);

	public abstract int getOrdemServicoCount();

}