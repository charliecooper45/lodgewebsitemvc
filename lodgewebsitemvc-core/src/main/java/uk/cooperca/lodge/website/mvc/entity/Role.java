package uk.cooperca.lodge.website.mvc.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A role that a registered user has.
 *
 * @author Charlie Cooper
 */
@Entity
@Table(name = "roles")
public class Role implements Serializable {

    public static enum RoleName {
        ROLE_ADMIN, ROLE_USER
    }

    private static final long serialVersionUID = 473287489620782709L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    @SequenceGenerator(name = "roles_id_seq", sequenceName = "roles_id_seq")
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private RoleName roleName;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_at", nullable = false)
    private DateTime createdAt;

    public Role() {
        // for Hibernate
    }

    public Role(RoleName roleName, DateTime createdAt) {
        this.roleName = roleName;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }
}
