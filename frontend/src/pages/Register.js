import { useState } from "react";
import { register } from "../services/authService";
import { useNavigate, Link } from "react-router-dom";

function Register() {
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    password: "",
    role: "USER",
    city: "",
    area: "",
    pincode: "", // ✅ Added
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      await register(form);
      navigate("/login");
    } catch (err) {
      setError("Registration failed. Email may already exist.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-900 via-blue-700 to-blue-500">
      <div className="bg-white/20 backdrop-blur-lg border border-white/30 p-10 rounded-3xl shadow-2xl w-full max-w-md text-white">
        <h2 className="text-3xl font-bold text-center mb-6 tracking-wide">
          Create Account 🚀
        </h2>

        <p className="text-center text-blue-100 mb-6">
          Join KaamSet and get started today
        </p>

        {error && <p className="text-red-300 text-center mb-4">{error}</p>}

        <form onSubmit={handleSubmit} className="space-y-5">
          <input
            name="fullName"
            placeholder="Full Name"
            onChange={handleChange}
            required
            className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
          />

          <input
            name="email"
            type="email"
            placeholder="Email"
            onChange={handleChange}
            required
            className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
          />

          <input
            name="password"
            type="password"
            placeholder="Password"
            onChange={handleChange}
            required
            className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
          />

          {/* CITY */}
          <input
            name="city"
            placeholder="City"
            onChange={handleChange}
            required
            className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
          />

          {/* AREA */}
          <input
            name="area"
            placeholder="Area"
            onChange={handleChange}
            required
            className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
          />

          {/* ✅ PINCODE */}
          <input
            name="pincode"
            placeholder="Pincode"
            onChange={handleChange}
            required
            pattern="[0-9]{6}"
            title="Enter a valid 6-digit pincode"
            className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
          />

          <select
            name="role"
            onChange={handleChange}
            className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
          >
            <option value="USER">Customer</option>
            <option value="WORKER">Worker</option>
          </select>

          <button
            type="submit"
            className="w-full bg-white text-blue-800 p-3 rounded-xl font-semibold shadow-lg hover:scale-105 transition duration-300"
          >
            Register
          </button>
        </form>

        <p className="text-center text-blue-100 mt-6 text-sm">
          Already have an account?{" "}
          <Link
            to="/login"
            className="font-semibold underline hover:text-white"
          >
            Login here
          </Link>
        </p>
      </div>
    </div>
  );
}

export default Register;
