
import dao.AlunoDAO;
import dao.EnderecoDAO;
import model.Aluno;
import model.Endereco;
import util.EntityManagerUtil;
import jakarta.persistence.EntityManager;

import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerUtil.getEmf(); //abrindo o sistema e a conexao com o BD


        //Aluno aluno = new Aluno(); //criamos um objeto Aluno
        //aluno.setNome("Juvenal Amaral"); //juvenal tem um nome
        //aluno.setRa("60004869"); //juvenal tem ra
        //aluno.setTelefone("(45) 98593-2694"); //juvenal tem telefone
       // aluno.setEmail("jujuama@gmail.com"); //juvenal tem email
       // aluno.setData_nasc(new Date("01/09/2007")); //juvenal nasceu um dia

        //criamos o objeto DAO responsavel pelas transacoes com o BD
        //no construtor passamos uma nova EntityManager
        //AlunoDAO alunoDAO = new AlunoDAO(EntityManagerUtil.getEntityManager());
        //alunoDAO.inserirAluno(aluno); //solicitamos um insert do aluno no BD
        //alunoDAO.editarAluno(aluno);


        Endereco endereco = new Endereco();
        endereco.setCep("95684-44");
        endereco.setLogradouro("Rua Alguma Coisa");
        endereco.setBairro("Santo Amaro");
        endereco.setComplemento("N/A");
        endereco.setUf("PR");
        endereco.setLocalidade("Guarapuava");



        EnderecoDAO enderecoDAO = new EnderecoDAO(EntityManagerUtil.getEntityManager());
        enderecoDAO.checarCEPScanner();


        EntityManagerUtil.closeEntityManagerFactory();

    }

}