package ru.epicprojects.localities.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.repositories.AssistanceRepository;
import ru.epicprojects.localities.repositories.LocalityRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AssistanceRepositoryIntegrationTest {

    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpassword");

    @BeforeAll
    static void setUp() {
        postgreSQLContainer.start();  // Запускаем контейнер перед тестами
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());  // Устанавливаем правильный JDBC URL
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();  // Отключаем контейнер после завершения тестов
    }

    @Autowired
    private AssistanceRepository assistanceRepository;

//    @Test
//    void testSaveAndFindLocality() {
//        LocalityEntity localityEntity = new LocalityEntity();
//        localityEntity.setLocality("Test Locality");
//        localityEntity.setRegion("Test Region");
//
//        localityRepository.save(localityEntity);
//
//        assertThat(localityEntity.getId()).isNotNull();  // Проверяем, что ID был сгенерирован
//        assertThat(localityRepository.findById(localityEntity.getId())).isPresent();  // Проверяем существование локалитета
//        assertThat(localityRepository.findById(localityEntity.getId()).get().getLocality()).isEqualTo("Test Locality");
//    }
//
//
//    @Test
//    void testDeleteLocality() {
//        // Создаем и сохраняем локалитет
//        LocalityEntity locality = new LocalityEntity();
//        locality.setLocality("Locality to Delete");
//        locality.setRegion("Region to Delete");
//        locality = localityRepository.save(locality);
//
//        localityRepository.deleteById(locality.getId());
//
//        assertThat(localityRepository.findById(locality.getId())).isNotPresent();
//    }
}
