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
import java.nio.file.Paths;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import io.github.techgnious.constants.IVConstants;
import io.github.techgnious.dto.IVAudioAttributes;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.IVVideoAttributes;
import io.github.techgnious.dto.ImageFormats;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.ImageException;
import io.github.techgnious.exception.VideoException;
import io.github.techgnious.utils.IVFileUtils;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.encode.enums.X264_PROFILE;
import ws.schild.jave.info.VideoSize;

/**
 * Base Class to handle compression or conversion of Image/Video Files.
 * 
 * Operations supported:
 * 
 * Resize Image. Resize Video and Convert Video.
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
	 * Defines the image resolution
	 */
	private ResizeResolution imageResolution = ResizeResolution.IMAGE_DEFAULT;
	/**
	 * Defines the video resolution
	 */
	private ResizeResolution videoResolution = ResizeResolution.VIDEO_DEFAULT;

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
		videoAttributes.setSize(new VideoSize(videoResolution.getWidth(), videoResolution.getHeight()));
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
	 * This method attempts to resize the image byte stream to lower resolution.
	 * 
	 * Returns resized byte stream.
	 * 
	 * @param data       - file data in byte array that is to be compressed
	 * @param fileFormat - file type
	 * @param resolution - Resolution of output image. Optional Field. Can be passed
	 *                   as null to use the default values
	 * @return - returns the compressed image in byte array
	 * @throws ImageException - throws exception if there is issue in process
	 */
	public byte[] resizeImage(byte[] data, ImageFormats fileFormat, ResizeResolution resolution) throws ImageException {
		if (resolution != null)
			imageResolution = resolution;
		return rescaleImage(data, imageResolution.getWidth(), imageResolution.getHeight(), fileFormat.getType());
	}

	/**
	 * This method attempts to resize the image byte stream to lower resolution with
	 * custom user defined resolution
	 * 
	 * Returns resized byte stream
	 * 
	 * @param data       - file data in byte array that is to be compressed
	 * @param fileFormat - file type
	 * @param res        - Custom Resolution of output image. Optional Field. Can be
	 *                   passed as null to use the default values
	 * @return - returns the compressed image in byte array
	 * @throws ImageException - throws exception if there is issue in process
	 */
	public byte[] resizeImageWithCustomRes(byte[] data, ImageFormats fileFormat, IVSize res) throws ImageException {
		return rescaleImage(data, res.getWidth(), res.getHeight(), fileFormat.getType());
	}

	/**
	 * This method attempts to resize the image file to lower resolution and saves
	 * it back in the path provided.
	 * 
	 * Returns path location of the object.
	 * 
	 * @param file            - file that is to be compressed
	 * @param fileFormat      - file type
	 * @param path            - location where the file to be saved
	 * @param imageResolution - Resolution of output image.Optional Field. Can be
	 *                        passed as null to use the default values
	 * @return - returns path location
	 * @throws ImageException - throws exception if there is issue in process
	 */
	public String resizeAndSaveImageToAPath(File file, ImageFormats fileFormat, String path,
			ResizeResolution imageResolution) throws ImageException {
		try {
			byte[] data = resizeImage(IVFileUtils.copyToByteArray(file), fileFormat, imageResolution);
			String fileName = file.getName();
			if (!file.getName().contains(fileFormat.getType()))
				fileName = fileName.substring(0, fileName.indexOf(".")) + "." + fileFormat.getType();
			return createAndStoreNewFile(fileName, path, data);
		} catch (IOException e) {
			throw new ImageException(e);
		}
	}

	/**
	 * This method attempts to resize the image file to lower resolution and saves
	 * it back in the path provided.
	 * 
	 * Returns path location of the object.
	 * 
	 * @param fileData        - file that is to be compressed
	 * @param fileName        - name of the file
	 * @param fileFormat      - file type
	 * @param path            - location where the file to be saved
	 * @param imageResolution - Resolution of output image.Optional Field. Can be
	 *                        passed as null to use the default values
	 * @return - path where file is stored
	 * @throws ImageException - throws exception if there is issue in process
	 */
	public String resizeAndSaveImageToAPath(byte[] fileData, String fileName, ImageFormats fileFormat, String path,
			ResizeResolution imageResolution) throws ImageException {
		try {
			byte[] data = resizeImage(fileData, fileFormat, imageResolution);
			if (!fileName.contains(fileFormat.getType()))
				fileName = fileName.substring(0, fileName.indexOf(".")) + "." + fileFormat.getType();
			return createAndStoreNewFile(fileName, path, data);
		} catch (IOException e) {
			throw new ImageException(e);
		}
	}

	/**
	 * This method attempts to resize the image file to lower resolution.
	 * 
	 * Returns resized image in byte array.
	 * 
	 * @param file            - file that is to be compressed
	 * @param fileFormat      - file type
	 * @param imageResolution - Resolution of output image.Optional Field. Can be
	 *                        passed as null to use the default values
	 * @return - byte array as response
	 * @throws ImageException - throws exception if there is issue in process
	 * @throws IOException    - throws exception if there is issue with file
	 */
	public byte[] resizeImageUsingFile(File file, ImageFormats fileFormat, ResizeResolution imageResolution)
			throws ImageException, IOException {
		return resizeImage(IVFileUtils.copyToByteArray(file), fileFormat, imageResolution);

	}

	/**
	 * This method attempts to resize the image received as Inputstream object to
	 * lower resolution.
	 * 
	 * Returns resized stream object.
	 * 
	 * @param stream          - Image input stream that is to be compressed
	 * @param fileFormat      - type of the file
	 * @param imageResolution - Resolution of the output image file.Optional Field.
	 *                        Can be passed as null to use the default values
	 * @return InputStream - returns output as a stream
	 * @throws ImageException - throws exception if there is issue in process
	 */
	public InputStream resizeImage(InputStream stream, ImageFormats fileFormat, ResizeResolution imageResolution)
			throws ImageException {
		InputStream targetStream = null;
		try {
			byte[] data = resizeImage(IVFileUtils.copyToByteArray(stream), fileFormat, imageResolution);
			targetStream = new ByteArrayInputStream(data);
			return targetStream;
		} catch (IOException e) {
			throw new ImageException(e);
		}
	}

	/**
	 * This method helps in converting the video content to reduced size with lower
	 * resolution.
	 * 
	 * Encodes the video with default attributes thereby reducing the size of the
	 * video with better quality.
	 * 
	 * Maintains the best compressed quality.
	 * 
	 * @param data       -indicates the video content to be compressed
	 * @param fileFormat -to indicate the video type
	 * @param resolution -Resolution of the output video
	 * @return -byte stream object with compressed video data
	 * @throws VideoException -throws exception when the data is incompatible for
	 *                        the encoding
	 */
	public byte[] reduceVideoSize(byte[] data, VideoFormats fileFormat, ResizeResolution resolution)
			throws VideoException {
		String fileType = setAttributes(fileFormat, resolution);
		return encodeVideo(data, fileType);
	}

	/**
	 * This method helps in converting the video to another format.
	 * 
	 * Helps in reducing size of video with lower resolution.
	 * 
	 * Encodes the video with default attributes thereby reducing the size of the
	 * video with better quality.
	 * 
	 * Maintains the best compressed quality.
	 * 
	 * @param data         -indicates the video content to be compressed
	 * @param inputFormat  -to indicate the format of input video
	 * @param outputFormat -to indicate the format of output video
	 * @param resolution   -Resolution of the output video
	 * @return -byte stream object with compressed video data
	 * @throws VideoException -throws exception when the data is incompatible for
	 *                        the encoding
	 */
	public byte[] convertAndResizeVideo(byte[] data, VideoFormats inputFormat, VideoFormats outputFormat,
			ResizeResolution resolution) throws VideoException {
		String fileType = inputFormat.getType();
		encodingAttributes.setInputFormat(fileType);
		encodingAttributes.setOutputFormat(outputFormat.getType());
		if (resolution != null) {
			Optional<VideoAttributes> videoAttr = encodingAttributes.getVideoAttributes();
			if (videoAttr.isPresent())
				videoAttr.get().setSize(new VideoSize(resolution.getWidth(), resolution.getHeight()));
		}
		return encodeVideo(data, fileType);
	}

	/**
	 * This method is used for resizing using custom resolution but with remaining
	 * attributes as default.
	 * 
	 * Encodes the video with default attributes thereby reducing the size of the
	 * video with better quality.
	 * 
	 * Maintains the best compressed quality.
	 * 
	 * @param data       -indicates the video content to be compressed
	 * @param fileFormat -to indicate the video type
	 * @param resolution -Resolution(width x height) of the output video
	 * @return -byte stream object with compressed video data
	 * @throws VideoException -throws exception when the data is incompatible for
	 *                        the encoding
	 */
	public byte[] reduceVideoSizeWithCustomRes(byte[] data, VideoFormats fileFormat, IVSize resolution)
			throws VideoException {
		String fileType = fileFormat.getType();
		encodingAttributes.setInputFormat(fileType);
		encodingAttributes.setOutputFormat(fileType);
		if (resolution != null) {
			Optional<VideoAttributes> videoAttr = encodingAttributes.getVideoAttributes();
			if (videoAttr.isPresent())
				videoAttr.get().setSize(new VideoSize(resolution.getWidth(), resolution.getHeight()));
		}
		return encodeVideo(data, fileType);
	}

	/**
	 * This method helps in converting the video content to reduced size with lower
	 * resolution.
	 * 
	 * Encodes the video with default attributes thereby reducing the size of the
	 * video with better quality.
	 * 
	 * Maintains the best compressed quality.
	 * 
	 * @param file       -indicates the video file object to be compressed
	 * @param fileFormat -to indicate the video type
	 * @param resolution -Resolution of the output video
	 * @return byte array object with encoded video
	 * @throws VideoException - throws exception if there is an issue with video
	 *                        processing
	 * @throws IOException    - throws exception if there is issue with file
	 */
	public byte[] reduceVideoSize(File file, VideoFormats fileFormat, ResizeResolution resolution)
			throws VideoException, IOException {
		String fileType = setAttributes(fileFormat, resolution);
		return encodeVideo(IVFileUtils.copyToByteArray(file), fileType);
	}

	/**
	 * This method helps in converting the video content to reduced size with lower
	 * resolution
	 * 
	 * Encodes the video with default attributes thereby reducing the size of the
	 * video with better quality and stores it in the user defined path .
	 * 
	 * Maintains the best compressed quality
	 * 
	 * @param file       -indicates the video file object to be compressed
	 * @param fileFormat -to indicate the video type
	 * @param resolution -Resolution of the output video
	 * @param path       -location to store the output file
	 * @return path location for the file stored
	 * @throws VideoException - throws exception if there is an issue with video
	 *                        processing
	 * @throws IOException    - throws exception if there is issue with file
	 */
	public String reduceVideoSizeAndSaveToAPath(File file, VideoFormats fileFormat, ResizeResolution resolution,
			String path) throws VideoException, IOException {
		String fileType = setAttributes(fileFormat, resolution);
		byte[] data = encodeVideo(IVFileUtils.copyToByteArray(file), fileType);
		String fileName = file.getName();
		if (!fileName.contains(fileFormat.getType()))
			fileName = fileName.substring(0, fileName.indexOf(".")) + "." + fileFormat.getType();
		return createAndStoreNewFile(fileName, path, data);

	}

	/**
	 * This method helps in converting the video content to reduced size with lower
	 * resolution
	 * 
	 * Encodes the video with default attributes thereby reducing the size of the
	 * video with better quality and stores it in the user defined path .
	 * 
	 * Maintains the best compressed quality
	 * 
	 * @param fileData   -indicates the video file object byte array to be
	 *                   compressed
	 * @param fileName   - name of the file
	 * @param fileFormat -to indicate the video type
	 * @param resolution -Resolution of the output video
	 * @param path       -location to store the output file
	 * @return path location for the file stored
	 * @throws VideoException - throws exception if there is an issue with video
	 *                        processing
	 * @throws IOException    - throws exception if there is issue with file
	 */
	public String reduceVideoSizeAndSaveToAPath(byte[] fileData, String fileName, VideoFormats fileFormat,
			ResizeResolution resolution, String path) throws VideoException, IOException {
		String fileType = setAttributes(fileFormat, resolution);
		byte[] data = encodeVideo(fileData, fileType);
		if (!fileName.contains(fileFormat.getType()))
			fileName = fileName.substring(0, fileName.indexOf(".")) + "." + fileFormat.getType();
		return createAndStoreNewFile(fileName, path, data);

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
	 * @return - returns byte array as response
	 * @throws VideoException - throws exception if there is an issue with video
	 *                        processing
	 */
	public byte[] encodeVideoWithAttributes(byte[] data, VideoFormats fileFormat, IVAudioAttributes audioAttribute,
			IVVideoAttributes videoAttribute) throws VideoException {
		String filetype = fileFormat.getType();
		setAudioAndVideoAttributes(filetype, audioAttribute, videoAttribute);
		return encodeVideo(data, filetype);
	}

	/**
	 * This method is used to convert the video from existing format to another
	 * format without compressing the data
	 * 
	 * @param data         - data that is to be converted
	 * @param inputFormat  - video format the data is to be converted
	 * @param outputFormat - video format the data is to be converted
	 * @return - returns byte array as response
	 * @throws VideoException - throws exception if there is an issue with video
	 *                        processing
	 */
	public byte[] convertVideoFormat(byte[] data, VideoFormats inputFormat, VideoFormats outputFormat)
			throws VideoException {
		String filetype = inputFormat.getType();
		AudioAttributes audioAttr = new AudioAttributes();
		VideoAttributes videoAttr = new VideoAttributes();
		encodingAttributes.setInputFormat(inputFormat.getType());
		encodingAttributes.setOutputFormat(outputFormat.getType());
		encodingAttributes.setAudioAttributes(audioAttr);
		encodingAttributes.setVideoAttributes(videoAttr);
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
			if (videoAttribute.getBitRate() != null)
				videoAttributes.setBitRate(videoAttribute.getBitRate());
			if (videoAttribute.getFrameRate() != null)
				videoAttributes.setFrameRate(videoAttribute.getFrameRate());
			if (videoAttribute.getSize() != null)
				videoAttributes.setSize(
						new VideoSize(videoAttribute.getSize().getWidth(), videoAttribute.getSize().getHeight()));
			encodingAttributes.setVideoAttributes(videoAttributes);
		}
		if (audioAttribute != null) {
			audioAttributes.setCodec(IVConstants.AUDIO_CODEC);
			if (audioAttribute.getBitRate() != null)
				audioAttributes.setBitRate(audioAttribute.getBitRate());
			if (audioAttribute.getChannels() != null)
				audioAttributes.setChannels(audioAttribute.getChannels());
			if (audioAttribute.getSamplingRate() != null)
				audioAttributes.setSamplingRate(audioAttribute.getSamplingRate());
			encodingAttributes.setAudioAttributes(audioAttributes);
		}
		encodingAttributes.setInputFormat(fileFormat);
		encodingAttributes.setOutputFormat(fileFormat);
	}

	/**
	 * This method encodes the video stream with custom encoding attributes and
	 * reduces the resolution and size of the file
	 * 
	 * @param data
	 * @param fileFormat
	 * @return - returns byte array as response
	 * @throws VideoException - throws exception if there is an issue with video
	 *                        processing
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
			throw new VideoException("Error Occurred while resizing the video", e);
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

	/**
	 * Rescales the images to lower resolution
	 * 
	 * @param data
	 * @param width
	 * @param height
	 * @param contentType
	 * @return - returns byte array as response
	 * @throws ImageException - throws exception if there is issue in process
	 */
	private byte[] rescaleImage(byte[] data, int width, int height, String contentType) throws ImageException {
		try {
			BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(data));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizedImage = new BufferedImage(width, height, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, width, height, null);
			g.dispose();
			writeImageToOutputstream(width, height, contentType, originalImage, outputStream, resizedImage);
			return outputStream.toByteArray();
		} catch (Exception e) {
			throw new ImageException("Byte Array doesn't contain valid Image", e);
		}
	}

	/**
	 * Checks for alpha for PNG Files and sets default values to it
	 * 
	 * @param width
	 * @param height
	 * @param contentType
	 * @param originalImage
	 * @param outputStream
	 * @param resizedImage
	 * @throws IOException
	 */
	private void writeImageToOutputstream(int width, int height, String contentType, BufferedImage originalImage,
			ByteArrayOutputStream outputStream, BufferedImage resizedImage) throws IOException {
		int type;
		Graphics2D g;
		try {
			ImageIO.write(resizedImage, contentType, outputStream);
		} catch (Exception e) {
			type = BufferedImage.TYPE_INT_RGB;
			resizedImage = new BufferedImage(width, height, type);
			g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, width, height, null);
			g.dispose();
			ImageIO.write(resizedImage, contentType, outputStream);
		}
	}

	/**
	 * Method to create a file and store data in local path
	 * 
	 * @param fileName -indicates the file name of the output object
	 * @param path     -location to store the output file
	 * @param data     -byte array of the output file
	 * @return - returns file path as response
	 * @throws IOException - throws exception if there is issue with file
	 */
	private String createAndStoreNewFile(String fileName, String path, byte[] data) throws IOException {
		checkForValidPath(path);
		String fullPath = path;
		if (!path.endsWith(File.separator))
			fullPath += File.separator;
		fullPath += fileName;
		File newFile = new File(fullPath);
		FileUtils.writeByteArrayToFile(newFile, data);
		return "File is saved in path::" + newFile.getAbsolutePath();
	}

	/**
	 * Method to check if input path is valid or not
	 * 
	 * @param path
	 * @throws IOException - throws exception if there is issue with path
	 */
	private void checkForValidPath(String path) throws IOException {
		try {
			Paths.get(path);
		} catch (Exception e) {
			throw new IOException("Invalid Path");
		}

	}

	/**
	 * @param fileFormat
	 * @param resolution
	 * @return
	 */
	private String setAttributes(VideoFormats fileFormat, ResizeResolution resolution) {
		String fileType = fileFormat.getType();
		encodingAttributes.setInputFormat(fileType);
		encodingAttributes.setOutputFormat(fileType);
		if (resolution != null) {
			Optional<VideoAttributes> videoAttr = encodingAttributes.getVideoAttributes();
			if (videoAttr.isPresent())
				videoAttr.get().setSize(new VideoSize(resolution.getWidth(), resolution.getHeight()));
		}
		return fileType;
	}

}
