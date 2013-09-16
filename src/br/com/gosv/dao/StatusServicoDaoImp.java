
package br.com.gosv.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.OrdemServico;
import br.com.gosv.model.StatusServico;

public class StatusServicoDaoImp implements StatusServicoDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StatusServicoDaoImp(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusServicoDao#getEntityManager()
	 */
    @Override
	public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusServicoDao#create(br.com.Gosv.model.StatusServico)
	 */
    @Override
	public void create(StatusServico statusServico) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrdemServico ordemServicoCodigo = statusServico.getOrdemServicoCodigo();
            if (ordemServicoCodigo != null) {
                ordemServicoCodigo = em.getReference(ordemServicoCodigo.getClass(), ordemServicoCodigo.getCodigo());
                statusServico.setOrdemServicoCodigo(ordemServicoCodigo);
            }
            em.persist(statusServico);
            if (ordemServicoCodigo != null) {
                ordemServicoCodigo.getStatusServicoList().add(statusServico);
                ordemServicoCodigo = em.merge(ordemServicoCodigo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusServicoDao#edit(br.com.Gosv.model.StatusServico)
	 */
    @Override
	public void edit(StatusServico statusServico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StatusServico persistentStatusServico = em.find(StatusServico.class, statusServico.getCodigo());
            OrdemServico ordemServicoCodigoOld = persistentStatusServico.getOrdemServicoCodigo();
            OrdemServico ordemServicoCodigoNew = statusServico.getOrdemServicoCodigo();
            if (ordemServicoCodigoNew != null) {
                ordemServicoCodigoNew = em.getReference(ordemServicoCodigoNew.getClass(), ordemServicoCodigoNew.getCodigo());
                statusServico.setOrdemServicoCodigo(ordemServicoCodigoNew);
            }
            statusServico = em.merge(statusServico);
            if (ordemServicoCodigoOld != null && !ordemServicoCodigoOld.equals(ordemServicoCodigoNew)) {
                ordemServicoCodigoOld.getStatusServicoList().remove(statusServico);
                ordemServicoCodigoOld = em.merge(ordemServicoCodigoOld);
            }
            if (ordemServicoCodigoNew != null && !ordemServicoCodigoNew.equals(ordemServicoCodigoOld)) {
                ordemServicoCodigoNew.getStatusServicoList().add(statusServico);
                ordemServicoCodigoNew = em.merge(ordemServicoCodigoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = statusServico.getCodigo();
                if (findStatusServico(id) == null) {
                    throw new NonexistentEntityException("The statusServico with id " + id + " no longer exists.");
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
	 * @see br.com.Gosv.dao.StatusServicoDao#destroy(java.lang.Integer)
	 */
    @Override
	public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StatusServico statusServico;
            try {
                statusServico = em.getReference(StatusServico.class, id);
                statusServico.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The statusServico with id " + id + " no longer exists.", enfe);
            }
            OrdemServico ordemServicoCodigo = statusServico.getOrdemServicoCodigo();
            if (ordemServicoCodigo != null) {
                ordemServicoCodigo.getStatusServicoList().remove(statusServico);
                ordemServicoCodigo = em.merge(ordemServicoCodigo);
            }
            em.remove(statusServico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusServicoDao#findStatusServicoEntities()
	 */
    @Override
	public List<StatusServico> findStatusServicoEntities() {
        return findStatusServicoEntities(true, -1, -1);
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusServicoDao#findStatusServicoEntities(int, int)
	 */
    @Override
	public List<StatusServico> findStatusServicoEntities(int maxResults, int firstResult) {
        return findStatusServicoEntities(false, maxResults, firstResult);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<StatusServico> findStatusServicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StatusServico.class));
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
	 * @see br.com.Gosv.dao.StatusServicoDao#findStatusServico(java.lang.Integer)
	 */
    @Override
	public StatusServico findStatusServico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StatusServico.class, id);
        } finally {
            em.close();
        }
    }
    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.StatusServicoDao#getStatusServicoCount()
	 */
    @Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public int getStatusServicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StatusServico> rt = cq.from(StatusServico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
