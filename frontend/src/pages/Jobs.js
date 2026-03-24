import { useEffect, useState } from "react";
import {
  getAvailableJobs,
  acceptJob,
  getAvailability,
} from "../services/jobService";
import toast from "react-hot-toast";
import JobCard from "../jobs/JobCard";

/* ================= SKELETON ================= */

function JobCardSkeleton() {
  return (
    <div className="bg-white rounded-2xl shadow-md p-6 animate-pulse">
      <div className="h-5 bg-gray-300 rounded w-3/4 mb-4"></div>
      <div className="h-4 bg-gray-200 rounded w-full mb-2"></div>
      <div className="h-4 bg-gray-200 rounded w-5/6 mb-4"></div>
      <div className="h-6 bg-gray-200 rounded-full w-24 mb-6"></div>
      <div className="h-10 bg-gray-300 rounded-lg"></div>
    </div>
  );
}

/* ================= MAIN ================= */

function Jobs() {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedJob, setSelectedJob] = useState(null);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [availability, setAvailability] = useState("AVAILABLE");

  /* ================= LOAD DATA ================= */

  useEffect(() => {
    loadJobs();
    loadAvailability();
  }, []);

  const loadAvailability = async () => {
    try {
      const data = await getAvailability();
      setAvailability(data.status);
    } catch {
      toast.error("Failed to load availability ❌");
    }
  };

  const loadJobs = async () => {
    try {
      const data = await getAvailableJobs();
      setJobs(data.content || []);
    } catch (err) {
      toast.error("Failed to load jobs ❌");
    } finally {
      setLoading(false);
    }
  };

  /* ================= ACCEPT JOB ================= */

  const handleAccept = async (jobId) => {
    if (availability === "BUSY") {
      toast.error("You are busy. Change availability to accept jobs.");
      return;
    }

    const toastId = toast.loading("Accepting job...");

    try {
      await acceptJob(jobId);
      toast.success("Job accepted 🎉", { id: toastId });
      setSelectedJob(null);
      loadJobs();
    } catch (err) {
      toast.error("Failed to accept job ❌", { id: toastId });
    }
  };

  /* ================= FILTER ================= */

  const filteredJobs = jobs.filter((job) => {
    const matchesTitle = job.title.toLowerCase().includes(search.toLowerCase());

    const matchesStatus = statusFilter === "ALL" || job.status === statusFilter;

    return matchesTitle && matchesStatus;
  });

  const statusStyles = {
    AVAILABLE: "bg-green-100 text-green-800",
    PENDING: "bg-yellow-100 text-yellow-800",
    ACCEPTED: "bg-blue-100 text-blue-800",
  };

  /* ================= LOADING UI ================= */

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-100 py-10 px-4">
        <div className="max-w-6xl mx-auto">
          <h1 className="text-4xl font-bold text-gray-800 mb-8">
            Available Jobs
          </h1>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {[...Array(6)].map((_, i) => (
              <JobCardSkeleton key={i} />
            ))}
          </div>
        </div>
      </div>
    );
  }

  /* ================= MAIN UI ================= */

  return (
    <div className="min-h-screen bg-gray-100 py-10 px-4">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-4xl font-bold text-gray-800 mb-8">
          Available Jobs
        </h1>

        {/* SEARCH & FILTER */}
        <div className="bg-white rounded-xl shadow p-4 mb-8 flex flex-col sm:flex-row gap-4">
          <input
            type="text"
            placeholder="Search by job title..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="flex-1 border rounded-lg px-4 py-2 focus:ring-2 focus:ring-green-500"
          />

          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="border rounded-lg px-4 py-2 focus:ring-2 focus:ring-green-500"
          >
            <option value="ALL">All Status</option>
            <option value="AVAILABLE">Available</option>
            <option value="PENDING">Pending</option>
            <option value="ACCEPTED">Accepted</option>
          </select>
        </div>

        {/* JOBS GRID */}
        {filteredJobs.length === 0 ? (
          <div className="text-center py-16 bg-white rounded-xl shadow">
            <p className="text-gray-500 text-lg">
              No jobs match your search 🔍
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredJobs.map((job) => (
              <JobCard
                key={job.id}
                job={job}
                onView={() => setSelectedJob(job)}
                onAccept={() => handleAccept(job.id)}
                disabled={availability === "BUSY"}
              />
            ))}
          </div>
        )}
      </div>

      {/* ================= MODAL ================= */}

      {selectedJob && (
        <div
          className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50"
          onClick={() => setSelectedJob(null)}
        >
          <div
            className="bg-white rounded-2xl w-full max-w-lg p-6 relative"
            onClick={(e) => e.stopPropagation()}
          >
            <button
              onClick={() => setSelectedJob(null)}
              className="absolute top-4 right-4 text-gray-400 hover:text-gray-600 text-xl"
            >
              ✕
            </button>

            <h2 className="text-2xl font-bold text-gray-800 mb-3">
              {selectedJob.title}
            </h2>

            <span
              className={`inline-block mb-4 px-3 py-1 text-sm rounded-full
                ${statusStyles[selectedJob.status]}`}
            >
              {selectedJob.status}
            </span>

            <p className="text-gray-600 mb-4">{selectedJob.description}</p>

            {selectedJob.location && (
              <p className="text-gray-700 mb-2">
                📍 <strong>Location:</strong> {selectedJob.location}
              </p>
            )}

            {selectedJob.createdAt && (
              <p className="text-gray-500 text-sm mb-6">
                Posted on {new Date(selectedJob.createdAt).toLocaleDateString()}
              </p>
            )}

            <button
              onClick={() => handleAccept(selectedJob.id)}
              disabled={
                selectedJob.status !== "AVAILABLE" || availability === "BUSY"
              }
              className={`w-full py-3 rounded-lg font-medium transition
                ${
                  selectedJob.status === "AVAILABLE" &&
                  availability === "AVAILABLE"
                    ? "bg-green-600 hover:bg-green-700 text-white"
                    : "bg-gray-300 text-gray-600 cursor-not-allowed"
                }`}
            >
              {availability === "BUSY" ? "You're currently busy" : "Accept Job"}
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default Jobs;
