package checkersgame;

import static java.lang.Math.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Rules {


    public static CheckerPiece[][] CB = new CheckerPiece[8][8];
    private static boolean pieceTaken;
    public static int redCount = 12;
    public static int blackCount = 12;
    private static char turn = 'r';
    private static int xChange, yChange;
    public static String[] moveOrder = new String[500];
    public static int numMoves = 0;
    private static int[] jpCoord = new int[2];
    private static boolean endGame;
    public static Image bpPattern = new Image("http://gimpforums.com"
            + "/attachment.php?aid=2158");
    public static Image rpPattern = new Image("http://www.wallpaper"
            + "hi.com/thumbnails/detail/20120709/black%20and%20white%"
            + "20minimalistic%20pattern%201680x1050%20wallpaper_www.w"
            + "allpaperhi.com_79.jpg");
    
   private static  int numMovAv;

    Rules() {
        intCB();
    }

    public static void intCB() {
        for (int i = 0; i < 8; i++) {
            for (int count = 0; count < 8; count++) {
                CB[i][count] = new CheckerPiece();
            }
        }

        CB[0][0].name = "[-]";
        CB[0][1].name = "[b]";
        CB[0][2].name = "[-]";
        CB[0][3].name = "[b]";
        CB[0][4].name = "[-]";
        CB[0][5].name = "[b]";
        CB[0][6].name = "[-]";
        CB[0][7].name = "[b]";

        CB[1][0].name = "[b]";
        CB[1][1].name = "[-]";
        CB[1][2].name = "[b]";
        CB[1][3].name = "[-]";
        CB[1][4].name = "[b]";
        CB[1][5].name = "[-]";
        CB[1][6].name = "[b]";
        CB[1][7].name = "[-]";

        CB[2][0].name = "[-]";
        CB[2][1].name = "[b]";
        CB[2][2].name = "[-]";
        CB[2][3].name = "[b]";
        CB[2][4].name = "[-]";
        CB[2][5].name = "[b]";
        CB[2][6].name = "[-]";
        CB[2][7].name = "[b]";

        for (int i = 3; i < 5; i++) {
            for (int count = 0; count < 8; count++) {
                CB[i][count].name = "[-]";
            }
        }

        CB[5][0].name = "[r]";
        CB[5][1].name = "[-]";
        CB[5][2].name = "[r]";
        CB[5][3].name = "[-]";
        CB[5][4].name = "[r]";
        CB[5][5].name = "[-]";
        CB[5][6].name = "[r]";
        CB[5][7].name = "[-]";

        CB[6][0].name = "[-]";
        CB[6][1].name = "[r]";
        CB[6][2].name = "[-]";
        CB[6][3].name = "[r]";
        CB[6][4].name = "[-]";
        CB[6][5].name = "[r]";
        CB[6][6].name = "[-]";
        CB[6][7].name = "[r]";

        CB[7][0].name = "[r]";
        CB[7][1].name = "[-]";
        CB[7][2].name = "[r]";
        CB[7][3].name = "[-]";
        CB[7][4].name = "[r]";
        CB[7][5].name = "[-]";
        CB[7][6].name = "[r]";
        CB[7][7].name = "[-]";

        for (int i = 0; i < 8; i++) {
            for (int count = 0; count < 8; count++) {
                if ("[r]".equals(CB[i][count].name)) {
                    CB[i][count].pattern = rpPattern;
                } else if ("[b]".equals(CB[i][count].name)) {
                    CB[i][count].pattern = bpPattern;
                } else {
                    CB[i][count].pattern = null;
                }
                CB[i][count].piece = new Circle(50 / 2 - 4, Color.BLACK);
            }
        }
        redCount = 12;
        blackCount = 12;
        numMoves = 0;
        turn = 'r';
    }

    public static boolean isLegal(int piece[], int toSpace[], 
            boolean update) {
        boolean leftRight;
        boolean legal;
        int numJA;
        String jumpedPiece;

        numJA = 0;

        legal = false;
        xChange = (toSpace[0]) - (piece[0]);
        yChange = (toSpace[1]) - (piece[1]);

        if (CB[piece[0]][piece[1]].name.equals("[r]") && turn == 'r') {

            if (CB[toSpace[0]][toSpace[1]].name.equals("[-]")
                    && xChange == -1) {
                legal = (xChange == -1);
                leftRight = (abs(yChange) == 1);
                legal = (leftRight == true && legal == true);
            }
            if (xChange == -2 && abs(yChange) == 2){
            jumpedPiece
                    = CB[toSpace[0] - xChange / 2]
                    [toSpace[1] - yChange / 2].name;
            if (jumpedPiece.equals("[b]") || jumpedPiece.equals("[B]")) {
                legal = (xChange == -2);
                legal = (CB[toSpace[0]][toSpace[1]].name.equals("[-]"));
                leftRight = (abs(yChange) == 2);
                if (leftRight == true && legal == true) {
                    legal = true;
                    jpCoord[0] = toSpace[0] - xChange / 2;
                    jpCoord[1] = toSpace[1] - yChange / 2;
                    pieceTaken = true;
                    
                    if (update == true) blackCount--;
                } else legal = false;
                
            }
        }

        }
            
        if (CB[piece[0]][piece[1]].name.equals("[R]") && turn == 'r') {
            if (CB[toSpace[0]][toSpace[1]].name.equals("[-]")) {
                legal = (abs(xChange) == 1);
                leftRight = (abs(yChange) == 1);
                legal = (leftRight == true && legal == true);
            }

            if (abs(xChange) == 2 && abs(yChange) == 2){
            jumpedPiece
                    = CB[toSpace[0] - xChange / 2]
                    [toSpace[1] - yChange / 2].name;
            if (jumpedPiece.equals("[b]") || jumpedPiece.equals("[B]")) {
                legal = (abs(xChange) == 2);
                legal = (CB[toSpace[0]][toSpace[1]].name.equals("[-]"));
                leftRight = (abs(yChange) == 2);
                if (leftRight == true && legal == true) {
                    jpCoord[0] = toSpace[0] - xChange / 2;
                    jpCoord[1] = toSpace[1] - yChange / 2;
                    pieceTaken = true;
                    if (update == true) blackCount--;
                } else legal = false;
                
            }
        }
        }

        if (CB[piece[0]][piece[1]].name.equals("[b]") && turn == 'b') {
            if (CB[toSpace[0]][toSpace[1]].name.equals("[-]")) {
                legal = (xChange == 1);
                leftRight = (abs(yChange) == 1);
                legal = (leftRight == true && legal == true);
            }
            
             if (xChange == 2 && abs(yChange) == 2){
            jumpedPiece
                    = CB[toSpace[0] - xChange / 2]
                    [toSpace[1] - yChange / 2].name;
            if (jumpedPiece.equals("[r]") || jumpedPiece.equals("[R]")) {
                legal = (xChange == 2);
                legal = (CB[toSpace[0]][toSpace[1]].name.equals("[-]"));
                leftRight = (abs(yChange) == 2);
                if (leftRight == true && legal == true) {
                    legal = true;
                    jpCoord[0] = toSpace[0] - xChange / 2;
                    jpCoord[1] = toSpace[1] - yChange / 2;
                    pieceTaken = true;
                    
                    if (update == true) redCount--;
                } else legal = false;
                
            }
        }
        }

        if (CB[piece[0]][piece[1]].name.equals("[B]") && turn == 'b') {
            if (CB[toSpace[0]][toSpace[1]].name.equals("[-]")) {
                legal = (abs(xChange) == 1);
                leftRight = (abs(yChange) == 1);
                legal = (leftRight == true && legal == true);
            }
            
            if (abs(xChange) == 2 && abs(yChange) == 2){
            jumpedPiece
                    = CB[toSpace[0] - xChange / 2]
                    [toSpace[1] - yChange / 2].name;
            if (jumpedPiece.equals("[r]") || jumpedPiece.equals("[R]")) {
                legal = (abs(xChange) == 2);
                legal = (CB[toSpace[0]][toSpace[1]].name.equals("[-]"));
                leftRight = (abs(yChange) == 2);
                if (leftRight == true && legal == true) {
                    jpCoord[0] = toSpace[0] - xChange / 2;
                    jpCoord[1] = toSpace[1] - yChange / 2;
                    pieceTaken = true;
                    if (update == true) redCount--;
                    
                } else legal = false;
                
            }
            }
        }
            
        int[] testPiece = new int[2];
        if (turn == 'r') {
            for (int i = 0; i < 8; i++) {
                for (int count = 0; count < 8; count++) {
                    testPiece[0] = i;
                    testPiece[1] = count;
                    if (CB[i][count].name.equals("[r]")) {
                        if (isJumpAvaliable(-2, testPiece)) numJA++;
                        
                        
                    }
                    if (CB[i][count].name.equals("[R]")) {
                        if (isJumpAvaliable(-2, testPiece)) numJA++;
                        
                        if (isJumpAvaliable(2, testPiece)) numJA++;
                        
                    }
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                for (int count = 0; count < 8; count++) {
                    testPiece[0] = i;
                    testPiece[1] = count;
                    if (CB[i][count].name.equals("[b]")) {
                        if (isJumpAvaliable(2, testPiece))  numJA++;
                        
                    }
                    if (CB[i][count].name.equals("[B]")) {
                        if (isJumpAvaliable(-2, testPiece)) numJA++;
                        
                        if (isJumpAvaliable(2, testPiece))  numJA++;
                        
                    }
                }
            }
        }
        if (numJA > 0 && Math.abs(xChange) != 2) legal = false;
        else{
            if (isPieceAt(piece) && update == true) {
                //staleM8(); 
           }
        }
       
        
        if (legal == true && update == true) updateBoard(piece, toSpace);
        //System.out.println("black count: " + redCount);
        //System.out.println("red count: " + blackCount);

        return legal;
    }

    private static boolean updateBoard(int piece[], int toSpace[]) {

        xChange = (toSpace[0]) - (piece[0]);
        yChange = (toSpace[1]) - (piece[1]);
        
        endGame = false;
        //#,#_#,#K
        numMoves++;
        moveOrder[numMoves - 1] = piece[0] + "," + piece[1]
                + "_" + toSpace[0] + "," + toSpace[1];

        if (CB[piece[0]][piece[1]].name.equals("[r]") && turn == 'r') {
            if (pieceTaken == true) {
                pieceTaken = false;
                CB[jpCoord[0]][jpCoord[1]].name = "[-]";
                CB[piece[0]][piece[1]].name = "[-]";
                CB[toSpace[0]][toSpace[1]].pattern
                        = CB[piece[0]][piece[1]].pattern;
                CB[toSpace[0]][toSpace[1]].name = "[r]";
                if (toSpace[0] != 0) 
                    CB[toSpace[0]][toSpace[1]].name = "[r]";
                 else 
                    CB[toSpace[0]][toSpace[1]].name = "[R]";
                

                if (isJumpAvaliable(xChange, toSpace) == false)  {
                    turn = 'b';
                    numMovAv = 0;
                }
           
               
            } else  {
                turn = 'b';
                numMovAv = 0;
            }
           
            
            CB[piece[0]][piece[1]].name = "[-]";
            CB[toSpace[0]][toSpace[1]].pattern
                    = CB[piece[0]][piece[1]].pattern;
            CB[toSpace[0]][toSpace[1]].name = "[r]";

            if (toSpace[0] != 0) 
                CB[toSpace[0]][toSpace[1]].name = "[r]";
            else 
                CB[toSpace[0]][toSpace[1]].name = "[R]";
            
        }

        if (CB[piece[0]][piece[1]].name.equals("[R]") && turn == 'r') {
            if (pieceTaken == true) {
                pieceTaken = false;
                CB[jpCoord[0]][jpCoord[1]].name = "[-]";

                CB[piece[0]][piece[1]].name = "[-]";
                CB[toSpace[0]][toSpace[1]].pattern
                        = CB[piece[0]][piece[1]].pattern;
                CB[toSpace[0]][toSpace[1]].name = "[R]";
                if (isJumpAvaliable(xChange, toSpace) == false) {
                    turn = 'b';
                    numMovAv = 0;
                }
                
            } else {
                turn = 'b'; 
                numMovAv = 0;
                CB[piece[0]][piece[1]].name = "[-]";
                CB[toSpace[0]][toSpace[1]].pattern
                        = CB[piece[0]][piece[1]].pattern;
                CB[toSpace[0]][toSpace[1]].name = "[R]";
            }
        }
        if (CB[piece[0]][piece[1]].name.equals("[b]") && turn == 'b') {
            if (pieceTaken == true) {
                pieceTaken = false;
                CB[jpCoord[0]][jpCoord[1]].name = "[-]";
                CB[toSpace[0]][toSpace[1]].pattern
                        = CB[piece[0]][piece[1]].pattern;
                CB[piece[0]][piece[1]].name = "[-]";
                if (toSpace[0] != 7) 
                    CB[toSpace[0]][toSpace[1]].name = "[b]";
                 else 
                    CB[toSpace[0]][toSpace[1]].name = "[B]";
                
                if (isJumpAvaliable(xChange, toSpace) == false) {
                    turn = 'r';
                    numMovAv = 0;
                }
                
            } else {
                turn = 'r';
       
                CB[piece[0]][piece[1]].name = "[-]";
                CB[toSpace[0]][toSpace[1]].pattern
                        = CB[piece[0]][piece[1]].pattern;
                if (toSpace[0] != 7) 
                    CB[toSpace[0]][toSpace[1]].name = "[b]";
                else 
                    CB[toSpace[0]][toSpace[1]].name = "[B]";
            }
        }

        if (CB[piece[0]][piece[1]].name.equals("[B]") && turn == 'b') {
            if (pieceTaken == true) {
                pieceTaken = false;
                CB[jpCoord[0]][jpCoord[1]].name = "[-]";

                CB[piece[0]][piece[1]].name = "[-]";
                CB[toSpace[0]][toSpace[1]].pattern
                        = CB[piece[0]][piece[1]].pattern;
                CB[toSpace[0]][toSpace[1]].name = "[B]";
                if (isJumpAvaliable(xChange, toSpace) == false) {
                    turn = 'r';
                    numMovAv = 0;
                }
                
            } else {
                turn = 'r';
                numMovAv = 0;

                CB[toSpace[0]][toSpace[1]].pattern
                        = CB[piece[0]][piece[1]].pattern;
                CB[piece[0]][piece[1]].name = "[-]";
                CB[toSpace[0]][toSpace[1]].name = "[B]";
            }
        }

        if ("[B]".equals(CB[toSpace[0]][toSpace[1]].name)
                || "[R]".equals(CB[toSpace[0]][toSpace[1]].name)) {
            moveOrder[numMoves - 1] += CB[toSpace[0]]
                    [toSpace[1]].name.charAt(1) + " ";
        } else 
            moveOrder[numMoves - 1] += turn + " ";
        

        endGame = (redCount == 0 || blackCount == 0);
        
        
        return endGame;
    }
    
    public static void staleM8(){
            boolean aMoveAv;
            int tPiece[] = new int[2];
            int count = 0;
           
            int numtruetimes;
            
            aMoveAv = false;
            numtruetimes = 0;
            int i = 0;
            
            for (; i < 8 && aMoveAv != true; i++){
                
                count = 0;
                for (; count < 8 && aMoveAv != true; count++){
                    tPiece[0] = i;
                    tPiece[1] = count;
                    if (isPieceAt(tPiece)){
                        
                        
                        if (!aMoveAv){
                        aMoveAv = moveAvaliable(tPiece, 
                                CB[i][count].name.charAt(1));
                        }
                         if (turn == 'r')   {

                                if (CB[i][count].name.equals("[r]")) {
                                    if (isJumpAvaliable(-2, 
                                            tPiece)) {
                                        count = 0;
                                        aMoveAv = true;
                                        numMovAv++;
                                    }
                        
                                    }
                                if (CB[i][count].name.equals("[R]")) {
                                        if (isJumpAvaliable(-2, tPiece)) 
                                        {
                                           count = 0;
                                           aMoveAv = true;
                                           numMovAv++;
                                        } 
                        
                                        if (isJumpAvaliable(2, tPiece)) 
                                        {
                                            aMoveAv = true;
                                            count = 0;
                                            numMovAv++;
                                        }
                                }

                        } else {

                                tPiece[0] = i;
                                tPiece[1] = count;
                                if (CB[i][count].name.equals("[b]")) {
                                    if (isJumpAvaliable(2, tPiece))  
                                    {
                                        aMoveAv = true;
                                        count = 0;
                                        numMovAv++;
                                    }
                                }
                                 if (CB[i][count].name.equals("[B]")) {
                                        if (isJumpAvaliable(-2, tPiece))
                                        {
                                            aMoveAv = true;
                                            count = 0;
                                            numMovAv++;
                                        }
                        
                                        if (isJumpAvaliable(2, tPiece))  
                                        {
                                            aMoveAv = true;
                                            count = 0;
                                            numMovAv++;
                                        }  
                                }
                        }
                         
                         
                      
                      
                    }
                    
                   
                      
                       numtruetimes += numMovAv;
                       System.out.println(" move avaialable: " + numtruetimes);  
                    
                }
                
                
                 if ( numtruetimes == 0 && aMoveAv == false && numMovAv == 0 ) {
                        if (turn == 'r') redCount = 0;
                        else blackCount = 0;
                } 
            }
            
            
           
        
    }

    public static void playMoves(int toTurn) {
        int pM[] = new int[2];
        int sM[] = new int[2];

        for (int i = 0; i < toTurn - 1; i++) {

            pM[0] = Integer.parseInt(moveOrder[i].charAt(0) + "");
            pM[1] = Integer.parseInt(moveOrder[i].charAt(2) + "");
            sM[0] = Integer.parseInt(moveOrder[i].charAt(4) + "");
            sM[1] = Integer.parseInt(moveOrder[i].charAt(6) + "");
            isLegal(pM, sM, true);

        }

    }
    
    
    private static boolean moveAvaliable(int[] testPiece, char p) {
        int testMove[] = new int[2];
        boolean oMA;
        
        oMA = false;
        
        
        if (p == 'r' && turn == 'r' && testPiece[0] != 0) {
            if (testPiece[1] != 0 ){
                testMove[0] = testPiece[0] -1;
                testMove[1] = testPiece[1] -1;
                if (isLegal(testPiece, testMove, false)) {
                    oMA = true;
                    numMovAv++;
                }
            }
            
            if (testPiece[1] != 7 ){
                testMove[0] = testPiece[0] -1;
                testMove[1] = testPiece[1] +1;
                if (isLegal(testPiece, testMove, false)) {
                    oMA = true;
                    numMovAv++;
                }
            }
        }
        
        if (p == 'b' && turn == 'b' && testPiece[1] != 7) {
            if (testPiece[1] != 0) {
                testMove[0] = testPiece[0] +1;
                testMove[1] = testPiece[1] -1;
                if (isLegal(testPiece, testMove, false)) {
                    oMA = true;
                    numMovAv++;
                }
            }
            
            if (testPiece[1] != 7) {
                testMove[0] = testPiece[0] +1;
                testMove[1] = testPiece[1] +1;
                if (isLegal(testPiece, testMove, false)) {
                    oMA = true;
                    numMovAv++;
                }
            }
        }
        
         if (p == 'R' && turn == 'r') {
             if (testPiece[0] != 0){
                if (testPiece[1] != 0) { 
                    testMove[0] = testPiece[0] -1;
                    testMove[1] = testPiece[1] -1;
                    if (isLegal(testPiece, testMove, false)) {
                        oMA = true;
                        numMovAv++;
                    }
                }
             
            if (testPiece[1] != 7){
                testMove[0] = testPiece[0] -1;
                testMove[1] = testPiece[1] +1;
                if (isLegal(testPiece, testMove, false)) {
                    oMA = true;
                    numMovAv++;
                }
                }
             }
            
             if (testPiece[0] != 7) {
                if (testPiece[1] != 0) {
                    testMove[0] = testPiece[0] +1;
                    testMove[1] = testPiece[1] -1;
                    if (isLegal(testPiece, testMove, false)) {
                        oMA = true;
                        numMovAv++;
                    }
                }
            
                    if (testPiece[1]  != 7){
                    testMove[0] = testPiece[0] +1;
                    testMove[1] = testPiece[1] +1;
                    if (isLegal(testPiece, testMove, false)) {
                        oMA = true;
                        numMovAv++;
                    }
                }
             }
        }
         
         if (p == 'B' && turn == 'b') {
             if (testPiece[0] != 0){
                if (testPiece[1] != 0) { 
                    testMove[0] = testPiece[0] -1;
                    testMove[1] = testPiece[1] -1;
                    if (isLegal(testPiece, testMove, false)) {
                        oMA = true;
                        numMovAv++;
                    }
                }
             
            if (testPiece[1] != 7){
                testMove[0] = testPiece[0] -1;
                testMove[1] = testPiece[1] +1;
                if (isLegal(testPiece, testMove, false)) {
                    oMA = true;
                    numMovAv++;
                }
                }
             }
            
             if (testPiece[0] != 7) {
                if (testPiece[1] != 0) {
                    testMove[0] = testPiece[0] +1;
                    testMove[1] = testPiece[1] -1;
                    if (isLegal(testPiece, testMove, false)) {
                        oMA = true;
                        numMovAv++;
                    }
                }
            
                    if (testPiece[1]  != 7){
                    testMove[0] = testPiece[0] +1;
                    testMove[1] = testPiece[1] +1;
                    if (isLegal(testPiece, testMove, false)) {
                        oMA = true;
                        numMovAv++;
                    }
                }
             }
 
        }
         
         return oMA;
    }

    private static boolean isJumpAvaliable(int xCHANGE, int[] piece) {
        boolean legal;
        legal = false;
        String pToJump;
        String kToJump;
        String pieceToJump;

        if (xCHANGE < 0) {
            pToJump = "[b]";
            kToJump = "[B]";
        } else {
            pToJump = "[r]";
            kToJump = "[R]";
        }

        if (CB[piece[0]][piece[1]].name.charAt(1) == 'R') {
            pToJump = "[b]";
            kToJump = "[B]";
        }
        if (CB[piece[0]][piece[1]].name.charAt(1) == 'B') {
            pToJump = "[r]";
            kToJump = "[R]";
        }

        try {
            pieceToJump = CB[piece[0] + xCHANGE / 2][piece[1] + 1].
                    name;
            if (pieceToJump.equals(pToJump) || pieceToJump.
                    equals(kToJump)) {
                if (CB[piece[0] + xCHANGE][piece[1] + 2].
                        name.equals("[-]")) {
                    legal = true;
                }
            }

            pieceToJump = CB[piece[0] + xCHANGE / 2][piece[1] - 1].name;
            if (pieceToJump.equals(pToJump) || pieceToJump.
                    equals(kToJump)) {
                if (CB[piece[0] + xCHANGE][piece[1] - 2].
                        name.equals("[-]")) {
                    legal = true;
                }
            }
        } catch (Exception e) {
        }

        return legal;
    }

    public static char getTurn() {
        return turn;
    }

    public static void setTurn(char t) {
        turn = t;
    }

    public static boolean isPieceAt(int[] piece) {
        boolean pieceAt;

        pieceAt = false;
        try {
            if (turn == 'r') {
                if (CB[piece[0]][piece[1]].name.equals("[r]")) 
                    pieceAt = true;
                
                if (CB[piece[0]][piece[1]].name.equals("[R]")) 
                    pieceAt = true;
                
            } else {
                if (CB[piece[0]][piece[1]].name.equals("[b]")) 
                    pieceAt = true;
                
                if (CB[piece[0]][piece[1]].name.equals("[B]")) 
                    pieceAt = true;
                
            }
        } catch (Exception e) {
            pieceAt = false;
        }
        return pieceAt;
    }

    public static int[] returnJP() {

        return jpCoord;
    }

    public static boolean isKing(int[] piece) {
        boolean king;

        king = (Character.isUpperCase(
                CB[piece[0]][piece[1]].name.charAt(1)));
        return king;
    }

    public static char gameEnded() {
        char winner;

        winner = 'c';
        if (redCount == 0) {
            winner = 'b';
        }
        if (blackCount == 0) {
            winner = 'r';
        }

        return winner;
    }
    
    public static int getRedCount(){
        return redCount;
    }
    
    public static int getBlackCount() {
        return blackCount;
    }

}
