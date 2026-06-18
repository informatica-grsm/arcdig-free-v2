package config;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author jprada
 */
public class SecuryConnectionFactory {

    private static SqlSessionFactory sqlSessionFactory;

    public static SqlSessionFactory getSqlSessionFactory() {
        Reader reader = null;
        try {
            String resource = "config/ArcDigSql.xml";
            try {
                reader = Resources.getResourceAsReader(resource);
            } catch (IOException ex) {
                Logger.getLogger(SecuryConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "SISAI");
            }
        } catch (SqlSessionException ex) {
            sqlSessionFactory = null; 
            Logger.getLogger(SecuryConnectionFactory.class.getName()).log(
                    Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(SecuryConnectionFactory.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        return sqlSessionFactory;
    }
}
