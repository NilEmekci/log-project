import React, { useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import Button from '@mui/material/Button';
import {toast} from "react-toastify";

const colors = [
    '#756AB6', // Mor
    '#5FBDFF', // Mavi
    '#1FAB89', // Koyu Yeşil
    '#FFBFBF', // Pembe
    '#D24545', // Kırmızı
];

const Dashboard = () => {
  const [data, setData] = useState([]);
  const [countries, setCountries] = useState([]);

  const fetchData = () => {
    const now = new Date().toISOString();
    fetch(`http://localhost:8081/log-consumer/getByDay/${now}`)
      .then(response => response.json())
      .then(jsonData => {
        const allCountries = new Set();
        const transformedData = jsonData.map(item => {
          const entry = { time: item.time };
          Object.entries(item.countryData).forEach(([country, value]) => {
            entry[country] = parseInt(value, 10);
            allCountries.add(country);
          });
          return entry;
        });
        setData(transformedData);
        setCountries([...allCountries]);
      })
            .catch(error => {
        toast.error("Log-Consumer service unavailable!", {
          position: "top-right",
          autoClose: 2000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "colored", 
        });
      });
    
  };

  useEffect(() => {
    fetchData(); 
    const interval = setInterval(() => {
      fetchData(); 
    }, 5000); 
    return () => clearInterval(interval); 
  }, []);

  return (
    <div style={{ width: '100%', height: '400px' }}>
      <Button variant="contained" color="primary" onClick={fetchData} style={{ marginBottom: '10px' }}>
        Refresh
      </Button>
      <ResponsiveContainer>
        <LineChart
          data={data}
          margin={{
            top: 5,
            right: 30,
            left: 20,
            bottom: 5,
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="time" />
          <YAxis />
          <Tooltip />
          <Legend />
          {countries.map((country, index) => (
            <Line
              key={index}
              type="monotone"
              dataKey={country}
              stroke={colors[index % colors.length]} 
              activeDot={{ r: 8 }}
            />
          ))}
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default Dashboard;