package br.com.felipe.controller;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.primefaces.model.UploadedFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.felipe.model.Pessoa;
import br.com.felipe.repository.PessoaRepository;
import br.com.felipe.util.Util;

@Named(value="PessoaController")
@RequestScoped
public class PessoaController {
	
	@Inject
	Pessoa pessoa;
	
	@Inject
	UsuarioController usuarioController;
	
	@Inject
	PessoaRepository pessoaRepository;
	
	private UploadedFile file;
	
	public UploadedFile getFile() {
		return file;
	}
	
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	//SALVA UM NOVO REGISTRO VIA INPUT

	public void salvarNovaPessoa(){
		pessoa.setUsuario(this.usuarioController.getUsuarioSession());
		
		//INFORMANDO QUE O CADASTRO FOI VIA INPUT
		pessoa.setOrigemCadastro("I");
		
		pessoaRepository.salvarNovoRegistro(this.pessoa);
		
		this.pessoa = null;
		
		Util.mensagemInfo("Registro cadastrado com sucesso!");
	}
	
	//REALIZANDO UPLOAD DE ARQUIVO
	
	public void uploadRegistros() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			if(this.file.getFileName().equals("")) {
				Util.mensagemAtencao("Nenhum arquivo selecionado!");
				return;
			}
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse(this.file.getInputstream());
			
			Element element = doc.getDocumentElement();
			
			NodeList nodes = element.getChildNodes();
			
			for (int i = 0; i <nodes.getLength(); i++) {
				Node node = nodes.item(i);
				
				if(node.getNodeType() == node.ELEMENT_NODE) {
					Element elementPessoa = (Element) node;
					
					//PEGANDO OS VALORES DO ARQUIVO XML
					String nome = elementPessoa.getElementsByTagName("nome").item(0).getChildNodes().item(0).getNodeValue();
					String sexo= elementPessoa.getElementsByTagName("sexo").item(0).getChildNodes().item(0).getNodeValue();
					String email = elementPessoa.getElementsByTagName("email").item(0).getChildNodes().item(0).getNodeValue();
					String endereco = elementPessoa.getElementsByTagName("endereco").item(0).getChildNodes().item(0).getNodeValue();
					
					Pessoa newPessoa = new Pessoa();
					
					newPessoa.setUsuario(this.usuarioController.getUsuarioSession());
					newPessoa.setEmail(email);
					newPessoa.setEndereco(endereco);
					newPessoa.setNome(nome);
					newPessoa.setOrigemCadastro("X");
					newPessoa.setSexo(sexo);
					
					//SALVANDO UM REGISTRO QUE VEIO DO ARQUIVO XML
					pessoaRepository.salvarNovoRegistro(newPessoa);
				
				}
				
			}
			
			Util.mensagemInfo("Registros cadastrados com sucesso!");
			
		} catch(ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
