package dao;

import jakarta.persistence.EntityManager;
import model.Aluno;

import java.util.List;

public class AlunoDAO {




    EntityManager em;

    public AlunoDAO(EntityManager em) {
        this.em = em;
    }

    public List<Aluno> findAll() {
        return em.createQuery("SELECT a FROM Aluno a", Aluno.class)
                .getResultList();
    }

    public Aluno findByRa(String ra) {
        return em.createQuery("SELECT a FROM Aluno a WHERE a.ra = :ra", Aluno.class)
                .setParameter("ra", ra).getSingleResult();
    }

    public Aluno findById(Long id) {
        return em.find(Aluno.class, id);
    }

    public Aluno inserirAluno(Aluno aluno) {
        try {
            em.getTransaction().begin();
            em.persist(aluno);
            em.getTransaction().commit();
            return aluno;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Algo de errado não deu certo: " + ex.getMessage());
            return null;
        } finally {
            if (em.isOpen()) {
                em.close();
                System.out.println("EntityManager fechado com sucesso!");
            }
        }
    }

    public Aluno editarAluno(Aluno aluno) {
        try {
            em.getTransaction().begin();
            em.merge(aluno);
            em.getTransaction().commit();
            return aluno;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Algo de errado não deu certo: " + ex.getMessage());
            return null;
        } finally {
            if (em.isOpen()) {
                em.close();
                System.out.println("EntityManager fechado com sucesso!");
            }
        }
    }

    public Boolean deletarAluno(Aluno aluno) {
        try {
            em.getTransaction().begin();
            em.remove(aluno);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Algo de errado não deu certo: " + ex.getMessage());
            return false;
        } finally {
            if (em.isOpen()) {
                em.close();
                System.out.println("EntityManager fechado com sucesso!");
            }
        }
    }

}
