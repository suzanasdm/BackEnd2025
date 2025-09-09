package dao;

import jakarta.persistence.EntityManager;
import model.Aluno;

import java.util.List;

public class AlunoDAO {

    //metodos de crud
    //create / read / update / delete

    EntityManager em;

    public AlunoDAO(EntityManager em) {
        this.em = em;
    }

    public List<Aluno> findAll() {
        return em.createQuery("SELECT a FROM Aluno a", Aluno.class)
                .getResultList();
    }

    public Aluno findByRa(String ra) { //HQL - Hibernate Query Language
        return em.createQuery("SELECT a FROM Aluno a WHERE a.ra = :ra", Aluno.class)
                .setParameter("ra", ra).getSingleResult();
    }

    public Aluno findById(Long id) {
        return em.find(Aluno.class, id);
    }

    public Aluno inserirAluno(Aluno aluno) {
        try {
            em.getTransaction().begin(); // abrir transacao com BD
            em.persist(aluno); //inserindo aluno no BD
            em.getTransaction().commit(); //confirma que é isso mesmo
            return aluno; //retorna o aluno com ID (gerado no BD)
        } catch (Exception ex) {
            em.getTransaction().rollback(); //desfazer a operação
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
            em.getTransaction().begin(); // abrir transacao com BD
            em.merge(aluno); //update do aluno no BD
            em.getTransaction().commit(); //confirma que é isso mesmo
            return aluno; //retorna o aluno com ID (gerado no BD)
        } catch (Exception ex) {
            em.getTransaction().rollback(); //desfazer a operação
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
            em.getTransaction().begin(); // abrir transacao com BD
            em.remove(aluno); //delete do aluno no BD
            em.getTransaction().commit(); //confirma que é isso mesmo
            return true; //retorna que deu certo a exclusão
        } catch (Exception ex) {
            em.getTransaction().rollback(); //desfazer a operação
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
