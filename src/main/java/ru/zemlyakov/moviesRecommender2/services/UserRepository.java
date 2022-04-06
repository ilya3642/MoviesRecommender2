package ru.zemlyakov.moviesRecommender2.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import ru.zemlyakov.moviesRecommender2.models.User;

import javax.persistence.QueryHint;
import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @QueryHints(value = {@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    Optional<User> findByChatId(long chatId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.historyOfViewing h WHERE u.chatId =?1")
    Optional<User> findByChatIdAndGetHistory(long chatId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.historyOfViewing m WHERE u.chatId =?1")
    Optional<User> findByChatIdAndGetFullInf(long chatId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.minYearOfCreateMovie = ?1, u.maxYearOfCreateMovie =?2, u.deepOfRecommend = 0 where u=?3")
    void updateYearsFilter(short minYear, short maxYear, User updatedUser);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.deepOfRecommend = ?1 where u=?2")
    void updateRecommendDeep(int deep, User updatedUser);


}
