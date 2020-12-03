package com.talentpath.favRecipEzSpringBt.controllers;

import com.talentpath.favRecipEzSpringBt.daos.UserRepository;
import com.talentpath.favRecipEzSpringBt.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userdata")
@CrossOrigin
public class UserController {
    @Autowired
    UserRepository userRepo;

    @GetMapping("/")
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    @PostMapping("/")
    public User addUser(@RequestBody User newUser){
        return userRepo.saveAndFlush(newUser);
    }

    @PutMapping
    public User editUser(@RequestBody User editedUser){
        //saveAndFlush can be both save and edit dep. on if finds matching ID
        return userRepo.saveAndFlush(editedUser);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Integer id){
        userRepo.deleteById(id);
        return true;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id){
        //add valid if not matching user ID
        return userRepo.getOne(id);
    }

}
