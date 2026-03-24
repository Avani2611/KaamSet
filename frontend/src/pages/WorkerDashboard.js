import { useEffect, useState } from "react";
import { getWorkerJobs, updateAvailability } from "../services/jobService";
import toast from "react-hot-toast";

/* ================= STAT CARD ================= */

function StatCard({ title, value, gradient }) {
  return (
    <div className="bg-white/80 backdrop-blur-sm border border-white/40 rounded-2xl shadow-md p-6 hover:shadow-xl transition duration-300">
      <p className="text-gray-500 text-sm">{title}</p>
      <h2
        className={`text-3xl font-bold mt-2 bg-gradient-to-r ${gradient} bg-clip-text text-transparent`}
      >
        {value}
      </h2>
    </div>
  );
}

/* ================= DASHBOARD ================= */

function WorkerDashboard() {
  const [jobs, setJobs] = useState([]);
  const [availability, setAvailability] = useState(
    localStorage.getItem("workerAvailability") || "AVAILABLE"
  );
  const [loadingAvailability, setLoadingAvailability] = useState(false);

  useEffect(() => {
    loadJobs();
  }, []);

  const loadJobs = async () => {
    try {
      const data = await getWorkerJobs();
      const allJobs = data.content || [];

      // 🔥 No frontend city filtering now
      // Backend will handle filtering later if needed
      setJobs(allJobs);
    } catch {
      toast.error("Failed to load jobs ❌");
    }
  };

  const accepted = jobs.filter((j) => j.status === "ACCEPTED").length;
  const completed = jobs.filter((j) => j.status === "COMPLETED").length;

  const toggleAvailability = async () => {
    const newStatus = availability === "AVAILABLE" ? "BUSY" : "AVAILABLE";
    setLoadingAvailability(true);

    const toastId = toast.loading("Updating availability...");

    try {
      await updateAvailability(newStatus);

      setAvailability(newStatus);
      localStorage.setItem("workerAvailability", newStatus);

      toast.success(
        `You are now ${newStatus === "AVAILABLE" ? "Available 🟢" : "Busy 🔴"}`,
        { id: toastId }
      );
    } catch {
      toast.error("Failed to update availability ❌", { id: toastId });
    } finally {
      setLoadingAvailability(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-blue-100 p-8">
      <div className="max-w-6xl mx-auto">
        {/* HEADER + AVAILABILITY */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-10 gap-6">
          <h1 className="text-4xl font-bold text-gray-800">
            Worker Dashboard 👷
          </h1>

          <div className="bg-white/80 backdrop-blur-sm border border-white/40 rounded-2xl shadow-md px-6 py-4 flex items-center gap-4">
            <span className="font-medium text-gray-700">Availability:</span>

            <button
              onClick={toggleAvailability}
              disabled={loadingAvailability}
              className={`px-6 py-2 rounded-full font-semibold transition-all duration-200 shadow-md
                ${
                  availability === "AVAILABLE"
                    ? "bg-gradient-to-r from-emerald-600 to-green-500 text-white"
                    : "bg-gradient-to-r from-red-600 to-red-500 text-white"
                }
                ${loadingAvailability && "opacity-60 cursor-not-allowed"}
              `}
            >
              {availability === "AVAILABLE" ? "AVAILABLE 🟢" : "BUSY 🔴"}
            </button>
          </div>
        </div>

        {/* STATS */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-6 mb-12">
          <StatCard
            title="Total Jobs"
            value={jobs.length}
            gradient="from-indigo-600 to-indigo-400"
          />
          <StatCard
            title="Accepted Jobs"
            value={accepted}
            gradient="from-blue-600 to-cyan-400"
          />
          <StatCard
            title="Completed Jobs"
            value={completed}
            gradient="from-emerald-600 to-green-400"
          />
        </div>

        {/* RECENT JOBS */}
        <div className="bg-white/80 backdrop-blur-sm border border-white/40 rounded-3xl shadow-md p-8">
          <h2 className="text-2xl font-semibold mb-6 text-gray-800">
            Recent Jobs
          </h2>

          {jobs.length === 0 ? (
            <p className="text-gray-500 text-center py-10">
              No jobs available 🚀
            </p>
          ) : (
            <div className="space-y-4">
              {jobs.slice(0, 5).map((job) => (
                <div
                  key={job.id}
                  className="flex justify-between items-center bg-white rounded-xl p-4 shadow-sm hover:shadow-md transition border border-gray-100"
                >
                  <div>
                    <h3 className="font-semibold text-gray-800">{job.title}</h3>

                    {/* ✅ Structured Location */}
                    <p className="text-sm text-gray-500 mt-1">
                      📍 {job.area}, {job.city} - {job.pincode}
                    </p>
                  </div>

                  <span
                    className={`px-4 py-1 text-xs font-medium rounded-full
                      ${
                        job.status === "COMPLETED"
                          ? "bg-emerald-100 text-emerald-700"
                          : job.status === "ACCEPTED"
                          ? "bg-blue-100 text-blue-700"
                          : "bg-yellow-100 text-yellow-700"
                      }`}
                  >
                    {job.status}
                  </span>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default WorkerDashboard;
