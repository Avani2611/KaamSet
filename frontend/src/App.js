import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import UserDashboard from "./pages/UserDashboard";
import Jobs from "./pages/Jobs";
import Login from "./pages/Login";
import Register from "./pages/Register";
import UserJobs from "./pages/UserJobs";
import WorkerDashboard from "./pages/WorkerDashboard";
import CreateJob from "./jobs/CreateJob";
import WorkerMyJobs from "./jobs/WorkerMyJobs";
import { Toaster } from "react-hot-toast";
import Home from "./pages/Home";

function App() {
  return (
    <BrowserRouter>
      <Toaster position="top-right" />

      <div className="min-h-screen bg-gray-100">
        <Navbar />

        <div className="p-6">
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            <Route path="/jobs" element={<Jobs />} />
            <Route path="/user/jobs" element={<UserJobs />} />
            <Route path="/worker/jobs" element={<WorkerMyJobs />} />

            <Route path="/worker" element={<WorkerDashboard />} />
            <Route path="/user" element={<UserDashboard />} />

            <Route path="/create-job" element={<CreateJob />} />
            <Route path="/" element={<Home />} />
          </Routes>
        </div>
      </div>
    </BrowserRouter>
  );
}

export default App;
