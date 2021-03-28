package utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class PlayersListViewCell extends ListCell<LeaderBoardItem> {
    @FXML
    private Label usernameLabel;

    @FXML
    private Label rankLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private GridPane gridPane;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(LeaderBoardItem leaderBoardItem, boolean empty) {
        super.updateItem(leaderBoardItem, empty);

        if (empty || leaderBoardItem == null){
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null){
                mLLoader = new FXMLLoader(getClass().getResource("/views/leaderBoardListCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e1){
                    e1.printStackTrace();
                }
            }
            usernameLabel.setText(leaderBoardItem.getUsername());
            rankLabel.setText(leaderBoardItem.getRank());
            scoreLabel.setText(String.valueOf(leaderBoardItem.getScore()));

            setText(null);
            setGraphic(gridPane);
        }
    }
}
