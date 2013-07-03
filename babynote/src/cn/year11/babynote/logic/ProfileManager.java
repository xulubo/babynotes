package cn.year11.babynote.logic;

import cn.year11.babynote.BabyNoteApplication;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.Profile;
import cn.year11.babynote.provider.ProfileDao;

public class ProfileManager {
	static private ProfileManager _instance;
	private ProfileDao mProfileDao;
	
	static public ProfileManager getInstance()
	{
		if (_instance == null) {
			_instance = new ProfileManager();
		}
		
		return _instance;
	}
	
	public ProfileManager()
	{
		mProfileDao = ProfileDao.getInstance();
	}
	
	// return the first baby profile
	public Profile getFirstProfile()
	{
		return mProfileDao.getProfile();
	}
}
