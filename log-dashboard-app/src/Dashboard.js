import React, { useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
const colors = [
    '#7B66FF', // Mor
    '#5FBDFF', // Mavi
    '#A8DF8E', // Yeşil
    '#FFBFBF', // Pembe
    '#413ea0', // Koyu Mavi
    '#e83f6f', // Pembe

  ];
const Dashboard = () => {
  const [data, setData] = useState([]);
  const [countries, setCountries] = useState([]);

  useEffect(() => {

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
      .catch(error => console.error('Error fetching data: ', error));
  }, []);

  return (
    <div style={{ width: '100%', height: '400px' }}>
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
