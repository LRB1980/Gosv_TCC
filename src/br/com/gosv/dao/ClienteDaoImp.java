
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
import br.com.gosv.model.OrdemServico;

public class ClienteDaoImp implements  ClienteDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClienteDaoImp(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.ClienteDao#getEntityManager()
	 */
    @Override
	public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.ClienteDao#create(br.com.Gosv.model.Cliente)
	 */
    @Override
	public void create(Cliente cliente) {
        if (cliente.getOrdemServicoList() == null) {
            cliente.setOrdemServicoList(new ArrayList<OrdemServico>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco enderecoCodigo = cliente.getEnderecoCodigo();
            if (enderecoCodigo != null) {
                enderecoCodigo = em.getReference(enderecoCodigo.getClass(), enderecoCodigo.getCodigo());
                cliente.setEnderecoCodigo(enderecoCodigo);
            }
            List<OrdemServico> attachedOrdemServicoList = new ArrayList<OrdemServico>();
            for (OrdemServico ordemServicoListOrdemServicoToAttach : cliente.getOrdemServicoList()) {
                ordemServicoListOrdemServicoToAttach = em.getReference(ordemServicoListOrdemServicoToAttach.getClass(), ordemServicoListOrdemServicoToAttach.getCodigo());
                attachedOrdemServicoList.add(ordemServicoListOrdemServicoToAttach);
            }
            cliente.setOrdemServicoList(attachedOrdemServicoList);
            em.persist(cliente);
            if (enderecoCodigo != null) {
                enderecoCodigo.getClienteList().add(cliente);
                enderecoCodigo = em.merge(enderecoCodigo);
            }
            for (OrdemServico ordemServicoListOrdemServico : cliente.getOrdemServicoList()) {
                Cliente oldClienteCodigoOfOrdemServicoListOrdemServico = ordemServicoListOrdemServico.getClienteCodigo();
                ordemServicoListOrdemServico.setClienteCodigo(cliente);
                ordemServicoListOrdemServico = em.merge(ordemServicoListOrdemServico);
                if (oldClienteCodigoOfOrdemServicoListOrdemServico != null) {
                    oldClienteCodigoOfOrdemServicoListOrdemServico.getOrdemServicoList().remove(ordemServicoListOrdemServico);
                    oldClienteCodigoOfOrdemServicoListOrdemServico = em.merge(oldClienteCodigoOfOrdemServicoListOrdemServico);
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
	 * @see br.com.Gosv.dao.ClienteDao#edit(br.com.Gosv.model.Cliente)
	 */
    @Override
	public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getCodigo());
            Endereco enderecoCodigoOld = persistentCliente.getEnderecoCodigo();
            Endereco enderecoCodigoNew = cliente.getEnderecoCodigo();
            List<OrdemServico> ordemServicoListOld = persistentCliente.getOrdemServicoList();
            List<OrdemServico> ordemServicoListNew = cliente.getOrdemServicoList();
            List<String> illegalOrphanMessages = null;
            for (OrdemServico ordemServicoListOldOrdemServico : ordemServicoListOld) {
                if (!ordemServicoListNew.contains(ordemServicoListOldOrdemServico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrdemServico " + ordemServicoListOldOrdemServico + " since its clienteCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (enderecoCodigoNew != null) {
                enderecoCodigoNew = em.getReference(enderecoCodigoNew.getClass(), enderecoCodigoNew.getCodigo());
                cliente.setEnderecoCodigo(enderecoCodigoNew);
            }
            List<OrdemServico> attachedOrdemServicoListNew = new ArrayList<OrdemServico>();
            for (OrdemServico ordemServicoListNewOrdemServicoToAttach : ordemServicoListNew) {
                ordemServicoListNewOrdemServicoToAttach = em.getReference(ordemServicoListNewOrdemServicoToAttach.getClass(), ordemServicoListNewOrdemServicoToAttach.getCodigo());
                attachedOrdemServicoListNew.add(ordemServicoListNewOrdemServicoToAttach);
            }
            ordemServicoListNew = attachedOrdemServicoListNew;
            cliente.setOrdemServicoList(ordemServicoListNew);
            cliente = em.merge(cliente);
            if (enderecoCodigoOld != null && !enderecoCodigoOld.equals(enderecoCodigoNew)) {
                enderecoCodigoOld.getClienteList().remove(cliente);
                enderecoCodigoOld = em.merge(enderecoCodigoOld);
            }
            if (enderecoCodigoNew != null && !enderecoCodigoNew.equals(enderecoCodigoOld)) {
                enderecoCodigoNew.getClienteList().add(cliente);
                enderecoCodigoNew = em.merge(enderecoCodigoNew);
            }
            for (OrdemServico ordemServicoListNewOrdemServico : ordemServicoListNew) {
                if (!ordemServicoListOld.contains(ordemServicoListNewOrdemServico)) {
                    Cliente oldClienteCodigoOfOrdemServicoListNewOrdemServico = ordemServicoListNewOrdemServico.getClienteCodigo();
                    ordemServicoListNewOrdemServico.setClienteCodigo(cliente);
                    ordemServicoListNewOrdemServico = em.merge(ordemServicoListNewOrdemServico);
                    if (oldClienteCodigoOfOrdemServicoListNewOrdemServico != null && !oldClienteCodigoOfOrdemServicoListNewOrdemServico.equals(cliente)) {
                        oldClienteCodigoOfOrdemServicoListNewOrdemServico.getOrdemServicoList().remove(ordemServicoListNewOrdemServico);
                        oldClienteCodigoOfOrdemServicoListNewOrdemServico = em.merge(oldClienteCodigoOfOrdemServicoListNewOrdemServico);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getCodigo();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
	 * @see br.com.Gosv.dao.ClienteDao#destroy(java.lang.Integer)
	 */
    @Override
	public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<OrdemServico> ordemServicoListOrphanCheck = cliente.getOrdemServicoList();
            for (OrdemServico ordemServicoListOrphanCheckOrdemServico : ordemServicoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the OrdemServico " + ordemServicoListOrphanCheckOrdemServico + " in its ordemServicoList field has a non-nullable clienteCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Endereco enderecoCodigo = cliente.getEnderecoCodigo();
            if (enderecoCodigo != null) {
                enderecoCodigo.getClienteList().remove(cliente);
                enderecoCodigo = em.merge(enderecoCodigo);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.ClienteDao#findClienteEntities()
	 */
    @Override
	public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.ClienteDao#findClienteEntities(int, int)
	 */
    @Override
	public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
	private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            @SuppressWarnings("rawtypes")
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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
	 * @see br.com.Gosv.dao.ClienteDao#findCliente(java.lang.Integer)
	 */
    @Override
	public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }
    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.ClienteDao#getClienteCount()
	 */
    @Override
	@SuppressWarnings("unchecked")
    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
        	@SuppressWarnings("rawtypes")
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
