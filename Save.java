package checkersgame;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Save {

    private final int numCB[][] = {
        {0, 1, 0, 2, 0, 3, 0, 4},
        {5, 0, 6, 0, 7, 0, 8, 0},
        {0, 9, 0, 10, 0, 11, 0, 12},
        {13, 0, 14, 0, 15, 0, 16, 0},
        {0, 17, 0, 18, 0, 19, 0, 20},
        {21, 0, 22, 0, 23, 0, 24, 0},
        {0, 25, 0, 26, 0, 27, 0, 28},
        {29, 0, 30, 0, 31, 0, 32, 0}

    };

    public static int numLoadedMoves;
    private int bCoord[][] = new int[12][2];
    private int rCoord[][] = new int[12][2];

    public void saveMoves()
            throws IOException {
        PrintStream P;
        File Save;
        String turn;

        try {
            Save = new File(getSavePath().toString());
            if (Rules.getTurn() == 'r') {
                turn = "red";
            } else {
                turn = "black";
            }
            P = new PrintStream(Save + ".txt");
            P.print(Rules.numMoves + " " + turn);
            P.println();
            for (int i = 0; i < Rules.numMoves; i++) {
                P.println(Rules.moveOrder[i]);
            }

            P.close();
        } catch (Exception e) {
        }
    }

    public void loadSavedMoves(boolean askStart) throws IOException {
        File Save;
        boolean valid;
        int numMoves;
        Scanner fileIn;
        String turn;

        turn = null;
        valid = true;
        numMoves = 0;

        try {

            Save = new File(getOpenPath().toString());

            if (Save.exists()) {
                fileIn = new Scanner(Save);

                try {
                    numMoves = Integer.parseInt(fileIn.next());
                    turn = fileIn.next();
                    numLoadedMoves = numMoves;
                    Rules.numMoves = numMoves;

                    for (int i = 0; i < Rules.numMoves; i++) {

                        Rules.moveOrder[i] = fileIn.next();
                    }
                } catch (Exception e) {
                    msgBox("Invalid File!");
                    valid = false;
                }
            } else {
                msgBox("File Does Not Exist!");
                valid = false;
            }

            //#,#_#,#c
            if (valid == true && numMoves != 0) {

                if (askStart == false) {
                    CheckersGame.onMove = Integer.parseInt(
                            inpBox("Start at turn (#" 
                                    + (Rules.numMoves + 1)
                                    + " turns): "));
                } else {
                    CheckersGame.onMove = Rules.numMoves + 1;
                }
                Rules.playMoves(CheckersGame.onMove);
                if ("red".equals(turn)) {
                    Rules.setTurn('r');
                } else {
                    Rules.setTurn('b');
                }
            }
        } catch (Exception e) {
        }
    }

    public void loadSavedState() throws IOException {
        File Save;
        Save = new File(getOpenPath().toString());
        Scanner fileIn;
        String turn;
        boolean valid;

        try {
            valid = true;
            if (Save.exists()) {
                fileIn = new Scanner(Save);
                turn = fileIn.next();

                if ("red".equals(turn)) {
                    Rules.setTurn('r');
                } else {
                    Rules.setTurn('b');
                }
                for (int i = 0; i < 8; i++) {
                    for (int count = 0; count < 8; count++) {
                        Rules.CB[i][count].name = fileIn.next();
                    }
                }
            } else {
                valid = false;
                msgBox("File Does Not Exist!");
            }
        } catch (Exception e) {
        }

    }

    private File getOpenPath() {
        File path;

        final JFileChooser fc = new JFileChooser();
        int returnVal;
        returnVal = fc.showOpenDialog(null);

        path = null;
        if (returnVal == 0) {
            path = fc.getSelectedFile();
        }

        return path;
    }

    public File getSavePath() {
        File Save;
        int retrival;

        Save = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("/home/me/Documents"));
        retrival = chooser.showSaveDialog(null);

        if (retrival == 0) {
            Save = chooser.getSelectedFile();
        }
        return Save;
    }

    private void numCBContains(String holder, int coord[][], int i) {
        int x, y;

        x = 0;
        y = 0;
        while (numCB[x][y] != Integer.parseInt(holder)) {
            y++;
            if (y == 8) {
                y = 0;
                x++;
            }
        }
        coord[i][0] = x;
        coord[i][1] = y;
    }

    private void setCoord(String pieces, int pieceCoord[][], char color) {
        int x, y;
        String holder;

        for (int i = 0; i < 12; i++) {
            pieceCoord[i][0] = -1;
        }
        holder = "0";
        for (int i = 0; i < pieces.length(); i++) {

            if (pieces.charAt(i) == ' ') {
                numCBContains(holder, pieceCoord, i);
                holder = "0";
                if (color == 'r') Rules.redCount++;
                else Rules.blackCount++;
                
            } else {
                holder += pieces.charAt(i);
            }
        }

        if (color == 'r') Rules.redCount++;
                else Rules.blackCount++;
        numCBContains(holder, pieceCoord, pieces.length() - 1);
        if (pieces.length() < 12) {
            for (int i = 0; i < 12 - pieces.length(); i++) {
                pieceCoord[pieces.length() + i][0] = -1;
            }
        }
    }

    public void userSetBoard() {
        String rUserInput;
        String bUserInput;

        Rules.redCount = 0;
        Rules.blackCount = 0;
        
        for (int i = 0; i < 8; i++) {
            for (int count = 0; count < 8; count++) {
                Rules.CB[count][i].name = "[-]";
            }
        }

        rUserInput = inpBox("Enter location of black Pieces: ");

        while (Character.isSpace(rUserInput.charAt(
                rUserInput.length() - 1))) {
            rUserInput = rUserInput.substring(0, 
                    rUserInput.length() - 1);
        }
        setCoord(rUserInput, rCoord, 'r');
        for (int i = 0; i < 12; i++) {
            if (rCoord[i][0] != -1) {
                Rules.CB[rCoord[i][0]][rCoord[i][1]].name = "[r]";
                Rules.CB[rCoord[i][0]][rCoord[i][1]].pattern = 
                        Rules.rpPattern;

            }
        }

        bUserInput = inpBox("Enter location of red Pieces: ");
        while (Character.isSpace(bUserInput.charAt(
                bUserInput.length() - 1))) {
            bUserInput = bUserInput.substring(0, 
                    bUserInput.length() - 1);
        }
        setCoord(bUserInput, bCoord, 'b');
        for (int i = 0; i < 12; i++) {
            if (bCoord[i][0] != -1) {
                Rules.CB[bCoord[i][0]][bCoord[i][1]].name = "[b]";
                Rules.CB[bCoord[i][0]][bCoord[i][1]].pattern = 
                        Rules.bpPattern;
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int count = 0; count < 8; count++) {
                Rules.CB[count][i].name = "[-]";
            }
        }

        for (int i = 0; i < 12; i++) {

            if (rCoord[i][0] >= 0 && rCoord[i][1] >= 0) {
                if (Rules.CB[rCoord[i][0]][rCoord[i][1]].
                        name.equals("[-]")) {
                    if (rCoord[i][0] == 0) {
                        Rules.CB[rCoord[i][0]][rCoord[i][1]].name = "[R]";
                    } else {
                        Rules.CB[rCoord[i][0]][rCoord[i][1]].name = "[r]";
                    }
                }

            }

            if (bCoord[i][0] >= 0 && bCoord[i][1] >= 0) {
                if (Rules.CB[bCoord[i][0]][bCoord[i][1]].name.equals("[-]")) {
                    if (bCoord[i][0] == 7) {
                        Rules.CB[bCoord[i][0]][bCoord[i][1]].name = "[B]";
                    } else {
                        Rules.CB[bCoord[i][0]][bCoord[i][1]].name = "[b]";
                    }
                }

            }
        }
        //Rules.staleM8();
    }

    public String inpBox(String dialogue) {
        String input;
        
        input = "";
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("");
        dialog.setContentText(dialogue);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
        input = result.get();
        }
        return input;
}

    public void msgBox(String dialogue) {

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("");
        alert.setContentText(dialogue); 
        
        alert.showAndWait();
    }

}
