import { useState } from "react";
import { login } from "../services/authService";
import { useNavigate, Link } from "react-router-dom";
import toast from "react-hot-toast";
import { FaEye, FaEyeSlash } from "react-icons/fa";

export default function Login() {
  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    const toastId = toast.loading("Logging in...");

    try {
      const res = await login(form.email, form.password);

      // Store user data
      localStorage.setItem("role", res.role);
      localStorage.setItem("city", res.city);
      localStorage.setItem("area", res.area);

      if (res.token) {
        localStorage.setItem("token", res.token);
      }

      toast.success("Login successful 🎉", { id: toastId });

      // Redirect based on role
      if (res.role === "USER") {
        navigate("/user");
      } else {
        navigate("/worker");
      }
    } catch (err) {
      toast.error("Invalid credentials ❌", { id: toastId });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-900 via-blue-700 to-blue-500">
      <div className="bg-white/20 backdrop-blur-lg border border-white/30 p-10 rounded-3xl shadow-2xl w-96 text-white">
        <h2 className="text-3xl font-bold text-center mb-6 tracking-wide">
          Welcome Back 👋
        </h2>

        <p className="text-center text-blue-100 mb-6">
          Login to continue with KaamSet
        </p>

        <form onSubmit={handleSubmit} className="space-y-5">
          {/* EMAIL */}
          <input
            name="email"
            type="email"
            placeholder="Email"
            value={form.email}
            onChange={handleChange}
            required
            className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
          />

          {/* PASSWORD WITH EYE ICON */}
          <div className="relative">
            <input
              name="password"
              type={showPassword ? "text" : "password"}
              placeholder="Password"
              value={form.password}
              onChange={handleChange}
              required
              className="w-full p-3 pr-12 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
            />

            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-600 hover:text-gray-800"
            >
              {showPassword ? <FaEyeSlash /> : <FaEye />}
            </button>
          </div>

          {/* LOGIN BUTTON */}
          <button
            type="submit"
            disabled={loading}
            className={`w-full p-3 rounded-xl font-semibold shadow-lg transition duration-300
              ${
                loading
                  ? "bg-gray-300 text-gray-600"
                  : "bg-white text-blue-800 hover:scale-105"
              }
            `}
          >
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>

        <p className="text-center text-blue-100 mt-6 text-sm">
          Don’t have an account?{" "}
          <Link
            to="/register"
            className="font-semibold underline hover:text-white"
          >
            Register here
          </Link>
        </p>
      </div>
    </div>
  );
}
