package model;


import jakarta.persistence.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@XmlRootElement(name = "xmlcep")
@XmlAccessorType(XmlAccessType.FIELD)
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private LocalDateTime dataHoraCadastro;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    public static Endereco unmarshalFromString(String stringXml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Endereco.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(stringXml);
        return (Endereco) unmarshaller.unmarshal(reader);
    }

}