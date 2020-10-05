/**
 * 
 */
package io.github.techgnious.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

/**
 * Util Class to handle the File operations within the project
 * 
 * @author srikanth.anreddy
 *
 */
public class IVFileUtils {

	private IVFileUtils() {
	}

	/**
	 * The default buffer size used when copying bytes.
	 */
	public static final int BUFFER_SIZE = 4096;

	/**
	 * Copy the contents of the given InputStream into a new byte array. Closes the
	 * stream when done.
	 * 
	 * @param in the stream to copy
	 * @return the new byte array that has been copied to (possibly empty)
	 * @throws IOException in case of I/O errors
	 */
	public static byte[] copyToByteArray(InputStream in) throws IOException {
		if (in == null) {
			return new byte[0];
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		copy(in, out);
		return out.toByteArray();
	}

	/**
	 * Copy the contents of the given input File into a new byte array.
	 * 
	 * @param in the file to copy from
	 * @return the new byte array that has been copied to
	 * @throws IOException in case of I/O errors
	 */
	public static byte[] copyToByteArray(File in) throws IOException {
		Assert.notNull(in, "No input File specified");
		return copyToByteArray(Files.newInputStream(in.toPath()));
	}

	/**
	 * Copy the contents of the given InputStream to the given OutputStream. Closes
	 * both streams when done.
	 * 
	 * @param in  the stream to copy from
	 * @param out the stream to copy to
	 * @return the number of bytes copied
	 * @throws IOException in case of I/O errors
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {
		Assert.notNull(in, "No InputStream specified");
		Assert.notNull(out, "No OutputStream specified");

		try {
			return StreamUtils.copy(in, out);
		} finally {
			close(in);
			close(out);
		}
	}

	/**
	 * Attempt to close the supplied {@link Closeable}, silently swallowing any
	 * exceptions.
	 * 
	 * @param closeable the {@code Closeable} to close
	 */
	private static void close(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException ex) {
			// ignore
		}
	}
}
