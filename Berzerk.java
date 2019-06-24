package sample;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.media.AudioClip;
import java.net.URL;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import java.util.*;

public class JeffreyPaulraj extends Application implements EventHandler<InputEvent>{
    GraphicsContext gc;
    Sprite sprite;
    Image character;
    AnimateObjects animate;
    int x = 0;
    int y = 0;
    AudioClip move;
    Canvas canvas;
    AudioClip background;
    AudioClip shootSound;
    AudioClip EnemySmash;
    AudioClip heartSound;
    ArrayList<EnemyNinja> totEnemies;
    double xinc = 0;
    //boolean xdir = true;
    boolean gameOver;
    int lives;
    int lvlNum = 1;
    Level lvlOne;
    Level lvlTwo;
    Heart heart;
    ArrayList<heroBullet> heroShoot;
    ArrayList<enemyBullet> enemyShoot;
    boolean shootStart;
    int heroX;
    int heroY;
    int score;
    boolean canMoveUp;
    boolean canMoveDown;
    boolean canMoveRight;
    boolean canMoveLeft;
    boolean gameWon;
    public static void main(String[]args){
        launch();
    }

    public void start(Stage stage){
     //   totEnemies= new ArrayList<EnemyNinja>();
        heroShoot = new ArrayList<heroBullet>();
        enemyShoot = new ArrayList<enemyBullet>();
        stage.setTitle("Berzerk");
        Group root = new Group();
        gameWon = false;
        lvlOne = new Level(1);
        lvlTwo = new Level(2);
        canvas = checkLevel().getCanvas();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        sprite = new Sprite(new Image("file:/Users/Jeffrey/Desktop/APCS/spriteRight.png"));
        character = sprite.getCharacter();
        createEnemies();
        lives = 3;
        heart = new Heart();
        URL shootURL = getClass().getResource("bulletSound.wav");
        shootSound = new AudioClip(shootURL.toString());
        URL enemySmash = getClass().getResource("enemySmash.wav");
        EnemySmash = new AudioClip(enemySmash.toString());
        URL heartURL = getClass().getResource("heart.wav");
        heartSound = new AudioClip(heartURL.toString());
        scene.addEventHandler(KeyEvent.KEY_PRESSED, this);
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
        animate = new AnimateObjects();
        animate.start();
        stage.show();
    }

    public void handle(final InputEvent event){
        //URL moveLink = getClass().getResource("Slap.wav");
        //move = new AudioClip(moveLink.toString());
        if(event instanceof KeyEvent){
            boolean left = ((KeyEvent)event).getCode() == KeyCode.LEFT;
            boolean right = ((KeyEvent)event).getCode() == KeyCode.RIGHT;
            boolean up = ((KeyEvent)event).getCode() == KeyCode.UP;
            boolean down = ((KeyEvent)event).getCode() == KeyCode.DOWN;
            boolean w = ((KeyEvent)event).getCode() == KeyCode.W;
            boolean a = ((KeyEvent)event).getCode() == KeyCode.A;
            boolean s = ((KeyEvent)event).getCode() == KeyCode.S;
            boolean d = ((KeyEvent)event).getCode() == KeyCode.D;
            boolean restart = ((KeyEvent)event).getCode() == KeyCode.ESCAPE;
            Rectangle2D restartRect = new Rectangle2D(400,502,400,400 );


            if(left && up){
                x-=1;
                y+=1;
            }
            else if(left && down){
                x-=1;
                y-=1;
            }
            else if(right && up){
                x+=1;
                y+=1;
            }
            else if(right && down){
                x+=1;
                y-=1;
            }
            else if(left && canMoveLeft){
                x-=1;
                sprite.setCharacter(new Image("file:/Users/Jeffrey/Desktop/APCS/spriteLeft.png"));
            }
            else if(right && canMoveRight){
                x+=1;
                sprite.setCharacter(new Image("file:/Users/Jeffrey/Desktop/APCS/spriteRight.png"));
            }
            else if(down && canMoveDown){
                y+=1;
                sprite.setCharacter(new Image("file:/Users/Jeffrey/Desktop/APCS/spriteLeft.png"));

            }
            else if(up && canMoveUp){
                y-=1;
                sprite.setCharacter(new Image("file:/Users/Jeffrey/Desktop/APCS/spriteRight.png"));
            }
            if(w){
                heroShoot.add(new heroBullet(sprite, "up"));
                shootStart = true;
                shootSound.play();
            }
            if(s){
                heroShoot.add(new heroBullet(sprite, "down"));
                shootStart = true;
                shootSound.play();
            }
            if(d){
                heroShoot.add(new heroBullet(sprite, "right"));
                shootStart = true;
                shootSound.play();
            }
            if(a){
               heroShoot.add(new heroBullet(sprite, "left"));
               shootStart = true;
               shootSound.play();
            }
            if(restart && (lives <= 0 || gameWon == true)){
               gameOver = false;
               lvlNum = 1;
               canvas = checkLevel().getCanvas();
               lives = 3;
               score = 0;
               createEnemies();
               heart = new Heart();
               x = 0;
               y = 0;
              // heart.setPresent(true);
            }

            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
        if(event instanceof MouseEvent){
          // System.out.println( ((MouseEvent)event).getX() );
           //System.out.println( ((MouseEvent)event).getY() );
        }
    }

    public class EnemyNinja{

        double x;
        double y;
        int xRange;
        int yRange;
        Image enemy;
        Rectangle2D rect;
        Boolean xdir;

        public EnemyNinja(double x, double y, int xRange, int yRange){
            this.x = x;
            this.y = y;
            this.xRange = xRange;
            this.yRange = yRange;
            enemy = new Image("file:/Users/Jeffrey/Desktop/APCS/enemy.png");
            xdir = true;
            this.rect = new Rectangle2D(x,y, enemy.getWidth(),enemy.getHeight());
        }

        public double getX(){
            return x;
        }
        public double getY(){
            return y;
        }
        public int getXRange(){
            return xRange;
        }
        public Image getImage(){
            return enemy;
        }
        public Rectangle2D getRect(){
            return rect;
        }
        public void setRect(Rectangle2D recta){
            rect = recta;
        }
        public boolean getXDir(){
            return xdir;
        }
        public void setXDir(boolean dir){
            xdir = dir;
        }

    }

    public class Heart{
        Image image;
        Rectangle2D rect;
        int i;
        boolean present;
        public Heart(){
            this.image = new Image("file:/Users/Jeffrey/Desktop/APCS/heart.png");
            if(lvlNum == 1)
                this.rect = new Rectangle2D(300,300,image.getWidth(),image.getHeight());
            if(lvlNum == 2)
                this.rect = new Rectangle2D(200,300,image.getWidth(),image.getHeight());
            this.present = true;
        }
        public Heart(int i){

        }
        public Image getImage() {
            return image;
        }

        public Rectangle2D getRect() {
            return rect;
        }

        public boolean isPresent() {
            return present;
        }

        public void setPresent(boolean present) {
            this.present = present;
        }
    }
    public class Sprite{

        Image character;
        Rectangle2D rect;

        public Sprite(Image character){
            this.character = character;
            rect = new Rectangle2D(100 + 3*x, 100 + 3*y, character.getWidth(), character.getHeight());
        }

        public Image getCharacter(){
            return character;
        }
        public Rectangle2D getRect(){
            return rect;
        }
        public void setCharacter(Image image){
            character = image;
        }
        public void setRect(Rectangle2D recta){
            rect = recta;
        }
    }

    public class Level{

        Canvas canvas;
        ArrayList<Rectangle2D> arr;

        public Level(int i){
            canvas = new Canvas(1200,900);
            arr = new ArrayList<Rectangle2D>();
            if(i == 1) {
                arr.add(new Rectangle2D(120,100,900,50));
                arr.add(new Rectangle2D(120,100,50,200));
                arr.add(new Rectangle2D(1020, 100, 50,200));
                arr.add(new Rectangle2D(120,600,900,50));
                arr.add(new Rectangle2D(120, 450, 50, 200));
                arr.add(new Rectangle2D(1020,450,50,200));
            }
            if(i == 2){
                arr.add(new Rectangle2D(0,50, 500,50));
                arr.add(new Rectangle2D(0,50, 50, 800));
                arr.add(new Rectangle2D(0, 700, 500,100));
                arr.add(new Rectangle2D(500,50,50,400));
                arr.add(new Rectangle2D(500, 600 , 50, 200));

                arr.add(new Rectangle2D(650,50, 500,50));
                arr.add(new Rectangle2D(1150,50, 50, 800));
                arr.add(new Rectangle2D(650, 700, 500,100));
                arr.add(new Rectangle2D(650,50,50,400));
                arr.add(new Rectangle2D(650, 600 , 50, 200));
            }
        }

        public Canvas getCanvas() {
            return canvas;
        }

        public ArrayList<Rectangle2D> getArr(){
            return arr;
        }
    }

    public class heroBullet{
        Image image;
        Rectangle2D rect;
        int speed;
        String dir;
        double bulletXInc;
        double bulletYInc;
        double startX;
        double startY;

        public heroBullet(Sprite sprite, String dir){
            if(dir.equals("up")) {
                this.image = new Image("file:/Users/Jeffrey/Desktop/APCS/heroBulletUp.png");
            }
            else if(dir.equals("down")){
                this.image = new Image("file:/Users/Jeffrey/Desktop/APCS/herobulletDown.png");
            }
            else if(dir.equals("right")){
                this.image = new Image("file:/Users/Jeffrey/Desktop/APCS/herobulletRight.png");
            }
            else{
                this.image = new Image("file:/Users/Jeffrey/Desktop/APCS/herobulletLeft.png");
            }
            this.rect = new Rectangle2D(heroX, heroY, image.getWidth(), image.getHeight());
            this.dir = dir;
            bulletXInc = 0;
            bulletYInc = 0;
            startY = 100 + 10*y + sprite.getCharacter().getHeight()/2;
            startX = 100 + 10*x + sprite.getCharacter().getWidth()/2;
        }

        public Image getImage() {
            return image;
        }

        public Rectangle2D getRect() {
            return rect;
        }

        public int getSpeed() {
            return speed;
        }

        public void setRect(Rectangle2D rect) {
            this.rect = rect;
        }

        public String getDir(){
            return dir;
        }

        public double getBulletXInc() {
            return bulletXInc;
        }

        public void setBulletXInc(double bulletXInc) {
            this.bulletXInc = bulletXInc;
        }

        public double getBulletYInc() {
            return bulletYInc;
        }

        public void setBulletYInc(double bulletYInc) {
            this.bulletYInc = bulletYInc;
        }

        public double getStartX() {
            return startX;
        }

        public double getStartY() {
            return startY;
        }
    }
    public class enemyBullet{
        Image image;
        Rectangle2D rect;
        int speed;
        String dir;
        double bulletXInc;
        double bulletYInc;
        double startX;
        double startY;

        public enemyBullet(EnemyNinja ninja, String dir){
            if(dir.equals("up")) {
                this.image = new Image("file:/Users/Jeffrey/Desktop/APCS/enemyBulletUp.png");
            }
            else if(dir.equals("down")){
                this.image = new Image("file:/Users/Jeffrey/Desktop/APCS/enemyBulletDown.png");
            }
            else if(dir.equals("right")){
                this.image = new Image("file:/Users/Jeffrey/Desktop/APCS/enemyBulletRight.png");
            }
            else{
                this.image = new Image("file:/Users/Jeffrey/Desktop/APCS/enemyBulletLeft.png");
            }
            this.rect = new Rectangle2D(ninja.getX() + xinc, ninja.getY(), image.getWidth(), image.getHeight());
            this.dir = dir;
            bulletXInc = 0;
            bulletYInc = 0;
            startY = ninja.getY() + ninja.getImage().getHeight()/2;
            startX = ninja.getX() + ninja.getImage().getWidth()/2;
        }

        public Image getImage() {
            return image;
        }

        public Rectangle2D getRect() {
            return rect;
        }

        public int getSpeed() {
            return speed;
        }

        public void setRect(Rectangle2D rect) {
            this.rect = rect;
        }

        public String getDir(){
            return dir;
        }

        public double getBulletXInc() {
            return bulletXInc;
        }

        public void setBulletXInc(double bulletXInc) {
            this.bulletXInc = bulletXInc;
        }

        public double getBulletYInc() {
            return bulletYInc;
        }

        public void setBulletYInc(double bulletYInc) {
            this.bulletYInc = bulletYInc;
        }

        public double getStartX() {
            return startX;
        }

        public double getStartY() {
            return startY;
        }
    }

    public class AnimateObjects extends AnimationTimer{
        public void handle(long now){
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            if(gameOver == false) {
                if(totEnemies.size() == 0 && lvlNum == 1){
                    lvlNum = 2;
                    createEnemies();
                    x = 0;
                    y = 0;
                    heart.setPresent(true);
                }
                else if(totEnemies.size() == 0 && lvlNum == 2){
                    gc.setStroke(Color.RED);
                    gc.setLineWidth(2);
                    Font font = Font.font( "Arial", FontWeight.NORMAL, 48 );
                    gc.setFont( font );
                    gc.setFill(Color.BLACK);
                    gc.fillText("Congratulations, YOU WON!", 300,500);
                    gc.strokeText("Congratulations, YOU WON!", 300,500);
                    gc.fillText("Press ESC to Play Again", 340,550);
                    gc.strokeText("Press ESC to Play Again", 340,550);
                    gameWon = true;
                }
                heroX = 100 + 10*x;
                heroY = 100 + 10*y;
                gc.drawImage(sprite.getCharacter(), heroX, heroY);
                sprite.setRect(new Rectangle2D(100 + 10 * x, 100 + 10 * y, sprite.getCharacter().getWidth(), sprite.getCharacter().getHeight()));
                if(sprite.getRect().intersects(heart.getRect())){
                    lives+=2;
                    heart = new Heart(0);
                    heart.setPresent(false);
                    heartSound.play();
                }
                else if(heart.isPresent() && lvlNum == 1){
                    gc.drawImage(heart.getImage(),300,300,heart.getImage().getWidth(),heart.getImage().getHeight());
                }
                else if(heart.isPresent() && lvlNum == 2){
                    heart = new Heart();
                    gc.drawImage(heart.getImage(),200,300,heart.getImage().getWidth(),heart.getImage().getHeight());
                }

                    EnemyNinja a;
                if (totEnemies.size() > 0) {
                    a = totEnemies.get(0);
                    if (totEnemies.get(0).getXDir() == true)
                        xinc += 0.25;
                    else
                        xinc -= 0.25;
                } else {
                    a = new EnemyNinja(0, 0, 0, 0);
                }

                if (xinc == a.getXRange()) {
                    for (int i = 0; i < totEnemies.size(); i++) {
                        totEnemies.get(i).setXDir(false);
                    }
                } else if (xinc == a.getXRange() * -1) {
                    for (int i = 0; i < totEnemies.size(); i++) {
                        totEnemies.get(i).setXDir(true);
                    }
                }


                for(int i = 0; i < totEnemies.size(); i++){
                    EnemyNinja ninja = totEnemies.get(i);
                    for(int j = 0; j < checkLevel().getArr().size(); j++){
                        Rectangle2D rect0 = checkLevel().getArr().get(j);
                        Rectangle2D rect1 = new EnemyNinja(ninja.getX(),ninja.getY(),50,0).getRect();
                        //Rectangle2D rect2 = new EnemyNinja(ninja.getX() + 50, ninja.getY(),50,0).getRect();
                        //Rectangle2D rect3 = new EnemyNinja(ninja.getX() - 50,ninja.getY(),50,0).getRect();
                        if(rect0.intersects(rect1))
                            totEnemies.remove(i);
                        else {
                            gc.drawImage(ninja.getImage(), ninja.getX() + xinc, ninja.getY());
                            ninja.setRect(new Rectangle2D(ninja.getX() + xinc, ninja.getY(), ninja.getImage().getWidth(), ninja.getImage().getHeight()));
                        }
                    }

                    }

                for(int i = 0; i < totEnemies.size(); i++) {
                    EnemyNinja ninja = totEnemies.get(i);
                    if ((ninja.getRect()).intersects(sprite.getRect())) {
                        EnemySmash.play();
                        totEnemies.remove(i);
                        lives--;
                    }
                }
                if(shootStart== true){
                    for(int i = 0; i < heroShoot.size(); i++){
                        if(heroShoot.get(i).getDir().equals("up")){
                            heroShoot.get(i).setBulletXInc(0);
                            heroShoot.get(i).setBulletYInc(heroShoot.get(i).getBulletYInc()-3);
                        }
                        else if(heroShoot.get(i).getDir().equals("down")){
                            heroShoot.get(i).setBulletXInc(0);
                            heroShoot.get(i).setBulletYInc(heroShoot.get(i).getBulletYInc()+3);
                        }
                        else if(heroShoot.get(i).getDir().equals("right")){
                            heroShoot.get(i).setBulletXInc(heroShoot.get(i).getBulletXInc() + 3);
                            heroShoot.get(i).setBulletYInc(0);
                        }
                        else if(heroShoot.get(i).getDir().equals("left")){
                            heroShoot.get(i).setBulletXInc(heroShoot.get(i).getBulletXInc() - 3);
                            heroShoot.get(i).setBulletYInc(0);
                        }
                        gc.drawImage(heroShoot.get(i).getImage(), heroShoot.get(i).getStartX() + heroShoot.get(i).getBulletXInc(), heroShoot.get(i).getStartY() + heroShoot.get(i).getBulletYInc());
                        heroShoot.get(i).setRect(new Rectangle2D(heroShoot.get(i).getStartX() + heroShoot.get(i).getBulletXInc(),heroShoot.get(i).getStartY() + heroShoot.get(i).getBulletYInc(), heroShoot.get(i).getImage().getWidth(),heroShoot.get(i).getImage().getHeight()));
                    }
                }

                for(int i = 0; i < totEnemies.size(); i++){
                    for(int j = 0; j < heroShoot.size(); j++){
                        if((totEnemies.size() > i) && (heroShoot.size()> j)) {
                            if(totEnemies.get(i).getRect().intersects(heroShoot.get(j).getRect())){
                               totEnemies.remove(i);
                               heroShoot.remove(j);
                               score += 1;
                           }
                        }
                    }
                }

                int numDir = (int)(Math.random()*4) + 1;
                int numNinja = (int)(Math.random()*200);
                String dir = "";
                if(numDir == 1)
                    dir = "up";
                else if(numDir == 2)
                    dir = "down";
                else if(numDir == 3)
                    dir = "left";
                else
                    dir = "right";
                if(numNinja < totEnemies.size()) {
                    enemyShoot.add(new enemyBullet(totEnemies.get(numNinja), dir));
                }
                    for (int i = 0; i < enemyShoot.size(); i++) {
                        if (enemyShoot.get(i).getDir().equals("up")) {
                            enemyShoot.get(i).setBulletXInc(0);
                            enemyShoot.get(i).setBulletYInc(enemyShoot.get(i).getBulletYInc() - 3);
                        } else if (enemyShoot.get(i).getDir().equals("down")) {
                            enemyShoot.get(i).setBulletXInc(0);
                            enemyShoot.get(i).setBulletYInc(enemyShoot.get(i).getBulletYInc() + 3);
                        } else if (enemyShoot.get(i).getDir().equals("right")) {
                            enemyShoot.get(i).setBulletXInc(enemyShoot.get(i).getBulletXInc() + 3);
                            enemyShoot.get(i).setBulletYInc(0);
                        } else if (enemyShoot.get(i).getDir().equals("left")) {
                            enemyShoot.get(i).setBulletXInc(enemyShoot.get(i).getBulletXInc() - 3);
                            enemyShoot.get(i).setBulletYInc(0);
                        }
                        gc.drawImage(enemyShoot.get(i).getImage(), enemyShoot.get(i).getStartX() + enemyShoot.get(i).getBulletXInc(), enemyShoot.get(i).getStartY() + enemyShoot.get(i).getBulletYInc());
                        enemyShoot.get(i).setRect(new Rectangle2D(enemyShoot.get(i).getStartX() + enemyShoot.get(i).getBulletXInc(), enemyShoot.get(i).getStartY() + enemyShoot.get(i).getBulletYInc(), enemyShoot.get(i).getImage().getWidth(), enemyShoot.get(i).getImage().getHeight()));
                        if(enemyShoot.get(i).getRect().intersects(sprite.getRect())){
                            enemyShoot.remove(i);
                            lives--;
                            EnemySmash.play();
                        }
                    }
                int countInterUp = 0;
                int countInterDown = 0;
                int countInterRight = 0;
                int countInterLeft = 0;
                for(int i = 0; i < checkLevel().getArr().size(); i++){
                    for(int b = 0; b < enemyShoot.size(); b++){
                        if(enemyShoot.get(b).getRect().intersects(checkLevel().getArr().get(i))){
                            enemyShoot.remove(b);
                        }
                    }
                    for(int c = 0; c < heroShoot.size(); c++){
                        if(heroShoot.get(c).getRect().intersects(checkLevel().getArr().get(i))){
                            heroShoot.remove(c);
                        }
                    }
                    if(checkLevel().getArr().get(i).intersects(new Rectangle2D(100 + 10*x, 90 + 10*y, sprite.getCharacter().getWidth(),sprite.getCharacter().getHeight()))){
                        countInterUp++;
                    }
                    else if(checkLevel().getArr().get(i).intersects(new Rectangle2D(100 + 10*x, 110 + 10*y, sprite.getCharacter().getWidth(),sprite.getCharacter().getHeight()))){
                        countInterDown++;
                    }
                    else if(checkLevel().getArr().get(i).intersects(new Rectangle2D(110 + 10*x, 100 + 10*y, sprite.getCharacter().getWidth(),sprite.getCharacter().getHeight()))){
                        countInterRight++;
                    }
                    else if(checkLevel().getArr().get(i).intersects(new Rectangle2D(90 + 10*x, 100 + 10*y, sprite.getCharacter().getWidth(),sprite.getCharacter().getHeight()))){
                        countInterLeft++;
                    }
                }
                    canMoveUp = (countInterUp == 0);
                    canMoveDown = (countInterDown == 0);
                    canMoveLeft = (countInterLeft == 0);
                    canMoveRight = (countInterRight == 0);


            }
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            Font font = Font.font( "Arial", FontWeight.NORMAL, 48 );
            gc.setFont( font );
            gc.setFill(Color.BLACK);
            gc.fillText("Lives: " + lives, 100,50);
            gc.strokeText("Lives: " + lives, 100,50);
            gc.setFill(Color.BLUE);
            gc.fillText("Enemies Left: " + totEnemies.size(), 400 ,50);
            gc.strokeText("Enemies Left: " + totEnemies.size(), 400 ,50);
            gc.setStroke(Color.GREEN);
            gc.fillText("Score: " + score + "00", 900, 50);
            gc.strokeText("Score: " + score + "00", 900, 50);

            if(lives <= 0){
                //gc.setFill(Color.PURPLE);
                gameOver = true;
                gc.setStroke(Color.WHITE);
                gc.setLineWidth(2);
                font = Font.font("COMIC SANS", FontWeight.NORMAL, 72);
                gc.setFont(font);
                if (lvlNum == 1) {
                    gc.fillText("GAME OVER", 360, 392);
                    gc.strokeText("GAME OVER", 360, 392);
                }
                gc.fillText("Press ESC to RESTART",200, 502);
                gc.strokeText("Press ESC to RESTART", 200, 502);
            }

            if(lvlNum == 1) {
                gc.setFill(Color.BLUE);
            }
            else {
                gc.setFill(Color.PURPLE);
            }
            for(int i = 0; i < checkLevel().getArr().size(); i++){
                Rectangle2D r = checkLevel().getArr().get(i);
                gc.fillRect(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
                for(int j = 0; j < totEnemies.size();j++){
                    if(r.intersects(totEnemies.get(j).getRect()))
                        totEnemies.get(j).setXDir(!totEnemies.get(j).getXDir());
                    }
                }
            }
        }

    public  void createEnemies(){
        totEnemies = new ArrayList<EnemyNinja>();
       // int num = (int)(Math.random()*6) + 9;
        for(int i = 0; i < 10; i++){
           // boolean intersect = true;
            //int enemyX = 0;
            //int enemyY = 0;
            int enemyX = (int)(Math.random()*800) + 50;
            int enemyY = (int)(Math.random()*750);
            totEnemies.add(new EnemyNinja(enemyX,enemyY,50,0));
        }
    }
    public Level checkLevel(){
        if(lvlNum == 1)
            return lvlOne;
        else
            return lvlTwo;
    }
}
