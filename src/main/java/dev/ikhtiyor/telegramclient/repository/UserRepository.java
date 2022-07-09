package dev.ikhtiyor.telegramclient.repository;

import dev.ikhtiyor.telegramclient.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author IkhtiyorDev  <br/>
 * Date 09/07/22
 **/

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
