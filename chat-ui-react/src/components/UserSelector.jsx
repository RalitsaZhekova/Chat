import { useState } from "react";

const UserSelector = ({ onUserSelect }) => {
  const [userId, setUserId] = useState("");

  const handleUserChange = (e) => {
    const value = e.target.value;
    setUserId(value);
    onUserSelect(value);
  };

  return (
    <div className="p-4 bg-gray-900 text-white">
      <input
        type="text"
        className="w-full p-2 border rounded bg-gray-700 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
        placeholder="Enter User ID"
        value={userId}
        onChange={handleUserChange}
      />
    </div>
  );
};

export default UserSelector;