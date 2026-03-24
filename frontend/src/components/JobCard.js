export default function JobCard({ job, onAccept }) {
  const { id, title, status, description, city, area } = job;

  const statusColors = {
    PENDING: "bg-yellow-100 text-yellow-700",
    ACCEPTED: "bg-blue-100 text-blue-700",
    COMPLETED: "bg-green-100 text-green-700",
    CANCELLED: "bg-red-100 text-red-700",
  };

  return (
    <div className="bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition">
      <div className="flex justify-between items-center">
        <h3 className="text-lg font-semibold">{title}</h3>
        <span
          className={`px-3 py-1 rounded-full text-sm ${statusColors[status]}`}
        >
          {status}
        </span>
      </div>

      <p className="text-gray-600 mt-2">{description}</p>
      <p className="text-sm text-gray-500 mt-3">
        📍 {city} {area ? `- ${area}` : ""}
      </p>

      {status === "PENDING" && (
        <button
          onClick={() => onAccept(id)}
          className="mt-4 bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700"
          aria-label={`Accept job ${title}`}
        >
          Accept Job
        </button>
      )}
    </div>
  );
}
