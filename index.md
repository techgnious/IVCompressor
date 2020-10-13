## Welcome to IVCompressor

IVCompressor is a powerful java library tool to resize the images and videos.

It is the only java library which is customly built to include both image and video compression algorithms to ease the conversion of Image and Video files in java.

It is the only java based library that provides direct video compression functionality without any need for including any more dependencies or extra coding.

### How it works
It works on the concept of resizing the images using BufferedImage and Graphics2D through core java.

It utilizes FFMPEG (a powerful video handling tool) through java to enhance the video attributes like bitrate,frame rate,channels,etc to enhance the quality of compression.

### Features
- Resize Images from higher resolution to smaller resolution with various resolutions provided
- Custom Resolution can be provided for compressing the images
- Convert Video Files to different format (size varies with the conversion format)
- Resize video files to smaller size by reducing the resolution with default settings
- Videos can be resized to smaller size with custom resolution
- Facility to provide custom values to various audio or video attributes
- Uses java based classes for Image compression to smaller resolution
- Uses best video enhancement software FFMPEG for Video compression and conversion
- Eases the conversion process by reducing the boiler plate code

### Background

IVCompressor was started by me (Srikanth Reddy) when i realized the lack of support for compression or resizing for video files through java. There are supported documentation or libraries for video compression on Android through java but when it comes to plain java or for a RESTFul services we couldn't find any suitable libraries which were helpful in compression for video files.

After a lot of research and my post in [StackOverFlow](https://stackoverflow.com/questions/63335797/how-can-i-resize-the-video-bytes-to-smaller-resolution-in-java) i found out that there are no direct libraries to handle video compression in java (except in case of Android which doesn't help in solving issue in plain java). I have heard of a suggestion using xuggler but i didn't find it useful for fulfilling my requirement.

With lot of research, i have found a way to utilize the FFMPEG executor through java and utilized it to compress the videos to smaller resolution. Hence i have decided to start a project to ease the conversion of videos as well as images and provide an artifact which can be handy to other people.

I hope atleast few developers would be benefited by using my project. Thanks!!!

### Implementation (Steps to use)

Implementation of IVCompressor in your project is very easy. Below steps explains the usage::

#### Step 1 

Add the dependency to your project. Artifact dependency can be obtained from [here](https://mvnrepository.com/artifact/io.github.techgnious/IVCompressor)

```markdown
# For Maven Project

<dependency>
    <groupId>io.github.techgnious</groupId>
    <artifactId>IVCompressor</artifactId>
    <version>2.0.0</version>
</dependency>

## For Gradle

compile group: 'io.github.techgnious', name: 'IVCompressor', version: '2.0.0'

### For Buildr

'io.github.techgnious:IVCompressor:jar:2.0.0'

```

#### Step 2

**Create an instance of IVCompressor**

    IVCompressor compressor = new IVCompressor();

Next step is to identify relevant method that is to be used from the IVCompressor

#### Step 3

**1) Compress an image from a byte array to smaller resolution.**

     compressor.resizeImage(file.getBytes(),ImageFormats.JPEG,ResizeResolution.R720P);
 
 ImageFormats helps in selecting the type of image and ResizeResolution helps in selecting Image Resolution.
 
 **2) Compress an image and save it to a specific path**
 
    compressor.resizeAndSaveImageToAPath(file, ImageFormats.JPEG, path, ResizeResolution.R480P);
    
* Variable file can be passed as either byte array or a File
* Compressed image will be saved to specified location mentioned in variable path.

**3) Compress an image with custom resolution**

    IVSize customRes=new IVSize();
	customRes.setWidth(400);
	customRes.setHeight(300);
	compressor.resizeImageWithCustomRes(file.getBytes(),ImageFormats.JPEG,customRes);

We can set the custom height and width using above method.

**4) Compress a video from a byte array to smaller resolution with default settings**

    compressor.reduceVideoSize(file.getBytes(), VideoFormats.MP4, ResizeResolution.R480P);
    
VideoFormats helps in selecting the format of the video file that is converted.

**5) Compress a video using custom resolution**

    IVSize customRes = new IVSize();
	customRes.setWidth(400);
	customRes.setHeight(300);
	compressor.reduceVideoSizeWithCustomRes(file.getBytes(),VideoFormats.MP4, customRes);

**6) Converting video from one format to another without resizing it to small size**

    compressor.convertVideoFormat(file.getBytes(), VideoFormats.MP4,VideoFormats.AVI);

Here the input format of the video is MP4 and the output format is AVI (Video will be converted from MP4 to AVI).

**7) Converting video from one format to another including resizing it to small size**

    compressor.convertAndResizeVideo(file.getBytes(), VideoFormats.MP4,VideoFormats.MOV, ResizeResolution.R720P);

Here the input format of the video is MP4 and the output format is MOV (Video will be converted from MP4 to AVI). Output video will be resized with default encoding attributes set by the IVCompressor.

**8) Compress a video by setting custom attributes and resolution**

    IVSize customRes = new IVSize();
	customRes.setWidth(400);
	customRes.setHeight(300);
	IVAudioAttributes audioAttribute = new IVAudioAttributes();
	// here 64kbit/s is 64000
	audioAttribute.setBitRate(64000);
	audioAttribute.setChannels(2);
	audioAttribute.setSamplingRate(44100);
	IVVideoAttributes videoAttribute = new IVVideoAttributes();
	// Here 160 kbps video is 160000
	videoAttribute.setBitRate(160000);
	// More the frames more quality and size, but keep it low based on //devices like mobile
	videoAttribute.setFrameRate(15);
	videoAttribute.setSize(customRes);
	compressor.encodeVideoWithAttributes(file.getBytes(), VideoFormats.MP4,audioAttribute, videoAttribute);

### About me
 
 I am a Software Developer specializing in Java based technologies. I have been working on many technologies including Microservices and AWS.
 
Know more about me @[About Me](https://techgnious.github.io/)

For any requirements for freelancing, drop me the details @[Mailbox](mailto:srikanthreddy111@yahoo.co.in)
### Support or Contact

**[Get on Maven](https://mvnrepository.com/artifact/io.github.techgnious/IVCompressor)** - Get the artifact through Maven Repository

**[Github Repo](https://github.com/techgnious/IVCompressor/)** - View the code on Github Repository

Need more clarity on functionalities? Check out our [documentation](https://javadoc.io/doc/io.github.techgnious/IVCompressor) or [contact me](https://techgnious.github.io/) and we’ll help you sort it out.

Please reach out on my [Mailbox](mailto:srikanthreddy111@yahoo.co.in) for any further support or clarifications if necessary.
