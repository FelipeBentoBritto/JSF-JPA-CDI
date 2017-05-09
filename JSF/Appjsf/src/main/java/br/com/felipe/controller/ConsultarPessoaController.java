package br.com.felipe.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.felipe.model.Pessoa;
import br.com.felipe.repository.PessoaRepository;

@Named(value="ConsultarPessoaController")
@ViewScoped
public class ConsultarPessoaController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject transient
	private Pessoa pessoa;
	
	@Produces
	private List<Pessoa> pessoas;
	
	@Inject transient
	private PessoaRepository pessoaRepository;
	
	public List<Pessoa> getPessoas() {
		return pessoas;
	}
	
	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	//CARREGAS AS PESSOAS NA INICIALIZAÇÃO
	
	@PostConstruct
	public void init() {
		//RETORNAR AS PESSOAS CADASTRADAS
		this.pessoas = pessoaRepository.getPessoas();
	}


	//CARREGA INFORMAÇÕES DE UMA PESSOA PARA SER EDITADA
	//@param pessoaModel

	public void editar(Pessoa pessoa) {
		//PEGA APENAS A PRIMEIRA LETRA DO SEXO PARA SETAR NO CAMPO(M OU F)
		pessoa.setSexo(pessoa.getSexo().substring(0,1));
		
		this.pessoa = pessoa;
	}
	
	//ATUALIZA O REGISTRO QUE FOI ALTERADO
	public void alterarRegistro() {
		this.pessoaRepository.alterarRegistro(this.pessoa);
		
		//RECARREGA OS REGISTROS
		this.init();
	}
	
	//EXCLUINDO UM REGISTRO
	//@param pessoa
	public void excluirPessoa(Pessoa pessoa) {
		//EXCLUI A PESSOA DO BANCO DE DADOS
		this.pessoaRepository.excluirRegistro(pessoa.getCodigo());
	
	
	//REMOVENDO A PESSOA DA LISTA
	//ASSIM QUE A PESSOA É REMOVIDA DA LISTA O DATATABLE É ATUALIZADO
	this.pessoas.remove(pessoa);
	
	}
	
	
}