/**
 * Copyright 2011 Massimo Gaddini
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  
 */
package com.sgxmobileapps.droidmaze.maze;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sgxmobileapps.droidmaze.maze.generator.MazeGenerator;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

/**
 * @author Massimo Gaddini
 *
 */
public class Maze {

    private static final int WALL_WIDTH     = 2;

    private MazeGenerator    mMazeGenerator = null;
    private int              mWidth         = 0;
    private int              mHeight        = 0;
    private float            mStepX;
    private float            mStepY;
    private int              mPx;
    private int              mPy;
    private MazeCell[][]     mMazeGrid;

    public void setMazeSize(int height, int width) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public void setMazeGenerator(MazeGenerator generator) {
        mMazeGenerator = generator;
    }

    public void generateMaze() {
        mMazeGrid = mMazeGenerator.generate(mHeight, mWidth);
    }

    public float getMaxMarkerDim() {
        return ( mStepX > mStepY ) ? mStepY : mStepX;
    }

    public float getMarkerStartPositionX() {
        return mPx + WALL_WIDTH;
    }

    public float getMarkerStartPositionY() {
        return mPy + WALL_WIDTH;
    }

    public void addMarker(TextureRegion markerTexture, IEntity layer, PhysicsWorld physicsWorld) {
        float scaleXY = getMaxMarkerDim() * 0.8f / markerTexture.getWidth();

        if (scaleXY > 1) {
            scaleXY = 1;
        }

        Sprite markerSprite = new Sprite(0, 0, markerTexture.getWidth() * scaleXY, markerTexture
                .getHeight()
                * scaleXY, markerTexture);

        // pieceSprite.setScaleCenter(0, 0);
        // pieceSprite.setScale(scaleXY, scaleXY);
        markerSprite.setPosition(getMarkerStartPositionX(), getMarkerStartPositionY());

        FixtureDef markerFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0);
        Body body = PhysicsFactory.createCircleBody(physicsWorld, markerSprite,
                BodyType.DynamicBody, markerFixtureDef);

        layer.attachChild(markerSprite);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(markerSprite, body, true, true));
    }

    public void drawMaze(int x, int y, int height, int width, IEntity layer,
            PhysicsWorld physicsWorld) {
        mPx = x;
        mPy = y;
        mStepX = ( width - ( mWidth * WALL_WIDTH ) - WALL_WIDTH ) / (float) mWidth;
        mStepY = ( height - ( mHeight * WALL_WIDTH ) - WALL_WIDTH ) / (float) mHeight;

        FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);

        /* box */
        Shape ground = new Rectangle(x, y + ( mStepY + WALL_WIDTH ) * mHeight, WALL_WIDTH
                + ( mStepX + WALL_WIDTH ) * mWidth, WALL_WIDTH);
        Shape roof = new Rectangle(x, y, WALL_WIDTH + ( mStepX + WALL_WIDTH ) * mWidth, WALL_WIDTH);
        Shape left = new Rectangle(x, y + WALL_WIDTH, WALL_WIDTH, ( mStepY + WALL_WIDTH ) * mHeight
                - WALL_WIDTH);
        Shape right = new Rectangle( ( mStepX + WALL_WIDTH ) * mWidth, y + WALL_WIDTH, WALL_WIDTH,
                ( mStepY + WALL_WIDTH ) * mHeight - WALL_WIDTH);

        layer.attachChild(ground);
        layer.attachChild(roof);
        layer.attachChild(left);
        layer.attachChild(right);

        PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(physicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(physicsWorld, left, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(physicsWorld, right, BodyType.StaticBody, wallFixtureDef);

        int start = -1;
        int end = -1;
        for (int i = 0; i < ( mHeight - 1 ); i++) {

            start = end = -1;
            for (int j = 0; j < mWidth; j++) {
                if (!mMazeGrid[i][j].isSouthWallClosed()) {
                    if (start >= 0) {
                        end = j;
                        // TODO draw line from start to end
                        Shape wallH = new Rectangle(x + ( ( start == 0 ) ? WALL_WIDTH : 0 ) + start
                                * ( mStepX + WALL_WIDTH ), y + ( i + 1 ) * ( mStepY + WALL_WIDTH ),
                                ( end - start ) * ( mStepX + WALL_WIDTH )
                                        + ( ( start == 0 ) ? 0 : WALL_WIDTH ), WALL_WIDTH);
                        PhysicsFactory.createBoxBody(physicsWorld, wallH, BodyType.StaticBody,
                                wallFixtureDef);
                        layer.attachChild(wallH);

                        start = end = -1;
                    }
                } else {
                    if (start < 0) {
                        start = j;
                    }
                }
            }

            if (start >= 0) {
                end = mWidth;
                Shape wallH = new Rectangle(x + ( ( start == 0 ) ? WALL_WIDTH : 0 ) + start
                        * ( mStepX + WALL_WIDTH ), y + ( i + 1 ) * ( mStepY + WALL_WIDTH ),
                        ( end - start ) * ( mStepX + WALL_WIDTH )
                                + ( ( start == 0 ) ? 0 : WALL_WIDTH ), WALL_WIDTH);
                PhysicsFactory.createBoxBody(physicsWorld, wallH, BodyType.StaticBody,
                        wallFixtureDef);
                layer.attachChild(wallH);
            }
        }

        for (int j = 0; j < ( mWidth - 1 ); j++) {

            start = end = -1;

            for (int i = 0; i < mHeight; i++) {
                if (!mMazeGrid[i][j].isEastWallClosed()) {
                    if (start >= 0) {
                        end = i;
                        // TODO draw line from start to end
                        Shape wallV = new Rectangle(x + ( j + 1 ) * ( mStepX + WALL_WIDTH ), y
                                + WALL_WIDTH + start * ( mStepY + WALL_WIDTH ), WALL_WIDTH,
                                ( end - start ) * ( mStepY + WALL_WIDTH ));
                        PhysicsFactory.createBoxBody(physicsWorld, wallV, BodyType.StaticBody,
                                wallFixtureDef);
                        layer.attachChild(wallV);

                        start = end = -1;
                    }
                } else {
                    if (start < 0) {
                        start = i;
                    }
                }
            }

            if (start >= 0) {
                end = mHeight;
                Shape wallV = new Rectangle(x + ( j + 1 ) * ( mStepX + WALL_WIDTH ), y + WALL_WIDTH
                        + start * ( mStepY + WALL_WIDTH ), WALL_WIDTH, ( end - start )
                        * ( mStepY + WALL_WIDTH ));
                PhysicsFactory.createBoxBody(physicsWorld, wallV, BodyType.StaticBody,
                        wallFixtureDef);
                layer.attachChild(wallV);
            }
        }
    }
}