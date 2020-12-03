package com.talentpath.favRecipEzSpringBt.daos;

import com.talentpath.favRecipEzSpringBt.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    //we're defining this as a repository that serves up Role objects
    //with an Integer @Id
    Optional<Role> findByName(Role.RoleName name);

}

