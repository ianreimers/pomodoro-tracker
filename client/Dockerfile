FROM node:21-alpine AS build
WORKDIR /app
ARG NEXT_PUBLIC_API_BASE_URL
ENV NEXT_PUBLIC_API_BASE_URL=${NEXT_PUBLIC_API_BASE_URL}

COPY package*.json .
RUN npm install

COPY . .
RUN npm run build

FROM node:21-alpine
WORKDIR /app
# Copy the build artifacts from the build stage to this stage
COPY --from=build /app/next.config.mjs ./
COPY --from=build /app/public ./public
COPY --from=build /app/.next ./.next
COPY --from=build /app/node_modules ./node_modules
COPY --from=build /app/package.json ./package.json

EXPOSE 3000

CMD ["npm", "start"]
