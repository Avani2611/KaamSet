export default function JobCard({ job, onAccept }) {
  const statusColors = {
    PENDING: "bg-yellow-100 text-yellow-700",
    ACCEPTED: "bg-blue-100 text-blue-700",
    COMPLETED: "bg-green-100 text-green-700",
    CANCELLED: "bg-red-100 text-red-700",
  };

  return (
    <div className="bg-white rounded-3xl shadow-md p-6 hover:shadow-xl hover:-translate-y-1 transition duration-300 border border-blue-50">
      {/* HEADER */}
      <div className="flex justify-between items-center mb-3">
        <h3 className="text-xl font-semibold text-blue-900">{job.title}</h3>

        <span
          className={`px-4 py-1 rounded-full text-sm font-medium ${
            statusColors[job.status]
          }`}
        >
          {job.status}
        </span>
      </div>

      {/* DESCRIPTION */}
      <p className="text-gray-600 leading-relaxed">{job.description}</p>

      {/* CITY & AREA */}
      <p className="text-sm text-gray-500 mt-4">
  📍 {job.city} {job.area ? `- ${job.area}` : ""}
      </p>

      {/* ACTION BUTTON */}
      {job.status === "PENDING" && (
        <button
          onClick={() => onAccept(job.id)}
          className="mt-6 w-full bg-blue-700 text-white px-4 py-3 rounded-xl font-semibold shadow-md hover:bg-blue-800 hover:scale-105 transition duration-300"
        >
          Accept Job
        </button>
      )}
    </div>
  );
}
