package raf.projekat_back.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.projekat_back.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public Optional<User> findUserById(Integer id);
    public Optional<User> findUserByEmail(String email);

}
