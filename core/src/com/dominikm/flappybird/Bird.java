package com.dominikm.flappybird;

import com.badlogic.gdx.graphics.Texture;

public class Bird {
    Texture[] birds;
    public float y;
    public int flapState;

    public Bird(){

    }
    public Bird(String[] textures){
        int i= 0 ;
        birds = new Texture[textures.length];
        for(String texture : textures){
            birds[i] = new Texture(texture);
            i++;
        }
        flapState = 0;
    }

    public void flapState(){
        if(this.flapState == 0){
            this.flapState = 1;
        }else {
            this.flapState = 0;
        }
    }
}
