package com.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BoardDisplayer extends Canvas {
    
    int[][] BoardData;
    Image imagesArray[] = new Image[32];
    public BoardDisplayer() {
     try{
    String root = System.getProperty("user.dir")+"\\src\\main\\";
    
    InputStream stream = new FileInputStream(root+"resources\\Images\\Empty.PNG");
    imagesArray[0] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\A.JPG");
    imagesArray[1] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\B.JPG");
    imagesArray[2] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\C.JPG");
    imagesArray[3] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\D.JPG");
    imagesArray[4] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\E.JPG");
    imagesArray[5] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\F.JPG");
    imagesArray[6] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\G.JPG");
    imagesArray[7] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\H.JPG");
    imagesArray[8] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\I.JPG");
    imagesArray[9] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\J.JPG");
    imagesArray[10] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\K.JPG");
    imagesArray[11] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\L.JPG");
    imagesArray[12] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\M.JPG");
    imagesArray[13] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\N.JPG");
    imagesArray[14] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\O.JPG");
    imagesArray[15] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\P.JPG");
    imagesArray[16] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\Q.JPG");
    imagesArray[17] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\R.JPG");
    imagesArray[18] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\S.JPG");
    imagesArray[19] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\T.JPG");
    imagesArray[20] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\U.JPG");
    imagesArray[21] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\V.JPG");
    imagesArray[22] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\W.JPG");
    imagesArray[23] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\X.JPG");
    imagesArray[24] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\Y.JPG");
    imagesArray[25] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\Z.JPG");
    imagesArray[26] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\TW.jpeg");
    imagesArray[27] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\DW.jpeg");
    imagesArray[28] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\TL.jpeg");
    imagesArray[29] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\DL.jpeg");
    imagesArray[30] = new Image(stream);
    stream = new FileInputStream(root+"resources\\Images\\S.jpeg");
    imagesArray[31] = new Image(stream);
    } catch(FileNotFoundException e){
        e.printStackTrace();
    }
    

    }   

    // Setter, getter for BoardData
    public void setBoardData(int[][] boardData) {
        BoardData = boardData;
        redraw();
    }
    // Getter for BoardData
    public int[][] getBoardData(){
        return BoardData;
    }

    // Redraw the board
    public void redraw(){
        if(BoardData != null)
        {
            double W_Canvas = getWidth();
            double H_Canvas = getHeight();
            double w_cell = W_Canvas / BoardData[0].length;
            double h_cell = H_Canvas / BoardData.length;

            // To draw on canvas. 
            // getGraphicsContext2D is from Canvas
            GraphicsContext gc = getGraphicsContext2D();
                           
            // Draw wherever there is 0
            for(int i=0; i<BoardData.length; i++){
                for(int j =0; j< BoardData[i].length; j++){
                    gc.drawImage(imagesArray[BoardData[i][j]],j*w_cell, i*h_cell, w_cell, h_cell);
                                       
                }
            }
        }
    }
}
