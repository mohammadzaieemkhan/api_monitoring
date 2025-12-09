// pages/alerts.js
import React, { useState, useEffect, useCallback } from 'react';
import { useRouter } from 'next/router';

function AlertsPage() {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filterStatus, setFilterStatus] = useState(''); // State for status filter
  const router = useRouter();

  const fetchAlerts = useCallback(async () => {
    setLoading(true);
    setError(null);
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      router.push('/login');
      return;
    }

    const queryParams = new URLSearchParams();
    if (filterStatus) queryParams.append('status', filterStatus);

    const url = `http://localhost:8081/api/query/alerts?${queryParams.toString()}`;

    try {
      const response = await fetch(url, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (!response.ok) {
        throw new Error('Failed to fetch alerts');
      }
      const data = await response.json();
      setAlerts(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [filterStatus, router]);

  useEffect(() => {
    fetchAlerts();
  }, [fetchAlerts]);

  const handleResolveAlert = async (id) => {
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      router.push('/login');
      return;
    }
    try {
      const response = await fetch(`http://localhost:8081/api/query/alerts/${id}/resolve`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (!response.ok) {
        throw new Error('Failed to resolve alert');
      }
      // Re-fetch alerts to update the list
      fetchAlerts();
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <p>Loading alerts...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div>
      <h1>Alert Viewer</h1>
      <div>
        <label htmlFor="filterStatus">Filter by Status:</label>
        <select id="filterStatus" value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
          <option value="">All</option>
          <option value="ACTIVE">Active</option>
          <option value="RESOLVED">Resolved</option>
        </select>
      </div>
      <table>
        <thead>
          <tr>
            <th>Type</th>
            <th>Message</th>
            <th>Service Name</th>
            <th>API Endpoint</th>
            <th>Status</th>
            <th>Timestamp</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {alerts.map((alert) => (
            <tr key={alert.id}>
              <td>{alert.type}</td>
              <td>{alert.message}</td>
              <td>{alert.serviceName}</td>
              <td>{alert.apiEndpoint}</td>
              <td>{alert.status}</td>
              <td>{new Date(alert.timestamp).toLocaleString()}</td>
              <td>
                {alert.status === 'ACTIVE' && (
                  <button onClick={() => handleResolveAlert(alert.id)}>Resolve</button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AlertsPage;
