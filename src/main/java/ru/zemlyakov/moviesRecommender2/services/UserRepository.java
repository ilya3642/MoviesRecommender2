package ru.zemlyakov.moviesRecommender2.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.zemlyakov.moviesRecommender2.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT u FROM User u WHERE u.chatId =?1")
    Optional<User> findByChatId(long chatId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.historyOfViewing m WHERE u.chatId =?1")
    Optional<User> findByChatIdAndGetHistory(long chatId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.favouriteGenres g WHERE u.chatId =?1")
    Optional<User> findByChatIdAndGetFavouriteGenres(long chatId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.historyOfViewing m LEFT JOIN FETCH u.favouriteGenres g WHERE u.chatId =?1")
    Optional<User> findByChatIdAndGetFullInf(long chatId);

}
