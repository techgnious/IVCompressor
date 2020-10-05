/**
 * 
 */
package io.github.techgnious.dto;

/**
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

}
