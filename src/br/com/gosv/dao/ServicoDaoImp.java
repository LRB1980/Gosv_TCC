
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
import br.com.gosv.model.OrdemServico;
import br.com.gosv.model.Servico;
import br.com.gosv.model.Setor;


public class ServicoDaoImp implements  ServicoDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServicoDaoImp(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.ServicoDao#getEntityManager()
	 */
    @Override
	public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.ServicoDao#create(br.com.Gosv.model.Servico)
	 */
    @Override
	public void create(Servico servico) {
        if (servico.getOrdemServicoList() == null) {
            servico.setOrdemServicoList(new ArrayList<OrdemServico>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Setor setorCodigo = servico.getSetorCodigo();
            if (setorCodigo != null) {
                setorCodigo = em.getReference(setorCodigo.getClass(), setorCodigo.getCodigo());
                servico.setSetorCodigo(setorCodigo);
            }
            List<OrdemServico> attachedOrdemServicoList = new ArrayList<OrdemServico>();
            for (OrdemServico ordemServicoListOrdemServicoToAttach : servico.getOrdemServicoList()) {
                ordemServicoListOrdemServicoToAttach = em.getReference(ordemServicoListOrdemServicoToAttach.getClass(), ordemServicoListOrdemServicoToAttach.getCodigo());
                attachedOrdemServicoList.add(ordemServicoListOrdemServicoToAttach);
            }
            servico.setOrdemServicoList(attachedOrdemServicoList);
            em.persist(servico);
            if (setorCodigo != null) {
                setorCodigo.getServicoList().add(servico);
                setorCodigo = em.merge(setorCodigo);
            }
            for (OrdemServico ordemServicoListOrdemServico : servico.getOrdemServicoList()) {
                Servico oldServicoCodigoOfOrdemServicoListOrdemServico = ordemServicoListOrdemServico.getServicoCodigo();
                ordemServicoListOrdemServico.setServicoCodigo(servico);
                ordemServicoListOrdemServico = em.merge(ordemServicoListOrdemServico);
                if (oldServicoCodigoOfOrdemServicoListOrdemServico != null) {
                    oldServicoCodigoOfOrdemServicoListOrdemServico.getOrdemServicoList().remove(ordemServicoListOrdemServico);
                    oldServicoCodigoOfOrdemServicoListOrdemServico = em.merge(oldServicoCodigoOfOrdemServicoListOrdemServico);
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
	 * @see br.com.Gosv.dao.ServicoDao#edit(br.com.Gosv.model.Servico)
	 */
    @Override
	public void edit(Servico servico) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Servico persistentServico = em.find(Servico.class, servico.getCodigo());
            Setor setorCodigoOld = persistentServico.getSetorCodigo();
            Setor setorCodigoNew = servico.getSetorCodigo();
            List<OrdemServico> ordemServicoListOld = persistentServico.getOrdemServicoList();
            List<OrdemServico> ordemServicoListNew = servico.getOrdemServicoList();
            List<String> illegalOrphanMessages = null;
            for (OrdemServico ordemServicoListOldOrdemServico : ordemServicoListOld) {
                if (!ordemServicoListNew.contains(ordemServicoListOldOrdemServico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrdemServico " + ordemServicoListOldOrdemServico + " since its servicoCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (setorCodigoNew != null) {
                setorCodigoNew = em.getReference(setorCodigoNew.getClass(), setorCodigoNew.getCodigo());
                servico.setSetorCodigo(setorCodigoNew);
            }
            List<OrdemServico> attachedOrdemServicoListNew = new ArrayList<OrdemServico>();
            for (OrdemServico ordemServicoListNewOrdemServicoToAttach : ordemServicoListNew) {
                ordemServicoListNewOrdemServicoToAttach = em.getReference(ordemServicoListNewOrdemServicoToAttach.getClass(), ordemServicoListNewOrdemServicoToAttach.getCodigo());
                attachedOrdemServicoListNew.add(ordemServicoListNewOrdemServicoToAttach);
            }
            ordemServicoListNew = attachedOrdemServicoListNew;
            servico.setOrdemServicoList(ordemServicoListNew);
            servico = em.merge(servico);
            if (setorCodigoOld != null && !setorCodigoOld.equals(setorCodigoNew)) {
                setorCodigoOld.getServicoList().remove(servico);
                setorCodigoOld = em.merge(setorCodigoOld);
            }
            if (setorCodigoNew != null && !setorCodigoNew.equals(setorCodigoOld)) {
                setorCodigoNew.getServicoList().add(servico);
                setorCodigoNew = em.merge(setorCodigoNew);
            }
            for (OrdemServico ordemServicoListNewOrdemServico : ordemServicoListNew) {
                if (!ordemServicoListOld.contains(ordemServicoListNewOrdemServico)) {
                    Servico oldServicoCodigoOfOrdemServicoListNewOrdemServico = ordemServicoListNewOrdemServico.getServicoCodigo();
                    ordemServicoListNewOrdemServico.setServicoCodigo(servico);
                    ordemServicoListNewOrdemServico = em.merge(ordemServicoListNewOrdemServico);
                    if (oldServicoCodigoOfOrdemServicoListNewOrdemServico != null && !oldServicoCodigoOfOrdemServicoListNewOrdemServico.equals(servico)) {
                        oldServicoCodigoOfOrdemServicoListNewOrdemServico.getOrdemServicoList().remove(ordemServicoListNewOrdemServico);
                        oldServicoCodigoOfOrdemServicoListNewOrdemServico = em.merge(oldServicoCodigoOfOrdemServicoListNewOrdemServico);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = servico.getCodigo();
                if (findServico(id) == null) {
                    throw new NonexistentEntityException("The servico with id " + id + " no longer exists.");
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
	 * @see br.com.Gosv.dao.ServicoDao#destroy(java.lang.Integer)
	 */
    @Override
	public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Servico servico;
            try {
                servico = em.getReference(Servico.class, id);
                servico.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The servico with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<OrdemServico> ordemServicoListOrphanCheck = servico.getOrdemServicoList();
            for (OrdemServico ordemServicoListOrphanCheckOrdemServico : ordemServicoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Servico (" + servico + ") cannot be destroyed since the OrdemServico " + ordemServicoListOrphanCheckOrdemServico + " in its ordemServicoList field has a non-nullable servicoCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Setor setorCodigo = servico.getSetorCodigo();
            if (setorCodigo != null) {
                setorCodigo.getServicoList().remove(servico);
                setorCodigo = em.merge(setorCodigo);
            }
            em.remove(servico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.ServicoDao#findServicoEntities()
	 */
    @Override
	public List<Servico> findServicoEntities() {
        return findServicoEntities(true, -1, -1);
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.ServicoDao#findServicoEntities(int, int)
	 */
    @Override
	public List<Servico> findServicoEntities(int maxResults, int firstResult) {
        return findServicoEntities(false, maxResults, firstResult);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<Servico> findServicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Servico.class));
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
	 * @see br.com.Gosv.dao.ServicoDao#findServico(java.lang.Integer)
	 */
    @Override
	public Servico findServico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Servico.class, id);
        } finally {
            em.close();
        }
    }
    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.ServicoDao#getServicoCount()
	 */
    @Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public int getServicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Servico> rt = cq.from(Servico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
