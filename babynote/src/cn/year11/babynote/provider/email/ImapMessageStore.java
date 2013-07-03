package cn.year11.babynote.provider.email;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import cn.year11.babynote.BabyNoteApplication;
import cn.year11.babynote.logic.SettingManager;
import cn.year11.babynote.provider.BaseEntity;
import cn.year11.babynote.provider.EventDao;
import cn.year11.babynote.provider.Profile;
import cn.year11.babynote.provider.ProfileDao;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.utils.FileUtils;
import cn.year11.utils.Log;

import com.android.email.mail.Address;
import com.android.email.mail.FetchProfile;
import com.android.email.mail.Flag;
import com.android.email.mail.Folder;
import com.android.email.mail.Message;
import com.android.email.mail.MessagingException;
import com.android.email.mail.Folder.FolderType;
import com.android.email.mail.Folder.OpenMode;
import com.android.email.mail.Message.RecipientType;
import com.android.email.mail.internet.BinaryTempFileBody;
import com.android.email.mail.internet.MimeMessage;
import com.android.email.mail.internet.TextBody;
import com.android.email.mail.store.ImapStore;

@SuppressWarnings("unused")
public class ImapMessageStore {
	static private Log _logger = Log.getLogger(ImapMessageStore.class);
	private String mUserName;
	private String mPassword;
	private String mMailboxProvider;
	private String mMailboxUrlFmt;
	private String BOX_URL_FMT_PROXY = "imap://%s:%s@192.168.1.10";

	static HashMap<String, String> MAILBOX_PROVIDERS = new HashMap<String, String>();
	static {
		MAILBOX_PROVIDERS.put("gmail", "imap+ssl+://%s:%s@imap.gmail.com:993");
		MAILBOX_PROVIDERS.put("qq", "imap://%s:%s@imap.qq.com");
		MAILBOX_PROVIDERS.put("163", "imap://%s:%s@imap.163.com");
	}

	public ImapMessageStore() throws Exception {

		if (!SettingManager.isAutoBackupEnabled()) {
			throw new Exception("请更新邮箱信息");
		}

		mMailboxProvider = SettingManager.getMailboxProvider();
		mUserName = SettingManager.getUserName();
		mPassword = SettingManager.getPassword();

		if (!TextUtils.isEmpty(mMailboxProvider)) {
			mMailboxUrlFmt = MAILBOX_PROVIDERS.get(mMailboxProvider);
		}

	}

	public ImapStore.ImapFolder openFolder() throws AuthenticationErrorException {
		com.android.email.mail.store.ImapStore imapStore;

		ImapStore.ImapFolder folder;
		boolean folderExists;
		String label = "babynote";
		try {
			imapStore = new com.android.email.mail.store.ImapStore(
					String.format(mMailboxUrlFmt, URLEncoder.encode(mUserName),
							URLEncoder.encode(mPassword).replace("+", "%20")));
			folder = (ImapStore.ImapFolder)imapStore.getFolder(label);
			folderExists = folder.exists();
			if (!folderExists) {
				folder.create(FolderType.HOLDS_MESSAGES);
			}
			folder.open(OpenMode.READ_WRITE, null);
		} catch (MessagingException e) {
			throw new AuthenticationErrorException(e);
		}

		return folder;
	}

	public void backupProfile(Folder folder, Profile profile) {
		try {

			Message message;
			message = MessageConvertor.convertToMessage(profile);
			Message[] messages = new Message[] { message };
			folder.appendMessages(messages);
			
			ProfileDao.getInstance().updateRemoteUid(profile.getId(), message.getUid());
			ProfileDao.getInstance().updateSyncTime(profile.getId());


		} catch (MessagingException e) {
			if (e != null)
				_logger.e("MessagingException", e);
		} catch (IOException e) {
			_logger.e("IOException", e);
		} catch (JSONException e) {
			e.printStackTrace();
			_logger.e("JSONException", e);
		}
	}

	public void deleteProfile(Folder folder, Profile profile) throws MessagingException
	{
		Message message = new MimeMessage();
		if (!TextUtils.isEmpty(profile.getRemoteUid()))
		{		
			message.setUid(profile.getRemoteUid());
			folder.setFlags(new Message[]{message}, new Flag[]{Flag.DELETED}, true);
			folder.expunge();
		}
	}
	
	public void backupProfile() {
		try {
			Folder folder = openFolder();
			Profile profile = ProfileDao.getInstance().getProfile();
			if (profile != null && profile.isSyncRequired()) {
				deleteProfile(folder, profile);
				backupProfile(folder, profile);
			}
			folder.close(true);
		} catch (AuthenticationErrorException e) {
			_logger.e("AuthenticationErrorException", e);
		} catch (MessagingException e) {
			if (e != null)
				_logger.e("MessagingException", e);
		}

	}

	public void backupEvent() throws AuthenticationErrorException {

		Folder folder = openFolder();
		Event[] events = EventDao.getInstance().findAllUnsynced();
		try {
			if (events.length == 0) {
				folder.close(true);
				return;
			}

			for (Event event : events) {
				Message message;
				try {
					message = MessageConvertor.convertToMessage(event);
				} catch (JSONException e) {
					e.printStackTrace();
					continue;
				}
				Message[] messages = new Message[] { message };
				folder.appendMessages(messages);
				EventDao.getInstance().updateSyncStatus(event.getId());
			}

			folder.close(true);

		} catch (MessagingException e) {
			if (e != null)
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void backup() {
		try {
			backupEvent();
			backupProfile();
		} catch (AuthenticationErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class GeneralErrorException extends Exception {
		private static final long serialVersionUID = 1L;

		public GeneralErrorException(String msg, Throwable t) {
			super(msg, t);
		}

		public GeneralErrorException(Context ctx, int msgId, Throwable t) {
			super(ctx.getString(msgId), t);
		}
	}

	public static class AuthenticationErrorException extends Exception {
		private static final long serialVersionUID = 1L;

		public AuthenticationErrorException(Throwable t) {
			super(t.getLocalizedMessage(), t);
		}
	}

	@SuppressWarnings("unchecked")
	public void restore() {
		long n = ProfileDao.getInstance().count();
		
		ImapStore.ImapFolder folder;

		try {
			folder = openFolder();
			Message[] messages = folder.getMessagesSince(new Date(), null);
			FetchProfile fp = new FetchProfile();
			// fp.add(FetchProfile.Item.ENVELOPE);
			// fp.add(FetchProfile.Item.FLAGS);
			// fp.add(FetchProfile.Item.BODY_SANE);
			fp.add(FetchProfile.Item.BODY);
			// fp.add(FetchProfile.Item.STRUCTURE);
			BinaryTempFileBody.setTempDirectory(FileUtils.getTmpDir());
			folder.fetch(messages, fp, null);
			
			for (Message m : messages) {
				String[] headers = m.getHeader("X-babynote-entity-type");

				if (headers == null || headers.length <= 0) {
					continue;
				}
				String entityType = headers[0];
				if (entityType.equals(Event.TABLE_NAME)) {
					Event event = MessageConvertor.convertToEvent(m);
					if (event != null) {
						if (!EventDao.getInstance().checkExistByGuid(
								event.getGuid())) {
							event.setSyncStatus(1);
							EventDao.getInstance().save(event);
						}
					}
				} else if (entityType.equals(Profile.TABLE_NAME)) {
					Profile profile = MessageConvertor.convertToProfile(m);
					if (profile == null) {
						continue;
					}
					
					Profile curProfile = ProfileDao.getInstance().getProfileByName(profile.getName());
					if (curProfile == null) {
						ProfileDao.getInstance().save(profile);
					}
					else {
						if (curProfile.getUpdateTime() < profile.getUpdateTime()) {
							ProfileDao.getInstance().updateByName(profile);
						}
					}

				}

			}
		} catch (AuthenticationErrorException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
