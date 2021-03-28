package views;

import Apps.GameApp;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import utils.LeaderBoardItem;
import utils.PlayersListViewCell;

import java.net.URL;
import java.util.ResourceBundle;

public class LeaderBoardController implements Initializable {
    @FXML
    private ListView<LeaderBoardItem> leaderBoardListView;

    private GameApp gameApp;
    private SortedList<LeaderBoardItem> sortedList;

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
}
