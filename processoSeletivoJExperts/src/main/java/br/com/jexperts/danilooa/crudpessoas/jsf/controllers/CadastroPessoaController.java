package br.com.jexperts.danilooa.crudpessoas.jsf.controllers;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;

import br.com.jexperts.danilooa.crudpessoas.entity.Pessoa;
import br.com.jexperts.danilooa.crudpessoas.enums.GeneroEnum;
import br.com.jexperts.danilooa.crudpessoas.enums.Mensagens;
import br.com.jexperts.danilooa.crudpessoas.jsf.utils.JavaServerFacesUtils;
import br.com.jexperts.danilooa.crudpessoas.service.PessoaService;

@ManagedBean
@ViewScoped
public class CadastroPessoaController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PessoaService pessoaService;

    private Pessoa pessoa;

    private Long idPessoa;

    public String getTituloPagina() {
	if (idPessoa == null) {
	    return JavaServerFacesUtils.getMessage(Mensagens.CADASTRO_PESSOA.name());
	}
	return JavaServerFacesUtils.getMessage(Mensagens.ALTERACAO_PESSOA.name());
    }

    public GeneroEnum[] getGeneros() {
	return GeneroEnum.values();
    }

    public String cadastrar() {
	String outcome = "pessoas";
	String chaveMensagem = Mensagens.ALTERACAO_REALIZADA_COM_SUCESSO.name();

	if (pessoa.getId() == null) {
	    chaveMensagem = Mensagens.INCLUSAO_REALIZADA_COM_SUCESSO.name();
	}

	try {
	    pessoaService.inserir(pessoa);
	    JavaServerFacesUtils.adicionarMensagemInformacao(chaveMensagem);
	} catch (EJBException e) {
	    outcome = null;
	    JavaServerFacesUtils.tratarEJBException(e);
	}
	return outcome;
    }

    private boolean nomeASerBuscadoValidao(String nomeASerBuscado) {
	if (nomeASerBuscado == null) {
	    return false;
	}
	if (nomeASerBuscado.trim().length() < 4) {
	    return false;
	}
	return true;
    }

    public List<Pessoa> pesquisarMaePorNome(String nomeASerBuscado) {
	if (!nomeASerBuscadoValidao(nomeASerBuscado)) {
	    return new ArrayList<Pessoa>();
	}

	return pessoaService.listarPorNome(nomeASerBuscado, 5, pessoa);
    }

    public List<Pessoa> pesquisarPaiPorNome(String nomeASerBuscado) {
	if (!nomeASerBuscadoValidao(nomeASerBuscado)) {
	    return new ArrayList<Pessoa>();
	}

	return pessoaService.listarPorNome(nomeASerBuscado, 5, pessoa);
    }

    public void tratarUploadImagem(FileUploadEvent event) {
	byte[] imagem = new byte[(int) event.getFile().getSize()];
	pessoa.setImagem(imagem);
	DataInputStream dataInputStream = null;

	try {
	    dataInputStream = new DataInputStream(event.getFile().getInputstream());
	    dataInputStream.readFully(imagem);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
	session.setAttribute("imagem", imagem);
    }

    public Long getIdPessoa() {
	return idPessoa;
    }

    public void setIdPessoa(Long idPessoa) {
	this.idPessoa = idPessoa;
    }

    public Pessoa getPessoa() {
	if (pessoa != null) {
	    return pessoa;
	}
	if (idPessoa != null) {
	    pessoa = pessoaService.getPessoa(idPessoa);
	    return pessoa;
	}
	pessoa = new Pessoa();
	return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
	this.pessoa = pessoa;
    }

    public boolean permitirQueCpfSejaEditado(){
	return pessoa.getId() == null;
    }
}
