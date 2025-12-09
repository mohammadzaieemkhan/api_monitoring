// pages/index.js
import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/router';

function HomePage() {
  const router = useRouter();
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('jwt_token');
    if (token) {
      setIsAuthenticated(true);
      // Optionally, validate token with backend here
    } else {
      router.push('/login');
    }
  }, []);

  if (!isAuthenticated) {
    return <p>Redirecting to login...</p>;
  }

  return (
    <div>
      <h1>Welcome to the API Monitoring Dashboard</h1>
      <p>You are authenticated!</p>
      {/* Rest of the dashboard content will go here */}
    </div>
  );
}

export default HomePage;
