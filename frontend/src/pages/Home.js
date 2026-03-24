import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function Home() {
  const [city, setCity] = useState("");
  const navigate = useNavigate();

  const handleSearch = () => {
    if (!city.trim()) return;

    // Store city temporarily
    localStorage.setItem("searchCity", city);

    // Later you can redirect to services page
    navigate("/register");
  };

  return (
    <div className="bg-blue-50 text-gray-800">
      {/* HERO SECTION */}
      <section className="bg-gradient-to-br from-blue-900 via-blue-700 to-blue-500 text-white">
        <div className="max-w-7xl mx-auto px-8 py-28 flex flex-col md:flex-row items-center justify-between">
          <div className="max-w-xl">
            <h1 className="text-6xl font-extrabold mb-6 tracking-tight">
              KaamSet
            </h1>

            <h2 className="text-2xl font-semibold mb-4 text-blue-100">
              Book Karo, Kaam Shuru.
            </h2>

            <p className="text-blue-100 mb-8 leading-relaxed">
              Professional home cleaning, maids, cooking help and household
              services delivered safely at your doorstep.
            </p>

            {/* 🔍 CITY SEARCH BAR */}
            <div className="flex gap-3 mt-6">
              <input
                type="text"
                placeholder="Enter your city"
                value={city}
                onChange={(e) => setCity(e.target.value)}
                className="px-4 py-3 rounded-xl border border-white/40 bg-white/90 text-gray-800 w-64 focus:outline-none focus:ring-2 focus:ring-blue-300 shadow-md"
              />
              <button
                onClick={handleSearch}
                className="bg-white text-blue-800 px-6 py-3 rounded-xl font-semibold shadow-lg hover:scale-105 transition duration-300"
              >
                Find Services
              </button>
            </div>

            {/* CTA BUTTONS */}
            <div className="flex gap-4 mt-8">
              <Link
                to="/register"
                className="bg-white text-blue-800 px-6 py-3 rounded-xl font-semibold shadow-lg hover:scale-105 transition duration-300"
              >
                Book a Service
              </Link>

              <Link
                to="/register"
                className="border border-white px-6 py-3 rounded-xl hover:bg-white hover:text-blue-800 transition duration-300"
              >
                Become a Worker
              </Link>
            </div>
          </div>

          <div className="mt-12 md:mt-0">
            <div className="bg-white/20 backdrop-blur-md p-8 rounded-3xl shadow-2xl w-80 border border-white/30">
              <p className="text-lg font-semibold mb-3">
                ✔ Verified Professionals
              </p>
              <p className="text-lg font-semibold mb-3">✔ Affordable Pricing</p>
              <p className="text-lg font-semibold">✔ Easy & Secure Booking</p>
            </div>
          </div>
        </div>
      </section>

      {/* SERVICES SECTION */}
      <section className="py-24 bg-white">
        <h2 className="text-4xl font-bold text-center text-blue-900 mb-16">
          Our Services
        </h2>

        <div className="grid md:grid-cols-3 gap-10 max-w-6xl mx-auto px-8">
          {[
            {
              title: "Home Cleaning",
              desc: "Regular and deep cleaning for spotless homes.",
            },
            {
              title: "Cooking Maid",
              desc: "Experienced home cooks for daily meals.",
            },
            {
              title: "Laundry & Ironing",
              desc: "Wash, fold and iron services at home.",
            },
            {
              title: "Deep Cleaning",
              desc: "Intensive kitchen & bathroom cleaning.",
            },
            {
              title: "Babysitting",
              desc: "Safe and trusted childcare support.",
            },
            {
              title: "Handyman",
              desc: "Repairs, installations and maintenance.",
            },
          ].map((service) => (
            <div
              key={service.title}
              className="bg-blue-50 p-8 rounded-3xl shadow-md hover:shadow-xl hover:-translate-y-2 transition duration-300"
            >
              <h3 className="text-xl font-semibold text-blue-800 mb-3">
                {service.title}
              </h3>
              <p className="text-gray-600">{service.desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* HOW IT WORKS */}
      <section className="py-24 bg-blue-50 text-center">
        <h2 className="text-4xl font-bold text-blue-900 mb-16">
          How KaamSet Works
        </h2>

        <div className="grid md:grid-cols-3 gap-10 max-w-6xl mx-auto px-8">
          {[
            "Select your required service.",
            "Verified professional gets assigned.",
            "Relax while we handle the work.",
          ].map((step, index) => (
            <div
              key={index}
              className="bg-white p-10 rounded-3xl shadow-md hover:shadow-xl transition"
            >
              <div className="text-5xl font-bold text-blue-600 mb-4">
                0{index + 1}
              </div>
              <p className="text-gray-600">{step}</p>
            </div>
          ))}
        </div>
      </section>

      {/* CTA */}
      <section className="bg-gradient-to-r from-blue-800 to-blue-600 text-white py-24 text-center">
        <h2 className="text-4xl font-bold mb-6">Make Home Management Simple</h2>

        <p className="mb-10 text-blue-100">
          Join thousands of happy households today.
        </p>

        <Link
          to="/register"
          className="bg-white text-blue-800 px-8 py-4 rounded-2xl font-semibold shadow-lg hover:scale-105 transition duration-300"
        >
          Get Started
        </Link>
      </section>
    </div>
  );
}
