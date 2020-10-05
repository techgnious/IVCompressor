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
 * @author srikanth.anreddy
 *
 */
public class IVAudioAttributes {

	/**
	 * The bitrate value for the encoding process. If null or not specified a
	 * default value will be picked.
	 */
	private Integer bitRate = null;

	/**
	 * The samplingRate value for the encoding process. If null or not specified a
	 * default value will be picked.
	 */
	private Integer samplingRate = null;

	/**
	 * The channels value (1=mono, 2=stereo) for the encoding process. If null or
	 * not specified a default value will be picked.
	 */
	private Integer channels = null;

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
	 * @return the samplingRate
	 */
	public Integer getSamplingRate() {
		return samplingRate;
	}

	/**
	 * @param samplingRate the samplingRate to set
	 */
	public void setSamplingRate(Integer samplingRate) {
		this.samplingRate = samplingRate;
	}

	/**
	 * @return the channels
	 */
	public Integer getChannels() {
		return channels;
	}

	/**
	 * @param channels the channels to set
	 */
	public void setChannels(Integer channels) {
		this.channels = channels;
	}

}
