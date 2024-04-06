FROM node:14

WORKDIR /app

# First, copy package.json and potentially package-lock.json or yarn.lock
COPY log-dashboard-app/package.json log-dashboard-app/package-lock.json* log-dashboard-app/yarn.lock* ./

RUN npm install

# Copy the entire application directory
COPY log-dashboard-app/ ./

RUN npm run build

EXPOSE 3000

CMD ["npm","start"]