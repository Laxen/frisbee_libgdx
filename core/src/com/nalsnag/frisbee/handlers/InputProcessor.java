package com.nalsnag.frisbee.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter {
    private int touchStartX;

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Input.dx = screenX - touchStartX;
        Input.isDragging = true;
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchStartX = screenX;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Input.isDragging = false;
        return true;
    }
}
