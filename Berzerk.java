package.sample;
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

public class Berzerk extends Application implements EventHandler<InputEvent>{
	GraphicsContext gc;
	Sprite sprite;
	Image character;
	AnimateObjects animate;
	int x = 0;
	int y = 0;
	AudioClip move;
	Canvas canvas;
	AudioClip background;
	AudioClip shoot;
	ArrayList<EnemyNinja> totEnemies;
	double xinc = 0;
	boolean xdir = true;
	boolean gameOver;
	int lives;
	public static void main(String[]args){
		launch();
	}


	public void start(Stage stage){
		totEnemies= new ArrayList<EnemyNinja>();
		stage.setTitle("Berzerk");
		Group root = new Group();
		canvas = new Canvas(1200, 900);
		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		sprite = new Sprite(new Image("file:/Users/Jeffrey/Desktop/APCS/sprite.png"));
		character = sprite.getCharacter();
		for(int i = 0; i < 10; i++){
			int enemyX = (int)(Math.random()*canvas.getWidth());
			int enemyY = (int)(Math.random()*canvas.getHeight());
			totEnemies.add(new EnemyNinja(enemyX,enemyY,15,5));
		}
		lives = 3;
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
			if(left && up){
				x-=1;
				y+=1;
				//setRotate(90);
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
			else if(left){
				x-=1;
			}
			else if(right){
				x+=1;
			}
			else if(down){
				y+=1;
			}
			else if(up){
				y-=1;
			}
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		}
		if(event instanceof MouseEvent){
			System.out.println( ((MouseEvent)event).getX() );
			System.out.println( ((MouseEvent)event).getY() );
		}
	}

	public class EnemyNinja{

		double x;
		double y;
		int xRange;
		int yRange;
		Image enemy;
		Rectangle2D rect;

		public EnemyNinja(double x, double y, int xRange, int yRange){
			this.x = x;
			this.y = y;
			this.xRange = xRange;
			this.yRange = yRange;
			enemy = new Image("file:/Users/Jeffrey/Desktop/APCS/enemy.png");
		}

		public double getX(){
			return x;
		}
		public double getY(){
			return y;
		}
		public int getXRange(){ return xRange;}
		public Image getImage(){
			return enemy;
		}
		public Rectangle2D getRect(){ return rect;}
		public void setRect(Rectangle2D recta){
			rect = recta;
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
		public void setRect(Rectangle2D recta){rect = recta; }
	}

	public class AnimateObjects extends AnimationTimer{
		public void handle(long now){
			if(gameOver == false){
				gc.drawImage(sprite.getCharacter(),100 +10*x,100 + 10*y);
				sprite.setRect(new Rectangle2D(100+10*x, 100+10*y, sprite.getCharacter().getWidth(), sprite.getCharacter().getHeight()));
				EnemyNinja a;
				if(totEnemies.size() > 0) {
					a = totEnemies.get(0);
				}
				else {
					a = new EnemyNinja(0, 0, 0, 0);
				}
				if(xinc == a.getXRange()) {
					xdir = false;
				}
				else if(xinc == a.getXRange()*-1){
					xdir = true;
				}

				if(xdir == true)
					xinc+=0.25;
				else
					xinc-=0.25;
				for(int i = 0; i < totEnemies.size(); i++){
					EnemyNinja ninja = totEnemies.get(i);
					gc.drawImage(ninja.getImage(),ninja.getX() + xinc,ninja.getY() + xinc);
					ninja.setRect(new Rectangle2D(ninja.getX() + xinc, ninja.getY()+ xinc, ninja.getImage().getWidth(), ninja.getImage().getHeight()));
				}

				for(int i = 0; i < totEnemies.size(); i++) {
					EnemyNinja ninja = totEnemies.get(i);
					if ((ninja.getRect()).intersects(sprite.getRect())) {
						System.out.println("HIT : " + i);
						totEnemies.remove(i);
						lives--;
					}
				}
			}
			gc.setStroke(Color.RED);
			gc.setLineWidth(2);
			Font font = Font.font( "Arial", FontWeight.NORMAL, 48 );
			gc.setFont( font );
			gc.fillText("Lives: " + lives, 100,50);
			gc.strokeText("Lives: " + lives, 100,50);

			if(lives == 0){
				//gc.setFill(Color.PURPLE);
				gameOver = true;
				gc.setStroke(Color.WHITE);
				gc.setLineWidth(2);
				font = Font.font("Arial", FontWeight.NORMAL, 72);
				gc.setFont(font);
				gc.fillText("GAME OVER", 360, 392);
				gc.strokeText("GAME OVER", 597 ,392);

			}
		}
	}
}


