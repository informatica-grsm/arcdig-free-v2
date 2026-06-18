/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.dao;


import config.SecuryConnectionFactory;
import gob.peam.arcdig.beans.Usuario;
import java.util.HashMap;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author jpgprog84
 */
public class UsuarioDao {

    private final SqlSessionFactory sqlSessionFactory;

    public UsuarioDao() {
        sqlSessionFactory = SecuryConnectionFactory.getSqlSessionFactory();
    }

    public Usuario buscarUsuarioPorLogin(String login) {
        Usuario usuario;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            usuario = sqlSession.selectOne("Usuario.buscarUsuarioPorLogin", login);
        } finally {
            sqlSession.close();
        }
        return usuario;
    }

    public Usuario buscarUsuarioPorPersona(Integer id) {
        Usuario usuario;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            usuario = sqlSession.selectOne("Usuario.buscarUsuarioPorPersona", id);
            return usuario;
        } finally {
            sqlSession.close();
        }
    }
    
    
   

    public boolean desactivarUsuario(Integer idUsuario) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Usuario.desactivarUsuario", idUsuario);
            session.commit();
            return true;
        } finally {
            session.close();
        }
    }
    
     public boolean desactivarUsuarioIdPers(Integer idPers) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Usuario.desactivarUsuarioIdPers", idPers);
            session.commit();
            return true;
        } finally {
            session.close();
        }
    }

    public boolean activarUsuario(Integer idUsuario) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("Usuario.activarUsuario", idUsuario);
            session.commit();
            return true;
        } finally {
            session.close();
        }
    }

    public boolean eliminarUsuario(Integer usua_id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("Usuario.delUsuario", usua_id);
            session.commit();
            return true;
        } finally {
            session.close();
        }
    }

    public boolean existeUsuarioParaEliminar(Integer usua_id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Usuario u = session.selectOne("Usuario.existeUsuarioParaEliminar", usua_id);
            if (u == null) {
                return false;
            } else {
                return true;
            }
        } finally {
            session.close();
        }
    }

    public boolean insertUsuario(Usuario usr) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("Usuario.insert", usr);
            session.commit();
            return true;
        } finally {
            session.close();
        }
    }

    public void updatePersonal(Usuario usr) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("Usuario.update", usr);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    public Integer getRepetido(HashMap hm) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return (Integer) sqlSession.selectOne("Usuario.getRepetido", hm);
        } finally {
            sqlSession.close();
        }
    }

    public Integer getRepetidoUpdate(HashMap hm) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return (Integer) sqlSession.selectOne("Usuario.getRepetidoUpdate", hm);
        } finally {
            sqlSession.close();
        }
    }
    
    public String getPassword(String usr){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return (String) sqlSession.selectOne("Usuario.getPassword", usr);
        } finally {
            sqlSession.close();
        }
    }
    
    public boolean activarUsuarioIdPers(Integer idPers) {
      SqlSession session = this.sqlSessionFactory.openSession();

      boolean var3;
      try {
         session.update("Usuario.activarUsuarioIdPers", idPers);
         session.commit();
         var3 = true;
      } finally {
         session.close();
      }

      return var3;
   }
}
