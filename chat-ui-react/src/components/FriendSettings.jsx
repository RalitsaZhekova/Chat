import { useState } from "react";
import axios from "axios";

const FriendSettings = ({ currentUser, closeSettings, refreshFriends }) => {
  const [query, setQuery] = useState("");
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  const handleSearch = async (e) => {
    const value = e.target.value;
    setQuery(value);

    if (value.length < 2) {
      setUsers([]);
      return;
    }

    setLoading(true);
    try {
      const res = await axios.get(`http://localhost:8081/users/search?query=${value}`);
      setUsers(Array.isArray(res.data.data) ? res.data.data : []);
    } catch (error) {
      console.error("Error searching users:", error);
      setUsers([]);
    }
    setLoading(false);
  };

  const addFriend = async (selectedUserId) => {
    if (!currentUser) {
      console.error("Error: Current user is not set.");
      return;
    }

    try {
      await axios.post(`http://localhost:8081/friends/${currentUser}/${selectedUserId}`);
      refreshFriends();
      closeSettings();
    } catch (error) {
      console.error("Error adding friend:", error);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-white p-6 rounded-lg shadow-lg w-96 border border-gray-300">
        <h2 className="text-lg font-bold mb-4">Friend Settings</h2>
        
        <input
          type="text"
          className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-gray-100 text-black"
          placeholder="Search users..."
          value={query}
          onChange={handleSearch}
        />
        
        {loading && <p className="text-gray-500 text-sm mt-2">Searching...</p>}
        
        {users.length > 0 && (
          <ul className="mt-2 border rounded bg-white max-h-40 overflow-y-auto">
            {users.map((user) => (
              <li
                key={user.id}
                className="p-2 border-b last:border-none cursor-pointer bg-gray-200 hover:bg-gray-300 text-black rounded transition"
                onClick={() => addFriend(user.id)}
              >
                {user.name}
              </li>
            ))}
          </ul>
        )}
        
        <div className="mt-4 flex justify-end">
          <button className="p-2 bg-gray-500 text-white rounded hover:bg-gray-600 transition" onClick={closeSettings}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default FriendSettings;