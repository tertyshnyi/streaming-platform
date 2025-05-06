FROM node:22.14 as build

WORKDIR /app

# Copy package.json and package-lock.json to the container
COPY package*.json ./

# Install project dependencies
RUN npm install

# Copy the entire project to the container
COPY . .

# Build the Angular app for production
RUN npm run build

# Use a smaller, production-ready image as the final image
FROM nginx:alpine

# copy the custom nginx configuration file to the container in the
# default location
COPY nginx.conf /etc/nginx/nginx.conf

# Copy the Angular app to the Nginx webserver's root directory
COPY --from=build /app/dist/coursequest-ui/browser /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
