package io.vilya.server.medusa.jdbc;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.google.common.reflect.Reflection;
import com.google.inject.AbstractModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.vilya.server.medusa.jdbc.demo.LogMapper;

/**
 * @author cafedada
 */
public class MyBatisModule extends AbstractModule {

	@Override
	protected void configure() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost/core?characterEncoding=utf8&serverTimezone=GMT%2B8");
		config.setUsername("dev");
		config.setPassword("123456");

		HikariDataSource dataSource = new HikariDataSource(config);
		Environment environment = new Environment("dev", new JdbcTransactionFactory(), dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.setMapUnderscoreToCamelCase(true);

		String resource = "mybatis/mappers/LogMapper.xml";
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);

		XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(inputStream, configuration, resource,
				configuration.getSqlFragments());
		xmlMapperBuilder.parse();

		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

		bind(LogMapper.class).toInstance(Reflection.newProxy(LogMapper.class,
				new MapperInterceptor<LogMapper>(sqlSessionFactory, LogMapper.class)));
	}

	private static class MapperInterceptor<T> implements InvocationHandler {

		private SqlSessionFactory sqlSessionFactory;

		private Class<T> mapperInterface;

		public MapperInterceptor(SqlSessionFactory sqlSessionFactory, Class<T> mapperInterface) {
			this.sqlSessionFactory = Objects.requireNonNull(sqlSessionFactory, "sqlSessionFactory");
			this.mapperInterface = Objects.requireNonNull(mapperInterface, "mapperInterface");
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			SqlSession sqlSession = sqlSessionFactory.openSession();
			T instance = sqlSession.getMapper(mapperInterface);
			try {
				Object result = method.invoke(instance, args);
				sqlSession.commit();
				return result;
			} catch (Exception e) {
				sqlSession.rollback();
				throw e;
			}
		}
	}

}
