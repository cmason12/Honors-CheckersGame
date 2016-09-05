package checkersgame;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class CheckersGame extends Application {

    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 50;
    private static final int NUM_PIECES = 12;
    private static int piece[] = new int[2];
    private static int toSpace[] = new int[2];
    private static int numClicks = 0;
    private static Rules rule;
    private final Save save = new Save();
    private static boolean legalMove;
    private final Image rsPattern
            = new Image(getClass().getResource("/Resources/"
                    + "Square1.jpg").toString());
    private final Image bsPattern
            = new Image(getClass().getResource("/Resources/"
                    + "Square2.jpg").toString());
    private final Image bpPattern
            = new Image(getClass().getResource("/Resources/"
                    + "BlackPiece.png").toString());

    private final Image rpPattern = new Image(getClass().getResource(
            "/Resources/" + "RedPiece.jpg").toString());
    private static String player1Name = null;
    private static String player2Name = null;
    private static String winner;
    private static char turn;

    static boolean piecePreview = false;
    private static int[] previewSquare = new int[2];
    private int movedPiece[] = new int[2];
    private Circle c = new Circle(SQUARE_SIZE / 2 - 6, Color.BLACK);
    private static boolean moveAssist = true;
    private MenuItem moveA = new MenuItem("Move Assist: ON");
    private MenuItem dragP = new MenuItem("Drag Piece: OFF");
    private MenuItem clickP = new MenuItem("Click Piece: ON");
    private Label[] p1Name = new Label[8];
    private Label[] p2Name = new Label[8];
    public static int onMove;
    private static boolean replayLoaded = false;
    private boolean dragEnabled = false;

    private static final DropShadow HIGHLIGHT
            = new DropShadow(15, Color.YELLOW);

    public class StartScreen extends Stage {

        Button openOther = new Button("Start Game");
        HBox x = new HBox();

        StartScreen() {
            openOther.setFont(new Font("Arial", 30));
            openOther.setTextFill(new ImagePattern(rpPattern));
            this.setTitle("Checkers Game");
            this.setResizable(false);
            this.getIcons().add(new Image(getClass().getResource("/Resources/"
                    + "CBIcon.png").toString()));
            x.getChildren().add(openOther);
            x.setAlignment(Pos.CENTER);
            x.setStyle("-fx-background-image: url('/Resources/CBIcon.png')");
            this.setScene(new Scene(x, 350, 350));
            this.show();

            openOther.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    new CheckersGameGUI();
                    close();
                }
            });
        }
    }

    public class CheckersGameGUI extends Stage {


        private void resetBoard(GridPane board) {

            for (int i = 0; i < 8; i++) {
                for (int count = 0; count < 8; count++) {
                    if(Character.isLetter(Rules.CB[count][i].
                            name.charAt(1))){
                        board.getChildren().remove(Rules.CB[count][i].piece);
                    }
                }
            }
            
            Rules.intCB();
            addPiecesToBoard(board);
            replayLoaded = false;

        }

        private void updateBoard(GridPane board) {

            for (int i = 0; i < 8; i++) {
                for (int count = 0; count < 8; count++) {
                    if(Character.isLetter(Rules.CB[count][i].
                            name.charAt(1))){
                        board.getChildren().remove(Rules.CB[count][i].piece);
                        Rules.CB[count][i].name = "[-]";
                    }
                }
            }

        }

        private void changeName(Label lbl[], int pos) {
            int player;
            String name;
            int numLessThan;

            if (pos == 1) {
                player = 1;
            } else {
                player = 2;
            }
            name = "aaaaaaaaaaaaaaaaaaaaaa";
            while (name.length() > 8) {
                name = save.inpBox("Player " + player + " Name: ");
                try {
                    if (name.length() > 8) {
                        save.msgBox("Name too long, Maximum size 8 "
                                + "characters");
                    }
                } catch (Exception e) {
                    name = "        ";
                }
            }

            if (name.length() < 8) {
                numLessThan = 8 - (8 - name.length());
                for (int i = numLessThan; i < 8; i++) {
                    name += " ";
                }
            }
            for (int i = 0; i < 8; i++) {
                lbl[i].setText(name.charAt(i) + "");
            }
            if (pos == 1) {
                player1Name = name;
            } else {
                player2Name = name;
            }

        }

        CheckersGameGUI() {
            this.setTitle("CheckersGame");
            this.getIcons().add(new Image(getClass().getResource("/Resources/"
                    + "CBIcon.png").toString()));
            Scene scene = new Scene(new Group());
            final GridPane checkerBoard = new GridPane();
            int numLessThan;
            String name;
            String temp;
            String temp2;

            Rules.intCB();
            temp = player2Name;
            temp2 = player1Name;
            if (player2Name != null) {

                player1Name = temp;

            } else {
                name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
                while (name.length() > 8) {
                    name = save.inpBox("Player 1 Name: ");
                    try {
                        if (name.length() > 8) {
                            save.msgBox("Name too long, Maximum size 8 "
                                    + "characters");
                        }
                    } catch (Exception e) {
                        name = "        ";
                    }
                }
                if (name.length() < 8) {
                    numLessThan = 8 - (8 - name.length());
                    for (int i = numLessThan; i < 8; i++) {
                        name += " ";
                    }
                }
                player1Name = name;
            }

            for (int i = 0; i < player1Name.length(); i++) {
                p1Name[i] = new Label(player1Name.charAt(i) + " ");
                p1Name[i].setFont(new Font("Arial", 30));
                p1Name[i].setTextFill(new ImagePattern(rpPattern));
                p1Name[i].setTextAlignment(TextAlignment.JUSTIFY);
            }

            for (int i = 0; i < player1Name.length(); i++) {
                checkerBoard.add(p1Name[i], 0 + (i), 500);
            }

            if (player2Name != null) {
                player2Name = temp2;
            } else {
                name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
                while (name.length() > 8) {
                    name = save.inpBox("Player 2 Name: ");
                    try {
                        if (name.length() > 8) {
                            save.msgBox("Name too long, Maximum size 8"
                                    + " characters");
                        }
                    } catch (Exception e) {
                        name = "        ";
                    }
                }

                if (name.length() < 8) {
                    numLessThan = 8 - (8 - name.length());
                    for (int i = numLessThan; i < 8; i++) {
                        name += " ";
                    }
                }
                player2Name = name;
            }

            for (int i = 0; i < player2Name.length(); i++) {
                p2Name[i] = new Label(player2Name.charAt(i) + " ");
                p2Name[i].setFont(new Font("Arial", 30));
                p2Name[i].setTextFill(new ImagePattern(bpPattern));
                p2Name[i].setTextAlignment(TextAlignment.JUSTIFY);
            }
            for (int i = 0; i < player2Name.length(); i++) {
                checkerBoard.add(p2Name[i], 0 + (i), 0);
            }
            this.setResizable(false);
            configureBoardLayout(checkerBoard);
            addSquaresToBoard(checkerBoard);

            addPiecesToBoard(checkerBoard);

            BorderPane root;
            root = new BorderPane();
            root.setCenter(checkerBoard);
            this.setScene(new Scene(root, 400, 530, Color.BLACK));
            root.setStyle("-fx-background-image: "
                    + "url('/Resources/background.jpg')");

            MenuBar menuBar = new MenuBar();
            Menu file = new Menu("_File");
            Menu option = new Menu("_Options");
            Menu edit = new Menu("_Edit");
            MenuItem pSave = new MenuItem("----Save----");
            MenuItem saveMoves = new MenuItem("Save Replay");

            MenuItem saveState = new MenuItem("Save Game");
            MenuItem pLoad = new MenuItem("----Load----");
            MenuItem loadMoves = new MenuItem("Load Replay");
            MenuItem loadState = new MenuItem("Load Game");

            MenuItem reset = new MenuItem("Reset");
            MenuItem click = new MenuItem("--Click--");
            MenuItem drag = new MenuItem("--Drag--");
            MenuItem nameC = new MenuItem("--Change Name--");
            MenuItem fPlayer = new MenuItem("Player 1");

            MenuItem sPlayer = new MenuItem("Player 2");
            MenuItem setBoard = new MenuItem("Set Board");
            MenuItem lMoves = new MenuItem("--Replay--");
            MenuItem forward = new MenuItem("Step Forward");
            MenuItem backwards = new MenuItem("Step Backwards");

            menuBar.getMenus().addAll(file, option, edit);
            file.getItems().addAll(pSave, saveState, saveMoves,
                     pLoad, loadState, loadMoves);
            option.getItems().addAll(reset, drag, dragP, click, clickP,
                    moveA, nameC, fPlayer, sPlayer);
            edit.getItems().addAll(setBoard, lMoves, forward, backwards);
            root.setTop(menuBar);

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override

                public void handle(MouseEvent event) {
                    if (dragEnabled == true) {
                        piece[0] = getCoord(event.getSceneY(), 'x');
                        piece[1] = getCoord(event.getSceneX(), 'y');
                        if (Rules.isPieceAt(piece)) {

                            Rules.CB[piece[0]][piece[1]].
                                    piece.setCursor(Cursor.HAND);
                        }
                    }
                }

            });

            root.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override

                public void handle(MouseEvent event) {
                    int[] jpCoord;
                    if (Rules.isPieceAt(piece) && dragEnabled == true) {

                        toSpace[0] = getCoord(event.getSceneY(), 'x');
                        toSpace[1] = getCoord(event.getSceneX(), 'y');

                        if (toSpace[1] != -1 && toSpace[0] != -1) {
                            legalMove = Rules.isLegal(piece, 
                                    toSpace, false);
                        } else {
                            legalMove = false;
                        }

                        if (legalMove) {

                            checkerBoard.getChildren().remove(
                                    Rules.CB[piece[0]][piece[1]].piece);
                            legalMove = Rules.isLegal(piece, toSpace, true);
                            Rules.CB[toSpace[0]][toSpace[1]].piece.
                                    setFill(new ImagePattern(
                                            Rules.CB[toSpace[0]]
                                                    [toSpace[1]].pattern));
                            
                            switch (Rules.CB[toSpace[0]]
                                        [toSpace[1]].name.charAt(1)) {
                                    case 'R':
                                        Rules.CB[toSpace[0]]
                                                [toSpace[1]].piece.
                                                setStroke(Color.LIGHTGREY);
                                        Rules.CB[toSpace[0]]
                                                [toSpace[1]].piece.
                                                setStrokeWidth(4);
                                        break;
                                    case 'B':
                                        Rules.CB[toSpace[0]]
                                                [toSpace[1]].piece.
                                                setStroke(Color.RED);
                                        Rules.CB[toSpace[0]]
                                                [toSpace[1]].piece.
                                                setStrokeWidth(4);
                                        break;
                                    default:
                                        Rules.CB[toSpace[0]]
                                                [toSpace[1]].piece.
                                                setStrokeWidth(0);
                                        break;
                            }
                                        
                            checkerBoard.add(Rules.CB[toSpace[0]]
                                    [toSpace[1]].piece, toSpace[1],
                                    toSpace[0] + 1);

                            if (Math.abs(toSpace[1] - piece[1]) == 2) {
                                jpCoord = Rules.returnJP();
                                checkerBoard.getChildren().
                                        remove(Rules.CB[jpCoord[0]]
                                                [jpCoord[1]].piece);

                                if (Rules.gameEnded() != 'c') {
                                    Alert alert = new Alert(null,
                                            "Save Replay?",
                                            ButtonType.YES, ButtonType.NO);
                                    alert.showAndWait();

                                    if (alert.getResult() == 
                                            ButtonType.YES) {
                                        try {
                                            save.saveMoves();
                                        } catch (Exception ex) {
                                            Logger.getLogger(
                                                    CheckersGame.
                                                            class.getName()).
                                                    log(Level.SEVERE, 
                                                            null, ex);
                                        }
                                    }
                                    
                                    replayLoaded = false;
                                    new ResetGame();
                                    close();
                                }
                            }

                        } else {
                            checkerBoard.getChildren().remove(
                                    Rules.CB[piece[0]][piece[1]].piece);

                            try {
                                Rules.CB[toSpace[0]][toSpace[1]].piece.
                                    setFill(new ImagePattern(
                                            Rules.CB[toSpace[0]]
                                                    [toSpace[1]].pattern));
                            } catch (Exception e){}
                           
                            checkerBoard.add(Rules.CB[piece[0]]
                                    [piece[1]].piece, piece[1],
                                    piece[0] + 1);
                        }
                    }

                }

            });

            root.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override

                public void handle(MouseEvent event) {

                    int[] pieceAt = new int[2];
                    pieceAt[0] = getCoord(event.getSceneY(), 'x');
                    pieceAt[1] = getCoord(event.getSceneX(), 'y');
                    if (Rules.isPieceAt(pieceAt)) {

                        Rules.CB[pieceAt[0]][pieceAt[1]].
                                piece.setCursor(Cursor.HAND);
                    }

                    if (event.getEventType() == MouseEvent.
                            MOUSE_MOVED && moveAssist == true
                            && dragEnabled == false) {

                        if (numClicks == 1) {

                            c.setMouseTransparent(true);
                            c.setFill(Color.rgb(0, 128, 0, .40));
                            c.setEffect(new GaussianBlur());

                            toSpace[0] = getCoord(event.getSceneY(), 'x');
                            toSpace[1] = getCoord(event.getSceneX(), 'y');

                            if (toSpace[1] != -1 && toSpace[0] != -1) {
                                legalMove = Rules.isLegal(piece,
                                        toSpace, false);
                            } else {
                                legalMove = false;
                            }

                          
                            if (legalMove) {

                                checkerBoard.getChildren().remove(c);
                                checkerBoard.add(c, toSpace[1],
                                        toSpace[0] + 1);
                                c.setOpacity(50);

                                previewSquare[0] = toSpace[0];
                                previewSquare[1] = toSpace[1];

                            } else if (previewSquare[0] != 0 &&
                                    previewSquare[1] != 0
                                    && movedPiece[0] != previewSquare[0]
                                    && movedPiece[1] != previewSquare[1]) {
                                checkerBoard.getChildren().remove(c);
                            }
                        }

                        if (previewSquare[0] != getCoord(
                                event.getSceneY(), 'x')
                                && previewSquare[1] != getCoord(
                                        event.getSceneX(), 'y')) {

                            checkerBoard.getChildren().remove(c);
                        }
                    }
                }
            });

            root.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override

                public void handle(MouseEvent event) {
                    int[] jpCoord = new int[2];

                    if (replayLoaded == false && dragEnabled == false) {
                        if (numClicks == 0) {
                            //clickedOnce = true;
                            piece[0] = getCoord(event.getSceneY(), 'x');
                            piece[1] = getCoord(event.getSceneX(), 'y');
                            if (Rules.isPieceAt(piece)) {

                                switch (Rules.CB[piece[0]]
                                        [piece[1]].name.charAt(1)) {
                                    case 'R':
                                        Rules.CB[piece[0]]
                                                [piece[1]].piece.
                                                setStroke(Color.LIGHTGREY);
                                        Rules.CB[piece[0]]
                                                [piece[1]].piece.
                                                setStrokeWidth(4);
                                        break;
                                    case 'B':
                                        Rules.CB[piece[0]]
                                                [piece[1]].piece.
                                                setStroke(Color.RED);
                                        Rules.CB[piece[0]]
                                                [piece[1]].piece.
                                                setStrokeWidth(4);
                                        break;
                                    default:
                                        Rules.CB[piece[0]]
                                                [piece[1]].piece.
                                                setStrokeWidth(0);
                                        break;
                                }
                                Rules.CB[piece[0]][piece[1]].
                                        piece.setEffect(HIGHLIGHT);
                                checkerBoard.getChildren().
                                        remove(Rules.CB[piece[0]]
                                                [piece[1]].piece);
                                checkerBoard.add(Rules.CB[piece[0]]
                                        [piece[1]].piece, piece[1],
                                        piece[0] + 1);
                                if (piece[0] != -1) {
                                    numClicks++;
                                }
                            }
                        } else {
                            toSpace[0] = getCoord(event.getSceneY(), 'x');
                            toSpace[1] = getCoord(event.getSceneX(), 'y');
                            numClicks = 0;
                            legalMove = Rules.isLegal(piece, toSpace, true);
                            if (legalMove) {

                                switch (Rules.CB[toSpace[0]]
                                        [toSpace[1]].name.charAt(1)) {
                                    case 'R':
                                        Rules.CB[toSpace[0]]
                                                [toSpace[1]].piece.
                                                setStroke(Color.LIGHTGREY);
                                        Rules.CB[toSpace[0]]
                                                [toSpace[1]].piece.
                                                setStrokeWidth(4);
                                        break;

                                    case 'B':
                                        Rules.CB[toSpace[0]]
                                                [toSpace[1]].piece.
                                                setStroke(Color.RED);
                                        Rules.CB[toSpace[0]]
                                                [toSpace[1]].piece.
                                                setStrokeWidth(4);
                                        break;
                                    default:
                                        Rules.CB[toSpace[0]][toSpace[1]].
                                                piece.setStrokeWidth(0);
                                        break;
                                }
                                movedPiece[0] = toSpace[0];
                                movedPiece[1] = toSpace[1];
                                turn = Rules.getTurn();

                                checkerBoard.setEffect(null);
                                checkerBoard.getChildren().
                                        remove(Rules.CB[piece[0]]
                                                [piece[1]].piece);
                                Rules.CB[toSpace[0]][toSpace[1]].
                                        piece.setFill(new ImagePattern(
                                                Rules.CB[toSpace[0]]
                                                        [toSpace[1]].
                                                        pattern));
                                Rules.CB[toSpace[0]][toSpace[1]].
                                        piece.setEffect(null);
                                checkerBoard.add(
                                        Rules.CB[toSpace[0]][toSpace[1]].
                                                piece, toSpace[1],
                                        toSpace[0] + 1);
                                if (Math.abs(toSpace[1] - piece[1]) == 2) {
                                    jpCoord = Rules.returnJP();
                                    checkerBoard.getChildren().
                                            remove(Rules.CB[jpCoord[0]]
                                                    [jpCoord[1]].piece);
                                }
                            } else {
                                Rules.CB[piece[0]][piece[1]].piece.
                                        setFill(new ImagePattern(
                                                Rules.CB[piece[0]]
                                                        [piece[1]].
                                                        pattern));
                                Rules.CB[piece[0]][piece[1]].piece.
                                        setEffect(null);
                            }
                        }
                        if (Rules.gameEnded() != 'c') {
                            Alert alert = new Alert(null, "Save Replay?",
                                    ButtonType.YES, ButtonType.NO);
                            alert.showAndWait();

                            if (alert.getResult() == ButtonType.YES) {
                                try {
                                    save.saveMoves();
                                } catch (Exception ex) {
                                    Logger.getLogger(CheckersGame.
                                            class.getName()).
                                            log(Level.SEVERE, null, ex);
                                }
                            }

                            new ResetGame();
                            close();
                        }
                    } else if (replayLoaded == true) {
                        int nMoves;
                        MouseButton button = event.getButton();
                        if (button == MouseButton.PRIMARY) {
                            if (onMove <= Save.numLoadedMoves) {
                                onMove++;
                                updateBoard(checkerBoard);
                                Rules.intCB();

                                Rules.playMoves(onMove);
                                addPiecesToBoard(checkerBoard);
                            }
                        } else if (button == MouseButton.SECONDARY) {
                            if (onMove > 1) {
                                onMove--;
                                updateBoard(checkerBoard);
                                Rules.intCB();

                                Rules.playMoves(onMove);
                                addPiecesToBoard(checkerBoard);
                            }
                        } else;

                    } else;

                }
            });

            saveMoves.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        save.saveMoves();
                    } catch (Exception ex) {
                        Logger.getLogger(CheckersGame.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }
                }
            });

            saveState.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Save save = new Save();
                    try {
                        save.saveMoves();
                    } catch (IOException ex) {
                        Logger.getLogger(CheckersGame.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }
                }
            });

            loadState.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Save save = new Save();
                    try {
                        updateBoard(checkerBoard);
                        Rules.intCB();
                        save.loadSavedMoves(true);
                        addPiecesToBoard(checkerBoard);
                    } catch (Exception e) {
                        Logger.getLogger(CheckersGame.class.getName()).
                                log(Level.SEVERE, null, e);
                        updateBoard(checkerBoard);
                        Rules.intCB();
                        addPiecesToBoard(checkerBoard);
                    }
                }
            });

            loadMoves.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Save save = new Save();
                    try {
                        updateBoard(checkerBoard);
                        Rules.intCB();
                        save.loadSavedMoves(false);
                        addPiecesToBoard(checkerBoard);
                        replayLoaded = true;
                    } catch (Exception ex) {
                        Logger.getLogger(CheckersGame.class.getName()).
                                log(Level.SEVERE, null, ex);
                        updateBoard(checkerBoard);
                        Rules.intCB();
                        addPiecesToBoard(checkerBoard);
                    }
                }
            });

            setBoard.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        updateBoard(checkerBoard);
                        save.userSetBoard();
                        addPiecesToBoard(checkerBoard);
                    } catch (Exception e) {
                        updateBoard(checkerBoard);
                        Rules.intCB();
                        addPiecesToBoard(checkerBoard);
                        save.msgBox("Invalid Input!");
                    }

                }
            });

            forward.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int nMoves;

                    if (replayLoaded == true) {
                        if (onMove != Rules.numMoves) {
                            updateBoard(checkerBoard);
                            nMoves = Rules.numMoves;
                            onMove++;
                            Rules.intCB();

                            Rules.playMoves(onMove);
                            addPiecesToBoard(checkerBoard);
                        }
                    } else {
                        save.msgBox("Replay not loaded!");
                    }

                }
            });

            backwards.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    int nMoves;

                    if (replayLoaded == true) {
                        if (onMove != 0) {
                            updateBoard(checkerBoard);
                            nMoves = Rules.numMoves;
                            onMove--;
                            Rules.intCB();

                            Rules.playMoves(onMove);
                            addPiecesToBoard(checkerBoard);
                        }
                    } else {
                        save.msgBox("Replay not loaded!");
                    }

                }
            });

            dragP.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (dragEnabled == false) {
                        dragEnabled = true;
                        dragP.setText("Drag Piece: ON");
                        clickP.setText("Click Piece: OFF");
                        Rules.CB[piece[0]][piece[1]].piece.setEffect(null);
                        moveA.setText("Move Assist: OFF");
                    } else {
                        dragEnabled = false;
                        dragP.setText("Drag Piece: OFF");
                        clickP.setText("Click Piece: ON");
                    }

                }
            });

            clickP.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (dragEnabled == false) {
                        //clickP.setText("Click Piece: OFF");
                    } else {
                        dragEnabled = false;
                        clickP.setText("Click Piece: ON");
                        dragP.setText("Drag Piece: OFF");
                    }

                }
            });

            moveA.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (moveAssist == false && dragEnabled == false) {
                        moveAssist = true;
                        moveA.setText("Move Assist: ON");
                    } else {
                        moveAssist = false;
                        moveA.setText("Move Assist: OFF");
                    }

                }
            });

            fPlayer.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    changeName(p1Name, 1);
                }
            });

            sPlayer.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    changeName(p2Name, 2);
                }
            });

            reset.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    resetBoard(checkerBoard);
                }
            });

            this.show();
        }
    }

    public class ResetGame extends Stage {

        Button resetGame = new Button("Play Again?");

        Label winningPlayer;
        StackPane rootPane = new StackPane();
        HBox x = new HBox();
        HBox x2 = new HBox();

        ResetGame() {
            DropShadow ds = new DropShadow();
            ds.setOffsetY(4.0f);
            ds.setColor(Color.BLACK);

            if (Rules.gameEnded() == 'r') {
                winner = player1Name;
            }
            if (Rules.gameEnded() == 'b') {
                winner = player2Name;
            }
            Rules.intCB();
            winningPlayer = new Label(winner + "\nWINS");
            winningPlayer.setFont(Font.font("Arial", FontWeight.BOLD, 
                    60));
            winningPlayer.setTextFill(Color.WHITE);
            winningPlayer.setEffect(ds);
            winningPlayer.setWrapText(true);

            this.setResizable(false);
            this.setTitle("Checkers Game");
            this.getIcons().add(new Image(getClass().getResource(
                    "/Resources/CBIcon.png").toString()));
            x.getChildren().add(winningPlayer);
            x.setAlignment(Pos.TOP_CENTER);

            resetGame.setFont(new Font("Arial", 30));
            resetGame.setTextFill(new ImagePattern(rpPattern));
            x2.getChildren().add(resetGame);

            x2.setAlignment(Pos.BOTTOM_CENTER);
            x.setStyle(
                    "-fx-background-image: "
                    + "url('/Resources/CBIcon.png')");
            this.setScene(new Scene(rootPane, 350, 350));
            rootPane.getChildren().addAll(x, x2);
            this.show();

            resetGame.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    new CheckersGameGUI();
                    close();
                }
            });
        }
    }

    @Override
    public void start(Stage primaryStage) {
        new StartScreen();
    }

    private int getCoord(double var, char letter) {
        int z;
        int off;

        off = 0;
        if (letter == 'y') {
            off = 0;
        }
        if (letter == 'x') {
            off = 85;
        }

        z = (((var - off)) < 0) ? -1
                : ((var - off) <= 50) ? 0
                : ((var - off) <= 100) ? 1
                : ((var - off) <= 150) ? 2
                : ((var - off) <= 200) ? 3
                : ((var - off) <= 250) ? 4
                : ((var - off) <= 300) ? 5
                : ((var - off) <= 350) ? 6
                : ((var - off) <= 400) ? 7
                : -1;
        return z;
    }

    private void addSquaresToBoard(GridPane board) {
        Color[] squareColors = new Color[]{Color.WHITE, Color.BLACK};
        for (int row = 1; row < BOARD_SIZE + 1; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle rect = new Rectangle(SQUARE_SIZE,
                        SQUARE_SIZE, squareColors[(row + col) % 2]);
                if ((row + col) % 2 == 0) {
                    rect.setFill(new ImagePattern(rsPattern));
                } else {
                    rect.setFill(new ImagePattern(bsPattern));
                }
                rect.setMouseTransparent(true);

                board.add(rect, col, row);
            }
        }
    }

    private void addPiecesToBoard(GridPane checkerBoard) {
        for (int i = 0; i < 8; i++) {
            for (int count = 0; count < 8; count++) {
                if (Rules.CB[i][count].name.equals("[r]")
                        || Rules.CB[i][count].name.equals("[R]")
                        || Rules.CB[i][count].name.equals("[b]")
                        || Rules.CB[i][count].name.equals("[B]")) {
                    if (Character.toLowerCase(
                            Rules.CB[i][count].name.charAt(1)) == 'r') {
                        Rules.CB[i][count].piece.
                                setFill(new ImagePattern(rpPattern));
                    }
                    if (Character.toLowerCase(
                            Rules.CB[i][count].name.charAt(1)) == 'b') {
                        Rules.CB[i][count].piece.setFill(new ImagePattern(
                                bpPattern));
                    }

                    if (Rules.CB[i][count].name.charAt(1) == 'R') {
                        Rules.CB[i][count].piece.setStroke(Color.LIGHTGREY);
                        Rules.CB[i][count].piece.setStrokeWidth(4);
                    }

                    if (Rules.CB[i][count].name.charAt(1) == 'B') {
                        Rules.CB[i][count].piece.setStroke(Color.RED);
                        Rules.CB[i][count].piece.setStrokeWidth(4);
                    }
                    checkerBoard.add(Rules.CB[i][count].piece, count, i + 1);
                }
            }
        }
    }

    private void configureBoardLayout(GridPane board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(SQUARE_SIZE);
            rowConstraints.setPrefHeight(SQUARE_SIZE);
            rowConstraints.setMaxHeight(SQUARE_SIZE);
            rowConstraints.setValignment(VPos.CENTER);
            board.getRowConstraints().add(rowConstraints);

            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setMinWidth(SQUARE_SIZE);
            colConstraints.setMaxWidth(SQUARE_SIZE);
            colConstraints.setPrefWidth(SQUARE_SIZE);
            colConstraints.setHalignment(HPos.CENTER);
            board.getColumnConstraints().add(colConstraints);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
