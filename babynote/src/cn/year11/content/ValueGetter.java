package cn.year11.content;

public interface ValueGetter {
	public Long getLong(String name);
	
	public Integer getInt(String name);
	
	public String getString(String name);
	
	public byte[] getBlob(String name);
}
