import axios from "axios";

const API_URL = "http://localhost:8080/api/jobs";

const getAuthHeader = () => {
  const token = localStorage.getItem("token");
  return {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
};

/* ================= WORKER ================= */

// Get available jobs
export const getAvailableJobs = async () => {
  const response = await axios.get(`${API_URL}/available`, getAuthHeader());
  return response.data;
};

// Get worker job history (accepted + completed)
export const getWorkerJobs = async () => {
  const response = await axios.get(
    `${API_URL}/worker/history`,
    getAuthHeader(),
  );
  return response.data;
};

// Accept job
export const acceptJob = async (jobId) => {
  const response = await axios.put(
    `${API_URL}/${jobId}/accept`,
    {},
    getAuthHeader(),
  );
  return response.data;
};

// Complete job
export const completeJob = async (jobId) => {
  const response = await axios.put(
    `${API_URL}/${jobId}/complete`,
    {},
    getAuthHeader(),
  );
  return response.data;
};

// Update availability
export const updateAvailability = async (status) => {
  const token = localStorage.getItem("token");

  const response = await axios.put(
    "http://localhost:8080/api/jobs/workers/availability",
    { status: status },
    {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    },
  );

  return response.data;
};

// Get availability
export const getAvailability = async () => {
  const response = await axios.get(
    `${API_URL}/workers/availability`,
    getAuthHeader(),
  );
  return response.data;
};

/* ================= USER ================= */

// Get user jobs
export const getUserJobs = async () => {
  const response = await axios.get(`${API_URL}/user/history`, getAuthHeader());
  return response.data;
};

// Create job
export const createJob = async (jobData) => {
  const response = await axios.post(`${API_URL}`, jobData, getAuthHeader());
  return response.data;
};

// Cancel job
export const cancelJob = async (jobId) => {
  const response = await axios.put(
    `${API_URL}/${jobId}/cancel`,
    {},
    getAuthHeader(),
  );
  return response.data;
};
