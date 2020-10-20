package com.nvathu.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import javax.xml.soap.Text;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;


	Texture[] bird;
	int birdState=0;
	int pause=0;
	float gravity=0.2f;
	float velocity= 0;
	int birdY=0;
	Rectangle birdRectangle;

	BitmapFont font;
	Texture dizzy;

	int score=0;
	int gameState=0;

	Random random;



	ArrayList<Integer> coinXs=new ArrayList<Integer>();
	ArrayList<Integer> coinYs=new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles= new ArrayList<Rectangle>();
	Texture coin;
	int coinCount;

	ArrayList<Integer> bombXs=new ArrayList<Integer>();
	ArrayList<Integer> bombYs=new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangles= new ArrayList<Rectangle>();
	Texture bomb;
	int bombCount;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");


		bird=new Texture[4];
		bird[0]=new Texture("frame-1.png");
		bird[1]=new Texture("frame-2.png");
		bird[2]=new Texture("frame-3.png");
		bird[3]=new Texture("frame-4.png");



		birdY=Gdx.graphics.getHeight()/2;
		coin=new Texture("coin.png");
		random=new Random();
		bomb=new Texture("bomb.png");

		dizzy= new Texture("dizzy-1.png");

		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

	}
	public void makeCoin(){
		float height= random.nextFloat()* Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void  makeBomb(){
		float height= random.nextFloat()* Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render (float deltaTime) {
		batch.begin();






		if(gameState==1){
			//game continues
			//COIN
			if(coinCount<100){
				coinCount++;
			}else{
				coinCount=0;
				makeCoin();
			}
			coinRectangles.clear();
			for(int i=0; i<coinXs.size();i++){
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinXs.set(i,coinXs.get(i)-4);
				coinRectangles.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}
			//BOMB
			if(bombCount<250){
				bombCount++;
			}else{
				bombCount=0;
				makeBomb();
			}
			bombRectangles.clear();
			for(int i=0; i<bombXs.size();i++){
				batch.draw(bomb,bombXs.get(i),bombYs.get(i));
				bombXs.set(i,bombXs.get(i)-8);
				bombRectangles.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
			}

			if(Gdx.input.justTouched()){
				velocity=-10;
			}

			if(pause < 8){
				pause ++;
			}else{
				pause=0;
				if(birdState < 3){
					birdState++;
				}else{
					birdState=0;
				}
			}

			velocity += gravity;
			birdY -= velocity;

			if(birdY<=0){
				birdY=0;
			}

		}else if (gameState==0){
			//BEGINNING
			if(Gdx.input.justTouched()){
				gameState=1;
			}
		}else if(gameState==2){
			//END GAME
			if(Gdx.input.justTouched()){
				gameState=1;
				birdY=Gdx.graphics.getHeight()/2;
				score=0;
				velocity=0;
				coinXs.clear();
				coinYs.clear();
				coinRectangles.clear();
				coinCount=0;
				bombXs.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombCount=0;

			}
		}
		if(gameState==2) {
			batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - bird[birdState].getWidth() / 2, birdY);
		}else{
			batch.draw(bird[birdState], Gdx.graphics.getWidth() / 2 - bird[birdState].getWidth() / 2, birdY);
		}
		birdRectangle= new Rectangle(Gdx.graphics.getWidth()/2-bird[birdState].getWidth()/2,birdY,bird[birdState].getWidth(),bird[birdState].getHeight());

		for(int i=0;i<coinRectangles.size();i++){
			if(Intersector.overlaps(birdRectangle,coinRectangles.get(i))){
				score++;

				coinRectangles.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		for(int i=0;i<bombRectangles.size();i++){
			if(Intersector.overlaps(birdRectangle,bombRectangles.get(i))){
				Gdx.app.log("Bomb!","Collision!");
				gameState=2;
			}
		}

		font.draw(batch,String.valueOf(score),100,200);

		batch.end();
	}


	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
