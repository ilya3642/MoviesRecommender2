package ru.zemlyakov.moviesRecommender2.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
public class Rating {

    @Column(
            name = "rating_kinopoisk",
            nullable = false,
            columnDefinition = "real"
    )
    @Size(max = 10)
    private float ratingKinopoisk;

    @Column(
            name = "rating_imdb",
            nullable = false,
            columnDefinition = "real"
    )
    @Size(max = 10)
    private float ratingIMDB;

    @Column(
            name = "rating_rotten_tomatoes",
            columnDefinition = "real"
    )
    @Size(max = 10)
    private Float ratingRottenTomatoes;

    @Column(
            name = "rating_metacritic",
            columnDefinition = "real"
    )
    @Size(max = 10)
    private Float ratingMetacritic;

    private float weightedAverage;


    public Rating(float ratingKinopoisk, float ratingIMDB, Float ratingRottenTomatoes, Float ratingMetacritic) {
        this.ratingKinopoisk = ratingKinopoisk;
        this.ratingIMDB = ratingIMDB;
        this.ratingRottenTomatoes = ratingRottenTomatoes;
        this.ratingMetacritic = ratingMetacritic;

        weightedAverage = ratingKinopoisk + ratingIMDB;
        float countScore = 0;

        if (ratingKinopoisk != 0)
            countScore++;
        if (ratingIMDB != 0)
            countScore++;

        if (ratingRottenTomatoes == 0 && ratingMetacritic == 0) {
            weightedAverage /= 2f;
        } else if (ratingRottenTomatoes == 0) {
            weightedAverage = (weightedAverage + ratingMetacritic) / ++countScore;
        }
        else if (ratingMetacritic == 0) {
            weightedAverage = (weightedAverage + ratingRottenTomatoes) / ++countScore;
        }
        else {
            weightedAverage += (ratingRottenTomatoes + ratingMetacritic) / 2f;
            weightedAverage /= ++countScore;
        }

    }

    public Rating() {
    }

    public float getRatingKinopoisk() {
        return ratingKinopoisk;
    }

    public void setRatingKinopoisk(float ratingKinopoisk) {
        this.ratingKinopoisk = ratingKinopoisk;
    }

    public float getRatingIMDB() {
        return ratingIMDB;
    }

    public void setRatingIMDB(float ratingIMDB) {
        this.ratingIMDB = ratingIMDB;
    }

    public Float getRatingRottenTomatoes() {
        return ratingRottenTomatoes;
    }

    public void setRatingRottenTomatoes(Float ratingRottenTomatoes) {
        this.ratingRottenTomatoes = ratingRottenTomatoes;
    }

    public Float getRatingMetacritic() {
        return ratingMetacritic;
    }

    public void setRatingMetacritic(Float ratingMetacritic) {
        this.ratingMetacritic = ratingMetacritic;
    }

    public float getWeightedAverage() {
        return weightedAverage;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ratingKinopoisk=" + ratingKinopoisk +
                ", ratingIMDB=" + ratingIMDB +
                ", ratingRottenTomatoes=" + ratingRottenTomatoes +
                ", ratingMetacritic=" + ratingMetacritic +
                ", weightedAverage=" + weightedAverage +
                '}';
    }
}
