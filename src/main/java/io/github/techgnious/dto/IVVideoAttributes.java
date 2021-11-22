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

import java.util.ArrayList;

import ws.schild.jave.encode.enums.X264_PROFILE;
import ws.schild.jave.filters.VideoFilter;

/**
 * Class to define video encoding attributes for enhancing the video compression
 * 
 * @author srikanth.anreddy
 *
 */
public class IVVideoAttributes {

	/**
	 * The bitrate value for the encoding process. If null or not specified a
	 * default value will be picked.
	 */
	private Integer bitRate = null;
	/**
	 * The frame rate value for the encoding process. If null or not specified a
	 * default value will be picked.
	 */
	private Integer frameRate = null;
	/**
	 * The video size for the encoding process. If null or not specified the source
	 * video size will not be modified.
	 */
	private IVSize size = null;
	
	
	private X264_PROFILE x264Profile = null;
	
	private PixelFormats pixelFormat = null;
	
    private final ArrayList<VideoFilter> videoFilters = new ArrayList<>();


    /**
	 * @return the bitRate
	 */
	public Integer getBitRate() {
		return bitRate;
	}

	/**
	 * @param bitRate the bitRate to set
	 */
	public void setBitRate(Integer bitRate) {
		this.bitRate = bitRate;
	}

	/**
	 * @return the frameRate
	 */
	public Integer getFrameRate() {
		return frameRate;
	}

	/**
	 * @param frameRate the frameRate to set
	 */
	public void setFrameRate(Integer frameRate) {
		this.frameRate = frameRate;
	}

	/**
	 * @return the size
	 */
	public IVSize getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(IVSize size) {
		this.size = size;
	}

    public X264_PROFILE getX264Profile() {
        return x264Profile;
    }

    public void setX264Profile(X264_PROFILE x264Profile) {
        this.x264Profile = x264Profile;
    }


    public PixelFormats getPixelFormat() {
        return pixelFormat;
    }

    public void setPixelFormat(PixelFormats pixelFormat) {
        this.pixelFormat = pixelFormat;
    }
    
    public ArrayList<VideoFilter> getVideoFilters() {
        return videoFilters;
    }
    
    public void addVideoFilter(VideoFilter vf) {
        videoFilters.add(vf);
    }
}
