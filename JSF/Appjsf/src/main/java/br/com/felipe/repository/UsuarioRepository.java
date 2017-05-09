package br.com.felipe.repository;

import java.io.Serializable;

import javax.persistence.Query;

import br.com.felipe.entity.UsuarioEntity;
import br.com.felipe.model.Usuario;
import br.com.felipe.util.Util;

public class UsuarioRepository implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public UsuarioEntity validaUsuario(Usuario usuario) {
		try {
			//QUERY QUE VAI SER EXECUTADA (UsuarioEntity.findUser)
			Query query = Util.jpaEntityManager().createNamedQuery("UsuarioEntity.findUser");
			
			//PARÂMETROS DA QUERY
			query.setParameter("usuario", usuario.getUsuario());
			query.setParameter("senha", usuario.getSenha());
			
			//RETORNA O USUÁRIO SE FOR LOCALIZADO
			return (UsuarioEntity)query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}
