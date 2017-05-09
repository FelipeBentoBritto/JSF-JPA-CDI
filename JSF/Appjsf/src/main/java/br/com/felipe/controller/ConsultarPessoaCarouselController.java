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

@Named(value="ConsultarPessoaCarouselController")
@ViewScoped
public class ConsultarPessoaCarouselController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject transient
	private PessoaRepository pessoaRepository;
	
	@Produces
	private List<Pessoa> pessoas;
	
	public List<Pessoa> getPessoas(){
		return pessoas;
	}

	@PostConstruct
	private void init(){
		this.pessoas = pessoaRepository.getPessoas();
	}
	
	
}
