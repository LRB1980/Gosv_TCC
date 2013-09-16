package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Funcionario;

public interface FuncionarioDao extends Serializable {

	public abstract EntityManager getEntityManager();

	public abstract void create(Funcionario funcionario);

	public abstract void edit(Funcionario funcionario)
			throws NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws NonexistentEntityException;

	public abstract List<Funcionario> findFuncionarioEntities();

	public abstract List<Funcionario> findFuncionarioEntities(int maxResults,
			int firstResult);

	public abstract Funcionario findFuncionario(Integer id);

	public abstract int getFuncionarioCount();

}