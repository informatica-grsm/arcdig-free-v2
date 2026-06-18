package gob.peam.arcdig.dao;

import config.SecuryConnectionFactory;
import gob.peam.arcdig.beans.Dependencia;
import gob.peam.arcdig.beans.Persona;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class PersonaDao {

    private SqlSessionFactory sqlSessionFactory;

    public PersonaDao() {
        sqlSessionFactory = SecuryConnectionFactory.getSqlSessionFactory();
    }

    public List<Persona> listarPersonas(HashMap hm) {
        List<Persona> personas;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            personas = session.selectList("Persona.listarPersonas", hm);
        } finally {
            session.close();
        }
        return personas;
    }

    public List<Persona> listarPersonasOficina(HashMap hm) {
        List<Persona> personas;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            personas = session.selectList("Persona.listarPersonasOficina", hm);
        } finally {
            session.close();
        }
        return personas;
    }

    public List<Persona> listarPersona(String filtro) {
        List<Persona> items;
        String c = "%" + filtro + "%";
        HashMap hm = new HashMap();
        hm.put("c", c);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            items = session.selectList("Persona.listarPersonas", hm);
        } finally {
            session.close();
        }
        return items;
    }

    public List<Persona> listarPersonalOficina(String query) {
        List<Persona> items;
        HashMap hm = new HashMap();
        hm.put("c", query);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            items = session.selectList("Persona.listarPersonalOficina", hm);
        } finally {
            session.close();
        }
        return items;
    }

    public List<Persona> getAll() {
        HashMap hm = new HashMap();
        hm.put("c", "%%");
        return listarPersonas(hm);
    }

    public List<Persona> getAllOficina(String ceco_padre) {
        HashMap hm = new HashMap();
        hm.put("c", "%%");
        hm.put("d", ceco_padre);
        return listarPersonasOficina(hm);
    }

    public String getNombrePersona(Integer id) {
        String c;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            c = sqlSession.selectOne("Persona.getNombrePersona", id);

        } finally {
            sqlSession.close();
        }
        return c;
    }

    public Persona getPersona(String dni) {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.selectOne("Persona.get", dni);

        } finally {
            sqlSession.close();
        }
    }

    public Persona buscarPersonaUsuario(Integer id) {
        HashMap hm = new HashMap();
        hm.put("id", id);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return (Persona) sqlSession.selectOne("Persona.buscarPersonaUsuario", hm);
        } finally {
            sqlSession.close();
        }
    }

    public Persona get(String id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.selectOne("Persona.getForId", id);

        } finally {
            sqlSession.close();
        }
    }
    
        public Persona getBuscarPersona(Integer id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.selectOne("Persona.buscarPersona", id);

        } finally {
            sqlSession.close();
        }
    }
    
    
   public HashMap actualizarPersona(Persona p, Integer op) {
      HashMap hm = new HashMap();
      SqlSession session;
      Integer count;
      if (op == 2) {
         session = this.sqlSessionFactory.openSession();

         try {
            count = (Integer)session.selectOne("Persona.buscarDniRepetido", p);
            if (count == 0) {
               session.update("Persona.updatePersona", p);
               session.commit();
               String login = p.getUsuario().getLogin();
               if ((Integer)session.selectOne("Usuario.getRepetido", login) != 0) {
                  session.update("Usuario.update", p);
                  session.commit();
               } else {
                  Persona per = new Persona();
                  per.setIdPersona(p.getIdPersona());
                  p.getUsuario().setPersona(per);
                  session.insert("Usuario.insert", p.getUsuario());
                  session.commit();
               }

               if ((Integer)session.selectOne("Dependencia.existeDependencia", p) > 0) {
                  session.update("Dependencia.updateDependencia", p);
                  session.commit();
               } else {
                  Dependencia d = new Dependencia();
                  d.setIdDependencia(1);
                  p.setDependencia(d);
                  session.insert("Dependencia.insertarTrabajadorInDependencia", p);
                  session.commit();
               }
            } else {
               hm.put("error", 1);
               hm.put("msn", "Ya Hay una persona con este DNI");
            }
         } finally {
            session.close();
         }
      } else {
         session = this.sqlSessionFactory.openSession();

         try {
            count = (Integer)session.selectOne("Persona.buscarDni", p);
            if (count == 0) {
               session.insert("Persona.insertPersona", p);
               session.commit();
               p.setIdPersona((Integer)session.selectOne("Persona.getMaxId"));
               session.insert("Dependencia.insertarTrabajadorInDependencia", p);
               session.commit();
               Persona per = new Persona();
               per.setIdPersona(p.getIdPersona());
               p.getUsuario().setPersona(per);
               session.insert("Usuario.insert", p.getUsuario());
               session.commit();
            } else {
               hm.put("error", 1);
               hm.put("msn", "Ya Hay una persona con este DNI");
            }
         } finally {
            session.close();
         }
      }

      return hm;
   }

    public HashMap actualizarSoloPersona(Persona p, Integer op) {
      HashMap hm = new HashMap();
      SqlSession session;
      Integer count;
      if (op == 2) {
         session = this.sqlSessionFactory.openSession();

         try {
            count = (Integer)session.selectOne("Persona.buscarDniRepetido", p);
            if (count == 0) {
               session.update("Persona.updatePersona", p);
               session.commit();
               session.update("Usuario.updateGrupo", p);
               session.commit();
               //session.update("Dependencia.updateDependencia", p);
               //session.commit();
            } else {
               hm.put("error", 1);
               hm.put("msn", "Ya Hay una persona con este DNI");
            }
         } finally {
            session.close();
         }
      } else {
         session = this.sqlSessionFactory.openSession();

         try {
            count = (Integer)session.selectOne("Persona.buscarDni", p);
            if (count == 0) {
               session.insert("Persona.insertPersona", p);
               session.commit();
               p.setIdPersona((Integer)session.selectOne("Persona.getMaxId"));
               session.insert("Dependencia.insertarTrabajadorInDependencia", p);
               session.commit();
            } else {
               hm.put("error", 1);
               hm.put("msn", "Ya Hay una persona con este DNI");
            }
         } finally {
            session.close();
         }
      }

      return hm;
   }
    
    public void activarPersona(Integer id) {
      SqlSession session = this.sqlSessionFactory.openSession();

      try {
         session.update("Persona.activar", id);
         session.commit();
         session.update("Usuario.activarUsuario", id);
         session.commit();
      } finally {
         session.close();
      }

   }
}
