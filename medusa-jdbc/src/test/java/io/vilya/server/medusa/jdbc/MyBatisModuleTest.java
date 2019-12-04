package io.vilya.server.medusa.jdbc;

import java.time.LocalDateTime;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

import io.vilya.server.medusa.jdbc.demo.Log;
import io.vilya.server.medusa.jdbc.demo.LogMapper;

/**
 * @author cafedada
 */
@ExtendWith(GuiceExtension.class)
@EnableGuiceModule(moduleProviders = MybatisModuleProvider.class)
public class MyBatisModuleTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MyBatisModuleTest.class);

	@Inject
	private Injector injector;
	
	@Test
	public void testQuery() {
		LogMapper logMapper = injector.getInstance(LogMapper.class);
		Log log = logMapper.selectByPrimaryKey(1);
		logger.info("{}", log);
	}
	
	@Test
	public void testInsert() {
		LogMapper logMapper = injector.getInstance(LogMapper.class);
		
		Log log = new Log();
		log.setLogContent("testInsert");
		log.setLogTime(LocalDateTime.now());
		log.setLogType(2);
		logMapper.insertSelective(log);
	}
	
}
