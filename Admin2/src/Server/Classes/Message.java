package Server.Classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Message {
	private String _id;
	private String index;
	private String senderId;
	private String receiverId;
	private String content;
	private String createTime;
	private Boolean seen;
	private Boolean senderDelete;
	private Boolean receiverDelete;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Message() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy@HH:mm:ss");
		Date date = new Date();
		_id = UUID.randomUUID().toString();
		receiverId = new String();
		senderId = new String();
		content = new String();
		createTime = formatter.format(date);
		index = "0";
		senderDelete = false;
		receiverDelete = false;
		seen = false;
	}

	public Message(String senderIdTemp, String receiverIdTemp, String contentTemp) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy@HH:mm:ss");
		Date date = new Date();
		
		senderId = senderIdTemp;
		content = contentTemp;
		receiverId = receiverIdTemp;
		_id = UUID.randomUUID().toString();
		createTime = formatter.format(date);
		senderDelete = false;
		receiverDelete = false;
		seen = false;
	}
	
	public Message(String senderIdTemp, String receiverIdTemp, String contentTemp, String indexTemp) {
		senderId = senderIdTemp;
		content = contentTemp;
		index = indexTemp;
		receiverId = receiverIdTemp;
		_id = UUID.randomUUID().toString();
		createTime = new String();
		senderDelete = false;
		receiverDelete = false;
		seen = false;
	}
	
	

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getId() {
		return _id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Boolean getSenderDelete() {
		return senderDelete;
	}

	public void setSenderDelete(Boolean senderDelete) {
		this.senderDelete = senderDelete;
	}

	public Boolean getReceiverDelete() {
		return receiverDelete;
	}

	public void setReceiverDelete(Boolean receiverDelete) {
		this.receiverDelete = receiverDelete;
	}

	public Boolean getSeen() {
		return seen;
	}

	public void setSeen(Boolean seen) {
		this.seen = seen;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
}
