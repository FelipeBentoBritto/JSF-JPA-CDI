package br.com.felipe.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.felipe.model.Pessoa;
import br.com.felipe.repository.PessoaRepository;

@Named(value="ExportarRegistrosXmlController")
@RequestScoped
public class ExportarRegistrosXmlController implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject transient
	PessoaRepository pessoaRepository;
	
	private StreamedContent arquivoDownload;
	
	//REALIZA O DOWNLOAD DO ARQUIVO XML
	//@RETURN
	
	public StreamedContent getArquivoDownload(){
		this.downloadArquivoXmlPessoa();
		
		return arquivoDownload;
	}
	
	//GERANDO O ARQUIVO XML PARA EXPORTAÇÃO
	//@return
	
	private File gerarXmlPessoas() {
		//MASCARA PARA FORMATAR A DATA QUE VAI SER ADICIONADA NO ARQUIVO XML
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		
		List<Pessoa> pessoas = pessoaRepository.getPessoas();
		
		//ELEMENTO RAIZ DO NOSSO ARQUIVO XML
		Element elementPessoas = new Element("Pessoas");
		
		Document documentoPessoas = new Document(elementPessoas);
		
		pessoas.forEach(pessoa -> {
			//MONTANDO AS TAGS DO XML COM OS SEUS VALORES
			Element elementPessoa = new Element("Pessoa");
			elementPessoa.addContent(new Element("codigo").setText(pessoa.getCodigo().toString()));
			elementPessoa.addContent(new Element("nome").setText(pessoa.getNome()));
			elementPessoa.addContent(new Element("sexo").setText(pessoa.getSexo()));
			
			//FORMATANDO A DATA
			String dataCadastroFormatada = pessoa.getDataCadastro().format(dateTimeFormatter);
			
			elementPessoa.addContent(new Element("dataCadastro").setText(dataCadastroFormatada));
			
			elementPessoa.addContent(new Element("email").setText(pessoa.getEmail()));
			elementPessoa.addContent(new Element("endereco").setText(pessoa.getEndereco()));
			elementPessoa.addContent(new Element("origemCadastro").setText(pessoa.getOrigemCadastro()));
			elementPessoa.addContent(new Element("usuarioCadastro").setText(pessoa.getUsuario().getUsuario()));
			
			elementPessoas.addContent(elementPessoa);
		});
		
		XMLOutputter xmlGerado = new XMLOutputter();
		try{
			//GERANDO O NOME DO ARQUIVO
			String nomeArquivo = "pessoas_".concat(java.util.UUID.randomUUID().toString().concat(".xml"));
			
			//CAMINHO ONDE VAI SER GERADO O ARQUIVO XML
									// MUDAR \/
			File arquivo = new File("C:/gerados/".concat(nomeArquivo));
			
			FileWriter fileWriter = new FileWriter(arquivo);
			
			xmlGerado.output(documentoPessoas, fileWriter);
			
			return arquivo;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
		
	}
	
	//PREPARA O ARQUIVO PARA DOWNLOAD
	public void downloadArquivoXmlPessoa() {
		File arquivoXml = this.gerarXmlPessoas();
		
		InputStream inputStream;
		
		try {
			inputStream = new FileInputStream(arquivoXml.getPath());
			
			arquivoDownload = new DefaultStreamedContent(inputStream, "application/xml", arquivoXml.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
}
