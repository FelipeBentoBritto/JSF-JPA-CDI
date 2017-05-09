package br.com.felipe.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.felipe.entity.PessoaEntity;
import br.com.felipe.entity.UsuarioEntity;
import br.com.felipe.model.Pessoa;
import br.com.felipe.model.Usuario;
import br.com.felipe.util.Util;

public class PessoaRepository {

	@Inject
	PessoaEntity pessoaEntity;
	
	EntityManager entityManager;
	
	public void salvarNovoRegistro(Pessoa pessoa) {
		entityManager = Util.jpaEntityManager();
		
		pessoaEntity = new PessoaEntity();
		pessoaEntity.setDataCadastro(LocalDateTime.now());
		pessoaEntity.setEmail(pessoa.getEmail());
		pessoaEntity.setEndereco(pessoa.getEndereco());
		pessoaEntity.setNome(pessoa.getNome());
		pessoaEntity.setOrigemCadastro(pessoa.getOrigemCadastro());
		pessoaEntity.setSexo(pessoa.getSexo());
		
		UsuarioEntity usuarioEntity = entityManager.find(UsuarioEntity.class, pessoa.getUsuario().getCodigo());
		
		pessoaEntity.setUsuarioEntity(usuarioEntity);
		
		entityManager.persist(pessoaEntity);
	}
	
	//MÉTODO PARA CONSULTAR A PESSOA
	
	public List<Pessoa> getPessoas() {
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		
		entityManager = Util.jpaEntityManager();
		
		Query query = entityManager.createNamedQuery("PessoaEntity.findAll");
		
		@SuppressWarnings("unchecked")
		Collection<PessoaEntity> pessoasEntity = (Collection<PessoaEntity>)query.getResultList();
		
		Pessoa pessoa = null;
		
		for (PessoaEntity pessoaEntity : pessoasEntity) {
			pessoa = new Pessoa();
			pessoa.setCodigo(pessoaEntity.getCodigo());
			pessoa.setDataCadastro(pessoaEntity.getDataCadastro());
			pessoa.setEmail(pessoaEntity.getEmail());
			pessoa.setEndereco(pessoaEntity.getEndereco());
			pessoa.setNome(pessoaEntity.getNome());
			
			if (pessoaEntity.getOrigemCadastro().equals("X")) {
				pessoa.setOrigemCadastro("XML");
				
			} else {
				pessoa.setOrigemCadastro("INPUT");
			}
			
			if (pessoaEntity.getSexo().equals("M")) {
				pessoa.setSexo("Masculino");
			} else {
				pessoa.setSexo("Feminino");
			}
			
			UsuarioEntity usuarioEntity = pessoaEntity.getUsuarioEntity();
			
			Usuario usuario = new Usuario();
			usuario.setUsuario(usuarioEntity.getUsuario());
			
			pessoa.setUsuario(usuario);
			
			pessoas.add(pessoa);
			
		}
		
		return pessoas;
	}
	
	//CONSULTA UMA PESSOA CADASTRADA PELO CÓDIGO
	//@param codigo
	//@return
	private PessoaEntity getPessoa(int codigo) {
		entityManager = Util.jpaEntityManager();
		
		return entityManager.find(PessoaEntity.class, codigo);
	}
	
	//ALTERA UM REGISTRO CADASTRADO NO BANCO DE DADOS
	//@param pessoa
	
	public void alterarRegistro(Pessoa pessoa) {
		entityManager = Util.jpaEntityManager();
		
		PessoaEntity pessoaEntity = this.getPessoa(pessoa.getCodigo());
		
		pessoaEntity.setEmail(pessoa.getEmail());
		pessoaEntity.setEndereco(pessoa.getEndereco());
		pessoaEntity.setNome(pessoa.getNome());
		pessoaEntity.setSexo(pessoa.getSexo());
		
		entityManager.merge(pessoaEntity);
		
	}
	
	//EXCLUI UM REGISTRO DO BANCO DE DADOS
	//@param codigo
	
	public void excluirRegistro(int codigo){
		entityManager = Util.jpaEntityManager();
		
		PessoaEntity pessoaEntity = this.getPessoa(codigo);
		
		entityManager.remove(pessoaEntity);
	}
	
	//RETORNA OS TIPOS DE PESSOA AGRUPADOS
	//@return
	
	public Hashtable<String, Integer> getOrigemPessoa() {
		Hashtable<String, Integer> hashtableRegistros = new Hashtable<String, Integer>();
		
		entityManager = Util.jpaEntityManager();
		
		Query query = entityManager.createNamedQuery("PessoaEntity.GroupByOrigemCadastro");
		
		@SuppressWarnings("unchecked")
		Collection<Object[]> collectionRegistros = (Collection<Object[]>)query.getResultList();
		
		for(Object[] objects : collectionRegistros) {
			String tipoPessoa = (String)objects[0];
			int totalDeRegistros = ((Number)objects[1]).intValue();
			
			if(tipoPessoa.equals("X")) {
				tipoPessoa = "XML";
			}
			else {
				tipoPessoa="INPUT";
			}
			hashtableRegistros.put(tipoPessoa, totalDeRegistros);
		}
		return hashtableRegistros;
	}
	
}
