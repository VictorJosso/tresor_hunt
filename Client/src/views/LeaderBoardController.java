package views;

import Apps.GameApp;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import utils.LeaderBoardItem;
import utils.PlayersListViewCell;
import javafx.scene.control.*;


import java.net.URL;
import java.util.ResourceBundle;

public class LeaderBoardController implements Initializable {
    @FXML
    private ListView<LeaderBoardItem> leaderBoardListView;

    private GameApp gameApp;
    private SortedList<LeaderBoardItem> sortedList;

    @FXML
    private Button revealHoleButton;

    public LeaderBoardController() {
    }

    public void setGameApp(GameApp app){
        this.gameApp = app;

        sortedList = new SortedList<>(app.getLeaderBoardItems());
        leaderBoardListView.setCellFactory(playersListView -> new PlayersListViewCell());
        leaderBoardListView.setItems(sortedList);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void disableButton() {
        if (gameApp.getPartie().getModeDeJeu().equals("3")) {
            revealHoleButton.setDisable(false);
        } else {
            revealHoleButton.setDisable(true);
        }
    }

    @FXML
    private void handleRevealHoleClick() {
        gameApp.mainApp.getConnectionHandler().send("300 REVEAL HOLE");
    }


    @FXML
    private void handleRevealMapClick() {
        int x = this.gameApp.getPlateau().getCoordonneesJoueurs().get(this.gameApp.getPlayerTurnUsername()).getX();
        int y = this.gameApp.getPlateau().getCoordonneesJoueurs().get(this.gameApp.getPlayerTurnUsername()).getY();
        gameApp.mainApp.getConnectionHandler().send("310 REVEAL MAP "+x+" "+y);

        System.out.println("310 REVEAL MAP "+x+" "+y);
    }
}
