# Base image
FROM node:18-alpine

# Create app directory
WORKDIR /usr/src/app

# A wildcard is used to ensure both package.json AND package-lock.json are copied
COPY --chown=node:node package*.json ./

# Bundle app source
COPY --chown=node:node . .

# Creates a "dist" folder with the production build
RUN npm run build

USER node

# Set NODE_ENV environment variable
ENV NODE_ENV production

# Running `npm ci` removes the existing node_modules directory and passing in --only=production ensures that only the production dependencies are installed. This ensures that the node_modules directory is as optimized as possible
RUN npm ci --only=production && npm cache clean --force

# Expose port 3000
EXPOSE 3000

# Start the server using the production build
CMD [ "node", "dist/main.js" ]