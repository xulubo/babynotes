package cn.year11.babynote.provider.email;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import cn.year11.babynote.logic.SettingManager;
import cn.year11.babynote.provider.Attachment;
import cn.year11.babynote.provider.AttachmentDao;
import cn.year11.babynote.provider.BaseEntity;
import cn.year11.babynote.provider.Profile;
import cn.year11.babynote.provider.Reminder;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.utils.DateUtils;
import cn.year11.babynote.utils.FileUtils;
import cn.year11.utils.JsonUtils;

import com.android.email.mail.Address;
import com.android.email.mail.Body;
import com.android.email.mail.Flag;
import com.android.email.mail.Message;
import com.android.email.mail.MessagingException;
import com.android.email.mail.Message.RecipientType;
import com.android.email.mail.internet.MimeBodyPart;
import com.android.email.mail.internet.MimeMessage;
import com.android.email.mail.internet.MimeMultipart;
import com.android.email.mail.internet.TextBody;

public class MessageConvertor {

	private static String HEADER_ATTACHMENT_META = "X-attachment-meta";

	public static Message convertToMessage(Profile profile)
			throws MessagingException, IOException, JSONException {
		if (profile == null) {
			return null;
		}

		BinaryTempFileBody.setTempDirectory(FileUtils.getTmpDir());
		Message msg = new MimeMessage();

		Address from = new Address("\"±¶±¥»’º«\" <babynote@year11.com>");
		msg.setFrom(from);
		Address to = new Address(SettingManager.getUserName());
		msg.setRecipient(RecipientType.TO, to);

		msg.setSubject(profile.getName() + " "
				+ DateUtils.formatDate(profile.getBirthday()));
		Date then = new Date(profile.getCreateTime());

		msg.setSentDate(then);
		msg.setInternalDate(then);

		msg.setHeader("X-babynote-entity-type", Profile.TABLE_NAME);
		msg.setFlag(Flag.SEEN, true);

		MimeMultipart body = new MimeMultipart();

		// set text body
		TextBody textBody = new TextBody(profile.toString());
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setBody(textBody);
		body.addBodyPart(textPart);

		// set text body
		textBody = new TextBody(JsonUtils.toString(profile));
		textPart = new MimeBodyPart();
		textPart.setBody(textBody);
		body.addBodyPart(textPart);

		// add portrait

		byte[] portrait = profile.getPortrait();
		if (portrait != null) {
			MimeBodyPart part = new MimeBodyPart();

			part.addHeader("Content-Type", "image");
			part.addHeader("Content-Disposition",
					"attachment; filename=\"portrait.bmp\"");

			part.addHeader("Content-Transfer-Encoding", "base64");
			
			ByteArrayInputStream in = new ByteArrayInputStream(portrait);

			BinaryTempFileBody tempBody = new BinaryTempFileBody();
			OutputStream out = tempBody.getOutputStream();

			IOUtils.copy(in, out);
			out.close();
			in.close();

			part.setBody(tempBody);
			body.addBodyPart(part);
		}

		msg.setBody(body);

		return msg;
	}

	public static Message convertToMessage(Event event)
			throws MessagingException, IOException, JSONException {
		if (event == null) {
			return null;
		}

		BinaryTempFileBody.setTempDirectory(FileUtils.getTmpDir());
		Message msg = new MimeMessage();

		msg.setSubject(event.getEventDetail());
		Address from = new Address("\"Robert Xu\" <xulubo@gmail.com>");
		msg.setFrom(from);
		Address to = new Address("\"Robert Xu\" <xulubo@gmail.com>");
		msg.setRecipient(RecipientType.TO, to);

		Date then = new Date(event.getBeginTime());

		msg.setSentDate(then);
		msg.setInternalDate(then);

		msg.setHeader("X-babynote-event-id", String.valueOf(event.getId()));
		msg.setHeader("X-babynote-backup_time", new Date().toGMTString());
		msg.setHeader("X-babynote-entity-type", "event");
		msg.setFlag(Flag.SEEN, true);

		MimeMultipart body = new MimeMultipart();

		// set text body
		TextBody textBody = new TextBody(event.getEventDetail());
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setBody(textBody);
		body.addBodyPart(textPart);

		// set text body
		textBody = new TextBody(JsonUtils.toString(event));
		textPart = new MimeBodyPart();
		textPart.setBody(textBody);
		body.addBodyPart(textPart);

		// add attachments
		Attachment[] atts = AttachmentDao.getInstance().findByEventId(
				event.getId());
		if (atts != null && atts.length > 0) {

			int i = 0;
			for (Attachment a : atts) {
				i++;
				MimeBodyPart part = new MimeBodyPart();

				if (a.isImage()) {
					part.addHeader("Content-Type", "image");
					part.addHeader("Content-Disposition", String.format(
							"attachment; filename=\"image%02d.jpg\"", i));
				} else if (a.isVoice()) {
					part.addHeader("Content-Type", "voice");
					part.addHeader("Content-Disposition", String.format(
							"attachment; filename=\"voice%02d.amr\"", i));
				} else {
					part.addHeader("Content-Type", "binary");
				}
				part.addHeader("Content-Transfer-Encoding", "base64");
				// don't save thumbnail
				a.setThumbNail(null);
				part.addHeader(HEADER_ATTACHMENT_META, JsonUtils.toString(a));

				File file = new File(a.getContentPath());
				FileInputStream in = new FileInputStream(file);

				BinaryTempFileBody tempBody = new BinaryTempFileBody();
				OutputStream out = tempBody.getOutputStream();

				IOUtils.copy(in, out);
				out.close();
				in.close();

				part.setBody(tempBody);
				body.addBodyPart(part);
			}
		}

		msg.setBody(body);

		return msg;
	}

	public static Event convertToEvent(Message message) {
		MimeMultipart body;
		Event event = null;
		try {
			body = (MimeMultipart) message.getBody();
			int i = 0;
			int count = body.getCount();

			// no body part?
			if (count < 2) {
				return null;
			}

			MimeBodyPart part = (MimeBodyPart) body.getBodyPart(1);
			String type = part.getContentType();
			if (type != null) {
				Body tmpBody = part.getBody();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(tmpBody.getInputStream(), baos);
				String tmpS = baos.toString();
				event = JsonUtils.parseEvent(tmpS);
			}

			if (event == null) {
				return null;
			}

			// get attachments, part index starts from 1
			for (i = 2; i < count; i++) {
				part = (MimeBodyPart) body.getBodyPart(i);
				type = part.getContentType();
				String[] headers = part.getHeader(HEADER_ATTACHMENT_META);
				if (headers == null || headers.length <= 0) {
					continue;
				}

				Attachment att = JsonUtils.parseAttachment(headers[0]);
				Body tmpBody = part.getBody();
				File file = new File(att.getContentPath());
				String dir;
				if (att.isImage()) {
					dir = FileUtils.getImageDir();
				} else if (att.isVoice()) {
					dir = FileUtils.getVoiceDir();
				} else {
					dir = null;
					att.setContentPath(null);
				}

				if (dir != null) {
					File outFile = new File(FileUtils.getImageDir(),
							file.getName());
					FileOutputStream fileOut = new FileOutputStream(outFile);
					IOUtils.copy(tmpBody.getInputStream(), fileOut);
					att.setContentPath(outFile.getPath());
				}

				event.addAttachment(att);
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return event;
	}

	public static Profile convertToProfile(Message message) {
		MimeMultipart body;
		Profile profile = null;
		try {
			body = (MimeMultipart) message.getBody();
			int count = body.getCount();

			// no body part?
			if (count < 2) {
				return null;
			}

			MimeBodyPart part = (MimeBodyPart) body.getBodyPart(1);
			String type = part.getContentType();
			if (type != null) {
				Body tmpBody = part.getBody();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(tmpBody.getInputStream(), baos);
				String tmpS = baos.toString();
				profile = JsonUtils.parseProfile(tmpS);
				profile.setRemoteUid(message.getUid());
				profile.setSyncTime(System.currentTimeMillis());
			}

			if (profile == null) {
				return null;
			}

			// get portrait, part index starts from 1
			if (count > 1) {
				part = (MimeBodyPart) body.getBodyPart(2);
				type = part.getContentType();
				if (type != null && type.equals("image")) {
					Body tmpBody = part.getBody();
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					IOUtils.copy(tmpBody.getInputStream(), os);
					profile.setPortrait(os.toByteArray());					
				}

			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return profile;
	}

	public static Reminder convertToReminder(Message message) {
		return null;
	}
}
