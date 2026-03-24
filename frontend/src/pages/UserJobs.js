import React, { useEffect, useState } from "react";
import { getUserJobs } from "../services/jobService";
import toast from "react-hot-toast";

const UserJobs = () => {
  const [jobs, setJobs] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadJobs();
  }, []);

  const loadJobs = async () => {
    try {
      const data = await getUserJobs();
      setJobs(data.content || []);
    } catch (err) {
      setError("Failed to load jobs ❌");
      toast.error("Failed to load jobs");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-blue-100 py-12 px-6">
      <div className="max-w-6xl mx-auto">

        {/* HEADER */}
        <h1 className="text-4xl font-bold text-gray-800 mb-10">
          My Posted Jobs 📋
        </h1>

        {/* ERROR */}
        {error && (
          <div className="bg-red-100 text-red-700 px-4 py-3 rounded-xl mb-6">
            {error}
          </div>
        )}

        {/* LOADING */}
        {loading ? (
          <p className="text-gray-500 text-lg">Loading jobs...</p>
        ) : jobs.length === 0 ? (
          <div className="bg-white/80 backdrop-blur-sm border border-white/40 rounded-2xl shadow-md p-12 text-center">
            <p className="text-gray-500 text-lg">
              No jobs posted yet 🚀
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            {jobs.map((job) => (
              <div
                key={job.id}
                className="bg-white/80 backdrop-blur-sm border border-white/40 rounded-2xl shadow-md p-6 hover:shadow-xl transition duration-300"
              >
                <h3 className="text-xl font-semibold text-gray-800">
                  {job.title}
                </h3>

                <p className="text-gray-600 mt-3 line-clamp-2">
                  {job.description}
                </p>

                <div className="mt-4 flex justify-between items-center">
                  <span className="text-sm text-gray-500">
                    📍 {job.location}
                  </span>

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
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default UserJobs;
