
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
import br.com.gosv.model.Cliente;
import br.com.gosv.model.OrdemServico;
import br.com.gosv.model.Servico;
import br.com.gosv.model.StatusServico;


public class OrdemServicoDaoImp implements OrdemServicoDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrdemServicoDaoImp(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.OrdemServicoDao#getEntityManager()
	 */
    @Override
	public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.OrdemServicoDao#create(br.com.Gosv.model.OrdemServico)
	 */
    @Override
	public void create(OrdemServico ordemServico) {
        if (ordemServico.getStatusServicoList() == null) {
            ordemServico.setStatusServicoList(new ArrayList<StatusServico>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Servico servicoCodigo = ordemServico.getServicoCodigo();
            if (servicoCodigo != null) {
                servicoCodigo = em.getReference(servicoCodigo.getClass(), servicoCodigo.getCodigo());
                ordemServico.setServicoCodigo(servicoCodigo);
            }
            Cliente clienteCodigo = ordemServico.getClienteCodigo();
            if (clienteCodigo != null) {
                clienteCodigo = em.getReference(clienteCodigo.getClass(), clienteCodigo.getCodigo());
                ordemServico.setClienteCodigo(clienteCodigo);
            }
            List<StatusServico> attachedStatusServicoList = new ArrayList<StatusServico>();
            for (StatusServico statusServicoListStatusServicoToAttach : ordemServico.getStatusServicoList()) {
                statusServicoListStatusServicoToAttach = em.getReference(statusServicoListStatusServicoToAttach.getClass(), statusServicoListStatusServicoToAttach.getCodigo());
                attachedStatusServicoList.add(statusServicoListStatusServicoToAttach);
            }
            ordemServico.setStatusServicoList(attachedStatusServicoList);
            em.persist(ordemServico);
            if (servicoCodigo != null) {
                servicoCodigo.getOrdemServicoList().add(ordemServico);
                servicoCodigo = em.merge(servicoCodigo);
            }
            if (clienteCodigo != null) {
                clienteCodigo.getOrdemServicoList().add(ordemServico);
                clienteCodigo = em.merge(clienteCodigo);
            }
            for (StatusServico statusServicoListStatusServico : ordemServico.getStatusServicoList()) {
                OrdemServico oldOrdemServicoCodigoOfStatusServicoListStatusServico = statusServicoListStatusServico.getOrdemServicoCodigo();
                statusServicoListStatusServico.setOrdemServicoCodigo(ordemServico);
                statusServicoListStatusServico = em.merge(statusServicoListStatusServico);
                if (oldOrdemServicoCodigoOfStatusServicoListStatusServico != null) {
                    oldOrdemServicoCodigoOfStatusServicoListStatusServico.getStatusServicoList().remove(statusServicoListStatusServico);
                    oldOrdemServicoCodigoOfStatusServicoListStatusServico = em.merge(oldOrdemServicoCodigoOfStatusServicoListStatusServico);
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
	 * @see br.com.Gosv.dao.OrdemServicoDao#edit(br.com.Gosv.model.OrdemServico)
	 */
    @Override
	public void edit(OrdemServico ordemServico) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrdemServico persistentOrdemServico = em.find(OrdemServico.class, ordemServico.getCodigo());
            Servico servicoCodigoOld = persistentOrdemServico.getServicoCodigo();
            Servico servicoCodigoNew = ordemServico.getServicoCodigo();
            Cliente clienteCodigoOld = persistentOrdemServico.getClienteCodigo();
            Cliente clienteCodigoNew = ordemServico.getClienteCodigo();
            List<StatusServico> statusServicoListOld = persistentOrdemServico.getStatusServicoList();
            List<StatusServico> statusServicoListNew = ordemServico.getStatusServicoList();
            List<String> illegalOrphanMessages = null;
            for (StatusServico statusServicoListOldStatusServico : statusServicoListOld) {
                if (!statusServicoListNew.contains(statusServicoListOldStatusServico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StatusServico " + statusServicoListOldStatusServico + " since its ordemServicoCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (servicoCodigoNew != null) {
                servicoCodigoNew = em.getReference(servicoCodigoNew.getClass(), servicoCodigoNew.getCodigo());
                ordemServico.setServicoCodigo(servicoCodigoNew);
            }
            if (clienteCodigoNew != null) {
                clienteCodigoNew = em.getReference(clienteCodigoNew.getClass(), clienteCodigoNew.getCodigo());
                ordemServico.setClienteCodigo(clienteCodigoNew);
            }
            List<StatusServico> attachedStatusServicoListNew = new ArrayList<StatusServico>();
            for (StatusServico statusServicoListNewStatusServicoToAttach : statusServicoListNew) {
                statusServicoListNewStatusServicoToAttach = em.getReference(statusServicoListNewStatusServicoToAttach.getClass(), statusServicoListNewStatusServicoToAttach.getCodigo());
                attachedStatusServicoListNew.add(statusServicoListNewStatusServicoToAttach);
            }
            statusServicoListNew = attachedStatusServicoListNew;
            ordemServico.setStatusServicoList(statusServicoListNew);
            ordemServico = em.merge(ordemServico);
            if (servicoCodigoOld != null && !servicoCodigoOld.equals(servicoCodigoNew)) {
                servicoCodigoOld.getOrdemServicoList().remove(ordemServico);
                servicoCodigoOld = em.merge(servicoCodigoOld);
            }
            if (servicoCodigoNew != null && !servicoCodigoNew.equals(servicoCodigoOld)) {
                servicoCodigoNew.getOrdemServicoList().add(ordemServico);
                servicoCodigoNew = em.merge(servicoCodigoNew);
            }
            if (clienteCodigoOld != null && !clienteCodigoOld.equals(clienteCodigoNew)) {
                clienteCodigoOld.getOrdemServicoList().remove(ordemServico);
                clienteCodigoOld = em.merge(clienteCodigoOld);
            }
            if (clienteCodigoNew != null && !clienteCodigoNew.equals(clienteCodigoOld)) {
                clienteCodigoNew.getOrdemServicoList().add(ordemServico);
                clienteCodigoNew = em.merge(clienteCodigoNew);
            }
            for (StatusServico statusServicoListNewStatusServico : statusServicoListNew) {
                if (!statusServicoListOld.contains(statusServicoListNewStatusServico)) {
                    OrdemServico oldOrdemServicoCodigoOfStatusServicoListNewStatusServico = statusServicoListNewStatusServico.getOrdemServicoCodigo();
                    statusServicoListNewStatusServico.setOrdemServicoCodigo(ordemServico);
                    statusServicoListNewStatusServico = em.merge(statusServicoListNewStatusServico);
                    if (oldOrdemServicoCodigoOfStatusServicoListNewStatusServico != null && !oldOrdemServicoCodigoOfStatusServicoListNewStatusServico.equals(ordemServico)) {
                        oldOrdemServicoCodigoOfStatusServicoListNewStatusServico.getStatusServicoList().remove(statusServicoListNewStatusServico);
                        oldOrdemServicoCodigoOfStatusServicoListNewStatusServico = em.merge(oldOrdemServicoCodigoOfStatusServicoListNewStatusServico);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ordemServico.getCodigo();
                if (findOrdemServico(id) == null) {
                    throw new NonexistentEntityException("The ordemServico with id " + id + " no longer exists.");
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
	 * @see br.com.Gosv.dao.OrdemServicoDao#destroy(java.lang.Integer)
	 */
    @Override
	public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrdemServico ordemServico;
            try {
                ordemServico = em.getReference(OrdemServico.class, id);
                ordemServico.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ordemServico with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<StatusServico> statusServicoListOrphanCheck = ordemServico.getStatusServicoList();
            for (StatusServico statusServicoListOrphanCheckStatusServico : statusServicoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This OrdemServico (" + ordemServico + ") cannot be destroyed since the StatusServico " + statusServicoListOrphanCheckStatusServico + " in its statusServicoList field has a non-nullable ordemServicoCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Servico servicoCodigo = ordemServico.getServicoCodigo();
            if (servicoCodigo != null) {
                servicoCodigo.getOrdemServicoList().remove(ordemServico);
                servicoCodigo = em.merge(servicoCodigo);
            }
            Cliente clienteCodigo = ordemServico.getClienteCodigo();
            if (clienteCodigo != null) {
                clienteCodigo.getOrdemServicoList().remove(ordemServico);
                clienteCodigo = em.merge(clienteCodigo);
            }
            em.remove(ordemServico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.OrdemServicoDao#findOrdemServicoEntities()
	 */
    @Override
	public List<OrdemServico> findOrdemServicoEntities() {
        return findOrdemServicoEntities(true, -1, -1);
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.OrdemServicoDao#findOrdemServicoEntities(int, int)
	 */
    @Override
	public List<OrdemServico> findOrdemServicoEntities(int maxResults, int firstResult) {
        return findOrdemServicoEntities(false, maxResults, firstResult);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<OrdemServico> findOrdemServicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OrdemServico.class));
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
	 * @see br.com.Gosv.dao.OrdemServicoDao#findOrdemServico(java.lang.Integer)
	 */
    @Override
	public OrdemServico findOrdemServico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrdemServico.class, id);
        } finally {
            em.close();
        }
    }
    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.OrdemServicoDao#getOrdemServicoCount()
	 */
    @Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public int getOrdemServicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OrdemServico> rt = cq.from(OrdemServico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
