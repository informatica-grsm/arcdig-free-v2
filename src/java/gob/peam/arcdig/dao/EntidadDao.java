package gob.peam.arcdig.dao;



import config.SecuryConnectionFactory;
import gob.peam.arcdig.beans.Entidad;
import java.io.Serializable;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class EntidadDao implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8832903930519973164L;
    private SqlSessionFactory sqlSessionFactory;

    public EntidadDao() {
        sqlSessionFactory = SecuryConnectionFactory.getSqlSessionFactory();
    }

    public Entidad seleccionarEntidad() {
        Entidad entidad;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            entidad = sqlSession.selectOne("Entidad.get");
            
        } finally {
            sqlSession.close();
        }
        return entidad;
    }

    
}
