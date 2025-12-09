// components/LogFilters.js
import React, { useState } from 'react';

function LogFilters({ onFilterChange }) {
  const [serviceName, setServiceName] = useState('');
  const [apiEndpoint, setApiEndpoint] = useState('');
  // Add more filter states as needed (date range, status, etc.)

  const handleFilterSubmit = (e) => {
    e.preventDefault();
    onFilterChange({ serviceName, apiEndpoint }); // Pass current filter values
  };

  return (
    <form onSubmit={handleFilterSubmit}>
      <div>
        <label htmlFor="filterServiceName">Service Name:</label>
        <input
          type="text"
          id="filterServiceName"
          value={serviceName}
          onChange={(e) => setServiceName(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="filterApiEndpoint">API Endpoint:</label>
        <input
          type="text"
          id="filterApiEndpoint"
          value={apiEndpoint}
          onChange={(e) => setApiEndpoint(e.target.value)}
        />
      </div>
      {/* Add more filter inputs here */}
      <button type="submit">Apply Filters</button>
    </form>
  );
}

export default LogFilters;
