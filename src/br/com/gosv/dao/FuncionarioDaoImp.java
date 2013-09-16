
package br.com.gosv.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.gosv.dao.exceptions.NonexistentEntityException;
import br.com.gosv.model.Funcionario;
import br.com.gosv.model.Setor;


public class FuncionarioDaoImp implements  FuncionarioDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FuncionarioDaoImp(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.FuncionarioDao#getEntityManager()
	 */
    @Override
	public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.FuncionarioDao#create(br.com.Gosv.model.Funcionario)
	 */
    @Override
	public void create(Funcionario funcionario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Setor setorCodigo = funcionario.getSetorCodigo();
            if (setorCodigo != null) {
                setorCodigo = em.getReference(setorCodigo.getClass(), setorCodigo.getCodigo());
                funcionario.setSetorCodigo(setorCodigo);
            }
            em.persist(funcionario);
            if (setorCodigo != null) {
                setorCodigo.getFuncionarioList().add(funcionario);
                setorCodigo = em.merge(setorCodigo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.FuncionarioDao#edit(br.com.Gosv.model.Funcionario)
	 */
    @Override
	public void edit(Funcionario funcionario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario persistentFuncionario = em.find(Funcionario.class, funcionario.getCodigo());
            Setor setorCodigoOld = persistentFuncionario.getSetorCodigo();
            Setor setorCodigoNew = funcionario.getSetorCodigo();
            if (setorCodigoNew != null) {
                setorCodigoNew = em.getReference(setorCodigoNew.getClass(), setorCodigoNew.getCodigo());
                funcionario.setSetorCodigo(setorCodigoNew);
            }
            funcionario = em.merge(funcionario);
            if (setorCodigoOld != null && !setorCodigoOld.equals(setorCodigoNew)) {
                setorCodigoOld.getFuncionarioList().remove(funcionario);
                setorCodigoOld = em.merge(setorCodigoOld);
            }
            if (setorCodigoNew != null && !setorCodigoNew.equals(setorCodigoOld)) {
                setorCodigoNew.getFuncionarioList().add(funcionario);
                setorCodigoNew = em.merge(setorCodigoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = funcionario.getCodigo();
                if (findFuncionario(id) == null) {
                    throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.");
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
	 * @see br.com.Gosv.dao.FuncionarioDao#destroy(java.lang.Integer)
	 */
    @Override
	public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario funcionario;
            try {
                funcionario = em.getReference(Funcionario.class, id);
                funcionario.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.", enfe);
            }
            Setor setorCodigo = funcionario.getSetorCodigo();
            if (setorCodigo != null) {
                setorCodigo.getFuncionarioList().remove(funcionario);
                setorCodigo = em.merge(setorCodigo);
            }
            em.remove(funcionario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.FuncionarioDao#findFuncionarioEntities()
	 */
    @Override
	public List<Funcionario> findFuncionarioEntities() {
        return findFuncionarioEntities(true, -1, -1);
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.FuncionarioDao#findFuncionarioEntities(int, int)
	 */
    @Override
	public List<Funcionario> findFuncionarioEntities(int maxResults, int firstResult) {
        return findFuncionarioEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Funcionario> findFuncionarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Funcionario.class));
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
	 * @see br.com.Gosv.dao.FuncionarioDao#findFuncionario(java.lang.Integer)
	 */
    @Override
	public Funcionario findFuncionario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Funcionario.class, id);
        } finally {
            em.close();
        }
    }
    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.FuncionarioDao#getFuncionarioCount()
	 */
    @Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public int getFuncionarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Funcionario> rt = cq.from(Funcionario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
