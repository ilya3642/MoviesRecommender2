package ru.zemlyakov.moviesRecommender2.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import ru.zemlyakov.moviesRecommender2.models.User;

import javax.persistence.QueryHint;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @QueryHints(value = {@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    Optional<User> findByChatId(long chatId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.historyOfViewing h LEFT JOIN FETCH h.genre WHERE u.chatId =?1")
    Optional<User> findByChatIdAndGetHistory(long chatId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.historyOfViewing m WHERE u.chatId =?1")
    Optional<User> findByChatIdAndGetFullInf(long chatId);

}
