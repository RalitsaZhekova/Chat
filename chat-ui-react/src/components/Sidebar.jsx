import { useState, useEffect } from "react";
import axios from "axios";
import FriendSettings from "./FriendSettings";
import ChannelSettings from "./ChannelSettings";
import AddUserForm from "./AddUserForm";

const Sidebar = ({ currentUser, setCurrentUser, selectedChat, setSelectedChat, setChatType, chatType }) => {
  const [channels, setChannels] = useState([]);
  const [friends, setFriends] = useState([]);
  const [newChannelName, setNewChannelName] = useState("");
  const [isFriendSettingsOpen, setIsFriendSettingsOpen] = useState(false);
  const [isChannelSettingsOpen, setIsChannelSettingsOpen] = useState(false);
  const [isAddUserOpen, setIsAddUserOpen] = useState(false);
  
  const [currentPage, setCurrentPage] = useState(1);
  const rowsPerPage = 3;
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => {
    if (currentUser) {
      fetchChannels();
      fetchFriends(currentPage);
    }
  }, [currentUser, currentPage]);

  const fetchChannels = async () => {
    if (!currentUser) {
      setChannels([]);
      return;
    }

    try {
      const res = await axios.get(`http://localhost:8081/channels/of_user/${currentUser}`);
      setChannels(res.data.data || []);
    } catch (error) {
      console.error("Error fetching channels:", error);
      setChannels([]);
    }
  };

  const fetchFriends = async (page = 1) => {
    try {
      const res = await axios.get(
        `http://localhost:8081/users/${currentUser}/friends?page=${page}&rowsPerPage=${rowsPerPage}`
      );
      setFriends(res.data.friends || []);
      setTotalPages(res.data.pagination.totalPages || 1);
    } catch (error) {
      console.error("Error fetching friends:", error);
    }
  };

  const createChannel = async () => {
    if (!newChannelName.trim()) return;
    try {
      await axios.post(`http://localhost:8081/channels?name=${newChannelName}&ownerId=${currentUser}&isPrivate=0`);
      setNewChannelName("");
      fetchChannels();
    } catch (error) {
      console.error("Error creating channel:", error);
    }
  };

  return (
    <div className="w-1/4 h-screen bg-gray-900 text-white p-4 flex flex-col">
      <input 
        type="text" 
        placeholder="Enter User ID" 
        className="w-full p-2 border mb-4 bg-gray-800 text-white rounded" 
        value={currentUser} 
        onChange={(e) => setCurrentUser(e.target.value)}
      />

      <h2 className="text-lg font-bold mb-2">Channels</h2>

      <div className="flex mb-2">
        <input
          type="text"
          placeholder="New Channel Name"
          className="flex-1 p-2 border rounded bg-gray-800 text-white mr-2"
          value={newChannelName}
          onChange={(e) => setNewChannelName(e.target.value)}
        />
        <button 
          className="p-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition"
          onClick={createChannel}
        >
          +
        </button>
      </div>

      {channels.map((ch) => (
        <div 
          key={ch.id} 
          className={`p-2 border cursor-pointer rounded transition ${
            selectedChat === ch.id && chatType === "channel" ? "bg-blue-600" : "hover:bg-gray-700"
          }`}
          onClick={() => { 
            setSelectedChat(ch.id); 
            setChatType("channel"); 
          }}
        >
          {ch.name}
        </div>
      ))}

      <button 
        className="w-full p-2 mt-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition"
        onClick={() => setIsChannelSettingsOpen(true)}
      >
        Channel Settings
      </button>

      <h2 className="text-lg font-bold mt-4 mb-2">Friends</h2>
      {friends.map((fr) => (
        <div 
          key={fr.id} 
          className={`p-2 border cursor-pointer rounded transition ${
            selectedChat === fr.id && chatType === "friend" ? "bg-green-600" : "hover:bg-gray-700"
          }`}
          onClick={() => { 
            setSelectedChat(fr.id); 
            setChatType("friend"); 
          }}
        >
          {fr.name}
        </div>
      ))}

      <div className="flex justify-between mt-2">
        <button 
          className={`p-2 ${currentPage > 1 ? "bg-gray-500 hover:bg-gray-600" : "bg-gray-700 cursor-not-allowed"} text-white rounded`} 
          disabled={currentPage === 1}
          onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
        >
          Prev
        </button>
        <span className="text-gray-300 p-2">Page {currentPage} of {totalPages}</span>
        <button 
          className={`p-2 ${currentPage < totalPages ? "bg-gray-500 hover:bg-gray-600" : "bg-gray-700 cursor-not-allowed"} text-white rounded`} 
          disabled={currentPage === totalPages}
          onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
        >
          Next
        </button>
      </div>

      <button 
        className="w-full p-2 mt-4 bg-green-500 text-white rounded hover:bg-green-600 transition"
        onClick={() => setIsFriendSettingsOpen(true)}
      >
        Add Friend
      </button>

      <button 
        className="w-full p-2 mt-2 bg-purple-500 text-white rounded hover:bg-purple-600 transition"
        onClick={() => setIsAddUserOpen(true)}
      >
        Add User
      </button>

      {isFriendSettingsOpen && (
        <FriendSettings 
          currentUser={currentUser} 
          closeSettings={() => setIsFriendSettingsOpen(false)} 
          refreshFriends={() => fetchFriends(currentPage)} 
        />
      )}
      {isChannelSettingsOpen && (
        <ChannelSettings 
          currentUser={currentUser} 
          closeSettings={() => setIsChannelSettingsOpen(false)} 
          refreshChannels={fetchChannels} 
        />
      )}
      {isAddUserOpen && (
        <AddUserForm closeSettings={() => setIsAddUserOpen(false)} />
      )}
    </div>
  );
};

export default Sidebar;