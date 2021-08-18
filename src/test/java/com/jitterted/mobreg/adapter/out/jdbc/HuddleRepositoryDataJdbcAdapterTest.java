package com.jitterted.mobreg.adapter.out.jdbc;

import com.jitterted.mobreg.domain.Huddle;
import com.jitterted.mobreg.domain.MemberId;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@Transactional
@Tag("integration")
class HuddleRepositoryDataJdbcAdapterTest {

    @Autowired
    HuddleRepositoryDataJdbcAdapter huddleRepositoryAdapter;

    @MockBean
    GrantedAuthoritiesMapper grantedAuthoritiesMapper;

    // create shared container with a container image name "postgres" and latest major release of PostgreSQL "13"
    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("posttest")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.sql.init.platform", () -> "postgresql");
    }


    @Test
    public void savedHuddleCanBeFoundByItsId() throws Exception {
        Huddle huddle = createWithRegisteredMemberHuddleNamed("test huddle");

        Huddle savedHuddle = huddleRepositoryAdapter.save(huddle);

        Optional<Huddle> found = huddleRepositoryAdapter.findById(savedHuddle.getId());

        assertThat(found)
                .isPresent()
                .get()
                .extracting(Huddle::name)
                .isEqualTo("test huddle");
    }

    @Test
    public void newRepositoryReturnsEmptyForFindAll() throws Exception {
        List<Huddle> huddles = huddleRepositoryAdapter.findAll();

        assertThat(huddles)
                .isEmpty();
    }

    @Test
    public void twoSavedHuddlesBothReturnedByFindAll() throws Exception {
        Huddle one = createWithRegisteredMemberHuddleNamed("one");
        Huddle two = createWithRegisteredMemberHuddleNamed("two");

        huddleRepositoryAdapter.save(one);
        huddleRepositoryAdapter.save(two);

        assertThat(huddleRepositoryAdapter.findAll())
                .hasSize(2);
    }

    @NotNull
    private Huddle createWithRegisteredMemberHuddleNamed(String huddleName) {
        Huddle huddle = new Huddle(huddleName, ZonedDateTime.now());
        huddle.registerById(MemberId.of(7L));
        return huddle;
    }
}