
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
import br.com.gosv.model.Endereco;


public class EnderecoDaoImp implements  EnderecoDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EnderecoDaoImp(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.EnderecoDao#getEntityManager()
	 */
    @Override
	public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.EnderecoDao#create(br.com.Gosv.model.Endereco)
	 */
    @Override
	public void create(Endereco endereco) {
        if (endereco.getClienteList() == null) {
            endereco.setClienteList(new ArrayList<Cliente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : endereco.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getCodigo());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            endereco.setClienteList(attachedClienteList);
            em.persist(endereco);
            for (Cliente clienteListCliente : endereco.getClienteList()) {
                Endereco oldEnderecoCodigoOfClienteListCliente = clienteListCliente.getEnderecoCodigo();
                clienteListCliente.setEnderecoCodigo(endereco);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldEnderecoCodigoOfClienteListCliente != null) {
                    oldEnderecoCodigoOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldEnderecoCodigoOfClienteListCliente = em.merge(oldEnderecoCodigoOfClienteListCliente);
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
	 * @see br.com.Gosv.dao.EnderecoDao#edit(br.com.Gosv.model.Endereco)
	 */
    @Override
	public void edit(Endereco endereco) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco persistentEndereco = em.find(Endereco.class, endereco.getCodigo());
            List<Cliente> clienteListOld = persistentEndereco.getClienteList();
            List<Cliente> clienteListNew = endereco.getClienteList();
            List<String> illegalOrphanMessages = null;
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteListOldCliente + " since its enderecoCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getCodigo());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            endereco.setClienteList(clienteListNew);
            endereco = em.merge(endereco);
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Endereco oldEnderecoCodigoOfClienteListNewCliente = clienteListNewCliente.getEnderecoCodigo();
                    clienteListNewCliente.setEnderecoCodigo(endereco);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldEnderecoCodigoOfClienteListNewCliente != null && !oldEnderecoCodigoOfClienteListNewCliente.equals(endereco)) {
                        oldEnderecoCodigoOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldEnderecoCodigoOfClienteListNewCliente = em.merge(oldEnderecoCodigoOfClienteListNewCliente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = endereco.getCodigo();
                if (findEndereco(id) == null) {
                    throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.");
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
	 * @see br.com.Gosv.dao.EnderecoDao#destroy(java.lang.Integer)
	 */
    @Override
	public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco endereco;
            try {
                endereco = em.getReference(Endereco.class, id);
                endereco.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cliente> clienteListOrphanCheck = endereco.getClienteList();
            for (Cliente clienteListOrphanCheckCliente : clienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Endereco (" + endereco + ") cannot be destroyed since the Cliente " + clienteListOrphanCheckCliente + " in its clienteList field has a non-nullable enderecoCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(endereco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.EnderecoDao#findEnderecoEntities()
	 */
    @Override
	public List<Endereco> findEnderecoEntities() {
        return findEnderecoEntities(true, -1, -1);
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.EnderecoDao#findEnderecoEntities(int, int)
	 */
    @Override
	public List<Endereco> findEnderecoEntities(int maxResults, int firstResult) {
        return findEnderecoEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
	private List<Endereco> findEnderecoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            @SuppressWarnings("rawtypes")
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Endereco.class));
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
	 * @see br.com.Gosv.dao.EnderecoDao#findEndereco(java.lang.Integer)
	 */
    @Override
	public Endereco findEndereco(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Endereco.class, id);
        } finally {
            em.close();
        }
    }
    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.EnderecoDao#getEnderecoCount()
	 */
    @Override
	@SuppressWarnings("unchecked")
    public int getEnderecoCount() {
        EntityManager em = getEntityManager();
        try {
        	 @SuppressWarnings("rawtypes")
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Endereco> rt = cq.from(Endereco.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
