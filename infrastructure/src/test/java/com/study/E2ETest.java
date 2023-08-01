package com.study;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.infrastructure.configuration.ObjectMapperConfig;
import com.study.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = WebServerConfig.class)
@Testcontainers
@AutoConfigureMockMvc
@ExtendWith(SQLCleanUpExtension.class)
@Import(ObjectMapperConfig.class)
@Tag("e2eTest")
public abstract class E2ETest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper mapper;

    private static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres")
                .withDatabaseName("planets_probe")
                .withUsername("planets_probe")
                .withPassword("planets_probe")
                .withReuse(true);
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    private static void setProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        registry.add("spring.flyway.enabled", () -> false);

    }
}