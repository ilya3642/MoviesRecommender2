package ru.zemlyakov.moviesRecommender2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zemlyakov.moviesRecommender2.models.User;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveOrUpdate(User newUser) {
            userRepository.save(newUser);
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

}
