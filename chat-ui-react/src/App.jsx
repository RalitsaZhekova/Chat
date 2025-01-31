import { useState, useEffect } from "react";
import Sidebar from "./components/Sidebar";
import ChatWindow from "./components/ChatWindow";

const App = () => {
  const [currentUser, setCurrentUser] = useState("");
  const [selectedChat, setSelectedChat] = useState(null);
  const [chatType, setChatType] = useState("channel");

  return (
    <div className="flex h-screen">
      <Sidebar currentUser={currentUser} setCurrentUser={setCurrentUser} selectedChat={selectedChat} setSelectedChat={setSelectedChat} chatType={chatType} setChatType={setChatType} />
      <ChatWindow currentUser={currentUser} selectedChat={selectedChat} chatType={chatType} />
    </div>
  );
};

export default App;