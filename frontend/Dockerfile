FROM node:20-alpine AS dev

WORKDIR /app

# Copiamos primero los archivos de dependencias para aprovechar la caché de capas de Docker
COPY package.json package-lock.json* ./

# Usamos la caché de BuildKit para acelerar la instalación
RUN --mount=type=cache,target=/root/.npm npm install

# Copiamos el resto del código (Asegúrate de tener el .dockerignore)
COPY . .

# Exponemos el puerto de Vite
EXPOSE 5173

# Ejecutamos Vite exponiéndolo a la red de Docker
CMD ["npm", "run", "dev", "--", "--host"]

FROM node:20-alpine AS builder

WORKDIR /app

COPY package.json package-lock.json* ./
RUN --mount=type=cache,target=/root/.npm npm ci

COPY . .
RUN npm run build

FROM nginx:1.27-alpine AS prod

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
