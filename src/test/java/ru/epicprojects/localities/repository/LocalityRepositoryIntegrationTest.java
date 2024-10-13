package ru.epicprojects.localities.repository;

import jdk.dynalink.linker.support.Guards;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.utility.DockerImageName;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityCompositeAK;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.repositories.AttractionRepository;
import ru.epicprojects.localities.repositories.LocalityRepository;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class LocalityRepositoryIntegrationTest {

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
    private LocalityRepository localityRepository;

    @Autowired
    private AttractionRepository attractionRepository;

    @Test
    void testSaveAndFindLocality() {
        LocalityEntity localityEntity = new LocalityEntity();
        localityEntity.setLocalityCompositeAK(new LocalityCompositeAK("Test Locality", "Test Region"));

        localityRepository.save(localityEntity);

        assertThat(localityEntity.getId()).isNotNull();  // Проверяем, что ID был сгенерирован
        assertThat(localityRepository.findById(localityEntity.getId())).isPresent();  // Проверяем существование локалитета
        assertThat(localityRepository.findById(localityEntity.getId())
                .get().getLocalityCompositeAK().getLocality()).isEqualTo("Test Locality");
    }


    @Test
    void testDeleteLocality() {
        LocalityEntity localityEntity = new LocalityEntity();
        localityEntity.setLocalityCompositeAK(new LocalityCompositeAK("Test Locality", "Test Region"));

        localityRepository.save(localityEntity);
        LocalityEntity locality = localityRepository.findByLocalityAndRegion(
                localityEntity.getLocalityCompositeAK().getLocality(),
                localityEntity.getLocalityCompositeAK().getRegion()).get();
        localityRepository.deleteById(locality.getId());

        assertThat(localityRepository.findById(locality.getId())).isNotPresent();
    }
}
