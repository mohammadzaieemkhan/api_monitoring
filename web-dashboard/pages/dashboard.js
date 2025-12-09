// pages/dashboard.js
import React, { useState, useEffect, useCallback } from 'react';
import { useRouter } from 'next/router';

function DashboardPage() {
  const [slowApiCount, setSlowApiCount] = useState(0);
  const [brokenApiCount, setBrokenApiCount] = useState(0);
  const [rateLimitViolations, setRateLimitViolations] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const router = useRouter();

  const fetchData = useCallback(async () => {
    setLoading(true);
    setError(null);
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      router.push('/login');
      return;
    }

    try {
      const headers = {
        'Authorization': `Bearer ${token}`
      };

      const slowApiCountResponse = await fetch('http://localhost:8081/api/dashboard/slow-api-count', { headers });
      const brokenApiCountResponse = await fetch('http://localhost:8081/api/dashboard/broken-api-count', { headers });
      const rateLimitViolationsResponse = await fetch('http://localhost:8081/api/dashboard/rate-limit-violations', { headers });

      if (!slowApiCountResponse.ok || !brokenApiCountResponse.ok || !rateLimitViolationsResponse.ok) {
        throw new Error('Failed to fetch dashboard data');
      }

      setSlowApiCount(await slowApiCountResponse.json());
      setBrokenApiCount(await brokenApiCountResponse.json());
      setRateLimitViolations(await rateLimitViolationsResponse.json());

    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [router]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  if (loading) return <p>Loading dashboard...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div>
      <h1>API Monitoring Dashboard</h1>
      <div>
        <h2>Slow API Count: {slowApiCount}</h2>
        <h2>Broken API Count: {brokenApiCount}</h2>
        <h2>Rate Limit Violations: {rateLimitViolations}</h2>
        {/* More widgets will go here */}
      </div>
    </div>
  );
}

export default DashboardPage;
