package com.babycenter.pregnancytracker.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.List;


public class StageContent
  implements Serializable, Externalizable
{
  private static final long serialVersionUID = 1L;
  public String baseHostName;


  public List<Week> weeks;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    paramObjectInput.readLong();
    this.baseHostName = paramObjectInput.readUTF();
    paramObjectInput.readInt();
    this.weeks = ((List)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    paramObjectOutput.writeLong(1L);
    paramObjectOutput.writeUTF(this.baseHostName);
    paramObjectOutput.writeInt(this.weeks.size());
    paramObjectOutput.writeObject(this.weeks);
  }
}

/* Location:           C:\work\tools\android-decompile-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name:     com.babycenter.pregnancytracker.common.StageContent
 * JD-Core Version:    0.6.0
 */