package com.babycenter.pregnancytracker.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.List;


public class Week
  implements Serializable, Externalizable
{
  public static final int FIRST_WEEK_IN_PREG = 2;
  public static final int LAST_WEEK_IN_PREG = 44;

  public List<Day> days;
  public boolean hasChecklistArtifact;

  public int sortOrder;

  public int stageId;
  public int weeksInPregnancy;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.stageId = paramObjectInput.readInt();
    this.sortOrder = paramObjectInput.readInt();
    this.weeksInPregnancy = paramObjectInput.readInt();
    this.hasChecklistArtifact = paramObjectInput.readBoolean();
    paramObjectInput.readInt();
    this.days = ((List)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    paramObjectOutput.writeInt(this.stageId);
    paramObjectOutput.writeInt(this.sortOrder);
    paramObjectOutput.writeInt(this.weeksInPregnancy);
    paramObjectOutput.writeBoolean(this.hasChecklistArtifact);
    paramObjectOutput.writeInt(this.days.size());
    paramObjectOutput.writeObject(this.days);
  }
}

/* Location:           C:\work\tools\android-decompile-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name:     com.babycenter.pregnancytracker.common.Week
 * JD-Core Version:    0.6.0
 */