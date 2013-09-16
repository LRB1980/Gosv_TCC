
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
import br.com.gosv.model.Visita;

public class AgendamentoDaoImp implements AgendamentoDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AgendamentoDaoImp(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.AgendamentoDao#getEntityManager()
	 */
    @Override
	public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.AgendamentoDao#create(br.com.Gosv.model.Agendamento)
	 */
    @Override
	public void create(Agendamento agendamento) {
        if (agendamento.getVisitaList() == null) {
            agendamento.setVisitaList(new ArrayList<Visita>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localizacao localizacaoCodigo = agendamento.getLocalizacaoCodigo();
            if (localizacaoCodigo != null) {
                localizacaoCodigo = em.getReference(localizacaoCodigo.getClass(), localizacaoCodigo.getCodigo());
                agendamento.setLocalizacaoCodigo(localizacaoCodigo);
            }
            List<Visita> attachedVisitaList = new ArrayList<Visita>();
            for (Visita visitaListVisitaToAttach : agendamento.getVisitaList()) {
                visitaListVisitaToAttach = em.getReference(visitaListVisitaToAttach.getClass(), visitaListVisitaToAttach.getCodigo());
                attachedVisitaList.add(visitaListVisitaToAttach);
            }
            agendamento.setVisitaList(attachedVisitaList);
            em.persist(agendamento);
            if (localizacaoCodigo != null) {
                localizacaoCodigo.getAgendamentoList().add(agendamento);
                localizacaoCodigo = em.merge(localizacaoCodigo);
            }
            for (Visita visitaListVisita : agendamento.getVisitaList()) {
                Agendamento oldAgendamentoCodigoOfVisitaListVisita = visitaListVisita.getAgendamentoCodigo();
                visitaListVisita.setAgendamentoCodigo(agendamento);
                visitaListVisita = em.merge(visitaListVisita);
                if (oldAgendamentoCodigoOfVisitaListVisita != null) {
                    oldAgendamentoCodigoOfVisitaListVisita.getVisitaList().remove(visitaListVisita);
                    oldAgendamentoCodigoOfVisitaListVisita = em.merge(oldAgendamentoCodigoOfVisitaListVisita);
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
	 * @see br.com.Gosv.dao.AgendamentoDao#edit(br.com.Gosv.model.Agendamento)
	 */
    @Override
	public void edit(Agendamento agendamento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agendamento persistentAgendamento = em.find(Agendamento.class, agendamento.getCodigo());
            Localizacao localizacaoCodigoOld = persistentAgendamento.getLocalizacaoCodigo();
            Localizacao localizacaoCodigoNew = agendamento.getLocalizacaoCodigo();
            List<Visita> visitaListOld = persistentAgendamento.getVisitaList();
            List<Visita> visitaListNew = agendamento.getVisitaList();
            List<String> illegalOrphanMessages = null;
            for (Visita visitaListOldVisita : visitaListOld) {
                if (!visitaListNew.contains(visitaListOldVisita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Visita " + visitaListOldVisita + " since its agendamentoCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (localizacaoCodigoNew != null) {
                localizacaoCodigoNew = em.getReference(localizacaoCodigoNew.getClass(), localizacaoCodigoNew.getCodigo());
                agendamento.setLocalizacaoCodigo(localizacaoCodigoNew);
            }
            List<Visita> attachedVisitaListNew = new ArrayList<Visita>();
            for (Visita visitaListNewVisitaToAttach : visitaListNew) {
                visitaListNewVisitaToAttach = em.getReference(visitaListNewVisitaToAttach.getClass(), visitaListNewVisitaToAttach.getCodigo());
                attachedVisitaListNew.add(visitaListNewVisitaToAttach);
            }
            visitaListNew = attachedVisitaListNew;
            agendamento.setVisitaList(visitaListNew);
            agendamento = em.merge(agendamento);
            if (localizacaoCodigoOld != null && !localizacaoCodigoOld.equals(localizacaoCodigoNew)) {
                localizacaoCodigoOld.getAgendamentoList().remove(agendamento);
                localizacaoCodigoOld = em.merge(localizacaoCodigoOld);
            }
            if (localizacaoCodigoNew != null && !localizacaoCodigoNew.equals(localizacaoCodigoOld)) {
                localizacaoCodigoNew.getAgendamentoList().add(agendamento);
                localizacaoCodigoNew = em.merge(localizacaoCodigoNew);
            }
            for (Visita visitaListNewVisita : visitaListNew) {
                if (!visitaListOld.contains(visitaListNewVisita)) {
                    Agendamento oldAgendamentoCodigoOfVisitaListNewVisita = visitaListNewVisita.getAgendamentoCodigo();
                    visitaListNewVisita.setAgendamentoCodigo(agendamento);
                    visitaListNewVisita = em.merge(visitaListNewVisita);
                    if (oldAgendamentoCodigoOfVisitaListNewVisita != null && !oldAgendamentoCodigoOfVisitaListNewVisita.equals(agendamento)) {
                        oldAgendamentoCodigoOfVisitaListNewVisita.getVisitaList().remove(visitaListNewVisita);
                        oldAgendamentoCodigoOfVisitaListNewVisita = em.merge(oldAgendamentoCodigoOfVisitaListNewVisita);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = agendamento.getCodigo();
                if (findAgendamento(id) == null) {
                    throw new NonexistentEntityException("The agendamento with id " + id + " no longer exists.");
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
	 * @see br.com.Gosv.dao.AgendamentoDao#destroy(java.lang.Integer)
	 */
    @Override
	public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agendamento agendamento;
            try {
                agendamento = em.getReference(Agendamento.class, id);
                agendamento.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The agendamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Visita> visitaListOrphanCheck = agendamento.getVisitaList();
            for (Visita visitaListOrphanCheckVisita : visitaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Agendamento (" + agendamento + ") cannot be destroyed since the Visita " + visitaListOrphanCheckVisita + " in its visitaList field has a non-nullable agendamentoCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Localizacao localizacaoCodigo = agendamento.getLocalizacaoCodigo();
            if (localizacaoCodigo != null) {
                localizacaoCodigo.getAgendamentoList().remove(agendamento);
                localizacaoCodigo = em.merge(localizacaoCodigo);
            }
            em.remove(agendamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.AgendamentoDao#findAgendamentoEntities()
	 */
    @Override
	public List<Agendamento> findAgendamentoEntities() {
        return findAgendamentoEntities(true, -1, -1);
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.AgendamentoDao#findAgendamentoEntities(int, int)
	 */
    @Override
	public List<Agendamento> findAgendamentoEntities(int maxResults, int firstResult) {
        return findAgendamentoEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
	private List<Agendamento> findAgendamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            @SuppressWarnings("rawtypes")
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Agendamento.class));
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
	 * @see br.com.Gosv.dao.AgendamentoDao#findAgendamento(java.lang.Integer)
	 */
    @Override
	public Agendamento findAgendamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Agendamento.class, id);
        } finally {
            em.close();
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.AgendamentoDao#getAgendamentoCount()
	 */
    @Override
	@SuppressWarnings("unchecked")
	public int getAgendamentoCount() {
        EntityManager em = getEntityManager();
        try {
            @SuppressWarnings("rawtypes")
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			Root<Agendamento> rt = cq.from(Agendamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
