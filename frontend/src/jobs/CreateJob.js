import { useState } from "react";
import { createJob } from "../services/jobService";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";

function CreateJob() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title: "",
    description: "",
    city: "",
    area: "",
    pincode: "",
    jobType: "", // default selected
    totalDays: "",
    perDaySalary: "",
  });

  const [loading, setLoading] = useState(false);

  // 🔥 Updated handleChange with smart reset logic
  const handleChange = (e) => {
    const { name, value } = e.target;

    // If switching job type
    if (name === "jobType") {
      if (value !== "CONTRACT") {
        setForm({
          ...form,
          jobType: value,
          totalDays: "",
          perDaySalary: "",
        });
      } else {
        setForm({
          ...form,
          jobType: value,
        });
      }
      return;
    }

    setForm({ ...form, [name]: value });
  };

  const validate = () => {
    if (!form.title.trim()) {
      toast.error("Job title is required");
      return false;
    }
    if (!form.description.trim()) {
      toast.error("Job description is required");
      return false;
    }
    if (!form.city.trim()) {
      toast.error("City is required");
      return false;
    }
    if (!form.area.trim()) {
      toast.error("Area is required");
      return false;
    }
    if (!form.pincode.trim()) {
      toast.error("Pincode is required");
      return false;
    }
    if (!form.jobType.trim()) {
      toast.error("Job type is required");
      return false;
    }

    // Contract validation
    if (form.jobType === "CONTRACT") {
      if (!form.totalDays || form.totalDays <= 0) {
        toast.error("Total days must be at least 1");
        return false;
      }
    }

    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validate()) return;

    setLoading(true);
    const toastId = toast.loading("Creating job...");

    try {
      await createJob(form);

      toast.success("Job posted successfully 🎉", { id: toastId });

      setForm({
        title: "",
        description: "",
        city: "",
        area: "",
        pincode: "",
        jobType: "",
        totalDays: "",
        perDaySalary: "",
      });

      navigate("/my-jobs");
    } catch (err) {
      toast.error("Failed to create job ❌", { id: toastId });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-900 via-blue-700 to-blue-500 py-16 px-4">
      <div className="max-w-3xl mx-auto">
        <h1 className="text-4xl font-bold text-white mb-10 text-center">
          Create New Job 📝
        </h1>

        <form
          onSubmit={handleSubmit}
          className="bg-white/20 backdrop-blur-lg border border-white/30 rounded-3xl shadow-2xl p-10 space-y-6 text-white"
        >
          {/* TITLE */}
          <div>
            <label className="block text-sm font-medium mb-2">Job Title</label>
            <input
              type="text"
              name="title"
              value={form.title}
              onChange={handleChange}
              placeholder="e.g. Plumber needed"
              className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
            />
          </div>

          {/* DESCRIPTION */}
          <div>
            <label className="block text-sm font-medium mb-2">
              Job Description
            </label>
            <textarea
              name="description"
              value={form.description}
              onChange={handleChange}
              rows="5"
              placeholder="Describe the job in detail..."
              className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
            />
          </div>

          {/* CITY */}
          <div>
            <label className="block text-sm font-medium mb-2">City</label>
            <input
              type="text"
              name="city"
              value={form.city}
              onChange={handleChange}
              placeholder="e.g. Mumbai"
              className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
            />
          </div>

          {/* AREA */}
          <div>
            <label className="block text-sm font-medium mb-2">Area</label>
            <input
              type="text"
              name="area"
              value={form.area}
              onChange={handleChange}
              placeholder="e.g. Andheri"
              className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
            />
          </div>

          {/* PINCODE */}
          <div>
            <label className="block text-sm font-medium mb-2">Pincode</label>
            <input
              type="text"
              name="pincode"
              placeholder="Enter a 6-digit Pincode"
              value={form.pincode}
              onChange={handleChange}
              className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
            />
          </div>

          {/* JOB TYPE */}
          <div>
            <label className="block text-sm font-medium mb-2">Job Type</label>
            <select
              name="jobType"
              value={form.jobType}
              onChange={handleChange}
              className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
            >
              <option value="ONE_TIME">One Time</option>
              <option value="CONTRACT">Contract</option>
            </select>
          </div>

          {/* NUMBER OF DAYS - ONLY FOR CONTRACT */}
          {form.jobType === "CONTRACT" && (
            <div>
              <label className="block text-sm font-medium mb-2">
                Number of Days
              </label>
              <input
                type="number"
                name="totalDays"
                value={form.totalDays}
                onChange={handleChange}
                placeholder="e.g. 5"
                className="w-full p-3 rounded-xl bg-white/80 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-300"
              />
            </div>
          )}

          {/* ACTION BUTTONS */}
          <div className="flex gap-4 pt-6">
            <button
              type="submit"
              disabled={loading}
              className={`flex-1 py-3 rounded-xl font-semibold shadow-lg transition duration-300
                ${
                  loading
                    ? "bg-gray-300 text-gray-600"
                    : "bg-white text-blue-800 hover:scale-105"
                }`}
            >
              {loading ? "Posting..." : "Post Job"}
            </button>

            <button
              type="button"
              onClick={() => navigate(-1)}
              className="flex-1 py-3 rounded-xl border border-white text-white hover:bg-white hover:text-blue-800 transition duration-300"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default CreateJob;
