package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Agendamento;

public interface AgendamentoDao extends Serializable {

	public abstract EntityManager getEntityManager();

	public abstract void create(Agendamento agendamento);

	public abstract void edit(Agendamento agendamento)
			throws IllegalOrphanException, NonexistentEntityException,
			Exception;

	public abstract void destroy(Integer id) throws IllegalOrphanException,
			NonexistentEntityException;

	public abstract List<Agendamento> findAgendamentoEntities();

	public abstract List<Agendamento> findAgendamentoEntities(int maxResults,
			int firstResult);

	public abstract Agendamento findAgendamento(Integer id);

	public abstract int getAgendamentoCount();

}