package views;

import Apps.GameApp;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import utils.LeaderBoardItem;
import utils.PlayersListViewCell;
import javafx.scene.control.*;
import java.util.Random;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * The type Leader board controller.
 */
public class LeaderBoardController implements Initializable {
    @FXML
    private ListView<LeaderBoardItem> leaderBoardListView;

    private GameApp gameApp;
    private SortedList<LeaderBoardItem> sortedList;

    @FXML
    private Button revealHoleButton;

    @FXML
    private Button revealMapButton;

    /**
     * Instantiates a new Leader board controller.
     */
    public LeaderBoardController() {
    }

    /**
     * Set game app.
     *
     * @param app the app
     */
    public void setGameApp(GameApp app){
        this.gameApp = app;

        sortedList = new SortedList<>(app.getLeaderBoardItems());
        leaderBoardListView.setCellFactory(playersListView -> new PlayersListViewCell());
        leaderBoardListView.setItems(sortedList);
        if (!(gameApp.getPartie().getModeDeJeu().equals("3"))) {
            revealHoleButton.setVisible(false);
            revealMapButton.setVisible(false);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }



    @FXML
    private void handleRevealHoleClick() {
        gameApp.mainApp.getConnectionHandler().send("300 REVEAL HOLE");
    }


    @FXML
    private void handleRevealMapClick() {
        Random random = new Random();

        int x = random.nextInt(this.gameApp.getPlateau().getDimX()-4);
        int y = random.nextInt(this.gameApp.getPlateau().getDimY()-4);
        gameApp.mainApp.getConnectionHandler().send("310 REVEAL MAP "+x+" "+y);

        System.out.println("310 REVEAL MAP "+x+" "+y);
    }
}
