package br.com.felipe.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

public class Util {

	public static EntityManager jpaEntityManager() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		ExternalContext externalContext = facesContext.getExternalContext();
		
		HttpServletRequest request = (HttpServletRequest)externalContext.getRequest();
		
		return (EntityManager)request.getAttribute("entityManager");
	}
	
	//MOSTRAR MENSAGEM
	public static void mensagem(String mensagem) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		facesContext.addMessage(null, new FacesMessage("Alerta", mensagem));
	}
	
	//MOSTRAR MENSAGEM
	public static void mensagemAtencao(String mensagem) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção:", mensagem));
		
	}
	
	//MOSTRAR MENSAGEM
	public static void mensagemInfo(String mensagem) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", mensagem));
	}
	
}
