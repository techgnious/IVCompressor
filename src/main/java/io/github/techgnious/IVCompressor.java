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
package io.github.techgnious;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import io.github.techgnious.constants.IVConstants;
import io.github.techgnious.dto.IVAudioAttributes;
import io.github.techgnious.dto.IVVideoAttributes;
import io.github.techgnious.dto.ImageFormats;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.ImageException;
import io.github.techgnious.exception.VideoException;
import io.github.techgnious.utils.IVFileUtils;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.VideoAttributes;
import ws.schild.jave.VideoAttributes.X264_PROFILE;
import ws.schild.jave.VideoSize;

/**
 * Base Class to handle compression or conversion of Image/Video Files
 * 
 * Operations supported:
 * 
 * Resize Image Convert Image Resize Video Resize and Convert Video
 * 
 * @author srikanth.anreddy
 *
 */
public class IVCompressor {

	/**
	 * Encoder that is used to reduce the quality of video
	 */
	private Encoder encoder;

	/**
	 * Encoder attributes for handling the quality of audio
	 */
	private AudioAttributes audioAttributes;

	/**
	 * Encoder attributes for handling the quality of video
	 */
	private VideoAttributes videoAttributes;

	/**
	 * Attributes controlling the encoding process.
	 */
	private EncodingAttributes encodingAttributes;

	/**
	 * Instance invokes with default encode settings and attributes
	 */
	public IVCompressor() {
		super();
		encoder = new Encoder();
		videoAttributes = new VideoAttributes();
		videoAttributes.setCodec(IVConstants.VIDEO_CODEC);
		videoAttributes.setX264Profile(X264_PROFILE.BASELINE);
		// Here 160 kbps video is 160000
		videoAttributes.setBitRate(160000);
		// More the frames more quality and size, but keep it low based on devices like
		// mobile
		videoAttributes.setFrameRate(15);
		videoAttributes.setSize(new VideoSize(400, 300));
		audioAttributes = new AudioAttributes();
		audioAttributes.setCodec(IVConstants.AUDIO_CODEC);
		// here 64kbit/s is 64000
		audioAttributes.setBitRate(64000);
		audioAttributes.setChannels(2);
		audioAttributes.setSamplingRate(44100);
		encodingAttributes = new EncodingAttributes();
		encodingAttributes.setVideoAttributes(videoAttributes);
		encodingAttributes.setAudioAttributes(audioAttributes);
	}

	/**
	 * This method attempts to resize the image byte stream to lower resolution
	 * 
	 * Returns resized byte stream
	 * 
	 * @param data
	 * @param fileFormat
	 * @return
	 * @throws ImageException
	 */
	public byte[] resizeImage(byte[] data, ImageFormats fileFormat) throws ImageException {
		int width = 480;
		int height = 360;
		String contentType = fileFormat.toString();
		try {
			BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(data));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizedImage = new BufferedImage(width, height, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, width, height, null);
			g.dispose();
			ImageIO.write(resizedImage, contentType, outputStream);
			return outputStream.toByteArray();
		} catch (Exception e) {
			throw new ImageException("ByteStream doesn't contain valid Image");
		}
	}

	/**
	 * This method attempts to resize the image file to lower resolution and saves
	 * it back in the path provided.
	 * 
	 * Returns path location of the object
	 * 
	 * @param file
	 * @param fileFormat
	 * @param path
	 * @return
	 * @throws ImageException
	 */
	public String resizeAndSaveImageToAPath(File file, ImageFormats fileFormat, String path) throws ImageException {
		try {
			byte[] data = resizeImage(IVFileUtils.copyToByteArray(file), fileFormat);
			Path filepath = Files.write(Paths.get(path), data);
			return "File is saved in path::" + filepath.toAbsolutePath();
		} catch (IOException e) {
			throw new ImageException(e);
		}
	}

	/**
	 * This method attempts to resize the image file to lower resolution
	 * 
	 * Returns resized image in byte aaray
	 * 
	 * @param file
	 * @param fileFormat
	 * @param path
	 * @return
	 * @throws ImageException
	 * @throws IOException
	 */
	public byte[] resizeImageUsingFile(File file, ImageFormats fileFormat, String path)
			throws ImageException, IOException {
		return resizeImage(IVFileUtils.copyToByteArray(file), fileFormat);

	}

	/**
	 * This method attempts to resize the image received as Inputstream object to
	 * lower resolution
	 * 
	 * Returns resized stream object
	 * 
	 * @param stream
	 * @param fileFormat
	 * @return
	 * @throws ImageException
	 */
	public InputStream resizeImage(InputStream stream, ImageFormats fileFormat) throws ImageException {
		InputStream targetStream = null;
		try {
			byte[] data = resizeImage(IVFileUtils.copyToByteArray(stream), fileFormat);
			targetStream = new ByteArrayInputStream(data);
			return targetStream;
		} catch (IOException e) {
			throw new ImageException(e);
		}
	}

	/**
	 * This method helps in converting the video content to reduced size with lower
	 * resolution
	 * 
	 * Encodes the video with default attributes thereby reducing the size of the
	 * video with better quality
	 * 
	 * Maintains the best compressed quality
	 * 
	 * @param data       indicates the video content to be compressed
	 * @param fileFormat to indicate the video type
	 * @return byte stream object with compressed video data
	 * @throws VideoException throws exception when the data is incompatible for the
	 *                        enconding
	 */
	public byte[] reduceVideoSize(byte[] data, VideoFormats fileFormat) throws VideoException {
		String fileType = fileFormat.toString();
		encodingAttributes.setFormat(fileType);
		return encodeVideo(data, fileType);
	}

	/**
	 * This method helps in converting the video content to reduced size with lower
	 * resolution
	 * 
	 * Encodes the video with default attributes thereby reducing the size of the
	 * video with better quality
	 * 
	 * Maintains the best compressed quality
	 * 
	 * @param file       indicates the video file object to be compressed
	 * @param fileFormat to indicate the video type
	 * @return byte array object with encoded video
	 * @throws VideoException
	 * @throws IOException
	 */
	public byte[] reduceVideoSize(File file, VideoFormats fileFormat) throws VideoException, IOException {
		String fileType = fileFormat.toString();
		encodingAttributes.setFormat(fileType);
		return encodeVideo(IVFileUtils.copyToByteArray(file), fileType);
	}

	/**
	 * This method helps in converting the video content to reduced size with lower
	 * resolution with user defined attributes
	 * 
	 * Encodes the video with custom attributes thereby reducing the size of the
	 * video with better quality
	 * 
	 * Maintains the best compressed quality
	 * 
	 * @param data           indicates the video content to be compressed
	 * @param fileFormat     to indicate the video type
	 * @param audioAttribute to customize audio encoding
	 * @param videoAttribute to customize video encoding
	 * @return
	 * @throws VideoException
	 */
	public byte[] encodeVideoWithAttributes(byte[] data, VideoFormats fileFormat, IVAudioAttributes audioAttribute,
			IVVideoAttributes videoAttribute) throws VideoException {
		String filetype = fileFormat.toString();
		setAudioAndVideoAttributes(filetype, audioAttribute, videoAttribute);
		return encodeVideo(data, filetype);
	}

	/**
	 * Method to set the user defined Audio and Video Attributes for encoding
	 * 
	 * @param fileFormat
	 * @param audioAttribute
	 * @param videoAttribute
	 */
	private void setAudioAndVideoAttributes(String fileFormat, IVAudioAttributes audioAttribute,
			IVVideoAttributes videoAttribute) {
		if (videoAttribute != null) {
			videoAttributes.setCodec(IVConstants.VIDEO_CODEC);
			videoAttributes.setX264Profile(X264_PROFILE.BASELINE);
			videoAttributes.setBitRate(videoAttribute.getBitRate());
			videoAttributes.setFrameRate(videoAttribute.getFrameRate());
			videoAttributes
					.setSize(new VideoSize(videoAttribute.getSize().getWidth(), videoAttribute.getSize().getHeight()));
			encodingAttributes.setVideoAttributes(videoAttributes);
		}
		if (audioAttribute != null) {
			audioAttributes.setCodec(IVConstants.AUDIO_CODEC);
			audioAttributes.setBitRate(audioAttribute.getBitRate());
			audioAttributes.setChannels(audioAttribute.getChannels());
			audioAttributes.setSamplingRate(audioAttribute.getSamplingRate());
			encodingAttributes.setAudioAttributes(audioAttributes);
		}
		encodingAttributes.setFormat(fileFormat);
	}

	/**
	 * This method encodes the video stream with custom encoding attributes and
	 * reduces the resolution and size of the file
	 * 
	 * @param data
	 * @param fileFormat
	 * @return
	 * @throws VideoException
	 */
	private byte[] encodeVideo(byte[] data, String fileFormat) throws VideoException {
		File target = null;
		File file = null;
		try {
			target = File.createTempFile(IVConstants.TARGET_FILENAME, fileFormat);
			file = File.createTempFile(IVConstants.SOURCE_FILENAME, fileFormat);
			FileUtils.writeByteArrayToFile(file, data);
			MultimediaObject source = new MultimediaObject(file);
			encoder.encode(source, target, encodingAttributes);
			return FileUtils.readFileToByteArray(target);
		} catch (Exception e) {
			throw new VideoException("Error Occurred while resizing the video");
		} finally {
			try {
				if (file != null)
					Files.deleteIfExists(file.toPath());
				if (target != null)
					Files.deleteIfExists(target.toPath());
			} catch (IOException e) {
				// ignore
			}

		}
	}

}
