package br.com.gosv.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Localizacao;

public interface LocalizacaoDao extends Serializable {

	public abstract EntityManager getEntityManager();

	public abstract void create(Localizacao localizacao);

	public abstract void edit(Localizacao localizacao)
			throws IllegalOrphanException, NonexistentEntityException,
			Exception;

	public abstract void destroy(Integer id) throws IllegalOrphanException,
			NonexistentEntityException;

	public abstract List<Localizacao> findLocalizacaoEntities();

	public abstract List<Localizacao> findLocalizacaoEntities(int maxResults,
			int firstResult);

	public abstract Localizacao findLocalizacao(Integer id);

	public abstract int getLocalizacaoCount();

}