import { useState, useEffect } from "react";
import axios from "axios";

const ChatWindow = ({ currentUser, selectedChat, chatType }) => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [userNames, setUserNames] = useState({});

  useEffect(() => {
    if (selectedChat) fetchMessages();
  }, [selectedChat]);

  useEffect(() => {
    if (messages.length > 0) fetchUserNames();
  }, [messages]);

  const fetchMessages = async () => {
    if (!selectedChat) return;
    const url =
      chatType === "channel"
        ? `http://localhost:8081/messages/channel/${selectedChat}`
        : `http://localhost:8081/messages/private/${currentUser}/${selectedChat}`;

    try {
      const res = await axios.get(url);
      setMessages(Array.isArray(res.data.data) ? res.data.data : []);
    } catch (error) {
      console.error("Error fetching messages:", error);
      setMessages([]);
    }
  };

  const fetchUserNames = async () => {
    const uniqueUserIds = [...new Set(messages.map((msg) => msg.senderId))];
    const userMap = { ...userNames };

    for (const userId of uniqueUserIds) {
      if (!userMap[userId]) {
        try {
          const res = await axios.get(`http://localhost:8081/users/${userId}`);
          userMap[userId] = res.data.data[0].name || `User ${userId}`;
        } catch (error) {
          console.error(`Error fetching user ${userId} name:`, error);
          userMap[userId] = `User ${userId}`;
        }
      }
    }
    setUserNames(userMap);
  };

  const sendMessage = async () => {
    const url = chatType === "channel"
      ? "http://localhost:8081/messages/channel/send"
      : "http://localhost:8081/messages/private/send";

    const payload = chatType === "channel"
      ? { senderId: currentUser, channelId: selectedChat, content: newMessage }
      : { senderId: currentUser, receiverId: selectedChat, content: newMessage };

    try {
      await axios.post(url, payload);
      setNewMessage("");
      fetchMessages();
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };

  return (
    <div className="flex-1 h-screen flex flex-col bg-gray-100">
      <div className="flex-1 p-4 overflow-y-auto border bg-white shadow-inner">
        {messages.map((msg) => (
          <div key={msg.id} className="mb-2 p-3 rounded-lg bg-gray-200 shadow-md">
            <strong className="text-gray-700">{userNames[msg.senderId] || msg.senderId}</strong>: {msg.content}
          </div>
        ))}
      </div>
      <div className="p-4 flex bg-gray-200 border-t">
        <input 
          type="text" 
          className="flex-1 p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500" 
          value={newMessage} 
          onChange={(e) => setNewMessage(e.target.value)}
        />
        <button className="ml-2 p-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition" onClick={sendMessage}>Send</button>
      </div>
    </div>
  );
};

export default ChatWindow;