package uk.cooperca.lodge.website.mvc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.cooperca.lodge.website.mvc.entity.Role;

/**
 * Spring Data repository for {@link Role}s.
 *
 * @author Charlie Cooper
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    /**
     * Retrieves the role with the given name.
     *
     * @param roleName the role name of the role
     *
     * @return the role
     */
    public Role findByRoleName(Role.RoleName roleName);
}
