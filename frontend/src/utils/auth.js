import { jwtDecode } from "jwt-decode";

export const getToken = () => localStorage.getItem("token");

export const logout = () => {
  localStorage.removeItem("token");
  window.location.href = "/login";
};

export const getUserRole = () => {
  const token = getToken();
  if (!token) return null;

  try {
    const decoded = jwtDecode(token);

    if (!decoded.role) return null;

    return decoded.role;
  } catch {
    return null;
  }
};

export const isLoggedIn = () => !!getToken();
