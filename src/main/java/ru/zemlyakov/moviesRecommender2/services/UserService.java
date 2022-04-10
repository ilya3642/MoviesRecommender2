package ru.zemlyakov.moviesRecommender2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.models.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserWatchMovieRepository userWatchMovieRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserWatchMovieRepository userWatchMovieRepository) {
        this.userRepository = userRepository;
        this.userWatchMovieRepository = userWatchMovieRepository;
    }

    public void saveOrUpdate(User newUser) {
        userRepository.save(newUser);
    }

    @Transactional
    public void updateUserYear(User updatedUser) {
        userRepository.updateYearsFilter(
                updatedUser.getMinYearOfCreateMovie(),
                updatedUser.getMaxYearOfCreateMovie(),
                updatedUser
        );
    }

    @Transactional
    public void updateUserRecommendDeep(User updatedUser) {
        userRepository.updateRecommendDeep(
                updatedUser.getDeepOfRecommend(),
                updatedUser
        );
    }

    public int getHistorySize(User user){
        return userWatchMovieRepository.countByUserEquals(user);
    }

    public User getUser(Long chatId) {
        Optional<User> userOptional =
                userRepository.findByChatId(chatId);
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("User not already be taken");
        } else
            return userOptional.get();
    }

    public List<Movie> getMovieFromUserHistory(User user, int numOfMovie) {
        PageRequest pageRequest = PageRequest.of(numOfMovie, 1, Sort.by("dateTimeToWatched").descending());
        return userWatchMovieRepository.getMovieFromUserHistory(user, pageRequest).getContent();
    }

    @Transactional
    public void deleteMovieFromHistory(User user, Movie movie){
        userWatchMovieRepository.deleteByUserAndMovie(user, movie);
    }

    public User getUserWithHistory(Long chatId) {
        Optional<User> userOptional =
                userRepository.findByChatIdAndGetWithHistory(chatId);
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("User not already be taken");
        } else
            return userOptional.get();
    }

}
