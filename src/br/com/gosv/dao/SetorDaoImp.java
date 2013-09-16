
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
import br.com.gosv.model.Funcionario;
import br.com.gosv.model.Servico;
import br.com.gosv.model.Setor;
import br.com.gosv.model.Visita;


public class SetorDaoImp implements  SetorDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SetorDaoImp(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.SetorDao#getEntityManager()
	 */
    @Override
	public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.SetorDao#create(br.com.Gosv.model.Setor)
	 */
    @Override
	public void create(Setor setor) {
        if (setor.getFuncionarioList() == null) {
            setor.setFuncionarioList(new ArrayList<Funcionario>());
        }
        if (setor.getVisitaList() == null) {
            setor.setVisitaList(new ArrayList<Visita>());
        }
        if (setor.getServicoList() == null) {
            setor.setServicoList(new ArrayList<Servico>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Funcionario> attachedFuncionarioList = new ArrayList<Funcionario>();
            for (Funcionario funcionarioListFuncionarioToAttach : setor.getFuncionarioList()) {
                funcionarioListFuncionarioToAttach = em.getReference(funcionarioListFuncionarioToAttach.getClass(), funcionarioListFuncionarioToAttach.getCodigo());
                attachedFuncionarioList.add(funcionarioListFuncionarioToAttach);
            }
            setor.setFuncionarioList(attachedFuncionarioList);
            List<Visita> attachedVisitaList = new ArrayList<Visita>();
            for (Visita visitaListVisitaToAttach : setor.getVisitaList()) {
                visitaListVisitaToAttach = em.getReference(visitaListVisitaToAttach.getClass(), visitaListVisitaToAttach.getCodigo());
                attachedVisitaList.add(visitaListVisitaToAttach);
            }
            setor.setVisitaList(attachedVisitaList);
            List<Servico> attachedServicoList = new ArrayList<Servico>();
            for (Servico servicoListServicoToAttach : setor.getServicoList()) {
                servicoListServicoToAttach = em.getReference(servicoListServicoToAttach.getClass(), servicoListServicoToAttach.getCodigo());
                attachedServicoList.add(servicoListServicoToAttach);
            }
            setor.setServicoList(attachedServicoList);
            em.persist(setor);
            for (Funcionario funcionarioListFuncionario : setor.getFuncionarioList()) {
                Setor oldSetorCodigoOfFuncionarioListFuncionario = funcionarioListFuncionario.getSetorCodigo();
                funcionarioListFuncionario.setSetorCodigo(setor);
                funcionarioListFuncionario = em.merge(funcionarioListFuncionario);
                if (oldSetorCodigoOfFuncionarioListFuncionario != null) {
                    oldSetorCodigoOfFuncionarioListFuncionario.getFuncionarioList().remove(funcionarioListFuncionario);
                    oldSetorCodigoOfFuncionarioListFuncionario = em.merge(oldSetorCodigoOfFuncionarioListFuncionario);
                }
            }
            for (Visita visitaListVisita : setor.getVisitaList()) {
                Setor oldSetorCodigoOfVisitaListVisita = visitaListVisita.getSetorCodigo();
                visitaListVisita.setSetorCodigo(setor);
                visitaListVisita = em.merge(visitaListVisita);
                if (oldSetorCodigoOfVisitaListVisita != null) {
                    oldSetorCodigoOfVisitaListVisita.getVisitaList().remove(visitaListVisita);
                    oldSetorCodigoOfVisitaListVisita = em.merge(oldSetorCodigoOfVisitaListVisita);
                }
            }
            for (Servico servicoListServico : setor.getServicoList()) {
                Setor oldSetorCodigoOfServicoListServico = servicoListServico.getSetorCodigo();
                servicoListServico.setSetorCodigo(setor);
                servicoListServico = em.merge(servicoListServico);
                if (oldSetorCodigoOfServicoListServico != null) {
                    oldSetorCodigoOfServicoListServico.getServicoList().remove(servicoListServico);
                    oldSetorCodigoOfServicoListServico = em.merge(oldSetorCodigoOfServicoListServico);
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
	 * @see br.com.Gosv.dao.SetorDao#edit(br.com.Gosv.model.Setor)
	 */
    @Override
	public void edit(Setor setor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Setor persistentSetor = em.find(Setor.class, setor.getCodigo());
            List<Funcionario> funcionarioListOld = persistentSetor.getFuncionarioList();
            List<Funcionario> funcionarioListNew = setor.getFuncionarioList();
            List<Visita> visitaListOld = persistentSetor.getVisitaList();
            List<Visita> visitaListNew = setor.getVisitaList();
            List<Servico> servicoListOld = persistentSetor.getServicoList();
            List<Servico> servicoListNew = setor.getServicoList();
            List<String> illegalOrphanMessages = null;
            for (Funcionario funcionarioListOldFuncionario : funcionarioListOld) {
                if (!funcionarioListNew.contains(funcionarioListOldFuncionario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Funcionario " + funcionarioListOldFuncionario + " since its setorCodigo field is not nullable.");
                }
            }
            for (Visita visitaListOldVisita : visitaListOld) {
                if (!visitaListNew.contains(visitaListOldVisita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Visita " + visitaListOldVisita + " since its setorCodigo field is not nullable.");
                }
            }
            for (Servico servicoListOldServico : servicoListOld) {
                if (!servicoListNew.contains(servicoListOldServico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Servico " + servicoListOldServico + " since its setorCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Funcionario> attachedFuncionarioListNew = new ArrayList<Funcionario>();
            for (Funcionario funcionarioListNewFuncionarioToAttach : funcionarioListNew) {
                funcionarioListNewFuncionarioToAttach = em.getReference(funcionarioListNewFuncionarioToAttach.getClass(), funcionarioListNewFuncionarioToAttach.getCodigo());
                attachedFuncionarioListNew.add(funcionarioListNewFuncionarioToAttach);
            }
            funcionarioListNew = attachedFuncionarioListNew;
            setor.setFuncionarioList(funcionarioListNew);
            List<Visita> attachedVisitaListNew = new ArrayList<Visita>();
            for (Visita visitaListNewVisitaToAttach : visitaListNew) {
                visitaListNewVisitaToAttach = em.getReference(visitaListNewVisitaToAttach.getClass(), visitaListNewVisitaToAttach.getCodigo());
                attachedVisitaListNew.add(visitaListNewVisitaToAttach);
            }
            visitaListNew = attachedVisitaListNew;
            setor.setVisitaList(visitaListNew);
            List<Servico> attachedServicoListNew = new ArrayList<Servico>();
            for (Servico servicoListNewServicoToAttach : servicoListNew) {
                servicoListNewServicoToAttach = em.getReference(servicoListNewServicoToAttach.getClass(), servicoListNewServicoToAttach.getCodigo());
                attachedServicoListNew.add(servicoListNewServicoToAttach);
            }
            servicoListNew = attachedServicoListNew;
            setor.setServicoList(servicoListNew);
            setor = em.merge(setor);
            for (Funcionario funcionarioListNewFuncionario : funcionarioListNew) {
                if (!funcionarioListOld.contains(funcionarioListNewFuncionario)) {
                    Setor oldSetorCodigoOfFuncionarioListNewFuncionario = funcionarioListNewFuncionario.getSetorCodigo();
                    funcionarioListNewFuncionario.setSetorCodigo(setor);
                    funcionarioListNewFuncionario = em.merge(funcionarioListNewFuncionario);
                    if (oldSetorCodigoOfFuncionarioListNewFuncionario != null && !oldSetorCodigoOfFuncionarioListNewFuncionario.equals(setor)) {
                        oldSetorCodigoOfFuncionarioListNewFuncionario.getFuncionarioList().remove(funcionarioListNewFuncionario);
                        oldSetorCodigoOfFuncionarioListNewFuncionario = em.merge(oldSetorCodigoOfFuncionarioListNewFuncionario);
                    }
                }
            }
            for (Visita visitaListNewVisita : visitaListNew) {
                if (!visitaListOld.contains(visitaListNewVisita)) {
                    Setor oldSetorCodigoOfVisitaListNewVisita = visitaListNewVisita.getSetorCodigo();
                    visitaListNewVisita.setSetorCodigo(setor);
                    visitaListNewVisita = em.merge(visitaListNewVisita);
                    if (oldSetorCodigoOfVisitaListNewVisita != null && !oldSetorCodigoOfVisitaListNewVisita.equals(setor)) {
                        oldSetorCodigoOfVisitaListNewVisita.getVisitaList().remove(visitaListNewVisita);
                        oldSetorCodigoOfVisitaListNewVisita = em.merge(oldSetorCodigoOfVisitaListNewVisita);
                    }
                }
            }
            for (Servico servicoListNewServico : servicoListNew) {
                if (!servicoListOld.contains(servicoListNewServico)) {
                    Setor oldSetorCodigoOfServicoListNewServico = servicoListNewServico.getSetorCodigo();
                    servicoListNewServico.setSetorCodigo(setor);
                    servicoListNewServico = em.merge(servicoListNewServico);
                    if (oldSetorCodigoOfServicoListNewServico != null && !oldSetorCodigoOfServicoListNewServico.equals(setor)) {
                        oldSetorCodigoOfServicoListNewServico.getServicoList().remove(servicoListNewServico);
                        oldSetorCodigoOfServicoListNewServico = em.merge(oldSetorCodigoOfServicoListNewServico);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = setor.getCodigo();
                if (findSetor(id) == null) {
                    throw new NonexistentEntityException("The setor with id " + id + " no longer exists.");
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
	 * @see br.com.Gosv.dao.SetorDao#destroy(java.lang.Integer)
	 */
    @Override
	public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Setor setor;
            try {
                setor = em.getReference(Setor.class, id);
                setor.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The setor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Funcionario> funcionarioListOrphanCheck = setor.getFuncionarioList();
            for (Funcionario funcionarioListOrphanCheckFuncionario : funcionarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Setor (" + setor + ") cannot be destroyed since the Funcionario " + funcionarioListOrphanCheckFuncionario + " in its funcionarioList field has a non-nullable setorCodigo field.");
            }
            List<Visita> visitaListOrphanCheck = setor.getVisitaList();
            for (Visita visitaListOrphanCheckVisita : visitaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Setor (" + setor + ") cannot be destroyed since the Visita " + visitaListOrphanCheckVisita + " in its visitaList field has a non-nullable setorCodigo field.");
            }
            List<Servico> servicoListOrphanCheck = setor.getServicoList();
            for (Servico servicoListOrphanCheckServico : servicoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Setor (" + setor + ") cannot be destroyed since the Servico " + servicoListOrphanCheckServico + " in its servicoList field has a non-nullable setorCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(setor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.SetorDao#findSetorEntities()
	 */
    @Override
	public List<Setor> findSetorEntities() {
        return findSetorEntities(true, -1, -1);
    }

    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.SetorDao#findSetorEntities(int, int)
	 */
    @Override
	public List<Setor> findSetorEntities(int maxResults, int firstResult) {
        return findSetorEntities(false, maxResults, firstResult);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<Setor> findSetorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Setor.class));
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
	 * @see br.com.Gosv.dao.SetorDao#findSetor(java.lang.Integer)
	 */
    @Override
	public Setor findSetor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Setor.class, id);
        } finally {
            em.close();
        }
    }
    /* (non-Javadoc)
	 * @see br.com.Gosv.dao.SetorDao#getSetorCount()
	 */
    @Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public int getSetorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Setor> rt = cq.from(Setor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
