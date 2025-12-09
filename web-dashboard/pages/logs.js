// pages/logs.js
import React, { useState, useEffect, useCallback } from 'react';
import { useRouter } from 'next/router';
import LogFilters from '../components/LogFilters'; // Import the new component

function LogsPage() {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({}); // State to hold current filters
  const router = useRouter();

  const fetchLogs = useCallback(async () => {
    setLoading(true);
    setError(null);
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      router.push('/login');
      return;
    }

    // Construct query parameters from filters
    const queryParams = new URLSearchParams();
    if (filters.serviceName) queryParams.append('serviceName', filters.serviceName);
    if (filters.apiEndpoint) queryParams.append('apiEndpoint', filters.apiEndpoint);
    // Add other filters as they are implemented

    const url = `http://localhost:8081/api/query/logs?${queryParams.toString()}`;

    try {
      const response = await fetch(url, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (!response.ok) {
        throw new Error('Failed to fetch logs');
      }
      const data = await response.json();
      setLogs(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [filters, router]);

  useEffect(() => {
    fetchLogs();
  }, [fetchLogs]);

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
  };

  if (loading) return <p>Loading logs...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div>
      <h1>API Request Explorer</h1>
      <LogFilters onFilterChange={handleFilterChange} /> {/* Integrate filter component */}
      <table>
        <thead>
          <tr>
            <th>Service Name</th>
            <th>Endpoint</th>
            <th>Method</th>
            <th>Status Code</th>
            <th>Latency (ms)</th>
            <th>Timestamp</th>
            <th>Event Type</th>
          </tr>
        </thead>
        <tbody>
          {logs.map((log, index) => (
            <tr key={index}>
              <td>{log.serviceName}</td>
              <td>{log.apiEndpoint}</td>
              <td>{log.requestMethod}</td>
              <td>{log.statusCode}</td>
              <td>{log.latency}</td>
              <td>{new Date(log.timestamp).toLocaleString()}</td>
              <td>{log.eventType}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default LogsPage;
