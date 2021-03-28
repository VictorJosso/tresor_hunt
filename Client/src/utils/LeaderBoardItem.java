package utils;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

public class LeaderBoardItem implements Comparable<LeaderBoardItem> {
    private final StringProperty username;
    private final StringProperty rank;
    private final IntegerProperty score;

    public LeaderBoardItem(String username, String rank, Integer score) {
        this.username = new SimpleStringProperty(username);
        this.rank = new SimpleStringProperty(rank);
        this.score = new SimpleIntegerProperty(score);
    }

    public LeaderBoardItem(){
        this(null, null, null);
    }

    @Override
    public int compareTo(LeaderBoardItem itemToCompare) {
        return itemToCompare.getScore() - this.getScore();
    }

    public static Callback<LeaderBoardItem, Observable[]> extractor() {
        return new Callback<LeaderBoardItem, Observable[]>() {
            @Override
            public Observable[] call(LeaderBoardItem param) {
                return new Observable[]{param.username, param.rank, param.score};
            }
        };
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getRank() {
        return rank.get();
    }

    public StringProperty rankProperty() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank.set(rank);
    }

    public int getScore() {
        return score.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void setScore(int score) {
        this.score.set(score);
    }
}
