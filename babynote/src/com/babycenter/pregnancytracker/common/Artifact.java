package com.babycenter.pregnancytracker.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;



public class Artifact
  implements Serializable, Externalizable
{
  public static final int ACTIVITY_TYPE = 2;
  public static final String ACTIVITY_TYPE_STRING = "activity";
  public static final int IMAGE_TYPE = 4;
  public static final String IMAGE_TYPE_STRING = "image";
  public static final int REMINDER_TYPE = 3;
  public static final String REMINDER_TYPE_STRING = "reminder";
  public static final int TEXT_TYPE = 1;
  public static final String TEXT_TYPE_STRING = "text";
  public static final int VIDEO_TYPE = 5;
  public static final String VIDEO_TYPE_STRING = "video";
  public int artifactType;

  public String category;
  public int daysInPregnancy;

  public long id;

  public int sortOrder;

  public String teaser;

  public String thumbnail;

  public String title;

  public String type;

  public String url;

  public Long videoInfo;
  public int weeksInPregnancy;

  public boolean isChecklistArtifact()
  {
    if (("activity".equalsIgnoreCase(this.type)) || ("reminder".equalsIgnoreCase(this.type))) {
    	return true;
    }
    return false;
  }

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.id = paramObjectInput.readLong();
    this.sortOrder = paramObjectInput.readInt();
    this.daysInPregnancy = paramObjectInput.readInt();
    this.weeksInPregnancy = paramObjectInput.readInt();
    this.artifactType = paramObjectInput.readInt();
    switch (this.artifactType)
    {
    default:

        break;
    case 1:
        this.type = "text";
        break;
    case 3:
        this.type = "reminder";
        break;
    case 2:
        this.type = "activity";
        break;
    case 4:
        this.type = "image";
        break;
    case 5:
        this.type = "video";
    }
    this.category = paramObjectInput.readUTF();
    this.title = paramObjectInput.readUTF();
    this.teaser = paramObjectInput.readUTF();
    this.url = paramObjectInput.readUTF();
    this.thumbnail = ((String)paramObjectInput.readObject());
    this.videoInfo = ((Long)paramObjectInput.readObject());
  }

  public String toString()
  {
    return this.id + ":" + this.artifactType + ":" + this.title;
  }

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    paramObjectOutput.writeLong(this.id);
    paramObjectOutput.writeInt(this.sortOrder);
    paramObjectOutput.writeInt(this.daysInPregnancy);
    paramObjectOutput.writeInt(this.weeksInPregnancy);
    if ("text".equalsIgnoreCase(this.type))
      this.artifactType = 1;
    if ("activity".equalsIgnoreCase(this.type))
      this.artifactType = 2;
    if ("reminder".equalsIgnoreCase(this.type))
      this.artifactType = 3;
    if ("image".equalsIgnoreCase(this.type))
      this.artifactType = 4;
    if ("video".equalsIgnoreCase(this.type))
      this.artifactType = 5;
    paramObjectOutput.writeInt(this.artifactType);
    paramObjectOutput.writeUTF(this.category);
    paramObjectOutput.writeUTF(this.title);
    paramObjectOutput.writeUTF(this.teaser);
    paramObjectOutput.writeUTF(this.url);
    paramObjectOutput.writeObject(this.thumbnail);
    paramObjectOutput.writeObject(this.videoInfo);
  }
}

