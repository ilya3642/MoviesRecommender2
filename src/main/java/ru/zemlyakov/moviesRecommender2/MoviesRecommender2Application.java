package ru.zemlyakov.moviesRecommender2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoviesRecommender2Application {

	public static void main(String[] args){
		SpringApplication.run(MoviesRecommender2Application.class, args);
	}

}
