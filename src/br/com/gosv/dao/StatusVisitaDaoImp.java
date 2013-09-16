
package br.com.gosv.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.StatusVisita;
import br.com.gosv.model.Visita;

public class StatusVisitaDaoImp implements  StatusVisitaDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StatusVisitaDaoImp(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusVisitaDao#getEntityManager()
	 */
    @Override
	public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusVisitaDao#create(br.com.Gosv.model.StatusVisita)
	 */
    @Override
	public void create(StatusVisita statusVisita) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Visita visitaCodigo = statusVisita.getVisitaCodigo();
            if (visitaCodigo != null) {
                visitaCodigo = em.getReference(visitaCodigo.getClass(), visitaCodigo.getCodigo());
                statusVisita.setVisitaCodigo(visitaCodigo);
            }
            em.persist(statusVisita);
            if (visitaCodigo != null) {
                visitaCodigo.getStatusVisitaList().add(statusVisita);
                visitaCodigo = em.merge(visitaCodigo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusVisitaDao#edit(br.com.Gosv.model.StatusVisita)
	 */
    @Override
	public void edit(StatusVisita statusVisita) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StatusVisita persistentStatusVisita = em.find(StatusVisita.class, statusVisita.getCodigo());
            Visita visitaCodigoOld = persistentStatusVisita.getVisitaCodigo();
            Visita visitaCodigoNew = statusVisita.getVisitaCodigo();
            if (visitaCodigoNew != null) {
                visitaCodigoNew = em.getReference(visitaCodigoNew.getClass(), visitaCodigoNew.getCodigo());
                statusVisita.setVisitaCodigo(visitaCodigoNew);
            }
            statusVisita = em.merge(statusVisita);
            if (visitaCodigoOld != null && !visitaCodigoOld.equals(visitaCodigoNew)) {
                visitaCodigoOld.getStatusVisitaList().remove(statusVisita);
                visitaCodigoOld = em.merge(visitaCodigoOld);
            }
            if (visitaCodigoNew != null && !visitaCodigoNew.equals(visitaCodigoOld)) {
                visitaCodigoNew.getStatusVisitaList().add(statusVisita);
                visitaCodigoNew = em.merge(visitaCodigoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = statusVisita.getCodigo();
                if (findStatusVisita(id) == null) {
                    throw new NonexistentEntityException("The statusVisita with id " + id + " no longer exists.");
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
	 * @see br.com.Gosv.dao.StatusVisitaDao#destroy(java.lang.Integer)
	 */
    @Override
	public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StatusVisita statusVisita;
            try {
                statusVisita = em.getReference(StatusVisita.class, id);
                statusVisita.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The statusVisita with id " + id + " no longer exists.", enfe);
            }
            Visita visitaCodigo = statusVisita.getVisitaCodigo();
            if (visitaCodigo != null) {
                visitaCodigo.getStatusVisitaList().remove(statusVisita);
                visitaCodigo = em.merge(visitaCodigo);
            }
            em.remove(statusVisita);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusVisitaDao#findStatusVisitaEntities()
	 */
    @Override
	public List<StatusVisita> findStatusVisitaEntities() {
        return findStatusVisitaEntities(true, -1, -1);
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusVisitaDao#findStatusVisitaEntities(int, int)
	 */
    @Override
	public List<StatusVisita> findStatusVisitaEntities(int maxResults, int firstResult) {
        return findStatusVisitaEntities(false, maxResults, firstResult);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<StatusVisita> findStatusVisitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StatusVisita.class));
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
	 * @see br.com.Gosv.dao.StatusVisitaDao#findStatusVisita(java.lang.Integer)
	 */
    @Override
	public StatusVisita findStatusVisita(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StatusVisita.class, id);
        } finally {
            em.close();
        }
    }
    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusVisitaDao#getStatusVisitaCount()
	 */
    @Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public int getStatusVisitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StatusVisita> rt = cq.from(StatusVisita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
