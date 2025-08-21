package com.studyswap.api;

import com.studyswap.backend.BackendApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;


@SpringBootTest(classes = BackendApplication.class)
@ComponentScan(basePackages = {"com.studyswap.backend", "com.studyswap.api"})
class ApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
