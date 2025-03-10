FROM node:16 AS build-stage

WORKDIR /app

COPY package*.json ./

RUN npm install
# Install CSS optimizer
#RUN npm install -g clean-css-cli


COPY . .
# Minify SCSS output
#RUN find src/app -name "*.component.scss" -exec cleancss -O2 -o {} {} \;


RUN npm run build --prod

FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=build-stage /app/dist/delivery-dashboard-ui /usr/share/nginx/html

EXPOSE 80
