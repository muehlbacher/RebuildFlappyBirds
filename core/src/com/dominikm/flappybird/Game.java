package com.dominikm.flappybird;


public class Game {

    public float velocity;
    public float gravity;
    public boolean death;
    public boolean deathAnimation;
    public float birdY;
    public int score;
    public int scoringTube;
    public int gameState;


    public Game(){
        gravity = 1.5f;
        death = false;
        score = 0;
        scoringTube = 0;
        gameState = 1;
        deathAnimation = true;
    }

    public void death(Bird bird) {
        if(deathAnimation == true) {
            bird.y = bird.y + 30;
            deathAnimation = false;
        }else{
            fall(bird);

            if(bird.y < 0) {
                death = true;
            }
        }
    }

    public void fall(Bird bird){
        velocity = velocity + gravity;
        bird.y -= velocity;
    }

}
