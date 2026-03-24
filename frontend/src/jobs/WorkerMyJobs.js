import { useEffect, useState } from "react";
import { getWorkerJobs, completeJob } from "../services/jobService";
import toast from "react-hot-toast";

/* ================= JOB CARD ================= */

function JobCard({ job, onComplete }) {
  const [loading, setLoading] = useState(false);

  const statusStyles = {
    ACCEPTED: "bg-indigo-100 text-indigo-700 border border-indigo-200",
    COMPLETED: "bg-emerald-100 text-emerald-700 border border-emerald-200",
  };

  const handleComplete = async () => {
    setLoading(true);
    const toastId = toast.loading("Marking job as completed...");

    try {
      await onComplete(job.id);
      toast.success("Job marked as completed ✅", { id: toastId });
    } catch {
      toast.error("Failed to complete job ❌", { id: toastId });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white/80 backdrop-blur-sm border border-gray-100 rounded-2xl shadow-sm p-6 hover:shadow-xl transition duration-300">
      {/* HEADER */}
      <div className="flex justify-between items-start">
        <div>
          <h3 className="text-xl font-semibold text-gray-800">{job.title}</h3>

          <p className="text-gray-600 mt-2 line-clamp-2">{job.description}</p>

          {job.city && (
            <p className="text-sm text-gray-500 mt-3 flex items-center gap-1">
              📍 {job.city} {job.area ? `- ${job.area}` : ""}
            </p>
          )}
        </div>

        <span
          className={`px-3 py-1 text-xs font-medium rounded-full ${
            statusStyles[job.status]
          }`}
        >
          {job.status}
        </span>
      </div>

      {/* DATE */}
      {job.createdAt && (
        <p className="text-xs text-gray-400 mt-5">
          Assigned on {new Date(job.createdAt).toLocaleDateString()}
        </p>
      )}

      {/* ACTION */}
      {job.status === "ACCEPTED" && (
        <button
          onClick={handleComplete}
          disabled={loading}
          className={`mt-6 w-full py-2.5 rounded-xl font-medium transition duration-200
            ${
              loading
                ? "bg-gray-200 text-gray-500 cursor-not-allowed"
                : "bg-gradient-to-r from-indigo-600 to-indigo-500 hover:opacity-90 text-white shadow-md"
            }`}
        >
          {loading ? "Completing..." : "Mark as Completed"}
        </button>
      )}
    </div>
  );
}

/* ================= MAIN PAGE ================= */

function MyJobs() {
  const [jobs, setJobs] = useState([]);
  const [activeTab, setActiveTab] = useState("ACCEPTED");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadMyJobs();
  }, []);

  const loadMyJobs = async () => {
    try {
      const data = await getWorkerJobs();
      setJobs(data.content || []);
    } catch {
      toast.error("Failed to load your jobs ❌");
    } finally {
      setLoading(false);
    }
  };

  const handleCompleteJob = async (jobId) => {
    await completeJob(jobId);
    loadMyJobs();
  };

  const filteredJobs = jobs.filter((job) => job.status === activeTab);

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-indigo-50 py-12 px-4">
      <div className="max-w-6xl mx-auto">
        {/* PAGE TITLE */}
        <h1 className="text-4xl font-bold text-gray-800 mb-10">My Jobs 🧰</h1>

        {/* TABS */}
        <div className="flex gap-4 mb-10">
          {["ACCEPTED", "COMPLETED"].map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`px-6 py-2.5 rounded-full font-medium transition duration-200
                ${
                  activeTab === tab
                    ? "bg-indigo-600 text-white shadow-md"
                    : "bg-white text-gray-600 border border-gray-200 hover:bg-gray-100"
                }`}
            >
              {tab === "ACCEPTED" ? "Accepted Jobs" : "Completed Jobs"}
            </button>
          ))}
        </div>

        {/* CONTENT */}
        {loading ? (
          <p className="text-gray-500 text-lg">Loading your jobs...</p>
        ) : filteredJobs.length === 0 ? (
          <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-12 text-center">
            <p className="text-gray-500 text-lg">
              No {activeTab.toLowerCase()} jobs yet 🚧
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            {filteredJobs.map((job) => (
              <JobCard key={job.id} job={job} onComplete={handleCompleteJob} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default MyJobs;
