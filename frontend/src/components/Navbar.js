import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { isLoggedIn, getUserRole, logout } from "../utils/auth";
import { getAvailability } from "../services/jobService";

const CITIES = ["Select City", "Indore", "Bhopal", "Delhi", "Mumbai"];

function Navbar() {
  const loggedIn = isLoggedIn();
  const role = getUserRole();

  const [availability, setAvailability] = useState(null);
  const [loading, setLoading] = useState(false);
  const [location, setLocation] = useState(
    localStorage.getItem("selectedCity") || "Select City",
  );

  useEffect(() => {
    if (loggedIn && role === "WORKER") {
      const loadAvailability = async () => {
        try {
          setLoading(true);
          const data = await getAvailability();
          setAvailability(data);
        } catch (err) {
          console.error("Failed to load availability", err);
        } finally {
          setLoading(false);
        }
      };
      loadAvailability();
    }
  }, [loggedIn, role]);

  const handleCityChange = (e) => {
    const city = e.target.value;
    setLocation(city);
    localStorage.setItem("selectedCity", city);
  };

  return (
    <nav className="sticky top-0 z-50 backdrop-blur-lg bg-white/80 border-b border-blue-100 shadow-sm px-8 py-4 flex justify-between items-center">
      <Link
        to="/"
        className="text-2xl font-extrabold tracking-tight text-blue-900"
      >
        Kaam<span className="text-blue-600">Set</span>
      </Link>

      <div className="flex items-center gap-6">
        {/* CITY SELECTOR */}
        <select
          value={location}
          onChange={handleCityChange}
          className="bg-white/80 border border-gray-200 px-3 py-2 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
        >
          {CITIES.map((city) => (
            <option key={city}>{city}</option>
          ))}
        </select>

        {/* WORKER AVAILABILITY */}
        {loggedIn && role === "WORKER" && (
          <span
            className={`px-4 py-1 text-sm font-medium rounded-full transition ${
              loading
                ? "bg-gray-100 text-gray-500 animate-pulse"
                : availability === "AVAILABLE"
                  ? "bg-emerald-100 text-emerald-700"
                  : "bg-red-100 text-red-700"
            }`}
          >
            {loading
              ? "⏳ Loading..."
              : availability === "AVAILABLE"
                ? "🟢 Available"
                : "🔴 Busy"}
          </span>
        )}

        {/* LOGIN / REGISTER / DASHBOARD */}
        {!loggedIn ? (
          <>
            <Link
              to="/login"
              className="text-gray-700 hover:text-blue-700 font-medium transition"
            >
              Login
            </Link>
            <Link
              to="/register"
              className="bg-gradient-to-r from-blue-700 to-indigo-600 text-white px-5 py-2 rounded-xl shadow-md hover:scale-105 transition duration-300"
            >
              Register
            </Link>
          </>
        ) : role === "USER" ? (
          <Link
            to="/user"
            className="font-medium text-gray-700 hover:text-blue-700 transition"
          >
            Dashboard
          </Link>
        ) : (
          <Link
            to="/jobs"
            className="font-medium text-gray-700 hover:text-blue-700 transition"
          >
            Available Jobs
          </Link>
        )}

        {loggedIn && (
          <button
            onClick={logout}
            className="text-red-600 hover:text-red-700 font-medium transition"
          >
            Logout
          </button>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
