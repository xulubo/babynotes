package com.babycenter.pregnancytracker.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.List;


public class Day
  implements Serializable, Externalizable
{
  public static final int FIRST_DAYS_TO_GO = 266;
  public static final int FIRST_DAY_IN_PREG = 14;
  public static final int LAST_DAYS_TO_GO = -34;
  public static final int LAST_DAY_IN_PREG = 314;

  public List<Artifact> artifacts;
  public int dayOfMonth;
  public int dayOfWeek;
  public int daysInPregnancy;
  public int daysToGo;
  public String formattedDate;
  public int month;

  public int sortOrder;
  public int year;

  public boolean equals(Object paramObject)
  {
    boolean i;
    if (this == paramObject)
      i = true;
    while (true)
    {

      if (paramObject == null)
      {
        i = false;
        continue;
      }
      if (!(paramObject instanceof Day))
      {
        i = false;
        continue;
      }
      Day localDay = (Day)paramObject;
      if (this.daysInPregnancy != localDay.daysInPregnancy)
      {
        i = false;
        continue;
      }
      i = true;
      return i;

    }
  }

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.sortOrder = paramObjectInput.readInt();
    this.daysInPregnancy = paramObjectInput.readInt();
    this.daysToGo = paramObjectInput.readInt();
    paramObjectInput.readInt();
    this.artifacts = ((List)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    paramObjectOutput.writeInt(this.sortOrder);
    paramObjectOutput.writeInt(this.daysInPregnancy);
    paramObjectOutput.writeInt(this.daysToGo);
    paramObjectOutput.writeInt(this.artifacts.size());
    paramObjectOutput.writeObject(this.artifacts);
  }
}

/* Location:           C:\work\tools\android-decompile-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name:     com.babycenter.pregnancytracker.common.Day
 * JD-Core Version:    0.6.0
 */