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
     //imageFileName0="../../resources/Images/Empty.PNG" imageFileName1="C:/Users/tgote/Desktop/demo/src/main/java/Images/A.JPG" imageFileName2="C:/Users/tgote/Desktop/demo/src/main/java/Images/B.JPG" imageFileName3="C:/Users/tgote/Desktop/demo/src/main/java/Images/C.JPG" imageFileName4="C:/Users/tgote/Desktop/demo/src/main/java/Images/D.JPG" imageFileName5="C:/Users/tgote/Desktop/demo/src/main/java/Images/E.JPG" imageFileName6="C:/Users/tgote/Desktop/demo/src/main/java/Images/F.JPG" imageFileName7="C:/Users/tgote/Desktop/demo/src/main/java/Images/G.JPG" imageFileName8="C:/Users/tgote/Desktop/demo/src/main/java/Images/H.JPG" imageFileName9="C:/Users/tgote/Desktop/demo/src/main/java/Images/I.JPG" imageFileName10="C:/Users/tgote/Desktop/demo/src/main/java/Images/J.JPG" imageFileName11="C:/Users/tgote/Desktop/demo/src/main/java/Images/K.JPG" imageFileName12="C:/Users/tgote/Desktop/demo/src/main/java/Images/L.JPG" imageFileName13="C:/Users/tgote/Desktop/demo/src/main/java/Images/M.JPG" imageFileName14="C:/Users/tgote/Desktop/demo/src/main/java/Images/N.JPG" imageFileName15="C:/Users/tgote/Desktop/demo/src/main/java/Images/O.JPG" imageFileName16="C:/Users/tgote/Desktop/demo/src/main/java/Images/P.JPG" imageFileName17="C:/Users/tgote/Desktop/demo/src/main/java/Images/Q.JPG" imageFileName18="C:/Users/tgote/Desktop/demo/src/main/java/Images/R.JPG" imageFileName19="C:/Users/tgote/Desktop/demo/src/main/java/Images/S.JPG" imageFileName20="C:/Users/tgote/Desktop/demo/src/main/java/Images/T.JPG" imageFileName21="C:/Users/tgote/Desktop/demo/src/main/java/Images/U.JPG" imageFileName22="C:/Users/tgote/Desktop/demo/src/main/java/Images/V.JPG" imageFileName23="C:/Users/tgote/Desktop/demo/src/main/java/Images/W.JPG" imageFileName24="C:/Users/tgote/Desktop/demo/src/main/java/Images/X.JPG" imageFileName25="C:/Users/tgote/Desktop/demo/src/main/java/Images/Y.JPG" imageFileName26="C:/Users/tgote/Desktop/demo/src/main/java/Images/Z.JPG" imageFileName27="C:/Users/tgote/Desktop/demo/src/main/java/Images/TW.jpeg" imageFileName28="C:/Users/tgote/Desktop/demo/src/main/java/Images/DW.jpeg" imageFileName29="C:/Users/tgote/Desktop/demo/src/main/java/Images/TL.jpeg" imageFileName30="C:/Users/tgote/Desktop/demo/src/main/java/Images/DL.jpeg" imageFileName31="C:/Users/tgote/Desktop/demo/src/main/java/Images/S.jpeg"
    try{
    String root = System.getProperty("user.dir")+"\\demo\\src\\main\\";
    System.out.println(root);
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
    
    
    
    // private StringProperty imageFileName0;
    // private StringProperty imageFileName1;
    // private StringProperty imageFileName2;
    // private StringProperty imageFileName3;
    // private StringProperty imageFileName4;
    // private StringProperty imageFileName5;
    // private StringProperty imageFileName6;
    // private StringProperty imageFileName7;
    // private StringProperty imageFileName8;
    // private StringProperty imageFileName9;
    // private StringProperty imageFileName10;
    // private StringProperty imageFileName11;
    // private StringProperty imageFileName12;
    // private StringProperty imageFileName13;
    // private StringProperty imageFileName14;
    // private StringProperty imageFileName15;
    // private StringProperty imageFileName16;
    // private StringProperty imageFileName17;
    // private StringProperty imageFileName18;
    // private StringProperty imageFileName19;
    // private StringProperty imageFileName20;
    // private StringProperty imageFileName21;
    // private StringProperty imageFileName22;
    // private StringProperty imageFileName23;
    // private StringProperty imageFileName24;
    // private StringProperty imageFileName25;
    // private StringProperty imageFileName26;
    // private StringProperty imageFileName27;
    // private StringProperty imageFileName28;
    // private StringProperty imageFileName29;
    // private StringProperty imageFileName30;
    // private StringProperty imageFileName31;


    
    // Constructor
       // imageFileName0 = new SimpleStringProperty();
        // imageFileName1 = new SimpleStringProperty();
        // imageFileName2 = new SimpleStringProperty();
        // imageFileName3 = new SimpleStringProperty();
        // imageFileName4 = new SimpleStringProperty();
        // imageFileName5 = new SimpleStringProperty();
        // imageFileName6 = new SimpleStringProperty();
        // imageFileName7 = new SimpleStringProperty();
        // imageFileName8 = new SimpleStringProperty();
        // imageFileName9 = new SimpleStringProperty();
        // imageFileName10 = new SimpleStringProperty();
        // imageFileName11 = new SimpleStringProperty();
        // imageFileName12 = new SimpleStringProperty();
        // imageFileName13 = new SimpleStringProperty();
        // imageFileName14 = new SimpleStringProperty();
        // imageFileName15 = new SimpleStringProperty();
        // imageFileName16 = new SimpleStringProperty();
        // imageFileName17 = new SimpleStringProperty();
        // imageFileName18 = new SimpleStringProperty();
        // imageFileName19 = new SimpleStringProperty();
        // imageFileName20 = new SimpleStringProperty();
        // imageFileName21 = new SimpleStringProperty();
        // imageFileName22 = new SimpleStringProperty();
        // imageFileName23 = new SimpleStringProperty();
        // imageFileName24 = new SimpleStringProperty();
        // imageFileName25 = new SimpleStringProperty();
        // imageFileName26 = new SimpleStringProperty();
        // imageFileName27 = new SimpleStringProperty();
        // imageFileName28 = new SimpleStringProperty();
        // imageFileName29 = new SimpleStringProperty();
        // imageFileName30 = new SimpleStringProperty();
        // imageFileName31 = new SimpleStringProperty();
    }

    // Setters, getters for imageFileName - want to return string not StringProperty
    public void setImageFileName(String imageFileName) {
        // if(i==0)
        // this.imageFileName0.set(imageFileName);
        // if(i==1)
        // this.imageFileName1.set(imageFileName);
        // if(i==2)
        // this.imageFileName2.set(imageFileName);
        // if(i==3)
        // this.imageFileName3.set(imageFileName);
        // if(i==4)
        // this.imageFileName4.set(imageFileName);
        // if(i==5)
        // this.imageFileName5.set(imageFileName);
        // if(i==6)
        // this.imageFileName6.set(imageFileName);
        // if(i==7)
        // this.imageFileName7.set(imageFileName);
        // if(i==8)
        // this.imageFileName8.set(imageFileName);
        // if(i==9)
        // this.imageFileName9.set(imageFileName);
        // if(i==10)
        // this.imageFileName10.set(imageFileName);
        // if(i==11)
        // this.imageFileName11.set(imageFileName);
        // if(i==12)
        // this.imageFileName12.set(imageFileName);
        // if(i==13)
        // this.imageFileName13.set(imageFileName);
        // if(i==14)
        // this.imageFileName14.set(imageFileName);
        // if(i==15)
        // this.imageFileName15.set(imageFileName);
        // if(i==16)
        // this.imageFileName16.set(imageFileName);
        // if(i==17)
        // this.imageFileName17.set(imageFileName);
        // if(i==18)
        // this.imageFileName18.set(imageFileName);
        // if(i==19)
        // this.imageFileName19.set(imageFileName);
        // if(i==20)
        // this.imageFileName20.set(imageFileName);
        // if(i==21)
        // this.imageFileName21.set(imageFileName);
        // if(i==22)
        // this.imageFileName22.set(imageFileName);
        // if(i==23)
        // this.imageFileName23.set(imageFileName);
        // if(i==24)
        // this.imageFileName24.set(imageFileName);
        // if(i==25)
        // this.imageFileName25.set(imageFileName);
        // if(i==26)
        // this.imageFileName26.set(imageFileName);
        // if(i==27)
        // this.imageFileName27.set(imageFileName);
        // if(i==28)
        // this.imageFileName28.set(imageFileName);
        // if(i==29)
        // this.imageFileName29.set(imageFileName);
        // if(i==30)
        // this.imageFileName30.set(imageFileName);
        // if(i==31)
        // this.imageFileName31.set(imageFileName);

    }    
   // public String getImageFileName() {
     //    return imageFileName.get();
        // if(i==1)
        // return imageFileName1.get();
        // if(i==2)
        // return imageFileName2.get();
        // if(i==3)
        // return imageFileName3.get();
        // if(i==4)
        // return imageFileName4.get();
        // if(i==5)
        // return imageFileName5.get();
        // if(i==6)
        // return imageFileName6.get();
        // if(i==7)
        // return imageFileName7.get();
        // if(i==8)
        // return imageFileName8.get();
        // if(i==9)
        // return imageFileName9.get();
        // if(i==10)
        // return imageFileName10.get();
        // if(i==11)
        // return imageFileName11.get();
        // if(i==12)
        // return imageFileName12.get();
        // if(i==13)
        // return imageFileName13.get();
        // if(i==14)
        // return imageFileName14.get();
        // if(i==15)
        // return imageFileName15.get();
        // if(i==16)
        // return imageFileName16.get();
        // if(i==17)
        // return imageFileName17.get();
        // if(i==18)
        // return imageFileName18.get();
        // if(i==19)
        // return imageFileName19.get();
        // if(i==20)
        // return imageFileName20.get();
        // if(i==21)
        // return imageFileName21.get();
        // if(i==22)
        // return imageFileName22.get();
        // if(i==23)
        // return imageFileName23.get();
        // if(i==24)
        // return imageFileName24.get();
        // if(i==25)
        // return imageFileName25.get();
        // if(i==26)
        // return imageFileName26.get();
        // if(i==27)
        // return imageFileName27.get();
        // if(i==28)
        // return imageFileName28.get();
        // if(i==29)
        // return imageFileName29.get();
        // if(i==30)
        // return imageFileName30.get();
        // if(i==31)
        // return imageFileName31.get();
        
        // return imageFileName0.get();
   // }

    // Setter, getter for BoardData
    public void setBoardData(int[][] boardData) {
        BoardData = boardData;
        redraw();
    }
    public int[][] getBoardData(){
        return BoardData;
    }

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
            
          
            
           // try {
                // imagesArray[0] = new Image(new FileInputStream(imageFileName0.get()));
                // imagesArray[1] = new Image(new FileInputStream(imageFileName1.get()));
                // imagesArray[2] = new Image(new FileInputStream(imageFileName2.get()));
                // imagesArray[3] = new Image(new FileInputStream(imageFileName3.get()));
                // imagesArray[4] = new Image(new FileInputStream(imageFileName4.get()));
                // imagesArray[5] = new Image(new FileInputStream(imageFileName5.get()));
                // imagesArray[6] = new Image(new FileInputStream(imageFileName6.get()));
                // imagesArray[7] = new Image(new FileInputStream(imageFileName7.get()));
                // imagesArray[8] = new Image(new FileInputStream(imageFileName8.get()));
                // imagesArray[9] = new Image(new FileInputStream(imageFileName9.get()));
                // imagesArray[10] = new Image(new FileInputStream(imageFileName10.get()));
                // imagesArray[11] = new Image(new FileInputStream(imageFileName11.get()));
                // imagesArray[12] = new Image(new FileInputStream(imageFileName12.get()));
                // imagesArray[13] = new Image(new FileInputStream(imageFileName13.get()));
                // imagesArray[14] = new Image(new FileInputStream(imageFileName14.get()));
                // imagesArray[15] = new Image(new FileInputStream(imageFileName15.get()));
                // imagesArray[16] = new Image(new FileInputStream(imageFileName16.get()));
                // imagesArray[17] = new Image(new FileInputStream(imageFileName17.get()));
                // imagesArray[18] = new Image(new FileInputStream(imageFileName18.get()));
                // imagesArray[19] = new Image(new FileInputStream(imageFileName19.get()));
                // imagesArray[20] = new Image(new FileInputStream(imageFileName20.get()));
                // imagesArray[21] = new Image(new FileInputStream(imageFileName21.get()));
                // imagesArray[22] = new Image(new FileInputStream(imageFileName22.get()));
                // imagesArray[23] = new Image(new FileInputStream(imageFileName23.get()));
                // imagesArray[24] = new Image(new FileInputStream(imageFileName24.get()));
                // imagesArray[25] = new Image(new FileInputStream(imageFileName25.get()));
                // imagesArray[26] = new Image(new FileInputStream(imageFileName26.get()));
                // imagesArray[27] = new Image(new FileInputStream(imageFileName27.get()));
                // imagesArray[28] = new Image(new FileInputStream(imageFileName28.get()));
                // imagesArray[29] = new Image(new FileInputStream(imageFileName29.get()));
                // imagesArray[30] = new Image(new FileInputStream(imageFileName30.get()));
                // imagesArray[31] = new Image(new FileInputStream(imageFileName31.get()));


            //  } catch (FileNotFoundException e) {
            //     e.printStackTrace();
            // }

            // Draw wherever there is 0
            for(int i=0; i<BoardData.length; i++){
                for(int j =0; j< BoardData[i].length; j++){
                    System.out.println(i+" "+j+" "+BoardData[i][j]);
                    gc.drawImage(imagesArray[BoardData[i][j]],j*w_cell, i*h_cell, w_cell, h_cell);
                                       
                }
            }
        }
    }
}
