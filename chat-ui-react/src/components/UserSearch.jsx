import { useState } from "react";
import axios from "axios";

const UserSearch = ({ currentUser, onFriendAdded }) => {
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
      setQuery("");
      setUsers([]);
      onFriendAdded();
    } catch (error) {
      console.error("Error adding friend:", error);
    }
  };

  return (
    <div className="absolute bottom-4 left-4 bg-white p-4 shadow-lg rounded-lg w-80">
      <input
        type="text"
        className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
        placeholder="Search users..."
        value={query}
        onChange={handleSearch}
      />
      {loading && <p className="text-gray-500 text-sm mt-2">Searching...</p>}
      {users.length > 0 && (
        <ul className="mt-2 border rounded bg-gray-100 max-h-40 overflow-y-auto">
          {users.map((user) => (
            <li
              key={user.id}
              className="p-2 border-b last:border-none cursor-pointer hover:bg-gray-300"
              onClick={() => addFriend(user.id)}
            >
              {user.name}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default UserSearch;