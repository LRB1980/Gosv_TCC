
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
import br.com.gosv.model.Setor;
import br.com.gosv.model.StatusVisita;
import br.com.gosv.model.Visita;


public class VisitaDaoImp implements  VisitaDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VisitaDaoImp(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.VisitaDao#getEntityManager()
	 */
    @Override
	public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.VisitaDao#create(br.com.Gosv.model.Visita)
	 */
    @Override
	public void create(Visita visita) {
        if (visita.getStatusVisitaList() == null) {
            visita.setStatusVisitaList(new ArrayList<StatusVisita>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Setor setorCodigo = visita.getSetorCodigo();
            if (setorCodigo != null) {
                setorCodigo = em.getReference(setorCodigo.getClass(), setorCodigo.getCodigo());
                visita.setSetorCodigo(setorCodigo);
            }
            Agendamento agendamentoCodigo = visita.getAgendamentoCodigo();
            if (agendamentoCodigo != null) {
                agendamentoCodigo = em.getReference(agendamentoCodigo.getClass(), agendamentoCodigo.getCodigo());
                visita.setAgendamentoCodigo(agendamentoCodigo);
            }
            List<StatusVisita> attachedStatusVisitaList = new ArrayList<StatusVisita>();
            for (StatusVisita statusVisitaListStatusVisitaToAttach : visita.getStatusVisitaList()) {
                statusVisitaListStatusVisitaToAttach = em.getReference(statusVisitaListStatusVisitaToAttach.getClass(), statusVisitaListStatusVisitaToAttach.getCodigo());
                attachedStatusVisitaList.add(statusVisitaListStatusVisitaToAttach);
            }
            visita.setStatusVisitaList(attachedStatusVisitaList);
            em.persist(visita);
            if (setorCodigo != null) {
                setorCodigo.getVisitaList().add(visita);
                setorCodigo = em.merge(setorCodigo);
            }
            if (agendamentoCodigo != null) {
                agendamentoCodigo.getVisitaList().add(visita);
                agendamentoCodigo = em.merge(agendamentoCodigo);
            }
            for (StatusVisita statusVisitaListStatusVisita : visita.getStatusVisitaList()) {
                Visita oldVisitaCodigoOfStatusVisitaListStatusVisita = statusVisitaListStatusVisita.getVisitaCodigo();
                statusVisitaListStatusVisita.setVisitaCodigo(visita);
                statusVisitaListStatusVisita = em.merge(statusVisitaListStatusVisita);
                if (oldVisitaCodigoOfStatusVisitaListStatusVisita != null) {
                    oldVisitaCodigoOfStatusVisitaListStatusVisita.getStatusVisitaList().remove(statusVisitaListStatusVisita);
                    oldVisitaCodigoOfStatusVisitaListStatusVisita = em.merge(oldVisitaCodigoOfStatusVisitaListStatusVisita);
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
	 * @see br.com.Gosv.dao.VisitaDao#edit(br.com.Gosv.model.Visita)
	 */
    @Override
	public void edit(Visita visita) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Visita persistentVisita = em.find(Visita.class, visita.getCodigo());
            Setor setorCodigoOld = persistentVisita.getSetorCodigo();
            Setor setorCodigoNew = visita.getSetorCodigo();
            Agendamento agendamentoCodigoOld = persistentVisita.getAgendamentoCodigo();
            Agendamento agendamentoCodigoNew = visita.getAgendamentoCodigo();
            List<StatusVisita> statusVisitaListOld = persistentVisita.getStatusVisitaList();
            List<StatusVisita> statusVisitaListNew = visita.getStatusVisitaList();
            List<String> illegalOrphanMessages = null;
            for (StatusVisita statusVisitaListOldStatusVisita : statusVisitaListOld) {
                if (!statusVisitaListNew.contains(statusVisitaListOldStatusVisita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StatusVisita " + statusVisitaListOldStatusVisita + " since its visitaCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (setorCodigoNew != null) {
                setorCodigoNew = em.getReference(setorCodigoNew.getClass(), setorCodigoNew.getCodigo());
                visita.setSetorCodigo(setorCodigoNew);
            }
            if (agendamentoCodigoNew != null) {
                agendamentoCodigoNew = em.getReference(agendamentoCodigoNew.getClass(), agendamentoCodigoNew.getCodigo());
                visita.setAgendamentoCodigo(agendamentoCodigoNew);
            }
            List<StatusVisita> attachedStatusVisitaListNew = new ArrayList<StatusVisita>();
            for (StatusVisita statusVisitaListNewStatusVisitaToAttach : statusVisitaListNew) {
                statusVisitaListNewStatusVisitaToAttach = em.getReference(statusVisitaListNewStatusVisitaToAttach.getClass(), statusVisitaListNewStatusVisitaToAttach.getCodigo());
                attachedStatusVisitaListNew.add(statusVisitaListNewStatusVisitaToAttach);
            }
            statusVisitaListNew = attachedStatusVisitaListNew;
            visita.setStatusVisitaList(statusVisitaListNew);
            visita = em.merge(visita);
            if (setorCodigoOld != null && !setorCodigoOld.equals(setorCodigoNew)) {
                setorCodigoOld.getVisitaList().remove(visita);
                setorCodigoOld = em.merge(setorCodigoOld);
            }
            if (setorCodigoNew != null && !setorCodigoNew.equals(setorCodigoOld)) {
                setorCodigoNew.getVisitaList().add(visita);
                setorCodigoNew = em.merge(setorCodigoNew);
            }
            if (agendamentoCodigoOld != null && !agendamentoCodigoOld.equals(agendamentoCodigoNew)) {
                agendamentoCodigoOld.getVisitaList().remove(visita);
                agendamentoCodigoOld = em.merge(agendamentoCodigoOld);
            }
            if (agendamentoCodigoNew != null && !agendamentoCodigoNew.equals(agendamentoCodigoOld)) {
                agendamentoCodigoNew.getVisitaList().add(visita);
                agendamentoCodigoNew = em.merge(agendamentoCodigoNew);
            }
            for (StatusVisita statusVisitaListNewStatusVisita : statusVisitaListNew) {
                if (!statusVisitaListOld.contains(statusVisitaListNewStatusVisita)) {
                    Visita oldVisitaCodigoOfStatusVisitaListNewStatusVisita = statusVisitaListNewStatusVisita.getVisitaCodigo();
                    statusVisitaListNewStatusVisita.setVisitaCodigo(visita);
                    statusVisitaListNewStatusVisita = em.merge(statusVisitaListNewStatusVisita);
                    if (oldVisitaCodigoOfStatusVisitaListNewStatusVisita != null && !oldVisitaCodigoOfStatusVisitaListNewStatusVisita.equals(visita)) {
                        oldVisitaCodigoOfStatusVisitaListNewStatusVisita.getStatusVisitaList().remove(statusVisitaListNewStatusVisita);
                        oldVisitaCodigoOfStatusVisitaListNewStatusVisita = em.merge(oldVisitaCodigoOfStatusVisitaListNewStatusVisita);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = visita.getCodigo();
                if (findVisita(id) == null) {
                    throw new NonexistentEntityException("The visita with id " + id + " no longer exists.");
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
	 * @see br.com.Gosv.dao.VisitaDao#destroy(java.lang.Integer)
	 */
    @Override
	public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Visita visita;
            try {
                visita = em.getReference(Visita.class, id);
                visita.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The visita with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<StatusVisita> statusVisitaListOrphanCheck = visita.getStatusVisitaList();
            for (StatusVisita statusVisitaListOrphanCheckStatusVisita : statusVisitaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Visita (" + visita + ") cannot be destroyed since the StatusVisita " + statusVisitaListOrphanCheckStatusVisita + " in its statusVisitaList field has a non-nullable visitaCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Setor setorCodigo = visita.getSetorCodigo();
            if (setorCodigo != null) {
                setorCodigo.getVisitaList().remove(visita);
                setorCodigo = em.merge(setorCodigo);
            }
            Agendamento agendamentoCodigo = visita.getAgendamentoCodigo();
            if (agendamentoCodigo != null) {
                agendamentoCodigo.getVisitaList().remove(visita);
                agendamentoCodigo = em.merge(agendamentoCodigo);
            }
            em.remove(visita);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.VisitaDao#findVisitaEntities()
	 */
    @Override
	public List<Visita> findVisitaEntities() {
        return findVisitaEntities(true, -1, -1);
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.VisitaDao#findVisitaEntities(int, int)
	 */
    @Override
	public List<Visita> findVisitaEntities(int maxResults, int firstResult) {
        return findVisitaEntities(false, maxResults, firstResult);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<Visita> findVisitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Visita.class));
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
	 * @see br.com.Gosv.dao.VisitaDao#findVisita(java.lang.Integer)
	 */
    @Override
	public Visita findVisita(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Visita.class, id);
        } finally {
            em.close();
        }
    }
    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.VisitaDao#getVisitaCount()
	 */
    @Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public int getVisitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Visita> rt = cq.from(Visita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
