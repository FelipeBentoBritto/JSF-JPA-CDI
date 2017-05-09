package br.com.felipe.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.felipe.entity.UsuarioEntity;
import br.com.felipe.model.Usuario;
import br.com.felipe.repository.UsuarioRepository;
import br.com.felipe.util.Util;

@Named(value="usuarioController")
@SessionScoped
public class UsuarioController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Usuario usuario;
	
	@Inject
	private UsuarioRepository usuarioRepository;
	
	@Inject
	private UsuarioEntity usuarioEntity;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Usuario getUsuarioSession(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		return (Usuario)facesContext.getExternalContext().getSessionMap().get("usuarioAutenticado");
	}
	
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		return "/index.xhtml?faces-redirect=true";
	}
	
	public String efetuarLogin() {
		if(StringUtils.isEmpty(usuario.getUsuario()) || StringUtils.isBlank(usuario.getUsuario())) {
			Util.mensagem("Favor informar o login!");
			
			return null;
		}
		else if(StringUtils.isEmpty(usuario.getSenha()) || StringUtils.isBlank(usuario.getSenha())) {
			Util.mensagem("Favor informar a senha!");
			
			return null;
		}
		else {
			
			usuarioEntity = usuarioRepository.validaUsuario(usuario);
					
					if(usuarioEntity != null) {
						usuario.setSenha(null);
						usuario.setCodigo(usuarioEntity.getCodigo());
						
						FacesContext facesContext = FacesContext.getCurrentInstance();
						
						facesContext.getExternalContext().getSessionMap().put("usuarioAutenticado", usuario);
						
						return "sistema/home?faces-redirect=true";
					}
					else {
						Util.mensagem("Não foi possível efetuar o login com esse usuário e senha!");
						return null;
					}
			
		}
	}
	
	

}
