import { useEffect, useState } from "react";
import { getUserJobs } from "../services/jobService";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";

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

/* ================= MAIN DASHBOARD ================= */

function UserDashboard() {
  const [jobs, setJobs] = useState([]);

  useEffect(() => {
    loadJobs();
  }, []);

  const loadJobs = async () => {
    const data = await getUserJobs();
    setJobs(data.content || []);
  };

  const pending = jobs.filter((j) => j.status === "PENDING").length;
  const accepted = jobs.filter((j) => j.status === "ACCEPTED").length;
  const completed = jobs.filter((j) => j.status === "COMPLETED").length;

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-blue-100 p-8">
      <div className="max-w-6xl mx-auto">
        {/* HEADER */}
        <h1 className="text-4xl font-bold text-gray-800 mb-10">
          User Dashboard 🧑‍💼
        </h1>

        {/* STATS */}
        <div className="grid grid-cols-1 sm:grid-cols-4 gap-6 mb-12">
          <StatCard
            title="Total Jobs"
            value={jobs.length}
            gradient="from-indigo-600 to-indigo-400"
          />
          <StatCard
            title="Pending"
            value={pending}
            gradient="from-yellow-500 to-orange-400"
          />
          <StatCard
            title="Accepted"
            value={accepted}
            gradient="from-blue-600 to-cyan-400"
          />
          <StatCard
            title="Completed"
            value={completed}
            gradient="from-emerald-600 to-green-400"
          />
        </div>

        {/* CHART SECTION */}
        <div className="bg-white/80 backdrop-blur-sm border border-white/40 rounded-3xl shadow-md p-8 mb-12">
          <h2 className="text-2xl font-semibold mb-6 text-gray-800">
            Job Status Overview 📊
          </h2>

          <div className="w-full h-72">
            <ResponsiveContainer>
              <PieChart>
                <Pie
                  data={[
                    { name: "Pending", value: pending },
                    { name: "Accepted", value: accepted },
                    { name: "Completed", value: completed },
                  ]}
                  dataKey="value"
                  nameKey="name"
                  outerRadius={110}
                  label
                >
                  <Cell fill="#F59E0B" />
                  <Cell fill="#3B82F6" />
                  <Cell fill="#10B981" />
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* RECENT JOBS */}
        <div className="bg-white/80 backdrop-blur-sm border border-white/40 rounded-3xl shadow-md p-8">
          <h2 className="text-2xl font-semibold mb-6 text-gray-800">
            Recent Posted Jobs
          </h2>

          {jobs.length === 0 ? (
            <p className="text-gray-500 text-center py-10">
              You haven’t posted any jobs yet 🚀
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
                    <p className="text-sm text-gray-500 mt-1">
                      📍 {job.location}
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

export default UserDashboard;
