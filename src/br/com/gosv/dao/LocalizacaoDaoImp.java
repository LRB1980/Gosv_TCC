
package br.com.gosv.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.gosv.dao.exceptions.IllegalOrphanException;
import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Agendamento;
import br.com.gosv.model.Localizacao;

public class LocalizacaoDaoImp implements LocalizacaoDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LocalizacaoDaoImp(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.LocalizacaoDao#getEntityManager()
	 */
    @Override
	public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.LocalizacaoDao#create(br.com.Gosv.model.Localizacao)
	 */
    @Override
	public void create(Localizacao localizacao) {
        if (localizacao.getAgendamentoList() == null) {
            localizacao.setAgendamentoList(new ArrayList<Agendamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Agendamento> attachedAgendamentoList = new ArrayList<Agendamento>();
            for (Agendamento agendamentoListAgendamentoToAttach : localizacao.getAgendamentoList()) {
                agendamentoListAgendamentoToAttach = em.getReference(agendamentoListAgendamentoToAttach.getClass(), agendamentoListAgendamentoToAttach.getCodigo());
                attachedAgendamentoList.add(agendamentoListAgendamentoToAttach);
            }
            localizacao.setAgendamentoList(attachedAgendamentoList);
            em.persist(localizacao);
            for (Agendamento agendamentoListAgendamento : localizacao.getAgendamentoList()) {
                Localizacao oldLocalizacaoCodigoOfAgendamentoListAgendamento = agendamentoListAgendamento.getLocalizacaoCodigo();
                agendamentoListAgendamento.setLocalizacaoCodigo(localizacao);
                agendamentoListAgendamento = em.merge(agendamentoListAgendamento);
                if (oldLocalizacaoCodigoOfAgendamentoListAgendamento != null) {
                    oldLocalizacaoCodigoOfAgendamentoListAgendamento.getAgendamentoList().remove(agendamentoListAgendamento);
                    oldLocalizacaoCodigoOfAgendamentoListAgendamento = em.merge(oldLocalizacaoCodigoOfAgendamentoListAgendamento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.LocalizacaoDao#edit(br.com.Gosv.model.Localizacao)
	 */
    @Override
	public void edit(Localizacao localizacao) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localizacao persistentLocalizacao = em.find(Localizacao.class, localizacao.getCodigo());
            List<Agendamento> agendamentoListOld = persistentLocalizacao.getAgendamentoList();
            List<Agendamento> agendamentoListNew = localizacao.getAgendamentoList();
            List<String> illegalOrphanMessages = null;
            for (Agendamento agendamentoListOldAgendamento : agendamentoListOld) {
                if (!agendamentoListNew.contains(agendamentoListOldAgendamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Agendamento " + agendamentoListOldAgendamento + " since its localizacaoCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Agendamento> attachedAgendamentoListNew = new ArrayList<Agendamento>();
            for (Agendamento agendamentoListNewAgendamentoToAttach : agendamentoListNew) {
                agendamentoListNewAgendamentoToAttach = em.getReference(agendamentoListNewAgendamentoToAttach.getClass(), agendamentoListNewAgendamentoToAttach.getCodigo());
                attachedAgendamentoListNew.add(agendamentoListNewAgendamentoToAttach);
            }
            agendamentoListNew = attachedAgendamentoListNew;
            localizacao.setAgendamentoList(agendamentoListNew);
            localizacao = em.merge(localizacao);
            for (Agendamento agendamentoListNewAgendamento : agendamentoListNew) {
                if (!agendamentoListOld.contains(agendamentoListNewAgendamento)) {
                    Localizacao oldLocalizacaoCodigoOfAgendamentoListNewAgendamento = agendamentoListNewAgendamento.getLocalizacaoCodigo();
                    agendamentoListNewAgendamento.setLocalizacaoCodigo(localizacao);
                    agendamentoListNewAgendamento = em.merge(agendamentoListNewAgendamento);
                    if (oldLocalizacaoCodigoOfAgendamentoListNewAgendamento != null && !oldLocalizacaoCodigoOfAgendamentoListNewAgendamento.equals(localizacao)) {
                        oldLocalizacaoCodigoOfAgendamentoListNewAgendamento.getAgendamentoList().remove(agendamentoListNewAgendamento);
                        oldLocalizacaoCodigoOfAgendamentoListNewAgendamento = em.merge(oldLocalizacaoCodigoOfAgendamentoListNewAgendamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = localizacao.getCodigo();
                if (findLocalizacao(id) == null) {
                    throw new NonexistentEntityException("The localizacao with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.LocalizacaoDao#destroy(java.lang.Integer)
	 */
    @Override
	public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localizacao localizacao;
            try {
                localizacao = em.getReference(Localizacao.class, id);
                localizacao.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The localizacao with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Agendamento> agendamentoListOrphanCheck = localizacao.getAgendamentoList();
            for (Agendamento agendamentoListOrphanCheckAgendamento : agendamentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Localizacao (" + localizacao + ") cannot be destroyed since the Agendamento " + agendamentoListOrphanCheckAgendamento + " in its agendamentoList field has a non-nullable localizacaoCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(localizacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.LocalizacaoDao#findLocalizacaoEntities()
	 */
    @Override
	public List<Localizacao> findLocalizacaoEntities() {
        return findLocalizacaoEntities(true, -1, -1);
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.LocalizacaoDao#findLocalizacaoEntities(int, int)
	 */
    @Override
	public List<Localizacao> findLocalizacaoEntities(int maxResults, int firstResult) {
        return findLocalizacaoEntities(false, maxResults, firstResult);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<Localizacao> findLocalizacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Localizacao.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.LocalizacaoDao#findLocalizacao(java.lang.Integer)
	 */
    @Override
	public Localizacao findLocalizacao(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Localizacao.class, id);
        } finally {
            em.close();
        }
    }
    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.LocalizacaoDao#getLocalizacaoCount()
	 */
    @Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public int getLocalizacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Localizacao> rt = cq.from(Localizacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
