package ru.zemlyakov.moviesRecommender2.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import ru.zemlyakov.moviesRecommender2.models.User;

import javax.persistence.QueryHint;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @QueryHints(value = {@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    Optional<User> findByChatId(long chatId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.historyOfViewing um WHERE u.chatId =?1")
    Optional<User> findByChatIdAndGetWithHistory(long chatId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.minYearOfCreateMovie = ?1, u.maxYearOfCreateMovie =?2, u.deepOfRecommend = 0 where u=?3")
    void updateYearsFilter(short minYear, short maxYear, User updatedUser);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.deepOfRecommend = ?1 where u=?2")
    void updateRecommendDeep(int deep, User updatedUser);

}
