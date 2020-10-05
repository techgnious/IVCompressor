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
 * Instances of this class report informations about images/videos size.
 * 
 * 240p = 426 x 240
 * 
 * 360p = 640 x 360
 * 
 * 480p = 640 x 480
 * 
 * 720p = 1280 x 720 - is usually known as HD or "HD Ready" resolution.
 * 
 * 1080p= 1920 x 1080 - is usually known as FHD or "Full HD" resolution
 * 
 * 1440p = 2560 x 1440 - is commonly known as QHD or Quad HD resolution
 * 
 * @author srikanth.anreddy
 *
 */
public class IVSize {

	/**
	 * The video width.
	 */
	private int width;

	/**
	 * The video height.
	 */

	private int height;

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

}
