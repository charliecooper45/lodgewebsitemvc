package uk.cooperca.lodge.website.mvc.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.cooperca.lodge.website.mvc.AbstractCoreTest;
import uk.cooperca.lodge.website.mvc.entity.Role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.cooperca.lodge.website.mvc.entity.Role.RoleName.ROLE_USER;

public class RoleRepositoryTest extends AbstractCoreTest {

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
