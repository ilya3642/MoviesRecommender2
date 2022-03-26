package ru.zemlyakov.moviesRecommender2.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.models.User;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveOrUpdate(User newUser) {
//        Optional<User> userOptional = userRepository
//                .findByChatId(newUser.getChatId());
//        if (userOptional.isPresent()) {
//            throw new IllegalStateException("User already be taken");
//        } else
            userRepository.save(newUser);
    }

    public boolean existUser(Long chatId){
        Optional<User> userOptional =
                userRepository.findByChatId(chatId);
        return userOptional.isPresent();
    }

    public User getUser(Long chatId) {
        Optional<User> userOptional =
                userRepository.findByChatId(chatId);
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("User not already be taken");
        } else
            return userOptional.get();
    }

    public User getUserWithHistory(Long chatId) {
        Optional<User> userOptional =
                userRepository.findByChatIdAndGetHistory(chatId);
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("User not already be taken");
        } else
            return userOptional.get();
    }

    public User getUserWithGenres(Long chatId) {
        Optional<User> userOptional =
                userRepository.findByChatId(chatId);
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("User not already be taken");
        } else
            return userOptional.get();
    }

    public User getUserWithFullInf(Long chatId){
        Optional<User> userOptional =
                userRepository.findByChatIdAndGetFullInf(chatId);
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("User not already be taken");
        } else
            return userOptional.get();
    }

    @Transactional
    public Set<Movie> getHistoryOfViewing(long chatId) {
        Set<Movie> history = getUser(chatId).getHistoryOfViewing();
        Hibernate.initialize(history);
        return history;
    }
}
