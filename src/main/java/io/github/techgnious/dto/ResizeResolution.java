/*
 * Copyright 2020 Srikanth Reddy Anreddy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.techgnious.dto;

/**
 * Enum Class that defines the allowed Image resolutions for resizing
 * 
 * @author srikanth.anreddy
 *
 */
public enum ResizeResolution {

	R240P(426, 240), R360P(480, 360), R480P(640, 480), R720P(1280, 720), R1080P(1920, 1080), R1440P(2560, 1440),
	IMAGE_DEFAULT(480, 360), VIDEO_DEFAULT(400, 300), THUMBNAIL(189, 100), SMALL_THUMBNAIL(100, 100);

	private int width;
	private int height;

	private ResizeResolution(int w, int h) {
		this.width = w;
		this.height = h;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
