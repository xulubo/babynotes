package cn.year11.babynote.provider.email;

import com.android.email.codec.binary.Base64OutputStream;
import com.android.email.mail.Body;
import com.android.email.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class BinaryTempFileBody
  implements Body
{
  private static File mTempDirectory;
  private File mFile;

  public static void setTempDirectory(File tempDirectory)
  {
    mTempDirectory = tempDirectory;
  }

  public BinaryTempFileBody() throws IOException {
    if (mTempDirectory == null)
      throw new RuntimeException(
        "setTempDirectory has not been called on BinaryTempFileBody!");
  }

  public OutputStream getOutputStream() throws IOException
  {
    this.mFile = File.createTempFile("body", null, mTempDirectory);
    this.mFile.deleteOnExit();
    return new FileOutputStream(this.mFile);
  }

  public InputStream getInputStream() throws MessagingException {
    try {
      return new BinaryTempFileBodyInputStream(new FileInputStream(this.mFile));
    } catch (IOException ioe) {
    	ioe.printStackTrace();
        throw new MessagingException("Unable to open body", ioe);
    }
  }

  public void writeTo(OutputStream out) throws IOException, MessagingException
  {
    InputStream in = getInputStream();
    Base64OutputStream base64Out = new Base64OutputStream(out);
    IOUtils.copy(in, base64Out);
    base64Out.close();
    //this.mFile.delete();
  }

  class BinaryTempFileBodyInputStream extends FilterInputStream {
    public BinaryTempFileBodyInputStream(InputStream in) {
      super(in);
    }

    public void close() throws IOException
    {
      super.close();
      BinaryTempFileBody.this.mFile.delete();
    }
  }
}