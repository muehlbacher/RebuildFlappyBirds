package com.dominikm.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	Game game;
	Bird bird;
	SpriteBatch batch;
	Texture background;
	Texture gameover;
	//ShapeRenderer shapeRenderer;
	BitmapFont font;

	//Texture[] birds;


	//int flapState = 0;
	//float birdY =  0;
	//float velocity = 0;
	//float gravity = 1.5f;
	Circle birdCircle;
	Rectangle[] tubeTopRectangle;
	Rectangle[] tubeBottomRectangle;

	//int gameState = 0;

	Texture bottomTube;
	Texture topTube;
	float gap = 400;
	float maxTubeOffset;
	Random randomgenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;


	String[] birdpng = new String[2];

	//int score = 0;
	//int scoringTube = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover = new Texture("gameover.png");
		game = new Game();


		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);


		tubeTopRectangle = new Rectangle[numberOfTubes];
		tubeBottomRectangle = new Rectangle[numberOfTubes];

		bottomTube = new Texture("bottomtube.png");
		topTube = new Texture("toptube.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomgenerator = new Random();

		distanceBetweenTubes  = Gdx.graphics.getWidth()  * 3/ 4 ;


		birdpng[0] = "bird.png";
		birdpng[1] = "bird2.png";

		bird = new Bird(birdpng);

		//birds =  new Texture[2];
		//birds[0] = new Texture("bird.png");
		//birds[1] = new Texture("bird2.png");

		startGame();

	}

	public void startGame () {
		game = new Game();
		bird.y = Gdx.graphics.getHeight() / 2 - bird.birds[0].getHeight() / 2;

		for(int i = 0; i < numberOfTubes; i++){

			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			tubeOffset[i] = (randomgenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeBottomRectangle[i] = new Rectangle();
			tubeTopRectangle[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
        batch.begin();
        //draw background
        batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (game.gameState == 1) {
			//Gamestart

			if( tubeX[game.scoringTube] < Gdx.graphics.getWidth() / 2 ) {
				game.score++;
				Gdx.app.log("Score", String.valueOf(game.score));
				if (game.scoringTube < numberOfTubes - 1){
					game.scoringTube++;
				}else{
					game.scoringTube = 0;
				}
			}

			if (Gdx.input.justTouched()) {
				//Fly up
				game.velocity = -20;
			}

			for(int i = 0; i < numberOfTubes; i++) {
				//Loop for setting new tubes

				if(tubeX[i] < - topTube.getWidth()){
					//set Tubes if out of bounds
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomgenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				}else {
					//tubes moving left
					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				//tubeX[i] = tubeX[i] - tubeVelocity;

				//draw tubes
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);

				tubeBottomRectangle[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
				tubeTopRectangle[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			}
			if(bird.y > 0 ) {
				game.fall(bird);
			} else {
				game.gameState = 2;
			}
		}	else if(game.gameState == 0){
			//Game Paused
			if (Gdx.input.justTouched()) {

				Gdx.app.log("Touched", "Yep!");

				game.gameState = 1;
			}
		} else if(game.gameState == 2) {
			//Game Over

			game.death(bird);

			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);

			if(game.death == true){
				if (Gdx.input.justTouched()) {

					Gdx.app.log("Touched", "Yep!");
					startGame();
				}
			}
		}

		bird.flapState();

        batch.draw(bird.birds[bird.flapState], Gdx.graphics.getWidth() / 2 - bird.birds[bird.flapState].getWidth() / 2 , bird.y);

		font.draw(batch, String.valueOf(game.score), 100, 200);

        batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2, bird.y + bird.birds[bird.flapState].getHeight() / 2, bird.birds[bird.flapState].getWidth() / 2);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for(int i = 0; i < numberOfTubes; i++) {

			//shapeRenderer.rect(tubeBottomRectangle[i].x, tubeBottomRectangle[i].y, tubeBottomRectangle[i].width, tubeBottomRectangle[i].height);
			//shapeRenderer.rect(tubeTopRectangle[i].x, tubeTopRectangle[i].y, tubeTopRectangle[i].width, tubeTopRectangle[i].height);

			if(Intersector.overlaps(birdCircle, tubeBottomRectangle[i]) || Intersector.overlaps(birdCircle, tubeTopRectangle[i]))
			{
				Gdx.app.log("Collision", "Boom!");
				game.gameState = 2;
			}
		}
			//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
