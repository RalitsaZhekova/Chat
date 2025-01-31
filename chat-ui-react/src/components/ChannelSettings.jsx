import { useState, useEffect } from "react";
import axios from "axios";

const ChannelSettings = ({ currentUser, closeSettings, refreshChannels }) => {
  const [channels, setChannels] = useState([]);
  const [selectedChannel, setSelectedChannel] = useState("");
  const [newChannelName, setNewChannelName] = useState("");
  const [channelMembers, setChannelMembers] = useState([]);
  const [searchResults, setSearchResults] = useState([]);
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    fetchChannels();
  }, []);

  useEffect(() => {
    if (selectedChannel) fetchChannelMembers();
  }, [selectedChannel]);

  const fetchChannels = async () => {
    try {
      const res = await axios.get(`http://localhost:8081/channels/of_user/${currentUser}`);
      setChannels(res.data.data || []);
    } catch (error) {
      console.error("Error fetching channels:", error);
    }
  };

  const fetchChannelMembers = async () => {
    try {
      const res = await axios.get(`http://localhost:8081/users/of_channel/${selectedChannel}`);
      setChannelMembers(res.data.data || []);
    } catch (error) {
      console.error("Error fetching channel members:", error);
    }
  };

    const renameChannel = async () => {
        try {
            await axios.put(`http://localhost:8081/channels/${selectedChannel}/rename?userId=${currentUser}&newName=${newChannelName}`);
            setNewChannelName("");
            await fetchChannels();
            await refreshChannels();
            if (selectedChannel) fetchChannelMembers();
        } catch (error) {
            if (error.response?.status === 403) setErrorMessage("You don't have permission to rename this channel.");
        }
    };

    const deleteChannel = async () => {
        try {
            await axios.delete(`http://localhost:8081/channels/${selectedChannel}?ownerId=${currentUser}`);
            await fetchChannels();
            await refreshChannels();
            closeSettings();
        } catch (error) {
            if (error.response?.status === 403) setErrorMessage("Only the owner can delete this channel.");
        }
    };

  const addMember = async (userId) => {
    try {
      await axios.post(`http://localhost:8081/channels/${selectedChannel}/members/add?requestingUserId=${currentUser}&targetUserId=${userId}&role=MEMBER`);
      await fetchChannelMembers();
    } catch (error) {
      if (error.response?.status === 403) setErrorMessage("You don't have permission to add members.");
    }
  };

  const removeMember = async (userId) => {
    const confirm = window.confirm(`Are you sure you want to remove this user from the channel?`);
    if (!confirm) return;

    try {
      await axios.delete(`http://localhost:8081/channels/${selectedChannel}/members/remove?requestingUserId=${currentUser}&targetUserId=${userId}`);
      await fetchChannelMembers();
    } catch (error) {
      if (error.response?.status === 403) setErrorMessage("You don't have permission to remove members.");
    }
  };

  const promoteMember = async (userId, userName) => {
    const confirm = window.confirm(`Are you sure you want to promote ${userName} to Admin?`);
    if (!confirm) return;

    try {
      await axios.put(`http://localhost:8081/channels/${selectedChannel}/promote/${userId}?requestingUserId=${currentUser}`);
      alert(`${userName} has been promoted to Admin.`);
      fetchChannelMembers();
    } catch (error) {
      if (error.response?.status === 403) setErrorMessage("You don't have permission to promote members.");
    }
  };

  const searchUsers = async (query) => {
    if (query.length < 2) {
      setSearchResults([]);
      return;
    }
    try {
      const res = await axios.get(`http://localhost:8081/users/search?query=${query}`);
      setSearchResults(res.data.data || []);
    } catch (error) {
      console.error("Error searching users:", error);
      setSearchResults([]);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-white p-6 rounded-lg shadow-lg w-96 border border-gray-300 relative">
        <h2 className="text-lg font-bold mb-4 text-black">Channel Settings</h2>

        <button className="absolute top-2 right-2 p-1 bg-gray-400 text-white rounded hover:bg-gray-600" onClick={closeSettings}>
          ✕
        </button>

        <select className="w-full p-2 border rounded bg-gray-100 text-black" value={selectedChannel} onChange={(e) => setSelectedChannel(e.target.value)}>
          <option value="">Select a channel</option>
          {channels.map((ch) => (
            <option key={ch.id} value={ch.id}>{ch.name}</option>
          ))}
        </select>

        <input type="text" placeholder="New Channel Name" className="w-full p-2 border rounded mt-2 text-black" value={newChannelName} onChange={(e) => setNewChannelName(e.target.value)} />
        <button className="w-full p-2 mt-2 bg-blue-500 text-white rounded hover:bg-blue-600" onClick={renameChannel}>Rename</button>
        <button className="w-full p-2 mt-2 bg-red-500 text-white rounded hover:bg-red-600" onClick={deleteChannel}>Delete</button>

        <h3 className="mt-4 font-bold text-black">Members</h3>
        {channelMembers.length === 0 ? (
          <p className="text-gray-600">No members found</p>
        ) : (
          channelMembers.map((member) => (
            <div key={member.id} className="flex justify-between bg-gray-200 p-2 rounded mt-2 text-black">
              {member.name}
              <div>
                <button className="text-blue-500 mr-2" onClick={() => promoteMember(member.id)}>Promote</button>
                <button className="text-red-500" onClick={() => removeMember(member.id)}>Remove</button>
              </div>
            </div>
          ))
        )}

        <h3 className="mt-4 font-bold text-black">Add Member</h3>
        <input type="text" placeholder="Search users..." className="w-full p-2 border rounded bg-gray-100 text-black" onChange={(e) => searchUsers(e.target.value)} />
        {searchResults.length > 0 && (
          <ul className="mt-2 border rounded bg-white max-h-40 overflow-y-auto text-black">
            {searchResults.map((user) => (
              <li key={user.id} className="p-2 border-b last:border-none cursor-pointer hover:bg-gray-300" onClick={() => addMember(user.id)}>
                {user.name}
              </li>
            ))}
          </ul>
        )}

        {errorMessage && <p className="text-red-500 mt-2">{errorMessage}</p>}
      </div>
    </div>
  );
};

export default ChannelSettings;