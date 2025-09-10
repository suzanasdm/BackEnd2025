package app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Aluno;
import model.Endereco;
import util.EntityManagerUtil;
import viaCep.ViaCepClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CadastroCepApp {

    public static void main(String[] args) {
        EntityManagerUtil.getEmf(); // abre conexões
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Informe o CEP (com ou sem hífen): ");
            String cepEntrada = sc.nextLine();

            String cepFormatado = formataCep(cepEntrada);

            EntityManager em = EntityManagerUtil.getEntityManager();

            Endereco enderecoEncontrado = buscarEnderecoPorCep(em, cepFormatado);

            if (enderecoEncontrado != null) {
                System.out.println("✅ CEP encontrado no banco: " + cepFormatado);
                System.out.println("Logradouro: " + nullSafe(enderecoEncontrado.getLogradouro()) +
                        " | Bairro: " + nullSafe(enderecoEncontrado.getBairro()) +
                        " | Cidade/UF: " + nullSafe(enderecoEncontrado.getLocalidade()) + "/" + nullSafe(enderecoEncontrado.getUf()));

                System.out.println("\n➡ Será cadastrado um NOVO aluno associado a este endereço.");
                Aluno novoAluno = lerDadosAluno(sc);

                Endereco enderecoCopia = clonarEndereco(enderecoEncontrado);
                enderecoCopia.setDataHoraCadastro(LocalDateTime.now());
                enderecoCopia.setAluno(novoAluno);
                novoAluno.getEnderecos().add(enderecoCopia);

                persistirAlunoComEndereco(em, novoAluno, enderecoCopia);
                System.out.println("🎉 Aluno cadastrado e associado a uma cópia do endereço existente com sucesso!");

            } else {
                System.out.println("⚠️ CEP não encontrado no banco. Consultando ViaCEP...");
                Endereco viaCep = ViaCepClient.buscarEnderecoXML(cepFormatado);
                viaCep.setCep(cepFormatado);
                viaCep.setDataHoraCadastro(LocalDateTime.now());

                System.out.println("Endereço retornado: " + nullSafe(viaCep.getLogradouro()) + ", " +
                        nullSafe(viaCep.getBairro()) + " - " + nullSafe(viaCep.getLocalidade()) + "/" + nullSafe(viaCep.getUf()));

                Aluno novoAluno = lerDadosAluno(sc);
                viaCep.setAluno(novoAluno);
                novoAluno.getEnderecos().add(viaCep);

                persistirAlunoComEndereco(em, novoAluno, viaCep);
                System.out.println("🎉 Endereço criado via ViaCEP e aluno cadastrado com sucesso!");
            }

        } catch (Exception ex) {
            System.out.println("❌ Ocorreu um erro: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            EntityManagerUtil.closeEntityManagerFactory();
        }
    }

    private static Endereco buscarEnderecoPorCep(EntityManager em, String cep) {
        TypedQuery<Endereco> q = em.createQuery(
                "SELECT e FROM Endereco e WHERE e.cep = :cep ORDER BY e.id DESC", Endereco.class);
        q.setParameter("cep", cep);
        q.setMaxResults(1);
        List<Endereco> lista = q.getResultList();
        return lista.isEmpty() ? null : lista.get(0);
    }

    private static void persistirAlunoComEndereco(EntityManager em, Aluno aluno, Endereco endereco) {
        try {
            em.getTransaction().begin();
            em.persist(aluno);
            em.persist(endereco);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    private static Aluno lerDadosAluno(Scanner sc) throws ParseException {
        Aluno aluno = new Aluno();
        System.out.print("Nome do aluno: ");
        aluno.setNome(sc.nextLine());

        System.out.print("RA: ");
        aluno.setRa(sc.nextLine());

        System.out.print("Email: ");
        aluno.setEmail(sc.nextLine());

        System.out.print("Data de nascimento (dd/MM/yyyy): ");
        String dataStr = sc.nextLine();
        Date data = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
        aluno.setData_nasc(data);

        System.out.print("Telefone: ");
        aluno.setTelefone(sc.nextLine());
        return aluno;
    }

    private static String formataCep(String raw) {
        String digitos = raw.replaceAll("\\D", "");
        if (digitos.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos.");
        }
        return digitos.substring(0,5) + "-" + digitos.substring(5);
    }

    private static Endereco clonarEndereco(Endereco src) {
        Endereco e = new Endereco();
        e.setCep(src.getCep());
        e.setLogradouro(src.getLogradouro());
        e.setComplemento(src.getComplemento());
        e.setBairro(src.getBairro());
        e.setLocalidade(src.getLocalidade());
        e.setUf(src.getUf());
        return e;
    }

    private static String nullSafe(String s) {
        return s == null ? "" : s;
    }
}