package com.talentpath.favRecipEzSpringBt.daos;

import com.talentpath.favRecipEzSpringBt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //we're defining this as a repository that serves up User objects
    //with an Integer @Id
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    //all this works by name
        //if had findByX, would be looking for a field named X
        //findBy___
            //if make return type be a List, you can do that as well
        //existBy___


}
