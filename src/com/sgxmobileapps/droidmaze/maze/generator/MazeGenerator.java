package com.sgxmobileapps.droidmaze.maze.generator;

import com.sgxmobileapps.droidmaze.maze.MazeCell;

public interface MazeGenerator {
	MazeCell[][] generate(int height, int width);
}