import { useState } from "react";
import axios from "axios";

const AddUserForm = ({ onUserAdded, closeSettings }) => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!name || !email) return;

    try {
      await axios.post("http://localhost:8081/users", { name, email });
      setName("");
      setEmail("");
      closeSettings();
      onUserAdded();
    } catch (error) {
      console.error("Error adding user:", error);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-white p-6 rounded-lg shadow-lg w-96 text-black">
        <h2 className="text-lg font-bold mb-4">Add New User</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full p-2 border rounded mb-2 bg-white text-black"
          />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full p-2 border rounded mb-4 bg-white text-black"
          />
          <div className="flex justify-between">
            <button 
              type="button" 
              className="p-2 bg-gray-500 text-white rounded hover:bg-gray-600 transition"
              onClick={closeSettings}
            >
              Cancel
            </button>
            <button 
              type="submit" 
              className="p-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition"
            >
              Add User
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddUserForm;