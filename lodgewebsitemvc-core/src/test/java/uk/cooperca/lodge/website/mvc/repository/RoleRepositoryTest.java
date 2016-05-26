package uk.cooperca.lodge.website.mvc.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.cooperca.lodge.website.mvc.config.CoreConfig;
import uk.cooperca.lodge.website.mvc.entity.Role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.cooperca.lodge.website.mvc.entity.Role.RoleName.ROLE_USER;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class})
@TestPropertySource(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=password"})
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Test
    public void test() {
        assertNotNull(repository);

        Role role = repository.findByRoleName(ROLE_USER);
        assertNotNull(role);
        assertEquals(ROLE_USER, role.getRoleName());
    }
}
